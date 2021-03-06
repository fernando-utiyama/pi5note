package univesp.pi5note.services;


import com.fazecast.jSerialComm.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;


@Slf4j
@Component
public class ArduinoService {

    @Value("${arduino.port}")
    String port;

    @Value("${arduino.rate}")
    int rate;

    @Value("${arduino.open.port.time}")
    int openPortTime;

    @Value("${arduino.sleeptime}")
    int sleeptime;

    public void send(String command) {
        SerialPort notePort = SerialPort.getCommPort(port);
        notePort.setComPortParameters(rate, 8, 1, 0);
        notePort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 1000, 0);
        notePort.openPort();

        if (notePort.isOpen()) {
            log.info("Port is open");
        } else {
            log.error("Failed to open port");
        }

        try {
            OutputStream outputStream = notePort.getOutputStream();
            outputStream.write(command.getBytes());
            outputStream.flush();

            notePort.closePort();
        } catch (IOException e) {
            notePort.closePort();
            log.error(e.getMessage());
        }
    }

    public String getResponse(String command) throws InterruptedException {
        String line = null;

        SerialPort notePort = SerialPort.getCommPort(port);
        notePort.setComPortParameters(rate, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        notePort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 1000, 0);
        notePort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 25000, 1000);
        notePort.openPort();

        if (notePort.isOpen()) {
            log.info("Port is open");
            Thread.sleep(openPortTime);
        } else {
            log.error("Failed to open port");
        }

        try {
            OutputStream outputStream = notePort.getOutputStream();
            outputStream.write(command.getBytes());
            outputStream.flush();
            log.info("Comando enviado: " + command);

        } catch (Exception e) {
            notePort.closePort();
            log.error(e.getMessage());
        }
        log.info("Aguardando (ms): " + sleeptime);
        Thread.sleep(sleeptime);

        log.info("Aguardando medida");
        BufferedReader reader = new BufferedReader(new InputStreamReader(notePort.getInputStream()));
        try {
            line = reader.readLine();
            log.info("Medida realizada: " + line);
        } catch (Exception e) {
            notePort.closePort();
            log.error(e.getMessage());
        }
        notePort.closePort();
        return line;
    }
}
