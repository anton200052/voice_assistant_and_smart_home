
class RecognitionState:
    def __init__(self):
        self.is_recognizing = False

    def start(self):
        self.is_recognizing = True

    def stop(self):
        self.is_recognizing = False
