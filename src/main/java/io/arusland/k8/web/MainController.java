package io.arusland.k8.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ruslan on 27.06.2015.
 */
@Controller
public class MainController {

    @ResponseBody
    @RequestMapping("/")
    public String hello(){
        return "I'm fine!";
    }
}
