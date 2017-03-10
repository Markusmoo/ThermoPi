package ca.tonsaker.thermopi.main;

import java.awt.*;

/**
 * Created by Markus Tonsaker on 2017-03-08.
 */
public class Config {

    //Debug
    public static final boolean debugMode = true;

    //Pins
    public static final int SPEAKER_PIN = 17;

    //Tone frequencies
    public static final int BUTTON_TONE = 1047;  //C6

    //Colour
    public static final Color COLOR_TEXT_RED = new Color(100, 0 ,0);
    public static final Color COLOR_TEXT_WHITE = new Color(187, 187, 187);
    public static final Color COLOR_TEXT_GREEN = new Color(50, 110, 50);

    //Status States
    public static final int STATUS_UNARMED = -1;
    public static final int STATUS_ARMED_AWAY = 0;
    public static final int STATUS_ARMED_HOME = 0;
    //public static final int STATUS_ = 0; TODO


    //Status
    public static int STATUS = STATUS_ARMED_HOME;

}
