import json

import pyaudio
from vosk import Model, KaldiRecognizer, SetLogLevel

from src.data_senders.data_sender import sendRecognizedText

assistant_name = "маркус"


#2
def start_recognizer(state):
    SetLogLevel(0)

    dataformat = pyaudio.paInt16  # Формат данных (16 бит)
    channels = 1  # Количество каналов (моно)
    rate = 16000  # Частота дискретизации (16 kHz)
    chunk = 1024

    audio = pyaudio.PyAudio()
    stream = audio.open(format=dataformat,
                        channels=channels,
                        rate=rate,
                        input=True,
                        frames_per_buffer=chunk)
    model = Model(model_name="vosk-model-small-ru-0.22")
    rec = KaldiRecognizer(model, rate)
    rec.SetWords(True)
    rec.SetPartialWords(True)

    print("Начинается захват аудио...")

    try:
        trigger_word = None

        while state.is_recognizing:
            data = stream.read(chunk)
            if len(data) == 0:
                break
            if rec.AcceptWaveform(data):
                result_json = rec.Result()
                result_dict = json.loads(result_json)
                recognized_text = result_dict.get("text", "")

                if recognized_text and (assistant_name in recognized_text or trigger_word is not None):
                    if trigger_word is not None:
                        recognized_text = trigger_word + " " + recognized_text
                        trigger_word = None

                    if recognized_text == assistant_name:
                        trigger_word = recognized_text

                    sendRecognizedText(recognized_text)

    except KeyboardInterrupt:
        print("Завершение программы...")

    finally:
        stream.stop_stream()
        stream.close()
        audio.terminate()
