package ca.tonsaker.thermopi.main.gui;

import ca.tonsaker.thermopi.main.Config;
import ca.tonsaker.thermopi.main.Debug;
import ca.tonsaker.thermopi.main.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Markus Tonsaker on 2017-03-07.
 */
public class SecurityGUI implements GUI, ActionListener{

    private JButton a0Button;
    private JButton a7Button;
    private JButton a4Button;
    private JButton a1Button;
    private JButton a8Button;
    private JButton a5Button;
    private JButton a2Button;
    private JButton a9Button;
    private JButton a6Button;
    private JButton a3Button;
    private JButton ARMHomeOrBackButton;
    private JButton ARMAwayOrEnterButton;
    public  JPanel securityPanel;
    private JPasswordField codeField;
    private JList zoneList;
    private JLabel statusLabel;
    private JLabel variableStatusLabel;

    private JButton[] numPad = {a0Button, a1Button, a2Button, a3Button, a4Button, a5Button, a6Button, a7Button, a8Button, a9Button};

    boolean isTyping = false;

    public SecurityGUI(){
        super();

        //Add listeners to keypad 0-9
        for(JButton b : numPad){
            b.addActionListener(this);
        }

        //Add Listeners to keypad Enter/ARMAway and Back/ARMHome
        ARMHomeOrBackButton.addActionListener(this);
        ARMAwayOrEnterButton.addActionListener(this);

        //Add Listener to Code Field
        codeField.addActionListener(this);

        //TODO Receive current state

        this.actionPerformed(new ActionEvent(new Object(), 0, "")); //Initialize
    }

    public void init(){

    }

    private void armAway(){
        Debug.println(Debug.LOW, "Requesting to ARM - AWAY..");
        if(JOptionPane.showConfirmDialog(securityPanel, "Are you sure you would like to ARM - AWAY?") != JOptionPane.OK_OPTION) return;
        System.out.println("Setting ThermoPi status to: ARM - AWAY..");
        Config.STATUS = Config.STATUS_ARMED_AWAY;  //TODO (Mode: DEBUG) Request to arm
        variableStatusLabel.setText("ARM - AWAY");
        variableStatusLabel.setForeground(Config.COLOR_TEXT_RED);
        for(JButton b : numPad){
            b.setEnabled(true);
        }
    }

    private  void armHome(){
        Debug.println(Debug.LOW, "Requesting to ARM - HOME..");
        if(JOptionPane.showConfirmDialog(securityPanel, "Are you sure you would like to ARM - HOME?") != JOptionPane.OK_OPTION) return;
        Debug.println(Debug.HIGH, "Setting ThermoPi status to: ARM - HOME..");
        Config.STATUS = Config.STATUS_ARMED_HOME;  //TODO (Mode: DEBUG) Request to arm
        variableStatusLabel.setText("ARM - HOME");
        variableStatusLabel.setForeground(Config.COLOR_TEXT_RED);
        for(JButton b : numPad){
            b.setEnabled(true);
        }
    }

    public void unlockAndUnArm(char[] code){
        if(Config.STATUS == Config.STATUS_UNARMED){
            Debug.println(Debug.LOW, "ThermoPi is already UNARMED");
            JOptionPane.showMessageDialog(securityPanel, "ThermoPi is already unarmed!");
            return;
        }
        codeField.setText("");
        isTyping = false;
        Debug.println(Debug.HIGH, "Attempting to UNARM ThermoPi with inputted code..");
        //TODO Send code and wait for response.
        //TODO Add failed unlock attempt counter.
        Debug.println(Debug.HIGH, "Code ACCEPTED - Unlocking ThermoPi..");
        Config.STATUS = Config.STATUS_UNARMED;
        variableStatusLabel.setText("UNARMED");
        variableStatusLabel.setForeground(Config.COLOR_TEXT_GREEN);
        for(JButton b : numPad){
            b.setEnabled(false);
        }
        JOptionPane.showMessageDialog(securityPanel, "ThermoPi is now UNARMED!");
    }

    public void setTyping(boolean typing){
        isTyping = typing;
        if(typing){
            ARMHomeOrBackButton.setText("Backspace");
            ARMHomeOrBackButton.setForeground(Config.COLOR_TEXT_GREEN);
            ARMAwayOrEnterButton.setText("Enter");
            ARMAwayOrEnterButton.setForeground(Config.COLOR_TEXT_GREEN);
        }else{

        }
    }

    public void backspaceCode(){
        char[] pass = codeField.getPassword();
        if (pass.length <= 0) return;
        codeField.setText(String.copyValueOf(codeField.getPassword()).substring(0, pass.length - 1));
    }

    public JPanel getGUI(){
        return securityPanel;
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if(src instanceof JButton){
            //KeyPad 0-9
            for(int idx = 0; idx < numPad.length; idx++){
                if(src.equals(numPad[idx])){
                    codeField.setText(String.copyValueOf(codeField.getPassword())+idx);
                    Utilities.buttonTone();
                    break;
                }
            }

            if(src.equals(ARMHomeOrBackButton)){
                if(isTyping){
                    backspaceCode();
                    Utilities.tone(100, Config.BUTTON_TONE);
                }else if(Config.STATUS == Config.STATUS_UNARMED){
                    //TODO ARM HOME
                    armHome();
                }
            }else if(src.equals(ARMAwayOrEnterButton)){
                if(isTyping){
                    unlockAndUnArm(codeField.getPassword());
                }else{
                    //TODO Implement ARM AWAY
                    armAway();
                }
            }

        }

        if(codeField.getPassword().length <= 0 && Config.STATUS == Config.STATUS_UNARMED){
            isTyping = false;
            ARMHomeOrBackButton.setText("ARM - Home");
            ARMHomeOrBackButton.setForeground(Config.COLOR_TEXT_RED);
            ARMAwayOrEnterButton.setText("ARM - Away");
            ARMAwayOrEnterButton.setForeground(Config.COLOR_TEXT_RED);
        }else if(codeField.getPassword().length > 0){
            if(!isTyping) setTyping(true);
            ARMHomeOrBackButton.setVisible(true);
            ARMAwayOrEnterButton.setVisible(true);
        }else{
            ARMHomeOrBackButton.setVisible(false);
            ARMAwayOrEnterButton.setVisible(false);
        }
        //Debug.println(Debug.DEBUG, String.valueOf(codeField.getPassword())); //TODO Remove this for security reasons
    }
}
