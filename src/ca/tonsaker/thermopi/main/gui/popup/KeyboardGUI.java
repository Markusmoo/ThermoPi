package ca.tonsaker.thermopi.main.gui.popup;

import ca.tonsaker.thermopi.main.Main;
import ca.tonsaker.thermopi.main.data.ConfigFile;
import ca.tonsaker.thermopi.main.gui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Marku on 2017-03-19.
 */
public class KeyboardGUI implements ActionListener, GUI {
    private JPanel keyboardAndDisplayPanel;
    private JTextField keyboardDisplay;

    //Keyboard letter buttons
    private JButton qButton;
    private JButton wButton;
    private JButton eButton;
    private JButton rButton;
    private JButton tButton;
    private JButton yButton;
    private JButton uButton;
    private JButton iButton;
    private JButton oButton;
    private JButton pButton;
    private JButton aButton;
    private JButton sButton;
    private JButton dButton;
    private JButton fButton;
    private JButton gButton;
    private JButton hButton;
    private JButton jButton;
    private JButton kButton;
    private JButton lButton;
    private JButton zButton;
    private JButton xButton;
    private JButton cButton;
    private JButton vButton;
    private JButton bButton;
    private JButton nButton;
    private JButton mButton;

    //Keyboard action buttons
    private JButton ENTERButton;
    private JButton enterButton;
    private JButton CAPSButton;
    private JButton SPACEButton;
    private JButton deleteButton;

    private JPanel displayPanel;
    private JPanel keyboardPanel;
    private JButton a1Button;
    private JButton a2Button;
    private JButton a3Button;
    private JButton a4Button;
    private JButton a5Button;
    private JButton a6Button;
    private JButton a7Button;
    private JButton a8Button;
    private JButton a9Button;
    private JButton a0Button;

    private Main mainFrame;
    private GUI lastGUI;
    private JTextField projectTextfield;

    private boolean capsLock = false;
    private boolean numOnly = false;

    private JButton[] keyboardButtons = {
            a1Button, a2Button, a3Button, a4Button, a5Button, a6Button, a7Button, a8Button, a9Button, a0Button,
            qButton, wButton, eButton, rButton, tButton, yButton, uButton, iButton, oButton, pButton,
            aButton, sButton, dButton, fButton, gButton, hButton, jButton, kButton, lButton,
            zButton, xButton, cButton, vButton, bButton, nButton, mButton};

    private JButton[] keyboardLetterButtons = {
            qButton, wButton, eButton, rButton, tButton, yButton, uButton, iButton, oButton, pButton,
            aButton, sButton, dButton, fButton, gButton, hButton, jButton, kButton, lButton,
            zButton, xButton, cButton, vButton, bButton, nButton, mButton};

    private JButton[] actionButtons = {ENTERButton, enterButton, CAPSButton, SPACEButton, deleteButton};

    public KeyboardGUI(Main mainFrame){
        this.mainFrame = mainFrame;
        CAPSButton.setForeground(ConfigFile.COLOR_TEXT_RED);
        for(JButton btn : keyboardButtons){
            btn.addActionListener(this);
            btn.setText(btn.getText().toLowerCase());
        }
        for(JButton btn : actionButtons){
            btn.addActionListener(this);
        }
    }

    public void enterText(JTextField textField){
        enterText(textField, false);
    }

    public void enterText(JTextField textField, boolean numOnly){
        this.numOnly = numOnly;
        if(numOnly){
            for(JButton btn : keyboardLetterButtons){
                btn.setEnabled(false);
            }
        }
        lastGUI = mainFrame.getCurrentGUI();
        projectTextfield = textField;
        mainFrame.switchGUI(mainFrame.keyboardGUI);
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

            //TODO Implement Action Buttons
            for(JButton btn : keyboardButtons){
                if(src.equals(btn)){
                    String typed;
                    if(capsLock){
                        typed = btn.getText().toUpperCase();
                    }else{
                        typed = btn.getText().toLowerCase();
                    }
                    keyboardDisplay.setText(keyboardDisplay.getText() + typed);
                    break;
                }
            }

            if(src.equals(enterButton) || src.equals(ENTERButton)){
                projectTextfield.setText(keyboardDisplay.getText());
                keyboardDisplay.setText("");
                mainFrame.switchGUI(lastGUI);
                lastGUI = null;
                projectTextfield = null;
                if(numOnly){
                    for(JButton btn : keyboardLetterButtons){
                        btn.setEnabled(true);
                    }
                }
            }else if(src.equals(CAPSButton)){
                capsLock = !capsLock;
                if(capsLock){
                    CAPSButton.setForeground(ConfigFile.COLOR_TEXT_GREEN);
                    for(JButton btn : keyboardButtons){
                        btn.setText(btn.getText().toUpperCase());
                    }
                }else{
                    CAPSButton.setForeground(ConfigFile.COLOR_TEXT_RED);
                    for(JButton btn : keyboardButtons){
                        btn.setText(btn.getText().toLowerCase());
                    }
                }
            }else if(src.equals(SPACEButton)){
                keyboardDisplay.setText(keyboardDisplay.getText() + " ");
            }else if(src.equals(deleteButton)){
                String text = keyboardDisplay.getText();
                if (text.length() > 0) keyboardDisplay.setText(text.substring(0, text.length()-1));
            }
        }

    }

    @Override
    public JPanel getGUI() {
        return keyboardAndDisplayPanel;
    }

    @Override
    public void init() {

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
    public void switchToGUI(GUI oldGUI) {

    }
}
