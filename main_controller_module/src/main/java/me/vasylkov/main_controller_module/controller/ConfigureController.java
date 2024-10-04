package me.vasylkov.main_controller_module.controller;

import lombok.RequiredArgsConstructor;
import me.vasylkov.main_controller_module.component.ModulesManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/configure")
public class ConfigureController
{
    private final ModulesManager modulesManager;

    @GetMapping()
    public String showMainMenu()
    {
        return "configure/main_menu";
    }

    @GetMapping("/health-list")
    public String showHealthList(Model model)
    {
        model.addAttribute("modules", modulesManager.getModules());
        return "configure/health_list";
    }
}
