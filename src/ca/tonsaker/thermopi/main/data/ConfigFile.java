package ca.tonsaker.thermopi.main.data;

import com.google.gson.annotations.Expose;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

import java.awt.*;
import java.util.Optional;

/**
 * Created by Markus Tonsaker on 2017-03-21.
 */
public class ConfigFile {

    public static boolean screenOn = true;

    public static final int TEMP_C = 0;
    public static final int TEMP_F = 1;
    public static final int TEMP_R = 2;
    public static final int TEMP_K = 3;

    //Display
    public static int screenTimeoutTime = 15;

    //Settings File Location
    public static final String SETTING_FILENAME = "config.json";
    public static final String SETTINGS_LINUX = System.getProperty("user.home") + "/ThermoPi/"; //TODO Test
    public static final String SETTINGS_WINDOWS = System.getenv("APPDATA") + "\\ThermoPi\\";
    public static final String SETTINGS_OSX = "~/Library/Preferences/ThermoPi/"; //TODO Test

    //Modes
    public static boolean debugMode = false;
    public static boolean safeMode = false;

    //Pins
    public static final Pin PIN_SCREEN = RaspiPin.GPIO_26;
    public static final Pin PIN_SPEAKER = RaspiPin.GPIO_27;
    public static final Pin PIN_HOME = RaspiPin.GPIO_28;
    public static final Pin PIN_TEMP = RaspiPin.GPIO_29;

    //Tone frequencies
    public static final int BUTTON_TONE = 1047;  //C6

    //Colour
    public static final Color COLOR_TEXT_RED = new Color(100, 0 ,0);
    public static final Color COLOR_TEXT_WHITE = new Color(187, 187, 187);
    public static final Color COLOR_TEXT_GREEN = new Color(50, 110, 50);
    public static final Color COLOR_TEXT_CYAN = new Color(85, 222, 255);
    public static final Color COLOR_TEXT_YELLOW = Color.YELLOW;
    public static final Color COLOR_TEXT_BLUE = new Color(0, 0, 100);

    //Status States
    public static final int STATUS_UNARMED = -1;
    public static final int STATUS_ARMED_AWAY = 0;
    public static final int STATUS_ARMED_HOME = 0;

    //Status
    public static int STATUS = STATUS_ARMED_HOME;

    public void setSettingsVariables(ConfigFile configFile){
        if(configFile == null) return;
        this.zoneNames = configFile.zoneNames;
        options.isButtonTone = configFile.options.isButtonTone;
        options.isKeypadTone = configFile.options.isKeypadTone;
        options.isConsoleColors = configFile.options.isConsoleColors;
        options.timeZoneUTC = configFile.options.timeZoneUTC;
        options.temperatureUnit = configFile.options.temperatureUnit;
        options.isDST = configFile.options.isDST;
        options.is12Hours = configFile.options.is12Hours;
        options.isTempsOn = configFile.options.isTempsOn;
    }

    @Expose public String[] zoneNames = {"Zone 1", "Zone 2", "Zone 3", "Zone 4", "Zone 5", "Zone 6"};

    @Expose public Options options = new Options();

    public class Options{
        @Expose public boolean isButtonTone = false;
        @Expose public boolean isKeypadTone = true;
        @Expose public boolean isConsoleColors = true;
        @Expose public boolean is12Hours = true;
        @Expose public boolean isDST = false;
        @Expose public int timeZoneUTC = 0;
        @Expose public int temperatureUnit = ConfigFile.TEMP_C;
        @Expose public boolean[] isTempsOn = {false, false, false};  //In order of Outside, Inside, Furnace
    }

}
