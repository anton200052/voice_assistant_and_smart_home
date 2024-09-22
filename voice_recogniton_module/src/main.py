from flask import Flask
from src.controllers.health_controller import health_check
from src.controllers.recognition_controller import start_recognition_route
from src.controllers.recognition_controller import stop_recognition_route


def create_app():
    app = Flask(__name__)


    app.add_url_rule('/health', 'health_check', health_check)
    app.add_url_rule('/recognition/start', 'start_recognition', start_recognition_route)
    app.add_url_rule('/recognition/stop', 'stop_recognition', stop_recognition_route)

    return app


if __name__ == '__main__':
    app = create_app()
    app.run(host='0.0.0.0', port=5000)
