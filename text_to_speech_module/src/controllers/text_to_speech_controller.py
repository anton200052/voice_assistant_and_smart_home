from flask import request, send_file, jsonify
from src.infrastructure.text_to_speech_service import text_to_speech
import asyncio


def handle_text_to_speech():
    data = request.get_json()

    if not data or 'text' not in data:
        return jsonify({"error": "Missing 'text' in request body"}), 400

    text = data.get('text')
    audio_file = asyncio.run(text_to_speech(text))
    return send_file(audio_file, mimetype='audio/mpeg', as_attachment=True, download_name="audio.mp3")
