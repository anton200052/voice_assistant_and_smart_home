package me.vasylkov.main_controller_module.component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import me.vasylkov.main_controller_module.services.VoiceRecognitionServerService;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.sound.sampled.*;

@Component
@RequiredArgsConstructor
public class MicrophoneListener {
    private final Logger logger;
    private final VoiceRecognitionServerService voiceRecognitionServerService;
    private final Object lock = new Object();

    private AudioFormat format;
    private DataLine.Info info;
    private TargetDataLine microphone;

    @Async
    public void startRecordingAndSendingMicAudioToServer() {
        initializeMicRecorder();
        startSendingMicAudioToServer();
    }

    private void initializeMicRecorder() {
        if (!AudioSystem.isLineSupported(info)) {
            logger.error("Микрофон не поддерживается");
            return;
        }

        if (voiceRecognitionServerService.isConnected()) {
            voiceRecognitionServerService.send(String.format("{ \"config\" : { \"sample_rate\" : %d } }", (int) format.getSampleRate()));
        }
    }

    private void startSendingMicAudioToServer() {
        try {
            microphone.open(format);
            microphone.start();

            byte[] buffer = new byte[1024];

                while (microphone.isOpen()) {
                    synchronized (lock) {
                        int bytesRead = microphone.read(buffer, 0, buffer.length);
                        if (bytesRead == -1) {
                            return;
                        }

                        if (!voiceRecognitionServerService.isConnected()) {
                            logger.error("Клиент отключен от сервиса распознавания речи.");
                            return;
                        }

                        voiceRecognitionServerService.send(buffer);
                    }
                }
        }
        catch (LineUnavailableException e) {
            logger.error("Ошибка отправки аудио на сервер: ", e);
        }
    }

    @PostConstruct
    public void init() {
        format = new AudioFormat(16000, 16, 1, true, false);
        info = new DataLine.Info(TargetDataLine.class, format);
        try {
            microphone = (TargetDataLine) AudioSystem.getLine(info);
        }
        catch (LineUnavailableException e) {
            logger.error("Микрофон недоступен: ", e);
        }
    }

    @PreDestroy
    public void destroy() {
        synchronized (lock) {
            if (microphone.isRunning()) {
                microphone.stop();
            }
            microphone.close();
        }
    }
}
