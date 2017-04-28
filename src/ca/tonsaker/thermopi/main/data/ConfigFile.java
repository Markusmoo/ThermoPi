package ca.tonsaker.thermopi.main.data;

import com.google.gson.annotations.Expose;

/**
 * Created by Marku on 2017-03-21.
 */
public class ConfigFile {

    public static final int TEMP_C = 0;
    public static final int TEMP_F = 1;
    public static final int TEMP_R = 2;
    public static final int TEMP_K = 3;

    public ConfigFile(){
        options = new Options();
    }

    @Expose public String[] zoneNames = {"Zone 1", "Zone 2", "Zone 3", "Zone 4", "Zone 5", "Zone 6"};

    @Expose public Options options;

    public class Options{
        @Expose public boolean isButtonTone;
        @Expose public boolean isKeypadTone;
        @Expose public boolean isConsoleColors;
        @Expose public boolean is12Hours;
        @Expose public boolean isDST;
        @Expose public int timeZoneUTC;
        @Expose public int temperatureUnit;
    }

}
