package ca.tonsaker.thermopi.main;

import ca.tonsaker.thermopi.main.data.ConfigFile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.apache.commons.lang3.SystemUtils;

import java.io.*;


/**
 * Created by Markus Tonsaker on 2017-03-08.
 */
public class Utilities implements GpioPinListenerDigital {

    Main mainFrame;

    private static GpioPinDigitalOutput screenPin;
    private static GpioPinDigitalOutput soundPin;
    private static GpioPinDigitalInput homePin;

    public Utilities(Main main){
        mainFrame = main;
        if(!ConfigFile.debugMode){
            soundPin = Main.gpio.provisionDigitalOutputPin(ConfigFile.PIN_SPEAKER, PinState.LOW);
            screenPin = Main.gpio.provisionDigitalOutputPin(ConfigFile.PIN_SCREEN, PinState.LOW);
            homePin = Main.gpio.provisionDigitalInputPin(ConfigFile.PIN_HOME, PinPullResistance.PULL_UP);

            homePin.addListener(this);
        }
    }

    public static void setScreenState(boolean on){
        Debug.println(Debug.LOW, "Changing screen state!  Screen on? = "+on);
        if(ConfigFile.debugMode) return;
        screenPin.setState(!on);
    }

    public static void tone(int durationMillis){
        Debug.println(Debug.LOW, "Playing tone.. Pin: " + ConfigFile.PIN_SPEAKER + ", Duration: " + durationMillis + " milliseconds");
        if(ConfigFile.debugMode) return;
        soundPin.pulse(durationMillis);
    }

    public static void buttonTone(){
        Debug.println(Debug.LOW, "Playing tone.. Pin: " + ConfigFile.PIN_SPEAKER + ", Duration: " + 100 + " milliseconds");
        if(ConfigFile.debugMode) return;
        soundPin.pulse(100);
    }

    public static void initializeFiles(){
        String path = getSettingsFilePath();
        if(path == null){
            Debug.println(Debug.ERROR, "Failed to create settings file..");
            return;
        }
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
            try {
                file = new File(path+ConfigFile.SETTING_FILENAME);
                file.createNewFile();
                Debug.println(Debug.MEDIUM, "Created config file at: " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ConfigFile loadSettings() throws FileNotFoundException{
        String path = getSettingsFilePath();
        if(path == null){
            Debug.println(Debug.ERROR, "Failed to load settings file..");
            return null;
        }

        Reader reader = new InputStreamReader(new FileInputStream(path+ConfigFile.SETTING_FILENAME));
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
        return gson.fromJson(reader, ConfigFile.class);
    }

    public static void saveSettings(ConfigFile cf) throws IOException{
        String path = getSettingsFilePath();
        if(path == null){
            Debug.println(Debug.ERROR, "Failed to save settings file..");
            return;
        }
        Writer writer = new OutputStreamWriter(new FileOutputStream(path+ConfigFile.SETTING_FILENAME));
        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

        writer.write(gson.toJson(cf));
        writer.flush();
        writer.close();

        Debug.println(Debug.HIGH, "ThermoPi Settings successfully saved to: \"" + path + "\"");
    }

    private static String getSettingsFilePath(){
        String path;
        if(SystemUtils.IS_OS_LINUX){
            path = ConfigFile.SETTINGS_LINUX;
        }else if(SystemUtils.IS_OS_WINDOWS){
            path = ConfigFile.SETTINGS_WINDOWS;
        }else if(SystemUtils.IS_OS_MAC){
            path = ConfigFile.SETTINGS_OSX;
        }else{
            Debug.println(Debug.ERROR, "System \""+ SystemUtils.OS_NAME +"\"is not of type: LINUX, WINDOWS, MAC");
            return null;
        }
        return path;
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent e) {
        Object src = e.getPin().getPin();

        if(src.equals(homePin.getPin()) && homePin.getState() == PinState.HIGH){
            mainFrame.switchGUI(mainFrame.homescreenGUI);
            if(mainFrame.cfg.options.isButtonTone) Utilities.buttonTone();

        }
    }
}
