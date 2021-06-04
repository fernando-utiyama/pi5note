package univesp.pi5note.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import univesp.pi5note.services.ArduinoService;

@RestController
@RequestMapping("/arduino")
@Slf4j
public class ArduinoResource {

    @Autowired
    private ArduinoService arduinoService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/command")
    public void sendCommand(@RequestParam(name = "command") String command) {
        arduinoService.send(command);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/response")
    public void getResponse() throws InterruptedException {
        arduinoService.getResponse("1111");
    }

}
