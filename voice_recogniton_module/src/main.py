from flask import Flask
from src.controllers.health_controller import health_check
from src.infrastructure.recognition_starter import start_recognition


def create_app():
    app = Flask(__name__)

    app.add_url_rule('/health', 'health_check', health_check)
    start_recognition()
    return app


if __name__ == '__main__':
    app = create_app()
    app.run(host='0.0.0.0', port=8081)
