package ca.tonsaker.thermopi.main;

import ca.tonsaker.thermopi.main.data.ConfigFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import org.apache.commons.lang3.SystemUtils;

import java.io.*;

import static ca.tonsaker.thermopi.main.Config.SETTINGS_LINUX;

/**
 * Created by Markus Tonsaker on 2017-03-08.
 */
public class Utilities{

    private static GpioPinDigitalOutput soundPin;

    public static void tone(int durationMillis){
        Debug.println(Debug.LOW, "Playing tone.. Pin: " + Config.SPEAKER_PIN + ", Duration: " + durationMillis + " milliseconds");
        if(Config.debugMode) return;
        soundPin.pulse(durationMillis);
    }

    public static void buttonTone(){
        Debug.println(Debug.LOW, "Playing tone.. Pin: " + Config.SPEAKER_PIN + ", Duration: " + 100 + " milliseconds");
        if(Config.debugMode) return;
        soundPin.pulse(100);
    }

    public static ConfigFile loadSettings() throws FileNotFoundException{
        String path;
        if(SystemUtils.IS_OS_LINUX){
            path = Config.SETTINGS_LINUX;
        }else if(SystemUtils.IS_OS_WINDOWS){
            path = Config.SETTINGS_WINDOWS;
        }else if(SystemUtils.IS_OS_MAC){
            path = Config.SETTINGS_OSX;
        }else{
            Debug.println(Debug.ERROR, "System \""+ SystemUtils.OS_NAME +"\"is not of type: LINUX, WINDOWS, MAC");
            return null;
        }

        Reader reader = new InputStreamReader(new FileInputStream(path));
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        return gson.fromJson(reader, ConfigFile.class);
    }

    public static void saveSettings(ConfigFile cf) throws IOException{
        String path;
        if(SystemUtils.IS_OS_LINUX){
            path = Config.SETTINGS_LINUX;
        }else if(SystemUtils.IS_OS_WINDOWS){
            path = Config.SETTINGS_WINDOWS;
        }else if(SystemUtils.IS_OS_MAC){
            path = Config.SETTINGS_OSX;
        }else{
            Debug.println(Debug.ERROR, "System \""+ SystemUtils.OS_NAME +"\"is not of type: LINUX, WINDOWS, MAC");
            return;
        }
        new File(path).mkdirs();
        Writer writer = new OutputStreamWriter(new FileOutputStream(path));
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

        writer.write(gson.toJson(cf));
        writer.flush();
        writer.close();

        Debug.println(Debug.HIGH, "ThermoPi Settings successfully saved to: \"" + path + "\"");
    }

    public static void init(){
        if(!Config.debugMode){
            soundPin = Main.gpio.provisionDigitalOutputPin(Config.SPEAKER_PIN, PinState.LOW); //TODO Enable for RPi Testing
        }
    }
}
