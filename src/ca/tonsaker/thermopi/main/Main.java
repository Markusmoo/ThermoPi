package ca.tonsaker.thermopi.main;

import ca.tonsaker.thermopi.main.gui.*;
import ca.tonsaker.thermopi.main.gui.popup.KeyboardGUI;
import ca.tonsaker.thermopi.main.gui.popup.OptionPaneGUI;
import com.bulenkov.darcula.DarculaLaf;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

/**
 * Acceptable runtime parameters
 *-debugmode :Disables GPIO
 *-safemode :Disables Console Theme
 *
 * Created by Markus Tonsaker on 2017-03-07.
 */
public class Main extends JFrame{

    /*
    TODO  - Create a logger that saves to a txt file.
    TODO  - Implement ThermoHQ Code
    TODO
    TODO
    TODO  - Finish all TODOs
     */

    GraphicsDevice graphicsDevice;

    public static GpioController gpio;

    public static GUI currentGUI;

    public OptionPaneGUI optionPaneGUI = new OptionPaneGUI(this);
    public KeyboardGUI keyboardGUI = new KeyboardGUI(this);
    public HomescreenGUI homescreenGUI = new HomescreenGUI(this);
    public SecurityGUI securityGUI = new SecurityGUI();
    public SettingsGUI settingsGUI = new SettingsGUI(this);
    public ThermostatGUI thermostatGUI = new ThermostatGUI();
    public static DebugGUI debugGUI;

    //Doesn't include debugGUI because debugGUI is in it's own window.
    private GUI[] guiList = {optionPaneGUI, keyboardGUI, homescreenGUI, securityGUI, settingsGUI, thermostatGUI};

    boolean isFullscreen;

    public static void main(String[] args) throws UnsupportedLookAndFeelException{
        for(String arg : args){
            if(arg.equals("-safemode")){
                Main.debugGUI = new DebugGUI();
                Debug.setDebugGUI(Main.debugGUI);
                Config.safeMode = true;
            }else if(arg.equals("-debugmode")){
                Config.debugMode = true;
            }
        }

        UIManager.getFont("Label.font");
        UIManager.setLookAndFeel(new DarculaLaf());

        if(!Config.safeMode) {
            Main.debugGUI = new DebugGUI();
            Debug.setDebugGUI(Main.debugGUI);
        }

        if (!Config.debugMode) gpio = GpioFactory.getInstance();

        Main main = new Main();
        Main.debugGUI.setMain(main);
        main.setVisible(true);
        //TODO On ThermoPi startup, contact ThermoHQ and receive current ThermoPi Security State
    }

    public Main(){
        super("SecurityGUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //TODO Replace with setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        if(!Config.debugMode) this.setUndecorated(true);
        init();

        if(!Config.debugMode) setFullscreen(true); else pack();
    }

    public void init(){
        //Initiate in this order: helper classes > GUI > exc.
        Utilities.init();
        Utilities.initializeFiles();
        try{
            Config.setSettingsVariables(Utilities.loadSettings());
        }catch(FileNotFoundException e){
            Debug.println(Debug.ERROR, "Failed to load settings file.");
        }

        /*keyboardGUI = new KeyboardGUI(this);
        optionPaneGUI = new OptionPaneGUI(this);
        homescreenGUI = new HomescreenGUI(this); //TODO Test
        securityGUI = new SecurityGUI();
        thermostatGUI = new ThermostatGUI();
        settingsGUI = new SettingsGUI(this);*/

        /*guiList = new GUI[6];
        guiList[0] = optionPaneGUI;
        guiList[1] = keyboardGUI;
        guiList[2] = homescreenGUI; //TODO Test
        guiList[3] = securityGUI;
        guiList[4] = settingsGUI;
        guiList[5] = thermostatGUI;*/

        for(GUI g : guiList){
            g.init();
        }
//        thermostatGUI.init();
//        keyboardGUI.init();
//        optionPaneGUI.init();  //TODO Test for removal
//        homescreenGUI.init();
//        securityGUI.init();
//        settingsGUI.init();

        currentGUI = homescreenGUI;
        this.switchGUI(homescreenGUI);
        if(Config.debugMode){
            debugGUI.setVisible(true);
        }
    }

    public void setScreenOff(){
        //TODO Turn screen off
        for(GUI g : guiList){
            g.screenSleep();
        }
    }

    public void setScreenOn(){
        for(GUI g : guiList){
            g.screenWakeup();
        }
        //TODO Turn screen on
    }

    public void switchGUI(GUI gui){
        getContentPane().setVisible(false);
        setContentPane(gui.getGUI());
        GUI oldGUI = currentGUI;
        currentGUI = gui;
        getContentPane().setVisible(true);
        validate();
        if(Config.debugMode) pack();
        oldGUI.switchAwayGUI(gui);
        gui.switchToGUI(oldGUI);
        Debug.println(Debug.LOW, "Switching GUI to: "+gui.toString());
    }

    public static GUI getCurrentGUI(){
        return Main.currentGUI;
    }

    public boolean setFullscreen(boolean fullscreen) {
        graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if(fullscreen) {
            try {
                if (!graphicsDevice.isFullScreenSupported()) {
                    Debug.println(Debug.HIGH, "Fullscreen not supported.. Attempting to put application into Fullscreen mode anyways");
                }
                Debug.println(Debug.MEDIUM, "Entering Fullscreen Mode");
                graphicsDevice.setFullScreenWindow(this);
                isFullscreen = true;
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                setVisible(true);
                validate();
            }
            return true;
        }else{
            graphicsDevice.setFullScreenWindow(null); //TODO Test on RPi
            this.pack();
            this.setVisible(true);
            isFullscreen = false;
            Debug.println(Debug.HIGH, "Exiting Fullscreen Mode");
            return true;
        }
    }

    public boolean isFullscreen(){
        return isFullscreen;
    }

}
