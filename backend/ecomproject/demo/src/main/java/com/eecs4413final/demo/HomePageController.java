package com.eecs4413final.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomePageController {

    @GetMapping("/index")
    public String index(@RequestParam(value = "name", defaultValue = "index") String name) {
      return String.format("Hello, welcome to our store %s!", name);
    }
}
