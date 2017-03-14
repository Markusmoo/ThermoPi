package ca.tonsaker.thermopi.main;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.SoftTone;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Markus Tonsaker on 2017-03-08.
 */
public class Utilities{

    private static GpioPinDigitalOutput soundPin;

    public static void tone(int durationMillis, int freq){
        Debug.println(Debug.LOW, "Playing tone.. Pin: " + Config.SPEAKER_PIN + ", Duration: " + durationMillis + " milliseconds");
        if(Config.debugMode) return;
        soundPin.pulse(durationMillis);
    }

    public static void buttonTone(){
        Debug.println(Debug.LOW, "Playing tone.. Pin: " + Config.SPEAKER_PIN + ", Duration: " + 100 + " milliseconds");
        if(Config.debugMode) return;
        soundPin.pulse(100);
    }

    public static void init(){
        if(!Config.debugMode){
            //soundPin = Main.gpio.provisionDigitalOutputPin(Config.SPEAKER_PIN, PinState.LOW);
        }
    }

}
