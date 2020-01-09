package com.together.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 请求测试类
 * @author maxiaoguang
 */

@Slf4j
@Controller
public class PageController {

    @GetMapping("/order")
    public String test5(Model model){
        int number =  (int)(Math.random()*100)+1 ;
        model.addAttribute("userId",number);
       return  "order";
    }

}
