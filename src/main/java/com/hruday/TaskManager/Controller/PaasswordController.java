package com.hruday.TaskManager.Controller;

import com.hruday.TaskManager.Service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/password")
public class PaasswordController {

    @Autowired
    private PasswordService passwordService;



}
