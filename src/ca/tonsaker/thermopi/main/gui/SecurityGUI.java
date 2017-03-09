package ca.tonsaker.thermopi.main.gui;

import ca.tonsaker.thermopi.main.Config;
import ca.tonsaker.thermopi.main.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Markus Tonsaker on 2017-03-07.
 */
public class SecurityGUI implements ActionListener{

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

    boolean isTyping = false;

    public SecurityGUI(){
        super();

        //Add listeners to keypad 0-9
        a0Button.addActionListener(this);
        a1Button.addActionListener(this);
        a2Button.addActionListener(this);
        a3Button.addActionListener(this);
        a4Button.addActionListener(this);
        a5Button.addActionListener(this);
        a6Button.addActionListener(this);
        a7Button.addActionListener(this);
        a8Button.addActionListener(this);
        a9Button.addActionListener(this);

        //Add Listeners to keypad Enter/ARMAway and Back/ARMHome
        ARMHomeOrBackButton.addActionListener(this);
        ARMAwayOrEnterButton.addActionListener(this);

    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if(src instanceof JButton){
            //KeyPad 0-9
            if(src.equals(a0Button)){
                codeField.setText(String.copyValueOf(codeField.getPassword())+"0");
                Utilities.tone(Config.SPEAKER_PIN, 100, Config.BUTTON_TONE);
            }else if(src.equals(a1Button)){
                codeField.setText(String.copyValueOf(codeField.getPassword())+"1");
                Utilities.tone(Config.SPEAKER_PIN, 100, Config.BUTTON_TONE);
            }else if(src.equals(a2Button)){
                codeField.setText(String.copyValueOf(codeField.getPassword())+"2");
                Utilities.tone(Config.SPEAKER_PIN, 100, Config.BUTTON_TONE);
            }else if(src.equals(a3Button)){
                codeField.setText(String.copyValueOf(codeField.getPassword())+"3");
                Utilities.tone(Config.SPEAKER_PIN, 100, Config.BUTTON_TONE);
            }else if(src.equals(a4Button)){
                codeField.setText(String.copyValueOf(codeField.getPassword())+"4");
                Utilities.tone(Config.SPEAKER_PIN, 100, Config.BUTTON_TONE);
            }else if(src.equals(a5Button)){
                codeField.setText(String.copyValueOf(codeField.getPassword())+"5");
                Utilities.tone(Config.SPEAKER_PIN, 100, Config.BUTTON_TONE);
            }else if(src.equals(a6Button)){
                codeField.setText(String.copyValueOf(codeField.getPassword())+"6");
                Utilities.tone(Config.SPEAKER_PIN, 100, Config.BUTTON_TONE);
            }else if(src.equals(a7Button)){
                codeField.setText(String.copyValueOf(codeField.getPassword())+"7");
                Utilities.tone(Config.SPEAKER_PIN, 100, Config.BUTTON_TONE);
            }else if(src.equals(a8Button)){
                codeField.setText(String.copyValueOf(codeField.getPassword())+"8");
                Utilities.tone(Config.SPEAKER_PIN, 100, Config.BUTTON_TONE);
            }else if(src.equals(a9Button)){
                codeField.setText(String.copyValueOf(codeField.getPassword())+"9");
                Utilities.tone(Config.SPEAKER_PIN, 100, Config.BUTTON_TONE);
            }


            if(src.equals(ARMHomeOrBackButton)){
                if(isTyping){
                    char[] pass = codeField.getPassword();
                    if (pass.length <= 0) return;
                    codeField.setText(String.copyValueOf(codeField.getPassword()).substring(0, pass.length - 1));
                    Utilities.tone(Config.SPEAKER_PIN, 100, Config.BUTTON_TONE);
                }else{
                    //TODO Implement ARM HOME
                }
            }else if(src.equals(ARMAwayOrEnterButton)){
                if(isTyping){
                    //TODO Send code to Security Receiver
                }else{
                    //TODO Implement ARM AWAY
                }
            }

            //TODO Implement rest of the buttons

        }
        if(codeField.getPassword().length <= 0){
            isTyping = false;
            ARMHomeOrBackButton.setText("ARM - Home");
            ARMHomeOrBackButton.setForeground(Config.COLOR_TEXT_RED);
            ARMAwayOrEnterButton.setText("ARM - Away");
            ARMAwayOrEnterButton.setForeground(Config.COLOR_TEXT_RED);
        }else{
            isTyping = true;
            ARMHomeOrBackButton.setText("Backspace");
            ARMHomeOrBackButton.setForeground(Config.COLOR_TEXT_GREEN);
            ARMAwayOrEnterButton.setText("Enter");
            ARMAwayOrEnterButton.setForeground(Config.COLOR_TEXT_GREEN);
        }
        System.out.println(codeField.getPassword()); //TODO Remove this for security reasons
    }
}
