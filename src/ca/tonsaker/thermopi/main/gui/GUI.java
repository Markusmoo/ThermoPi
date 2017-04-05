package ca.tonsaker.thermopi.main.gui;

import javax.swing.*;

/**
 * Created by Markus Tonsaker on 2017-03-13.
 */
public interface GUI {

    JPanel getGUI();
    void init();

    void switchToGUI(GUI oldScreen);
    void switchAwayGUI(GUI newScreen);
    void screenWakeup();
    void screenSleep();

}
