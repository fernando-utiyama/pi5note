package univesp.pi5note.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

    @Value("${profile.local}")
    private boolean local;

    @Value("${input.file}")
    private String inputFile;

    @Value("${output.file}")
    private String outputFile;

    @Scheduled(fixedDelay = 10000L)
    public void rotina() {
        if (local) {
            localRotina();
        } else {
            awsRotina();
        }
    }

    private void localRotina() {
        try {
            String command = readCommand();
            if (command != null) {
                String response = arduinoService.getResponse(command);
                log.info("Arduino response: " + response);
                writeFile(response);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void awsRotina() {
        for (RequisicaoDTO requisicao : awsService.getCommands()) {
            try {
                requisicao.setStatus("RUNNING");
                awsService.postResponse(requisicao);

                String response = arduinoService.getResponse(requisicao.getCommand());
                requisicao.setMedidas(response);
                requisicao.setStatus("FINISHED");

                awsService.postResponse(requisicao);
            } catch (Exception e) {
                requisicao.setStatus("ERROR");
                awsService.postResponse(requisicao);
                log.error(e.getMessage());
            }
        }
    }

    private String readCommand() throws Exception {
        String command =  null;
        try {
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader reader = new BufferedReader(fileReader);
            command = reader.readLine();
            log.info(command);
            return command;
        } catch (FileNotFoundException e) {
            log.info("Arquivo não encontrado");
        }
        return command;
    }

    private void writeFile(String medidas) throws Exception {
        PrintWriter printWriter = new PrintWriter(new FileWriter(outputFile));
        printWriter.println(medidas);
        printWriter.close();
    }

    private void deleteFiles() {
        File output = new File(outputFile);
        File input = new File(inputFile);

        output.delete();
        input.delete();
    }

}
