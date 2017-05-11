package ca.tonsaker.thermopi.main.gui;

import ca.tonsaker.thermopi.main.Main;
import ca.tonsaker.thermopi.main.Utilities;
import ca.tonsaker.thermopi.main.data.ConfigFile;
import ca.tonsaker.thermopi.main.data.communication.CommLink;
import ca.tonsaker.thermopi.main.gui.helper.TextFilter;
import ca.tonsaker.thermopi.main.gui.popup.KeyboardGUI;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import java.awt.*;
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

    JPasswordField[] passwordFields = {confirmPasswordField, newPasswordField, oldPasswordField};
    JTextField[] zones = {zoneTextField1, zoneTextField2, zoneTextField3, zoneTextField4, zoneTextField5, zoneTextField6};

    Main main;

    //TODO Add WIP to buttons

    public SettingsGUI(Main main){
        this.main = main;
        fullscreenToggleButton.addActionListener(this);
        openDebuggerButton.addActionListener(this);
        applyButton.addActionListener(this);
        nextPageButton.addActionListener(this);
        testAlarmButton.addActionListener(this);
        testFurnaceButton.addActionListener(this);
        for(JTextField zone : zones){
            zone.addMouseListener(this);
            PlainDocument pd = (PlainDocument) zone.getDocument();
            pd.setDocumentFilter(new TextFilter(false, 10));
        }
        for(JPasswordField pass : passwordFields){
            PlainDocument pd = (PlainDocument) pass.getDocument();
            pd.setDocumentFilter(new TextFilter(true, 8));
            pass.addMouseListener(this);
            //TODO Allow maximum 8 NUMBERS ONLY
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
        main.cfg.options.isKeypadTone = playKeypadToneCheckBox.isSelected();
        main.cfg.options.isButtonTone = playButtonToneCheckBox.isSelected();
        main.cfg.options.isConsoleColors = showConsoleColoursCheckBox.isSelected();
        String[] zoneNames = new String[zones.length];
        for(int idx = 0; idx < zoneNames.length; idx++){
            zoneNames[idx] = zones[idx].getText();
        }
        main.cfg.zoneNames = zoneNames;
        //TODO Send and test new password (if any) to ThermoHQ

        try{
            Utilities.saveSettings(main.cfg);
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

        if(src instanceof JButton) {
            if (src.equals(fullscreenToggleButton)) {
                main.setFullscreen(!main.isFullscreen());
                if (main.isFullscreen()) {
                    fullscreenToggleButton.setText("Exit Fullscreen");
                } else {
                    fullscreenToggleButton.setText("Enter Fullscreen");
                }
            }else if (src.equals(applyButton)) {
                applySettings();
            }else if (src.equals(openDebuggerButton)) {
                main.debugGUI.setVisible(true);
                main.debugGUI.setExtendedState(JFrame.NORMAL);
                main.debugGUI.toFront();
            }else if (src.equals(nextPageButton)){
                main.switchGUI(main.settings2GUI);
            }else if (src.equals(testAlarmButton)) {
                if(!ConfigFile.debugMode) CommLink.sendTestAlarm();
            }else if (src.equals(testFurnaceButton)){
                if(!ConfigFile.debugMode) CommLink.sendTestFurnace();
            }

            if(main.cfg.options.isButtonTone) Utilities.buttonTone();
        }
    }

    @Override
    public void switchToGUI(GUI oldGUI) {
        if(oldGUI instanceof KeyboardGUI || oldGUI instanceof Settings2GUI) return; //If switched from keyboard or SettingsPanel, do not reset settings.
        playButtonToneCheckBox.setSelected(main.cfg.options.isButtonTone);
        playKeypadToneCheckBox.setSelected(main.cfg.options.isKeypadTone);
        showConsoleColoursCheckBox.setSelected(main.cfg.options.isConsoleColors);
        int idx = 0;
        if(zones != null)
        for(JTextField f : zones){
            if(main.cfg.zoneNames[idx] != null) f.setText(main.cfg.zoneNames[idx]);
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
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        Object src = e.getSource();
        if(src instanceof JTextField){
            for(JTextField zone : zones){
                if(src.equals(zone)){
                    main.keyboardGUI.enterText(zone);
                    break;
                }
            }
            for(JPasswordField pass : passwordFields){
                if(src.equals(pass)){
                    main.keyboardGUI.enterText(pass);
                    break;
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
