import io
import os
import uuid
import edge_tts
import yaml

with open("config/config.yaml", "r") as file:
    config = yaml.safe_load(file)
voice_name = config["voice"]["voice_name"]


async def text_to_speech(text):
    unique_filename = f"output_{uuid.uuid4()}.mp3"

    communicate = edge_tts.Communicate(text, voice=voice_name)

    await communicate.save(unique_filename)

    with open(unique_filename, "rb") as audio_file:
        audio_data = io.BytesIO(audio_file.read())

    audio_data.seek(0)

    os.remove(unique_filename)

    return audio_data
