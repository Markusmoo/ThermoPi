package ca.tonsaker.thermopi.main;

import com.pi4j.wiringpi.SoftTone;

import javax.swing.*;

/**
 * Created by Markus Tonsaker on 2017-03-08.
 */
public class Utilities{

    public static void tone(int pin, int durationMillis, int freq){
        Debug.println(Debug.LOW, "Playing tone.. Pin: " + pin + ", Duration: " + durationMillis + " milliseconds, Frequency: " + freq + " hz");
        if(Config.debugMode) return;

        SoftTone.softToneWrite(pin, freq);
        Timer delay = new Timer(durationMillis, e -> SoftTone.softToneStop(pin));
        delay.start();
    }

}
