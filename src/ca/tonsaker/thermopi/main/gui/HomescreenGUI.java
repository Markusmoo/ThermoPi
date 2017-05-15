package ca.tonsaker.thermopi.main.gui;

import ca.tonsaker.thermopi.main.Debug;
import ca.tonsaker.thermopi.main.Main;
import ca.tonsaker.thermopi.main.Utilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Markus Tonsaker on 2017-03-13.
 *
 * TODO Add clock offset
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
        updateClock();
        clockUpdater = new Timer(1000, this);
        clockUpdater.start();
    }

    public void updateClock(){
        long millis = System.currentTimeMillis();
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;
        hour += main.cfg.options.timeZoneUTC;
        if(main.cfg.options.isDST) hour++;
        boolean inTheAm = false;
        if(main.cfg.options.is12Hours) {
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
        if(inTheAm && main.cfg.options.is12Hours) timestamp = " AM"; else if(main.cfg.options.is12Hours) timestamp = " PM";
        if(main.cfg.options.is12Hours) {
            String t = String.format("%02d:%02d", hour, minute) + timestamp;
            if(t.indexOf('0') == 0) t = t.substring(1);
            time.setText(t);
        }else {
            time.setText(String.format("%02d:%02d", hour, minute) + timestamp);
        }
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
        updateClock();
        clockUpdater.restart();
    }

    @Override
    public void screenWakeup() {
        updateClock();
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
                if(main.cfg.options.isTempsOn[0] || main.cfg.options.isTempsOn[1] || main.cfg.options.isTempsOn[2]) {
                    main.switchGUI(main.thermostatGUI);
                }else{
                    JOptionPane.showMessageDialog(main, "Thermostat is disabled! \n\n (Info: To enable thermostat goto settings and \n"+
                            "under \"Temperature\" select which units to be on. \n"+
                            "Make sure your ThermoHQ has support for \nthis feature.  Contact your installer.)");
                }
            }else if(src.equals(settingsButton)){
                main.switchGUI(main.settingsGUI);
            }else if(src.equals(weatherButton)){
                //TODO Switch to Weather
                Debug.wipPopup();
            }
            if(main.cfg.options.isButtonTone) Utilities.buttonTone();
        }
    }

    //TODO Add weather panel
}
