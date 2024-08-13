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

    base_point = landmarks[0]  # 첫 번째 랜드마크(예: 코)를 기준으로 설정
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

def calculate_euclidean_distance(landmark1, landmark2):
    dist = np.sqrt((landmark1['x'] - landmark2['x']) ** 2 +
                   (landmark1['y'] - landmark2['y']) ** 2 +
                   (landmark1['z'] - landmark2['z']) ** 2)
    return dist

def calculate_dtw_similarity(base_landmarks, compare_landmarks):
    base_points = [[lm['x'], lm['y'], lm['z']] for lm in base_landmarks]
    compare_points = [[lm['x'], lm['y'], lm['z']] for lm in compare_landmarks]

    distance, _ = fastdtw(base_points, compare_points, dist=euclidean)
    return 1 / (1 + distance)

def nonlinear_scaling(similarity):
    return np.sqrt(similarity) * 100

# 두 비디오의 유사도 분석
def get_analyze(base_landmarks, compare_landmarks):
    base_length = len(base_landmarks)
    compare_length = len(compare_landmarks)

    max_length = max(base_length, compare_length)
    total_similarity = 0

    for i in range(max_length):
        if i < base_length and i < compare_length:
            base_landmarks_norm = normalize_landmarks(base_landmarks[i]['landmarks'])
            compare_landmarks_norm = normalize_landmarks(compare_landmarks[i]['landmarks'])

            frame_similarity = calculate_dtw_similarity(base_landmarks_norm, compare_landmarks_norm)
        else:
            frame_similarity = 0.1

        total_similarity += frame_similarity

    similarity_percentage = (total_similarity / max_length)
    scaled_similarity = nonlinear_scaling(similarity_percentage)
    return scaled_similarity
