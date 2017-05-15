package ca.tonsaker.thermopi.main.gui;

import ca.tonsaker.thermopi.main.Debug;
import ca.tonsaker.thermopi.main.Main;
import ca.tonsaker.thermopi.main.Utilities;
import ca.tonsaker.thermopi.main.data.ConfigFile;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Markus Tonsaker on 2017-04-02.
 */
public class ThermostatGUI implements GUI, ActionListener{
    private JPanel thermostatPanel;
    private JButton decreaseButton;
    private JButton increaseButton;
    private JLabel setToVar;
    private JPanel setToPanel;
    private JPanel insidePanel;
    private JPanel outsidePanel;
    private JLabel insideVar;
    private JLabel outsideVar;

    private boolean inFocus = false;
    private boolean isSleep = false;

    private Timer updater = new Timer(10000, this);

    private int tempSet = 0;
    private int tempInside = 0;
    private int tempOutside = 0;

    private Main main;

    public ThermostatGUI(Main main){
        this.main = main;
        increaseButton.addActionListener(this);
        decreaseButton.addActionListener(this);
    }

    @NotNull
    @Contract(pure = true)
    private String convertTemp(int temp){
        switch(main.cfg.options.temperatureUnit){
            case(ConfigFile.TEMP_C): return temp + " °C";
            case(ConfigFile.TEMP_F): return ((9/5) * temp + 32) + " °F";
            case(ConfigFile.TEMP_R): return ((temp+273.15)*(9/5)) + " °R";
            case(ConfigFile.TEMP_K): return (temp+273.15) + " K";
            default: return temp + " °C";
        }
    }

    public void updateTemp(){
        Debug.println(Debug.DEBUG, "Updating temperatures..");
        //TODO Request temperature from ThermoHQ
    }

    public void setTemperatureSet(int temp){
        if(isSleep || !inFocus) return;
        setToVar.setText(convertTemp(temp));
        tempSet = temp;
        Debug.println(Debug.MEDIUM, "Setting inside temperature to: "+temp+" degrees celsius");
    }

    public void setTemperatureInside(int temp){
        if(isSleep || !inFocus) return;
        insideVar.setText(convertTemp(temp));
        tempInside = temp;
        Debug.println(Debug.DEBUG, "Inside temperature changed to: "+temp+" degrees celsius");
    }

    public void setTemperatureOutside(int temp){
        if(isSleep || !inFocus) return;
        outsideVar.setText(convertTemp(temp));
        tempOutside = temp;
        Debug.println(Debug.DEBUG, "Outside temperature changed to: "+temp+" degrees celsius");
    }

    public int getTemperatureSet(){
        return tempSet;
    }

    public int getTemperatureInside(){
        return tempInside;
    }

    public int getTemperatureOutside(){
        return tempOutside;
    }

    @Override
    public JPanel getGUI() {
        return thermostatPanel;
    }

    @Override
    public void init() {
        updateTemp();
    }

    @Override
    public void switchToGUI(GUI oldScreen) {
        inFocus = true;
        updateTemp();
        setTemperatureSet(getTemperatureSet());
        setTemperatureInside(getTemperatureInside());
        setTemperatureOutside(getTemperatureOutside());
        updater.restart();
    }

    @Override
    public void switchAwayGUI(GUI newScreen) {
        inFocus = false;
        updater.stop();
    }

    @Override
    public void screenWakeup() {
        isSleep = false;
        updateTemp();
        if(inFocus) updater.restart();
    }

    @Override
    public void screenSleep() {
        isSleep = true;
        updater.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if(src instanceof JButton){
            if(src.equals(increaseButton)){
                setTemperatureSet(getTemperatureSet()+1);
            }
            if(src.equals(decreaseButton)){
                setTemperatureSet(getTemperatureSet()-1);
            }
            if(Main.cfg.options.isButtonTone) Utilities.buttonTone();
        }else if(src instanceof Timer){
            if(src.equals(updater)){
                updateTemp();
            }
        }
    }
}
