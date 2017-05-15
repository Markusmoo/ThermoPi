package ca.tonsaker.thermopi.main.gui;

import ca.tonsaker.thermopi.main.Debug;
import ca.tonsaker.thermopi.main.Main;
import ca.tonsaker.thermopi.main.Utilities;
import ca.tonsaker.thermopi.main.data.ConfigFile;
import ca.tonsaker.thermopi.main.gui.popup.KeyboardGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** TODO Implement function of the settings
 * Created by 300145948 on 06/04/2017.
 */
public class Settings2GUI implements GUI, ActionListener{

    private static final int TIMEZONE_MINIMUM_VALUE = -12;  //WARNING: If > 0, you must rewrite switchToGUI method!

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
    private JCheckBox outsideCheckbox;
    private JCheckBox insideCheckBox;
    private JCheckBox furnaceCheckBox;

    Main main;

    public Settings2GUI(Main main){
        this.main = main;

        previousPageButton.addActionListener(this);
        applyButton.addActionListener(this);

        timeGroup.add(hour12RadioButton);
        timeGroup.add(hour24RadioButton);
        timeGroup.setSelected(hour12RadioButton.getModel(), true);

        tempGroup.add(tempCRadioButton);
        tempGroup.add(tempFRadioButton);
        tempGroup.add(tempRRadioButton);
        tempGroup.add(tempKRadioButton);
        tempGroup.setSelected(tempCRadioButton.getModel(), true);

        timezoneComboBox.setSelectedIndex(6);
    }

    protected boolean[] getTempsOn(){
        return new boolean[]{outsideCheckbox.isSelected(), insideCheckBox.isSelected(), furnaceCheckBox.isSelected()};
    }

    protected int getTimezone(){
        String tz = (String) timezoneComboBox.getSelectedItem();
        tz = tz.substring(3,6).replace("±", "").replace("+","");
        if(tz.charAt(1) == '0') tz = tz.replace("0","");
        return Integer.parseInt(tz);
    }

    protected boolean isDST(){
        return dstCheckbox.isSelected();
    }

    protected boolean is12Hours(){
        return hour12RadioButton.isSelected();
    }

    protected int getTempUnit(){
        if(tempCRadioButton.isSelected()) return ConfigFile.TEMP_C;
        if(tempFRadioButton.isSelected()) return ConfigFile.TEMP_F;
        if(tempRRadioButton.isSelected()) return ConfigFile.TEMP_R;
        if(tempKRadioButton.isSelected()) return ConfigFile.TEMP_K;
        Debug.println(Debug.ERROR, "Failed to retrieve temp unit from Setting2GUI.java.. Nothing selected?");
        return -1;
    }

    @Override
    public JPanel getGUI() {
        return settingsPage2;
    }

    @Override
    public void init() {
        if(main.cfg.options.is12Hours) timeGroup.setSelected(hour12RadioButton.getModel(), true);
        timezoneComboBox.setSelectedIndex(main.cfg.options.timeZoneUTC + Math.abs(TIMEZONE_MINIMUM_VALUE)); //Rewrite this line if TIMEZONE_MINIMUM_VALUE is > 0
        dstCheckbox.setSelected(main.cfg.options.isDST);
        outsideCheckbox.setSelected(main.cfg.options.isTempsOn[0]);
        insideCheckBox.setSelected(main.cfg.options.isTempsOn[1]);
        furnaceCheckBox.setSelected(main.cfg.options.isTempsOn[2]);
        switch(main.cfg.options.temperatureUnit){
            case(ConfigFile.TEMP_C): tempGroup.setSelected(tempCRadioButton.getModel(), true); break;
            case(ConfigFile.TEMP_F): tempGroup.setSelected(tempFRadioButton.getModel(), true); break;
            case(ConfigFile.TEMP_R): tempGroup.setSelected(tempRRadioButton.getModel(), true); break;
            case(ConfigFile.TEMP_K): tempGroup.setSelected(tempKRadioButton.getModel(), true); break;
            default: Debug.println(Debug.ERROR, "switchToGUI(GUI) in Settings2GUI.java received an incorrect temperature code of: "+main.cfg.options.temperatureUnit);
        }
    }

    @Override
    public void switchToGUI(GUI oldScreen) {
        if(oldScreen instanceof KeyboardGUI || oldScreen instanceof SettingsGUI) return; //If switched from keyboard or SettingsPanel, do not reset settings.
        this.init();
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
