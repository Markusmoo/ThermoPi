package ca.tonsaker.thermopi.main.gui;

import ca.tonsaker.thermopi.main.Config;
import ca.tonsaker.thermopi.main.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Marku on 2017-03-19.
 */
public class KeyboardGUI implements ActionListener, GUI{
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

    private Main mainFrame;
    private GUI lastGUI;
    private JTextField projectTextfield;

    private boolean capsLock = false;

    private JButton[] keyboardButtons = {qButton, wButton, eButton, rButton, tButton, yButton, uButton, iButton, oButton, pButton,
                                        aButton, sButton, dButton, fButton, gButton, hButton, jButton, kButton, lButton,
                                        zButton, xButton, cButton, vButton, bButton, nButton, mButton};

    private JButton[] actionButtons = {ENTERButton, enterButton, CAPSButton, SPACEButton, deleteButton};

    public KeyboardGUI(Main mainFrame){
        this.mainFrame = mainFrame;
        CAPSButton.setForeground(Config.COLOR_TEXT_RED);
        for(JButton btn : keyboardButtons){
            btn.addActionListener(this);
            btn.setText(btn.getText().toLowerCase());
        }
        for(JButton btn : actionButtons){
            btn.addActionListener(this);
        }
    }

    public void enterText(JTextField textField){
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
            }else if(src.equals(CAPSButton)){
                capsLock = !capsLock;
                if(capsLock){
                    CAPSButton.setForeground(Config.COLOR_TEXT_GREEN);
                    for(JButton btn : keyboardButtons){
                        btn.setText(btn.getText().toUpperCase());
                    }
                }else{
                    CAPSButton.setForeground(Config.COLOR_TEXT_RED);
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
}
