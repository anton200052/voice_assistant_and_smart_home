package me.vasylkov.main_controller_module.controller;

import lombok.RequiredArgsConstructor;
import me.vasylkov.main_controller_module.component.ModulesManager;
import me.vasylkov.main_controller_module.dto.AIProperty;
import me.vasylkov.main_controller_module.services.RequestToAIService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/configure")
public class ConfigureController
{
    private final ModulesManager modulesManager;
    private final RequestToAIService requestToAIService;

    @GetMapping()
    public String showMainMenu()
    {
        return "configure/main_menu";
    }

    @GetMapping("/health/list")
    public String showHealthList(Model model)
    {
        model.addAttribute("modules", modulesManager.getModules());
        return "configure/health_list";
    }

    @GetMapping("/ai-property/list")
    public String showAIPropertyList(Model model)
    {
        List<AIProperty> aiProperties = requestToAIService.getAIProperties();
        if (aiProperties == null || aiProperties.isEmpty())
        {
            model.addAttribute("errorMessage", "Ошибка получения настроек модуля ИИ. Возможно в модуле произошла ошибка, или он не был запущен.");
        }
        else
        {
            model.addAttribute("properties", aiProperties);
        }
        return "configure/ai_property_list";
    }

    @GetMapping("/ai-property/update-form")
    public String showPropertyChangeForm(@RequestParam(name = "propertyKey") String propertyKey, Model model)
    {
        AIProperty aiProperty = requestToAIService.getPropertyByKey(propertyKey);
        if (aiProperty == null)
        {
            model.addAttribute("errorMessage", "Ошибка получения настройки для отображения формы из БД.");
        }
        else
        {
            model.addAttribute("aiProperty", aiProperty);
        }
        return "configure/ai_property_update_form";
    }

    @PostMapping("/ai-property/save")
    public String saveProperty(@ModelAttribute("aiProperty") AIProperty property, RedirectAttributes redirectAttributes)
    {
        AIProperty checkProperty = requestToAIService.saveProperty(property);
        if (checkProperty == null)
        {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка сохранения настройки в базе данных модуля ИИ. Проверьте работоспособность сервиса.");
        }
        else
        {
            redirectAttributes.addFlashAttribute("successMessage", String.format("Настройка %s успешно изменена для модуля ИИ", property.getKey()));
        }
        return "redirect:/configure/ai-property/list";
    }

}
