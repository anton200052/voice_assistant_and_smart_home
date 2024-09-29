package me.vasylkov.main_controller_module.services;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class AudioPlayerServiceImp implements AudioPlayerService
{
    @Override
    public void play(Path path, boolean deleteAfter)
    {
        try (FileInputStream fis = new FileInputStream(path.toFile()))
        {
            Player player = new Player(fis);
            player.play();

            if (deleteAfter)
            {
                Files.deleteIfExists(path);
            }
        }
        catch (JavaLayerException | IOException e)
        {
            e.printStackTrace();
        }
    }
}
