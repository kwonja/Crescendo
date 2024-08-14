import os
import time
import queue
import threading
import psutil
from flask import Flask, request, jsonify
from service import analyze_service
from dotenv import load_dotenv

app = Flask(__name__)
load_dotenv()

BASE_URL = os.environ.get("BASE_URL")
task_queue = queue.PriorityQueue()
semaphore_value = 3
semaphore = threading.Semaphore(semaphore_value)
memory_check_time = 10
min_thread = 1
max_thread = 10

def calculate_priority(video_length, start_time):
    wait_time = time.time() - start_time
    priority = (wait_time + video_length) / video_length
    return priority

def enqueue_task(video_length, func, *args):
    start_time = time.time()
    priority = calculate_priority(video_length, start_time)
    future = queue.Queue()
    task_queue.put((priority, start_time, func, args, future))
    return future.get()

def task_worker():
    while True:
        priority, start_time, func, args, future = task_queue.get()
        try:
            with semaphore:
                result = func(*args)
                future.put(result)
        finally:
            task_queue.task_done()

def adjust_semaphore_based_on_memory():
    global semaphore_value, semaphore

    while True:
        memory = psutil.virtual_memory()
        available_memory_ratio = memory.available / memory.total
        print(f"Memory check: {available_memory_ratio} at {time.time()}")

        if available_memory_ratio < 0.2:
            semaphore_value = max(min_thread, semaphore_value - 1)
        elif available_memory_ratio > 0.5:
            semaphore_value = min(max_thread, semaphore_value + 1)

        semaphore = threading.Semaphore(semaphore_value)
        print(f"Adjusted Semaphore Value: {semaphore_value} at {time.time()}")
        time.sleep(memory_check_time)


threading.Thread(target=adjust_semaphore_based_on_memory, daemon=True).start()

for _ in range(semaphore_value):
    threading.Thread(target=task_worker, daemon=True).start()

@app.route('/api/v1/challenge/similarity/landmark', methods=['POST'])
def get_landmarks():
    data = request.json
    video_url = data.get('video_url')
    video_url = BASE_URL + video_url

    if not video_url:
        return jsonify({"error": "video_url is required"}), 400

    video_length = analyze_service.get_video_length(video_url)

    result = enqueue_task(video_length, process_landmarks, video_url)

    return jsonify(result), 200

def process_landmarks(video_url):
    landmarks = analyze_service.get_landmark_position(video_url)

    response_data = {
        "landmark_positions": [
            {
                "frame_number": frame_data['frame'],
                "landmarks": [
                    {
                        "landmark_index": idx,
                        "x": lm['x'],
                        "y": lm['y'],
                        "z": lm['z'],
                        "visibility": lm['visibility']
                    }
                    for idx, lm in enumerate(frame_data['landmarks'])
                ]
            }
            for frame_data in landmarks
        ]
    }

    return response_data

@app.route('/api/v1/challenge/similarity/analyze', methods=['POST'])
def analyze_similarity():
    data = request.json
    video_url = data.get('video_url')
    video_url = BASE_URL + video_url
    landmark_positions = data.get('landmark_positions')

    if not video_url or not landmark_positions:
        return jsonify({"error": "video_url and landmark_positions are required"}), 400

    video_length = analyze_service.get_video_length(video_url)

    result = enqueue_task(video_length, process_similarity, video_url, landmark_positions)

    return jsonify(result), 200

def process_similarity(video_url, landmark_positions):
    compare_landmarks = analyze_service.get_landmark_position(video_url)

    base_landmarks = [
        {
            "frame": frame_data['frame_number'],
            "landmarks": [
                {
                    "x": lm['x'],
                    "y": lm['y'],
                    "z": lm['z'],
                    "visibility": lm['visibility']
                }
                for lm in frame_data['landmarks']
            ]
        }
        for frame_data in landmark_positions
    ]

    similarity_percentage = analyze_service.get_analyze(base_landmarks, compare_landmarks)

    response_data = {
        "similarity": similarity_percentage
    }

    return response_data

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
