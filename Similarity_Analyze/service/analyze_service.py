import mediapipe as mp
import cv2
import numpy as np

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

def calculate_cosine_similarity(landmark1, landmark2):
    vector1 = np.array([landmark1['x'], landmark1['y'], landmark1['z']])
    vector2 = np.array([landmark2['x'], landmark2['y'], landmark2['z']])
    norm1 = np.linalg.norm(vector1)
    norm2 = np.linalg.norm(vector2)

    if norm1 == 0 or norm2 == 0:
        return 0.0

    dot_product = np.dot(vector1, vector2)
    return dot_product / (norm1 * norm2)

