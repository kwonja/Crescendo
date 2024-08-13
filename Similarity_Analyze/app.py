import os
from flask import Flask, request, jsonify
from service import analyze_service
from dotenv import load_dotenv

app = Flask(__name__)
load_dotenv()

BASE_URL = os.environ.get("BASE_URL")

@app.route('/api/v1/challenge/similarity/landmark', methods=['POST'])
def get_landmarks():
    print("Request received")

    data = request.json
    video_url = data.get('video_url')

    print(video_url)

    if not video_url:
        return jsonify({"error": "video_url is required"}), 400

    video_url = BASE_URL + video_url

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

    return jsonify(response_data), 200

@app.route('/api/v1/challenge/similarity/analyze', methods=['POST'])
def analyze_similarity():
    data = request.json
    video_url = data.get('video_url')
    landmark_positions = data.get('landmark_positions')

    if not video_url or not landmark_positions:
        return jsonify({"error": "video_url and landmark_positions are required"}), 400

    video_url = BASE_URL + video_url

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

    compare_landmarks = analyze_service.get_landmark_position(video_url)

    similarity_percentage = analyze_service.get_analyze(base_landmarks, compare_landmarks)

    response_data = {
        "similarity": similarity_percentage
    }

    return jsonify(response_data), 200

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
