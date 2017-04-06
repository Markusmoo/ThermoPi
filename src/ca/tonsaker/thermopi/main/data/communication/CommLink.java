package ca.tonsaker.thermopi.main.data.communication;

import com.pi4j.io.serial.*;

import java.io.IOException;

/**
 * Created by Markus Tonsaker on 2017-03-19.
 *
 * By default, the serial port on the Raspberry Pi is configured as a console port for communicating with the
 * Linux OS shell. If you want to use the serial port in a software program,
 * you must disable the OS from using this port. Please see this blog article by Clayton Smith
 * for step-by-step instructions on how to disable the OS console for this port:
 * http://www.irrational.net/2012/04/19/using-the-raspberry-pis-serial-port/
 */
public class CommLink implements SerialDataEventListener{

    Serial serial;
    SerialConfig serialConfig;

    public CommLink(){
        serial = SerialFactory.createInstance();
        //TODO Finish implementation
    }

    public void setupSerial(){
        serialConfig = new SerialConfig();
        try {
            serialConfig.device(SerialPort.getDefaultPort()).baud(Baud._9600)
                    .dataBits(DataBits._8)
                    .parity(Parity.NONE)
                    .stopBits(StopBits._1)
                    .flowControl(FlowControl.NONE);
        }catch (IOException e){
            //TODO Debug Error
            e.printStackTrace();
        }catch (InterruptedException e2) {
            //TODO Debug Error
            e2.printStackTrace();
        }
        serial.addListener(this);
        try {
            serial.open(serialConfig);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendData(String str){

    }

    @Override
    public void dataReceived(SerialDataEvent serialDataEvent) {
        //serialDataEvent.
    }
}
