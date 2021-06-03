package univesp.pi5note.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import univesp.pi5note.services.AwsService;
import univesp.pi5note.services.RequisicaoDTO;

import java.util.List;

@RestController
@RequestMapping("/aws")
@Slf4j
public class AwsResource {

    @Autowired
    private AwsService awsService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/command")
    public List<RequisicaoDTO> getCommand() {
        return awsService.getCommands();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/response")
    public void postResponse(@RequestParam(name = "id") Long id) {
        RequisicaoDTO requisicaoDTO = new RequisicaoDTO();
        requisicaoDTO.setId(id);
        requisicaoDTO.setMedidas("Volume1: 132; Peso1: 100");
        awsService.postResponse(requisicaoDTO);
    }

}
