# Marcus Voice Assistant

Marcus is an **open‑source, modular voice assistant** built on a micro‑service architecture that combines the robustness of **Java Spring Boot** with the agility of **Python FastAPI**.  
It can hold natural, free‑form conversations like a real person and seamlessly manage Zigbee‑based smart‑home devices.

---

## ✨ Key Features

* **Conversational AI** – Natural dialogue powered by an AI engine written in Spring Boot.  
* **Smart‑Home Control** – Native Zigbee support to switch lights, thermostats, plugs and more.  
* **Scalable Micro‑services** – Independent modules that you can deploy, scale and update separately.  

---

## 🗂️ Repository Layout

| Module (folder) | Language / Framework | Purpose | Client / Server side |
|-----------------|----------------------|---------|----------------------|
| **`main_controller_module`** | Java 21 / Spring Boot | Routes requests between micro‑services | Client side |
| **`ai_module`** | Java 21 / Spring Boot | Core intelligence: intent classification, dialogue management | Server side |
| **`smart_home_module`** | Java 21 / Spring Boot | Smart‑home integration via Zigbee (Zigbee2MQTT) & declarative device mapping | Server side |
| **`voice_recognition_module`** | Python 3.10 / FastAPI | Speech‑to‑Text (STT) micro‑service built on Vosk | Server side |
| **`text_to_speech_module`** | Python 3.10 / FastAPI | Text‑to‑Speech (TTS) micro‑service powered by Edge TTS | Server side |
