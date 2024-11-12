from fastapi import FastAPI
from src.routers.text_to_speech_router import router
app = FastAPI()

app.include_router(router)
