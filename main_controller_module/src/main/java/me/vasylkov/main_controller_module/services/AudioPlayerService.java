package me.vasylkov.main_controller_module.services;

import java.nio.file.Path;

public interface AudioPlayerService
{
    void play(Path filePath, boolean deleteAfter);
}
