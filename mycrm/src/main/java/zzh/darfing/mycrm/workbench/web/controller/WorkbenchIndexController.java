package zzh.darfing.mycrm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/workbench")
public class WorkbenchIndexController {

    @RequestMapping("/index.do")
    public String index() {
        return "workbench/index";
    }
}
