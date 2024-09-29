package me.vasylkov.main_controller_module.services;

import java.nio.file.Path;

public interface RequestToTTSService
{
    Path requestToTTS(String text);
}
