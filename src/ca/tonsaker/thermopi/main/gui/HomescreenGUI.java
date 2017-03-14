package ca.tonsaker.thermopi.main.gui;

import ca.tonsaker.thermopi.main.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Marku on 2017-03-13.
 */
public class HomescreenGUI implements GUI, ActionListener{
    private JButton securityBtn;
    private JButton settingsButton;
    private JLabel time;
    private JButton thermostatButton;
    private JButton weatherButton;
    private JPanel weatherPanel;
    private JPanel homescreenPanel;

    private JButton[] buttons = {securityBtn, settingsButton, thermostatButton, weatherButton};

    private Main main;

    public HomescreenGUI(Main main){
        for(JButton btn : buttons) btn.addActionListener(this);
        this.main = main;
    }

    @Override
    public JPanel getGUI() {
        return homescreenPanel;
    }

    @Override
    public void init() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if(src instanceof JButton){
            if(src.equals(securityBtn)){
                main.switchGUI(main.securityGUI);
            }else if(src.equals(thermostatButton)){
                //TODO Switch to Thermostat
            }else if(src.equals(settingsButton)){
                //TODO Switch to Settings
            }else if(src.equals(weatherButton)){
                //TODO Switch to Weather
            }
        }
    }

    //TODO Add weather panel
}
