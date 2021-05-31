package univesp.pi5note.services;


import com.fazecast.jSerialComm.SerialPort;
import javafx.print.Printer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;


@Slf4j
@Component
public class ArduinoService {

    @Value("${arduino.port}")
    String port;

    public void send(String command) {
        SerialPort notePort = SerialPort.getCommPort(port);
        notePort.setComPortParameters(9600, 8, 1, 0);
        notePort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 1000, 0);
        notePort.openPort();

        if (notePort.openPort()) {
            System.out.println("Port is open :)");
        } else {
            System.out.println("Failed to open port :(");
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
            FileWriter file = new FileWriter("output.txt", true);
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.println(line);
            file.close();

        } catch (IOException e) {
            notePort.closePort();
            log.error(e.getMessage());
        }
        notePort.closePort();
        return Float.valueOf(line);
    }
}
