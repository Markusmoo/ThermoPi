package ca.tonsaker.thermopi.main;

import ca.tonsaker.thermopi.main.gui.SecurityGUI;
import com.bulenkov.darcula.DarculaLaf;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

import javax.swing.*;

/**
 * Created by Marku on 2017-03-07.
 */
public class Main extends JFrame{

    //public static final GpioController gpio = GpioFactory.getInstance();  //TODO Research

    public static void main(String[] args) throws UnsupportedLookAndFeelException{
        UIManager.setLookAndFeel(new DarculaLaf());
        new Main().setVisible(true);

    }

    public Main(){
        super("SecurityGUI");
        setContentPane(new SecurityGUI().securityPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //TODO Replace with setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pack();
    }

}
