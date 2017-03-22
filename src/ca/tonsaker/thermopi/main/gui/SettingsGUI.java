package ca.tonsaker.thermopi.main.gui;

import ca.tonsaker.thermopi.main.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Created by Marku on 2017-03-17.
 */
public class SettingsGUI implements GUI, ActionListener, FocusListener{
    private JButton restartThermoPiButton;
    private JButton fullscreenToggleButton;
    private JButton applyButton;
    private JCheckBox playKeypadToneCheckBox;
    private JCheckBox playButtonToneCheckBox;
    private JCheckBox showConsoleColoursCheckBox;
    private JButton testThermoHQButton;
    private JButton testFurnaceButton;
    private JButton testAlarmButton;
    private JTextField zoneTextField1;
    private JTextField zoneTextField2;
    private JTextField zoneTextField3;
    private JTextField zoneTextField4;
    private JTextField zoneTextField5;
    private JTextField zoneTextField6;
    private JPanel settingsPanel;
    private JPanel zonePanel;
    private JPanel passcodePanel;
    private JPasswordField confirmPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField oldPasswordField;
    private JPanel buttonPanel;
    private JButton openDebuggerButton;

    JTextField[] passwordFields = {confirmPasswordField, newPasswordField, oldPasswordField};
    JTextField[] zones = {zoneTextField1, zoneTextField2, zoneTextField3, zoneTextField4, zoneTextField5, zoneTextField6};

    Main main;

    public SettingsGUI(Main main){
        this.main = main;
        fullscreenToggleButton.addActionListener(this);
        for(JTextField zone : zones){
            zone.addFocusListener(this);
        }
        for(JTextField pass : passwordFields){
            pass.addFocusListener(this);
        }
    }

    @Override
    public JPanel getGUI() {
        return settingsPanel;
    }

    @Override
    public void init() {
        if(main.isFullscreen()){
            fullscreenToggleButton.setText("Exit Fullscreen");
        }else{
            fullscreenToggleButton.setText("Enter Fullscreen");
        }
    }


    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if(src instanceof JButton){
            if(src.equals(fullscreenToggleButton)){
                main.setFullscreen(!main.isFullscreen());
                if(main.isFullscreen()){
                    fullscreenToggleButton.setText("Exit Fullscreen");
                }else{
                    fullscreenToggleButton.setText("Enter Fullscreen");
                }
            }
        }
    }

    /**
     * Invoked when a component gains the keyboard focus.
     *
     * @param e
     */
    @Override
    public void focusGained(FocusEvent e) {
        Object src = e.getSource();
        if(src instanceof JTextField){
            for(JTextField zone : zones){
                if(src.equals(zone)){
                    main.keyboardGUI.enterText(zone);
                    break;
                }
            }
            for(JTextField pass : passwordFields){
                if(src.equals(pass)){
                    main.keyboardGUI.enterText(pass, true);
                }
            }
        }
    }

    /**
     * Invoked when a component loses the keyboard focus.
     *
     * @param e
     */
    @Override
    public void focusLost(FocusEvent e) {}
}
