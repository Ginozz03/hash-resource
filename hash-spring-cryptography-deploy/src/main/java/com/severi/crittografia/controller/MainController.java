package com.severi.crittografia.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.md2.MD2;
import service.sha1.SHA1;

@Controller
@Slf4j
public class MainController {

    @GetMapping("/teoriasha")
    public String teoriaSha(){
        return "teoriasha";
    }

    @GetMapping("/teoriamd2")
    public String teoriaMD2(){
        return "teoriamd2";
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/sha1")
    public String sha1(){
        return "sha1";
    }

    @GetMapping("/md2")
    public String md5(){
        return "md2";
    }

    @PostMapping("/sha1")
    public String encryptSHA1(Model model, @RequestParam() String messaggio){
        if(messaggio == ""){
            return "sha1";
        }
        String message = SHA1.converti(messaggio);
        log.info("Messaggio in chiaro {}", messaggio);
        log.info("Messaggio criptato {}", message);
        model.addAttribute("messaggio", message);
        model.addAttribute("fasi", SHA1.getFasi());
        return "sha1";
    }

    @PostMapping("/md2")
    public String encryptMD2(Model model, @RequestParam() String messaggio){

        MD2 md2 = new MD2(messaggio);

        String message = md2.encrypt();

        if(messaggio == ""){
            return "md2";
        }
        log.info("Messaggio in chiaro {}", messaggio);
        log.info("Messaggio criptato {}", message);
        model.addAttribute("messaggio", message);
        model.addAttribute("fasi", md2.getFasi());
        return "md2";
    }

}
