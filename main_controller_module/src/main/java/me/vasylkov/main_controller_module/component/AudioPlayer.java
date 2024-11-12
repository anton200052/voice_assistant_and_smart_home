package me.vasylkov.main_controller_module.component;

import jakarta.annotation.PreDestroy;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor
public class AudioPlayer {
    private final Logger logger;
    private volatile Future<?> currentTask;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private volatile Player currentPlayer;

    public void play(Path path, boolean deleteAfter) {
        if (currentPlayer != null) {
            currentPlayer.close();
        }

        if (currentTask != null && !currentTask.isDone()) {
            currentTask.cancel(true);
        }

        currentTask = executorService.submit(() -> {
            try (FileInputStream fis = new FileInputStream(path.toFile())) {
                Player player = new Player(fis);
                currentPlayer = player;

                player.play();

                if (deleteAfter) {
                    Files.deleteIfExists(path);
                }
            }
            catch (JavaLayerException | IOException e) {
                logger.error("Ошибка озвучивания аудиофайла", e);
            }
            finally {
                currentPlayer = null;
            }
        });
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdownNow();
    }
}
