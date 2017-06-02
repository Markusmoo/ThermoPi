package ca.tonsaker.thermopi.main.gui;

import ca.tonsaker.thermopi.main.Debug;
import ca.tonsaker.thermopi.main.Main;
import ca.tonsaker.thermopi.main.Utilities;
import ca.tonsaker.thermopi.main.data.ConfigFile;
import ca.tonsaker.thermopi.main.data.communication.CommLink;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

/**
 * Created by Markus Tonsaker on 2017-03-07.
 * TODO Prevent user from being able to select zones
 */
public class SecurityGUI implements GUI, ActionListener{

    public static final int UNARMED = 22;
    public static final int AWAY = 33;
    public static final int HOME = 44;

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
    private DefaultListModel<String> zoneListModel;
    private JLabel statusLabel;
    public JLabel variableStatusLabel;

    public JButton[] numPad = {a0Button, a1Button, a2Button, a3Button, a4Button, a5Button, a6Button, a7Button, a8Button, a9Button};

    private boolean[] selectedZones;

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

        this.actionPerformed(new ActionEvent(new Object(), 0, "")); //Initialize
        for(MouseListener m : zoneList.getMouseListeners()){
            zoneList.removeMouseListener(m);
        }
        for(MouseMotionListener m : zoneList.getMouseMotionListeners()){
            zoneList.removeMouseMotionListener(m);
        }
    }

    public void init(){
        initUIComponents();
    }

    private void requestArmHome() {
        Debug.println(Debug.LOW, "Requesting to ARM - HOME..");
        if (JOptionPane.showConfirmDialog(securityPanel, "Are you sure you would like to ARM - HOME?") != JOptionPane.OK_OPTION)
            return;
        if(CommLink.sendArm(CommLink.HOME)) JOptionPane.showMessageDialog(securityPanel, "Arming for HOME");
    }

    private void requestArmAway(){
        Debug.println(Debug.LOW, "Requesting to ARM - AWAY..");
        if(JOptionPane.showConfirmDialog(securityPanel, "Are you sure you would like to ARM - AWAY?") != JOptionPane.OK_OPTION)
            return;
        if(CommLink.sendArm(CommLink.AWAY)) JOptionPane.showMessageDialog(securityPanel, "Arming for AWAY");
    }

    @Override
    public void switchToGUI(GUI oldGUI) {
        initUIComponents();
    }

    public void highlightZone(int idx, boolean highlighted) {
        selectedZones[idx] = highlighted;
        int length = 0;
        for(boolean b : selectedZones) if(b) length++;
        int[] arrayList = new int[length];
        int arrayIdx = 0;
        for(int i = 0; i < selectedZones.length; i++) if(selectedZones[i]) arrayList[arrayIdx++] = i;
        zoneList.setSelectedIndices(arrayList);
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

    public void unlockAndUnArm(char[] code){
        if(ConfigFile.STATUS == ConfigFile.STATUS_UNARMED){
            Debug.println(Debug.LOW, "ThermoPi is already UNARMED");
            JOptionPane.showMessageDialog(securityPanel, "ThermoPi is already unarmed!");
            return;
        }
        codeField.setText("");
        isTyping = false;
        Debug.println(Debug.HIGH, "Attempting to UNARM ThermoPi with inputted code..");
        if(!CommLink.sendUnarm(code)) JOptionPane.showMessageDialog(securityPanel, "Invalid code!");
    }

    public void displayState(int state){
        if(state == UNARMED){
            System.out.println("1");
            variableStatusLabel.setText("UNARMED");
            variableStatusLabel.setForeground(ConfigFile.COLOR_TEXT_GREEN);
            ARMHomeOrBackButton.setText("ARM - Home");
            ARMHomeOrBackButton.setForeground(ConfigFile.COLOR_TEXT_RED);
            ARMAwayOrEnterButton.setText("ARM - Away");
            ARMAwayOrEnterButton.setForeground(ConfigFile.COLOR_TEXT_RED);
            ARMHomeOrBackButton.setVisible(true);
            ARMAwayOrEnterButton.setVisible(true);
            codeField.setText("");
        }else if(state == HOME){
            System.out.println("2");
            variableStatusLabel.setText("ARM - HOME");
            variableStatusLabel.setForeground(ConfigFile.COLOR_TEXT_RED);
            ARMHomeOrBackButton.setVisible(false);
            ARMAwayOrEnterButton.setVisible(false);
        }else if(state == AWAY){
            System.out.println("3");
            variableStatusLabel.setText("ARM - AWAY");
            variableStatusLabel.setForeground(ConfigFile.COLOR_TEXT_RED);
            ARMHomeOrBackButton.setVisible(false);
            ARMAwayOrEnterButton.setVisible(false);
        }
    }

    public void setTyping(boolean typing){
        isTyping = typing;
        if(typing){
            ARMHomeOrBackButton.setText("Backspace");
            ARMHomeOrBackButton.setForeground(ConfigFile.COLOR_TEXT_GREEN);
            ARMAwayOrEnterButton.setText("Enter");
            ARMAwayOrEnterButton.setForeground(ConfigFile.COLOR_TEXT_GREEN);
            ARMHomeOrBackButton.setVisible(true);
            ARMAwayOrEnterButton.setVisible(true);
        }else{
            ARMHomeOrBackButton.setVisible(false);
            ARMAwayOrEnterButton.setVisible(false);
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
                    break;
                }
            }

            if(src.equals(ARMHomeOrBackButton)){
                if(isTyping){
                    backspaceCode();
                }else if(ConfigFile.STATUS == ConfigFile.STATUS_UNARMED){
                    //TODO ARM HOME
                    requestArmHome();
                }
            }else if(src.equals(ARMAwayOrEnterButton)){
                if(isTyping){
                    unlockAndUnArm(codeField.getPassword());
                }else{
                    //TODO Implement ARM AWAY
                    requestArmAway();
                }
            }
            if(Main.cfg.options.isKeypadTone) Utilities.buttonTone();
        }

        if(codeField.getPassword().length <= 0 && ConfigFile.STATUS == ConfigFile.STATUS_UNARMED){
            isTyping = false;
            ARMHomeOrBackButton.setText("ARM - Home");
            ARMHomeOrBackButton.setForeground(ConfigFile.COLOR_TEXT_RED);
            ARMAwayOrEnterButton.setText("ARM - Away");
            ARMAwayOrEnterButton.setForeground(ConfigFile.COLOR_TEXT_RED);
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

    private void initUIComponents() {
        zoneListModel = new DefaultListModel<>();
        for(int idx = 0; idx < Main.cfg.zoneNames.length; idx++){
            zoneListModel.add(idx, Main.cfg.zoneNames[idx]);
        }
        zoneList.setModel(zoneListModel);
        selectedZones = new boolean[zoneListModel.size()];
        for(int i = 0; i < selectedZones.length; i++){
            selectedZones[i] = false;
        }
    }
}
