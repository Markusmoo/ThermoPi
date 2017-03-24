package ca.tonsaker.thermopi.main;

import ca.tonsaker.thermopi.main.data.ConfigFile;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import java.awt.*;

/**
 * Created by Markus Tonsaker on 2017-03-08.
 */
public class Config {

    //Settings File Location
    public static final String SETTING_FILENAME = "config.json";
    public static final String SETTINGS_LINUX = "~\\ThermoPi\\"; //TODO Test
    public static final String SETTINGS_WINDOWS = System.getenv("APPDATA") + "\\ThermoPi\\";
    public static final String SETTINGS_OSX = "~\\Library\\Preferences\\ThermoPi\\"; //TODO Test

    //Time
    public static int timezone = -7;
    public static boolean timeFormat12hour = false;

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

    //**********SETTINGS***********
    public static String[] zoneNames = {""};

    public static boolean buttonTone = false;
    public static boolean keypadTone = false;
    public static boolean consoleColors = false;

    public static void setSettingsVariables(ConfigFile configFile){
        if(configFile == null) return;
        Config.zoneNames = configFile.zoneNames;
        Config.buttonTone = configFile.options.buttonTone;
        Config.keypadTone = configFile.options.keypadTone;
        Config.consoleColors = configFile.options.consoleColors;
    }

    public static ConfigFile createConfigFile(){
        ConfigFile cfg = new ConfigFile();
        cfg.zoneNames = Config.zoneNames;
        cfg.options.buttonTone = Config.buttonTone;
        cfg.options.keypadTone = Config.keypadTone;
        cfg.options.consoleColors = Config.consoleColors;
        return cfg;
    }

}
