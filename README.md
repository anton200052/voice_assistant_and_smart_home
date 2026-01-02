# Marcus Voice Assistant

![Java](https://img.shields.io/badge/Java-Spring_Boot-orange)
![Python](https://img.shields.io/badge/Python-FastAPI-blue)
![Architecture](https://img.shields.io/badge/Architecture-Microservices-purple)

**Marcus** is an open-source, modular voice assistant built on a robust micro-service architecture. It combines the enterprise reliability of **Java Spring Boot** with the agility of **Python FastAPI** to deliver natural conversation and seamless smart home control.

## 🏆 Achievements & Role

**🥈 2nd Place at IDEAFEST 2024** This project secured the 2nd place award at the *IDEAFEST 2024 International Student Competition*, selected from over **50 participating teams**.

> **Note on Contribution:** I acted as the **Sole Developer** for this project, responsible for the entire technical architecture, backend implementation, and hardware integration. My teammates focused on the business model validation and product presentation.

---

## 🏗️ Architecture Overview

The system is strictly divided into two logical parts: **Client-side** (Hardware/Controller) and **Server-side** (Core Intelligence).

### 1. The Client
Acts as the physical interface. It handles audio input/output and routes raw data to the server.
* **Main Controller:** Java-based orchestrator.

### 2. The Server
Hosts the heavy computing and logic modules.
* **AI Engine:** Processes natural language.
* **Voice Processing:** Python microservices for STT (Speech-to-Text) and TTS (Text-to-Speech).
* **Smart Home Hub:** Manages Zigbee devices.

---

## ✨ Key Features

* **Conversational AI:** Natural, context-aware dialogue powered by a custom Spring Boot AI engine.
* **OpenAI Function Calling:** Leverages LLM Function Calling to intelligently translate natural language requests (e.g., *"Turn off the lights in the kitchen"*) into structured Zigbee commands.
* **Zigbee Smart Home Control:** Native integration via Zigbee2MQTT to control lights, thermostats, plugs, and sensors.
* **Scalable Micro-services:** Independent modules deployed via Docker, allowing for polyglot development (Java & Python) and independent scaling.

---

## 🗂️ Module Breakdown

| Module | Stack | Type | Description |
| :--- | :--- | :--- | :--- |
| **`main_controller_module`** | Java / Spring Boot | **Client** | The physical device controller. Routes audio streams and requests between the user and the server ecosystem. |
| **`ai_module`** | Java / Spring Boot | **Server** | The "Brain". Handles intent classification, dialogue management, and decision making. |
| **`smart_home_module`** | Java / Spring Boot | **Server** | Executes device commands via **Zigbee2MQTT**. Uses declarative mapping to manage device states. |
| **`voice_recognition_module`** | Python / FastAPI | **Server** | Speech-to-Text (STT) micro-service powered by **Vosk**. High-performance offline recognition. |
| **`text_to_speech_module`** | Python / FastAPI | **Server** | Text-to-Speech (TTS) micro-service powered by **Edge TTS** for natural-sounding voice synthesis. |

---
## 🏆 Awards & Certificates

| 🥈 2nd Place Diploma | 📜 Participation Certificate |
| :---: | :---: |
| <img src="https://github.com/user-attachments/assets/a0274059-d377-4db6-83f8-d9d2e821ea0d" width="100%"> | <img src="https://github.com/user-attachments/assets/4af1900d-0561-421e-95d6-2216cb47be88" width="100%"> |
| *Diploma for securing 2nd place at IDEAFEST 2024* | *Certificate of participation in the international competition* |
