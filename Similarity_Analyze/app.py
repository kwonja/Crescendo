from flask import Flask
from service import analyze_service

app = Flask(__name__)


@app.route('/')
def hello_world():
    video_url = "C:\\Users\\SSAFY\\Downloads\\구구단챌린지1.mp4"
    return analyze_service.get_landmark_position(video_url)


if __name__ == '__main__':
    app.run()
