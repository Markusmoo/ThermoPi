package ca.tonsaker.thermopi.main.data;

import com.google.gson.annotations.Expose;

/**
 * Created by Marku on 2017-03-21.
 */
public class ConfigFile {

    public ConfigFile(){
        options = new Options();
    }

    @Expose public String[] zoneNames = {"Zone 1", "Zone 2", "Zone 3", "Zone 4", "Zone 5", "Zone 6"};

    @Expose public Options options;

    public class Options{
        @Expose public boolean buttonTone;
        @Expose public boolean keypadTone;
        @Expose public boolean consoleColors;
    }

}
