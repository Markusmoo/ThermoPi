package ca.tonsaker.thermopi.main.gui;

import javax.swing.*;

/**
 * Created by Marku on 2017-04-02.
 */
public class ThermostatGUI implements GUI{
    private JPanel thermostatPanel;
    private JButton decreaseButton;
    private JButton increaseButton;
    private JLabel setToVar;

    @Override
    public JPanel getGUI() {
        return thermostatPanel;
    }

    @Override
    public void init() {

    }

    @Override
    public void switchPerformed(GUI oldScreen) {

    }
}
