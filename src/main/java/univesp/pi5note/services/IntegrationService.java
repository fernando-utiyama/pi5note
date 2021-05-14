package univesp.pi5note.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class IntegrationService {

    @Autowired
    private AwsService awsService;

    @Autowired
    private ArduinoService arduinoService;

//    @Scheduled(fixedDelay = 10000L)
    public void rotina() throws InterruptedException {
        for (RequisicaoDTO requisicao : awsService.getCommands()) {
            arduinoService.send(requisicao.getCommand());

            Thread.sleep(30000L);

            requisicao.setPeso(arduinoService.getResponse());
            requisicao.setStatus("FINISHED");

            awsService.postResponse(requisicao);

            arduinoService.send("RESTART");
        }
    }
}
