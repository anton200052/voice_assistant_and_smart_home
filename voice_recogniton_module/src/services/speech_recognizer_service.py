import yaml
from vosk import Model, KaldiRecognizer

with open("config/config.yaml", "r") as file:
    config = yaml.safe_load(file)
model_name = config["model"]["model_name"]

model = Model(model_name=model_name)


async def initialize_recognizer(rate):
    recognizer = KaldiRecognizer(model, rate)
    recognizer.SetWords(True)
    recognizer.SetPartialWords(True)
    return recognizer


async def recognize_data_chunk(recognizer, data):
    if recognizer.AcceptWaveform(data):
        return recognizer.Result()
    return None
