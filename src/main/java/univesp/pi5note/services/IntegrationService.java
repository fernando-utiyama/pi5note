package univesp.pi5note.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.PrintWriter;

@Slf4j
@Component
@EnableScheduling
public class IntegrationService {

    @Autowired
    private AwsService awsService;

    @Autowired
    private ArduinoService arduinoService;

    @Scheduled(fixedDelay = 5000L)
    public void rotina() {
        for (RequisicaoDTO requisicao : awsService.getCommands()) {
            try {
                requisicao.setStatus("RUNNING");
                awsService.postResponse(requisicao);

                String response = arduinoService.getResponse(requisicao.getCommand());
                requisicao.setMedidas(response);
                requisicao.setStatus("FINISHED");

                awsService.postResponse(requisicao);

                writeFile(requisicao);
            } catch (Exception e) {
                requisicao.setStatus("ERROR");
                awsService.postResponse(requisicao);
                log.error(e.getMessage());
            }
        }
    }

    private void writeFile(RequisicaoDTO requisicao) {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter("output.txt"))) {
            printWriter.println(requisicao.command);
            printWriter.println(requisicao.medidas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
