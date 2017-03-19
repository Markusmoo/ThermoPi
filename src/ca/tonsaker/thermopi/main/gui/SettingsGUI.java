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
    private JButton testSirenButton;
    private JTextField zoneTextField1;
    private JTextField zoneTextField2;
    private JTextField zoneTextField3;
    private JTextField zoneTextField4;
    private JTextField zoneTextField5;
    private JTextField zoneTextField6;
    private JPanel settingsPanel;
    private JPanel zonePanel;
    private JPanel passcodePanel;
    private JButton a7Button;
    private JButton a9Button;
    private JButton a8Button;
    private JButton a4Button;
    private JButton a5Button;
    private JButton a6Button;
    private JButton a1Button;
    private JButton a2Button;
    private JButton a3Button;
    private JButton a0Button;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;
    private JPasswordField passwordField3;
    private JPanel buttonPanel;

    JButton[] numPad = {a0Button, a1Button, a2Button, a3Button, a4Button, a5Button, a6Button, a7Button, a8Button, a9Button};
    JTextField[] zones = {zoneTextField1, zoneTextField2, zoneTextField3, zoneTextField4, zoneTextField5, zoneTextField6};

    Main main;

    public SettingsGUI(Main main){
        this.main = main;
        //TODO Add Listeners
        for(JButton btn : numPad){
            btn.addActionListener(this);
        }
        for(JTextField zone : zones){
            zone.addFocusListener(this);
        }
    }

    @Override
    public JPanel getGUI() {
        return settingsPanel;
    }

    @Override
    public void init() {

    }


    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
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
