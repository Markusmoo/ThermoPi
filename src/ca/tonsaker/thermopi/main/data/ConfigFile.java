package ca.tonsaker.thermopi.main.data;

import com.google.gson.annotations.Expose;

/**
 * Created by Marku on 2017-03-21.
 */
public class ConfigFile {

    @Expose public String[] zoneNames;

    @Expose public Options options;

    public class Options{
        @Expose public boolean buttonTone;
        @Expose public boolean keypadTone;
        @Expose public boolean consoleColors;
    }

}
