from flask import Flask, request, jsonify
from service import analyze_service

app = Flask(__name__)


@app.route('/similarity/landmark', methods=['POST'])
def get_landmarks():
    data = request.json
    video_url = data.get('video_url')

    if not video_url:
        return jsonify({"error": "video_url is required"}), 400

    # 비디오에서 랜드마크 추출
    landmarks = analyze_service.get_landmark_position(video_url)

    # 랜드마크 데이터를 JSON 형식으로 반환
    return jsonify({"landmarks": landmarks}), 200

@app.route('/similarity/analyze', methods=['POST'])
def analyze_similarity():
    data = request.json
    video_url = data.get('video_url')
    base_landmarks = data.get('base_landmarks')

    if not video_url or not base_landmarks:
        return jsonify({"error": "video_url and base_landmarks are required"}), 400

    # 비디오에서 랜드마크 추출
    compare_landmarks = analyze_service.get_landmark_position(video_url)

    # 두 랜드마크 세트를 비교하여 유사도 계산
    similarity_percentage = analyze_service.get_analyze(base_landmarks, compare_landmarks)

    # 유사도를 JSON 형식으로 반환
    return jsonify({"similarity_percentage": similarity_percentage}), 200

if __name__ == '__main__':
    app.run()
