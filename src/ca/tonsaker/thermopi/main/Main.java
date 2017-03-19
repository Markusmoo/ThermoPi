package ca.tonsaker.thermopi.main;

import ca.tonsaker.thermopi.main.gui.*;
import com.bulenkov.darcula.DarculaLaf;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.RaspiPin;

import javax.swing.*;

/**
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

    //public static final GpioController gpio = GpioFactory.getInstance();  //TODO DEBUG

    public GUI currentGUI;

    public KeyboardGUI keyboardGUI;
    public HomescreenGUI homescreenGUI;
    public SecurityGUI securityGUI;
    public SettingsGUI settingsGUI;
    public static DebugGUI debugGUI;

    public static void main(String[] args) throws UnsupportedLookAndFeelException{
        UIManager.getFont("Label.font"); //TODO Temp work-around
        UIManager.setLookAndFeel(new DarculaLaf()); //TODO Resolve theme load error on PI

        Main.debugGUI = new DebugGUI();
        Debug.setDebugGUI(Main.debugGUI);

        Main main = new Main();
        Main.debugGUI.setMain(main);
        main.setVisible(true);
        //TODO On ThermoPi startup, contact ThermoHQ and receive current ThermoPi Security State
    }

    public Main(){
        super("SecurityGUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //TODO Replace with setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pack();
        init();
    }

    public void init(){
        //Initiate in this order: helper classes > GUI > exc.
        Utilities.init();

        keyboardGUI = new KeyboardGUI(this);
        homescreenGUI = new HomescreenGUI(this);
        securityGUI = new SecurityGUI();
        settingsGUI = new SettingsGUI(this);

        this.switchGUI(homescreenGUI);
        if(Config.debugMode){
            debugGUI.setVisible(true);
        }
    }

    public void switchGUI(GUI gui){
        getContentPane().setVisible(false);
        setContentPane(gui.getGUI());
        currentGUI = gui;
        getContentPane().setVisible(true);
        pack();
    }

    public GUI getCurrentGUI(){
        return currentGUI;
    }

}
