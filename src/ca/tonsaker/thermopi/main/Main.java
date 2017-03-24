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
    public OptionPaneGUI optionPaneGUI;
    public KeyboardGUI keyboardGUI;
    public HomescreenGUI homescreenGUI;
    public SecurityGUI securityGUI;
    public SettingsGUI settingsGUI;
    public static DebugGUI debugGUI;

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

        keyboardGUI = new KeyboardGUI(this);
        optionPaneGUI = new OptionPaneGUI(this);
        homescreenGUI = new HomescreenGUI(this);
        securityGUI = new SecurityGUI();
        settingsGUI = new SettingsGUI(this);

        keyboardGUI.init();
        optionPaneGUI.init();
        homescreenGUI.init();
        securityGUI.init();
        settingsGUI.init();

        this.switchGUI(homescreenGUI);
        if(Config.debugMode){
            debugGUI.setVisible(true);
        }
    }

    public void switchGUI(GUI gui){
        getContentPane().setVisible(false);
        setContentPane(gui.getGUI());
        GUI oldGUI = currentGUI;
        currentGUI = gui;
        getContentPane().setVisible(true);
        validate();
        if(Config.debugMode) pack();
        gui.switchPerformed(oldGUI);
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
