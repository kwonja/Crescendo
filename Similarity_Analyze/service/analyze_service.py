import numpy as np
import cv2
import mediapipe as mp
from fastdtw import fastdtw
from scipy.spatial.distance import euclidean

def get_video_length(video_path):
    cap = cv2.VideoCapture(video_path)
    if not cap.isOpened():
        return float('inf')
    length = int(cap.get(cv2.CAP_PROP_FRAME_COUNT)) / cap.get(cv2.CAP_PROP_FPS)
    cap.release()
    return length

def get_landmark_position(video_path):
    mp_pose = mp.solutions.pose
    cap = cv2.VideoCapture(video_path)

    if not cap.isOpened():
        print(f"Error: Could not open video file {video_path}")
        return []

    processed_landmarks = []

    with mp_pose.Pose(static_image_mode=False) as pose:
        while cap.isOpened():
            ret, frame = cap.read()
            if not ret:
                print("Finished reading the video or encountered an error.")
                break

            results = pose.process(cv2.cvtColor(frame, cv2.COLOR_BGR2RGB))

            if results.pose_landmarks:
                landmarks = [{'x': lm.x, 'y': lm.y, 'z': lm.z, 'visibility': lm.visibility} for lm in results.pose_landmarks.landmark]
                frame_data = {
                    'frame': int(cap.get(cv2.CAP_PROP_POS_FRAMES)),
                    'landmarks': landmarks
                }
                processed_landmarks.append(frame_data)

    cap.release()
    return processed_landmarks

def normalize_landmarks(landmarks):
    if not landmarks:
        return landmarks

    base_point = landmarks[0]
    normalized_landmarks = []

    for lm in landmarks:
        normalized_landmark = {
            'x': lm['x'] - base_point['x'],
            'y': lm['y'] - base_point['y'],
            'z': lm['z'] - base_point['z'],
            'visibility': lm['visibility']
        }
        normalized_landmarks.append(normalized_landmark)

    return normalized_landmarks

def calculate_dtw_similarity(base_landmarks, compare_landmarks):
    base_points = [[lm['x'], lm['y'], lm['z']] for lm in base_landmarks]
    compare_points = [[lm['x'], lm['y'], lm['z']] for lm in compare_landmarks]

    # FastDTW를 사용하여 DTW 거리 계산
    distance, _ = fastdtw(base_points, compare_points, dist=euclidean)

    # DTW 거리 값을 적절히 스케일링하여 유사도로 변환
    max_distance = np.sqrt(3)  # 최대 거리 (Euclidean 거리에서 최대 차원 수를 반영)
    similarity = np.exp(-distance / max_distance)  # 거리 값을 유사도로 변환 (0-1 사이)

    return similarity

def get_analyze(base_landmarks, compare_landmarks):
    total_similarity = 0

    for base_frame, compare_frame in zip(base_landmarks, compare_landmarks):
        base_landmarks_norm = normalize_landmarks(base_frame['landmarks'])
        compare_landmarks_norm = normalize_landmarks(compare_frame['landmarks'])

        frame_similarity = calculate_dtw_similarity(base_landmarks_norm, compare_landmarks_norm)
        total_similarity += frame_similarity

    similarity_percentage = total_similarity / min(len(base_landmarks), len(compare_landmarks))
    return similarity_percentage * 100  # 유사도를 0-100 범위로 변환하여 반환