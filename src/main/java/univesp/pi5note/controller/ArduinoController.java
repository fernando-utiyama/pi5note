package univesp.pi5note.controller;

import lombok.extern.slf4j.Slf4j;
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
public class ArduinoController {

    ArduinoService arduinoService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/arduino/command")
    public void sendCommand(@RequestParam(name = "command") String command) {
        arduinoService.send(command);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/arduino/response")
    public void getResponse() {
        arduinoService.getResponse();
    }

}
