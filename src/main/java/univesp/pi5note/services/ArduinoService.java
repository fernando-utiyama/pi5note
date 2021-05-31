package univesp.pi5note.services;


import com.fazecast.jSerialComm.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


@Slf4j
@Component
public class ArduinoService {

    @Value("${arduino.port}")
    String port;

    public void send(String command) {
        SerialPort notePort = SerialPort.getCommPort(port);
        notePort.setComPortParameters(9600, 8, 1, 0);
        notePort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);
        notePort.openPort();

        if (notePort.openPort()) {
            System.out.println("Port is open :)");
        } else {
            System.out.println("Failed to open port :(");
        }

        try {
            notePort.getOutputStream().write(command.getBytes());
            notePort.getOutputStream().flush();
            notePort.closePort();
        } catch (IOException e) {
            notePort.closePort();
            log.error(e.getMessage());
        }
    }

    public Float getResponse() {
        SerialPort notePort = SerialPort.getCommPort(port);
        notePort.openPort();

        notePort.setComPortParameters(9600, 8, 1, 0);
        notePort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 0);

        if (notePort.openPort()) {
            System.out.println("Port is open :)");
        } else {
            System.out.println("Failed to open port :(");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(notePort.getInputStream()));

        String line = "0";
        try {
            line = reader.readLine();
            log.info(line);
        } catch (IOException e) {
            notePort.closePort();
            log.error(e.getMessage());
        }
        notePort.closePort();
        return Float.valueOf(line);
    }
}
