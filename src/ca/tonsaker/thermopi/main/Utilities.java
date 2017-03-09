package ca.tonsaker.thermopi.main;

import com.pi4j.wiringpi.SoftTone;

import javax.swing.*;

/**
 * Created by Marku on 2017-03-08.
 */
public class Utilities{

    public static void tone(int pin, int durationMillis, int freq){
        if(Config.NON_PI_DEBUG){
            System.out.println("Playing tone.. Pin: " + pin + ", Duration: " + durationMillis + " milliseconds, Frequency: " + freq + " hz");
            return;
        }
        SoftTone.softToneWrite(pin, freq);
        Timer delay = new Timer(durationMillis, e -> SoftTone.softToneStop(pin));
        delay.start();
    }

}
