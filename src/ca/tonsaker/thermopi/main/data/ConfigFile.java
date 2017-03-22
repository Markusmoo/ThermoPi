package ca.tonsaker.thermopi.main.data;

import com.google.gson.annotations.Expose;

/**
 * Created by Marku on 2017-03-21.
 */
public class ConfigFile {

    @Expose public ZoneName[] zoneNames;

    public class ZoneName{
        @Expose String name;
    }

    @Expose Options options;

    public class Options{
        @Expose boolean buttonTone;
        @Expose boolean keypadTone;
        @Expose boolean consoleColors;
    }

}
