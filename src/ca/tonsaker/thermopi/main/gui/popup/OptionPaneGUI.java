package ca.tonsaker.thermopi.main.gui.popup;

import ca.tonsaker.thermopi.main.Main;
import ca.tonsaker.thermopi.main.gui.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Marku on 2017-03-20.
 */

//TODO Figure out way to pause code and return a value like JOptionPane
public class OptionPaneGUI implements GUI, ActionListener{

    private Main mainFrame;
    private GUI lastGui;

    private JLabel questionLabel;
    private JButton leftButton;
    private JButton centerButton;
    private JButton rightButton;
    private JPanel optionPanel;

    public OptionPaneGUI(Main mainFrame){
        this.mainFrame = mainFrame;
    }

    public void createOptionPane(String msg, String leftButtonTxt, String centerButtonTxt, String rightButtonTxt){
        this.lastGui = mainFrame.getCurrentGUI();
        this.mainFrame.switchGUI(this);

        questionLabel.setText(msg);

        leftButton.addActionListener(this);
        centerButton.addActionListener(this);
        rightButton.addActionListener(this);

        if(leftButtonTxt != null && !leftButtonTxt.equals("")) leftButton.setText(leftButtonTxt); else leftButton.setEnabled(false);
        if(centerButtonTxt != null && !centerButtonTxt.equals("")) centerButton.setText(centerButtonTxt); else centerButton.setEnabled(false);
        if(rightButtonTxt != null && !rightButtonTxt.equals("")) rightButton.setText(rightButtonTxt); else rightButton.setEnabled(false);
    }

    @Override
    public JPanel getGUI() {
        return optionPanel;
    }

    @Override
    public void init() {

    }

    @Override
    public void switchPerformed() {

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
            leftButton.setEnabled(true);
            centerButton.setEnabled(true);
            rightButton.setEnabled(true);
            mainFrame.switchGUI(lastGui);
        }
    }
}
