package ca.tonsaker.thermopi.main.gui;

import ca.tonsaker.thermopi.main.Debug;
import ca.tonsaker.thermopi.main.Main;
import ca.tonsaker.thermopi.main.gui.helper.JColorTextPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Markus Tonsaker on 2017-03-13.
 *
 * TODO Change close button on GUI to set visible to false instead of closing application
 */
public class DebugGUI extends JFrame implements ActionListener{

    private JButton settingsButton;
    private JButton thermostatButton;
    private JButton weatherButton;
    private JButton homescreenButton;
    private JButton securityButton;
    private JPanel contentPane;
    private JColorTextPane textPane;

    private JButton[] buttons = {settingsButton, thermostatButton, weatherButton, homescreenButton, securityButton};

    private Main main;

    public DebugGUI(){
        super("ThermoPi Debugger");
        setContentPane(this.contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 100;
        if(height >= 100) setBounds(0,0,this.getWidth(), height);
        Debug.setFilters(true, true, true, true);
        Debug.consoleColorVisible(true);

        for(JButton btn : buttons) btn.addActionListener(this);
    }

    public void setMain(Main main){
        this.main = main;
    }

    public JColorTextPane getConsole(){
        return textPane;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if(src instanceof JButton){
            if(src.equals(homescreenButton)){
                main.switchGUI(main.homescreenGUI);
            }else if(src.equals(securityButton)){
                main.switchGUI(main.securityGUI);
            }else if(src.equals(thermostatButton)){
                main.switchGUI(main.thermostatGUI);
            }else if(src.equals(weatherButton)){
                //TODO main.switchGUI(main.weatherGUI);
                Debug.wipPopup();
            }else if(src.equals(settingsButton)){
                main.switchGUI(main.settingsGUI);
            }
        }
    }
}
