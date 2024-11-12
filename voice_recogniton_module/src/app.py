from fastapi import FastAPI
from src.routers.audio_bytes_router import router
app = FastAPI()

app.include_router(router)
