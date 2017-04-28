package ca.tonsaker.thermopi.main.gui;

import ca.tonsaker.thermopi.main.Config;
import ca.tonsaker.thermopi.main.Main;
import ca.tonsaker.thermopi.main.Utilities;
import ca.tonsaker.thermopi.main.gui.popup.KeyboardGUI;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * Created by Marku on 2017-03-17.
 */
public class SettingsGUI implements GUI, ActionListener, MouseListener{
    private JButton restartThermoPiButton;
    private JButton fullscreenToggleButton;
    private JButton applyButton;
    private JCheckBox playKeypadToneCheckBox;
    private JCheckBox playButtonToneCheckBox;
    private JCheckBox showConsoleColoursCheckBox;
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
    private JPanel testingPanel;
    private JButton nextPageButton;

    JTextField[] passwordFields = {confirmPasswordField, newPasswordField, oldPasswordField};
    JTextField[] zones = {zoneTextField1, zoneTextField2, zoneTextField3, zoneTextField4, zoneTextField5, zoneTextField6};

    Main main;

    public SettingsGUI(Main main){
        this.main = main;
        fullscreenToggleButton.addActionListener(this);
        openDebuggerButton.addActionListener(this);
        applyButton.addActionListener(this);
        nextPageButton.addActionListener(this);
        for(JTextField zone : zones){
            zone.addMouseListener(this);
        }
        for(JTextField pass : passwordFields){
            pass.addMouseListener(this);
        }
    }

    @Override
    public JPanel getGUI() {
        return settingsPanel;
    }

    @Override
    public void init() {

    }

    public void applySettings(){
        Config.keypadTone = playKeypadToneCheckBox.isSelected();
        Config.buttonTone = playButtonToneCheckBox.isSelected();
        Config.showConsoleColors = showConsoleColoursCheckBox.isSelected();
        String[] zoneNames = new String[zones.length];
        for(int idx = 0; idx < zoneNames.length; idx++){
            zoneNames[idx] = zones[idx].getText();
        }
        Config.zoneNames = zoneNames;
        //TODO Send and test new password (if any) to ThermoHQ

        try{
            Utilities.saveSettings(Config.createConfigFile());
        }catch (IOException e){
            e.printStackTrace();
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
                if(Config.buttonTone) Utilities.buttonTone();
            }else if(src.equals(applyButton)){
                applySettings();
                if(Config.buttonTone) Utilities.buttonTone();
            }else if(src.equals(openDebuggerButton)){
                main.debugGUI.setVisible(true);
                main.debugGUI.setExtendedState(JFrame.NORMAL);
                main.debugGUI.toFront();
                if(Config.buttonTone) Utilities.buttonTone();
            }else if(src.equals(nextPageButton)){
                main.switchGUI(main.settings2GUI);
            }
        }
    }

    @Override
    public void switchToGUI(GUI oldGUI) {
        if(oldGUI instanceof KeyboardGUI || oldGUI instanceof Settings2GUI) return; //If switched from keyboard or SettingsPanel, do not reset settings.
        playButtonToneCheckBox.setSelected(Config.buttonTone);
        playKeypadToneCheckBox.setSelected(Config.keypadTone);
        showConsoleColoursCheckBox.setSelected(Config.showConsoleColors);
        int idx = 0;
        if(zones != null)
        for(JTextField f : zones){
            if(Config.zoneNames[idx] != null) f.setText(Config.zoneNames[idx]);
            idx++;
        }
        oldPasswordField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");

        if(main.isFullscreen()){
            fullscreenToggleButton.setText("Exit Fullscreen");
        }else{
            fullscreenToggleButton.setText("Enter Fullscreen");
        }
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
    public void mouseClicked(MouseEvent e) {
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

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
