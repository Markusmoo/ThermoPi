package ca.tonsaker.thermopi.main.gui;

import ca.tonsaker.thermopi.main.Main;
import ca.tonsaker.thermopi.main.Utilities;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by 300145948 on 06/04/2017.
 */
public class Settings2GUI implements GUI, ActionListener{
    private JPanel settingsPage2;
    private JComboBox timezoneComboBox;
    private JCheckBox dstCheckbox;

    ButtonGroup timeGroup = new ButtonGroup();
    private JRadioButton hour12RadioButton;
    private JRadioButton hour24RadioButton;

    ButtonGroup tempGroup = new ButtonGroup();
    private JRadioButton tempCRadioButton;
    private JRadioButton tempFRadioButton;
    private JRadioButton tempRRadioButton;
    private JRadioButton tempKRadioButton;
    private JButton previousPageButton;
    private JButton applyButton;
    private JCheckBox tempEnabled;
    private JCheckBox insideCheckBox;
    private JCheckBox furnaceCheckBox;

    Main main;

    public Settings2GUI(Main main){
        this.main = main;

        timeGroup.add(hour12RadioButton);
        timeGroup.add(hour24RadioButton);

        tempGroup.add(tempCRadioButton);
        tempGroup.add(tempFRadioButton);
        tempGroup.add(tempRRadioButton);
        tempGroup.add(tempKRadioButton);
    }

    @Override
    public JPanel getGUI() {
        return settingsPage2;
    }

    @Override
    public void init() {

    }

    @Override
    public void switchToGUI(GUI oldScreen) {

    }

    @Override
    public void switchAwayGUI(GUI newScreen) {

    }

    @Override
    public void screenWakeup() {

    }

    @Override
    public void screenSleep() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if(src.equals(applyButton)){
            main.settingsGUI.applySettings();
        }else if(src.equals(previousPageButton)){
            main.switchGUI(main.settingsGUI);
        }
    }
}
