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

    public void send(String command) {
        SerialPort notePort = SerialPort.getCommPort(port);
        notePort.setComPortParameters(rate, 8, 1, 0);
        notePort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 1000, 0);
        notePort.openPort();

        if (notePort.openPort()) {
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

    public String getResponse() {
        SerialPort notePort = SerialPort.getCommPort(port);
        notePort.openPort();

        notePort.setComPortParameters(rate, 8, 1, 0);
        notePort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);

        if (notePort.openPort()) {
            log.info("Port is open");
        } else {
            log.error("Failed to open port");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(notePort.getInputStream()));
        String line = null;
        try {
            line = reader.readLine();

        } catch (IOException e) {
            notePort.closePort();
            log.error(e.getMessage());
        }
        notePort.closePort();
        return line;
    }
}
