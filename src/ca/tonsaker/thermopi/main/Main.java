package ca.tonsaker.thermopi.main;

import ca.tonsaker.thermopi.main.data.ConfigFile;
import ca.tonsaker.thermopi.main.data.communication.CommLink;
import ca.tonsaker.thermopi.main.gui.*;
import ca.tonsaker.thermopi.main.gui.popup.KeyboardGUI;
import ca.tonsaker.thermopi.main.gui.popup.OptionPaneGUI;
import com.bulenkov.darcula.DarculaLaf;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
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

    public CommLink lnk;

    public static ConfigFile cfg = new ConfigFile();

    public static GpioController gpio;
    public static Utilities util;

    public static GUI currentGUI;

    public OptionPaneGUI optionPaneGUI = new OptionPaneGUI(this);
    public KeyboardGUI keyboardGUI = new KeyboardGUI(this);
    public HomescreenGUI homescreenGUI = new HomescreenGUI(this);
    public SecurityGUI securityGUI = new SecurityGUI();
    public SettingsGUI settingsGUI = new SettingsGUI(this);
    public Settings2GUI settings2GUI = new Settings2GUI(this);
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
                ConfigFile.safeMode = true;
            }else if(arg.equals("-debugmode")){
                ConfigFile.debugMode = true;
            }
        }
        UIManager.getFont("Label.font");
        UIManager.setLookAndFeel(new DarculaLaf());

        if(!ConfigFile.safeMode) {
            Main.debugGUI = new DebugGUI();
            Debug.setDebugGUI(Main.debugGUI);
        }

        if (!ConfigFile.debugMode) gpio = GpioFactory.getInstance();

        Main main = new Main();
        Main.debugGUI.setMain(main);
        main.setVisible(true);
        //TODO On ThermoPi startup, contact ThermoHQ and receive current ThermoPi Security State
    }

    public Main() {
        super("SecurityGUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //TODO Replace with setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        util = new Utilities(this);
        lnk = new CommLink(this);

//        if(Config.debugMode) {
//            this.hideCursor();
//        }

        init();

        if(!ConfigFile.debugMode){
            this.setUndecorated(true);
            setFullscreen(true);
        }else {
            pack();
        }
    }

    public void init(){
        //Initiate in this order: helper classes > GUI > exc.
        Utilities.initializeFiles();
        try{
            cfg.setSettingsVariables(Utilities.loadSettings());
        }catch(FileNotFoundException e){
            Debug.println(Debug.ERROR, "Failed to load settings file.");
        }

        for(GUI g : guiList){
            g.init();
        }

        currentGUI = homescreenGUI;
        this.switchGUI(homescreenGUI);
        if(ConfigFile.debugMode) {
            debugGUI.setVisible(true);
        }
    }

    public void hideCursor(){
        this.setCursor(this.getToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "null"));
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
        if(ConfigFile.debugMode) pack();
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
            debugGUI.toFront();
            return true;
        }
    }

    public boolean isFullscreen(){
        return isFullscreen;
    }

}
