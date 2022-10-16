package zzh.darfing.mycrm.settings.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    @RequestMapping("/toIndex.do")
    public String toIndex() {
        return "settings/index";
    }
}
