package ca.tonsaker.thermopi.main.data;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import com.pi4j.io.serial.SerialFactory;

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

    public CommLink(){
        serial = SerialFactory.createInstance();
        //TODO Finish implementation
    }


    @Override
    public void dataReceived(SerialDataEvent serialDataEvent) {

    }
}
