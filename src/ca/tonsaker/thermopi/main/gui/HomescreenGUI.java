package ca.tonsaker.thermopi.main.gui;

import ca.tonsaker.thermopi.main.Config;
import ca.tonsaker.thermopi.main.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

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

    private Timer clockUpdater;

    private JButton[] buttons = {securityBtn, settingsButton, thermostatButton, weatherButton};

    private Main main;

    public HomescreenGUI(Main main){
        for(JButton btn : buttons) btn.addActionListener(this);
        this.main = main;
    }

    public void initClock(){
        clockUpdater = new Timer(1000, this);
        clockUpdater.start();
    }


    public void updateClock(){
        long millis = System.currentTimeMillis();
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;
        hour += Config.timezone;
        boolean inTheAm = false;
        if(Config.timeFormat12hour) {
            inTheAm = (hour <= 11) || hour == 24;
            if (hour > 12) hour -= 12;
            if (hour == 0) hour = 12;
        }else{
            if(hour > 24){
                hour -= 24;
            }else if(hour < 0){
                hour += 24;
            }
        }
        String timestamp = "";
        if(inTheAm && Config.timeFormat12hour) timestamp = " AM"; else if(Config.timeFormat12hour) timestamp = " PM";
        time.setText(String.format("%02d:%02d", hour, minute)+timestamp);
    }

    @Override
    public JPanel getGUI() {
        return homescreenPanel;
    }

    @Override
    public void init() {
        initClock();
    }

    @Override
    public void switchAwayGUI(GUI newScreen) {
        clockUpdater.stop();
    }

    @Override
    public void switchToGUI(GUI oldGUI){
        clockUpdater.restart();
    }

    @Override
    public void screenWakeup() {
        clockUpdater.restart();
    }

    @Override
    public void screenSleep() {
        clockUpdater.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if(src instanceof Timer){
            if(src.equals(clockUpdater)){
                updateClock();
            }
        }

        if(src instanceof JButton){
            if(src.equals(securityBtn)){
                main.switchGUI(main.securityGUI);
            }else if(src.equals(thermostatButton)){
                main.switchGUI(main.thermostatGUI);
            }else if(src.equals(settingsButton)){
                main.switchGUI(main.settingsGUI);
            }else if(src.equals(weatherButton)){
                //TODO Switch to Weather
            }
        }
    }

    //TODO Add weather panel
}
