from flask import Flask
from src.controllers.health_controller import handle_health_check
from src.controllers.text_to_speech_controller import handle_text_to_speech


def create_app():
    app = Flask(__name__)

    app.add_url_rule('/health', 'health_check', handle_health_check)
    app.add_url_rule('/text-to-speech', 'text_to_speech', handle_text_to_speech, methods=['POST'])

    return app


if __name__ == '__main__':
    app = create_app()
    app.run(host='0.0.0.0', port=8083)
