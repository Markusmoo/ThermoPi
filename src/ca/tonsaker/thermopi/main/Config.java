package ca.tonsaker.thermopi.main;

import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import java.awt.*;

/**
 * Created by Markus Tonsaker on 2017-03-08.
 */
public class Config {

    //Settings File Location
    public static final String SETTINGS_LINUX = "~/thermopi/config.json";
    public static final String SETTINGS_WINDOWS = "%appdata%/thermopi/config.json";
    public static final String SETTINGS_OSX = "~/Library/Preferences/thermopi/config.json";

    //Modes
    public static boolean debugMode = false;
    public static boolean safeMode = false;

    //Pins
    public static final Pin SPEAKER_PIN = RaspiPin.GPIO_27;

    //Tone frequencies
    public static final int BUTTON_TONE = 1047;  //C6

    //Colour
    public static final Color COLOR_TEXT_RED = new Color(100, 0 ,0);
    public static final Color COLOR_TEXT_WHITE = new Color(187, 187, 187);
    public static final Color COLOR_TEXT_GREEN = new Color(50, 110, 50);
    public static final Color COLOR_TEXT_CYAN = new Color(85, 222, 255);
    public static final Color COLOR_TEXT_YELLOW = Color.YELLOW;
    public static final Color COLOR_TEXT_BLUE = new Color(0, 0, 100);


    //Console Colour
    public static Color CONSOLE_COLOR = COLOR_TEXT_WHITE;

    //Status States
    public static final int STATUS_UNARMED = -1;
    public static final int STATUS_ARMED_AWAY = 0;
    public static final int STATUS_ARMED_HOME = 0;

    //Status
    public static int STATUS = STATUS_ARMED_HOME;

}
