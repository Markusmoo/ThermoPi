package ca.tonsaker.thermopi.main.gui;

import javax.swing.*;

/**
 * Created by Marku on 2017-03-13.
 */
public interface GUI {

    JPanel getGUI();
    void init();
    void switchPerformed();

}
