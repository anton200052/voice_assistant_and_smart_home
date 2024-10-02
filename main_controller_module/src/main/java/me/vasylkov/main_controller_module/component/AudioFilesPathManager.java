package me.vasylkov.main_controller_module.component;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
@RequiredArgsConstructor
public class AudioFilesPathManager
{
    private final Logger logger;

    public Path getAudioPathFromResources(String fileName)
    {
        ClassPathResource resource = new ClassPathResource("audio/" + fileName);
        Path tempFile = null;

        try (InputStream inputStream = resource.getInputStream())
        {
            tempFile = Files.createTempFile("audio_", ".mp3");
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e)
        {
            logger.error("Ошибка получения аудиофайла из ресурсов приложения", e);
        }

        return tempFile;
    }
}
