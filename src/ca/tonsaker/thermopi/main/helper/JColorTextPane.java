package ca.tonsaker.thermopi.main.helper;

import ca.tonsaker.thermopi.main.Config;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * Created by Marku on 2017-03-13.
 */
public class JColorTextPane extends JTextPane{

    private Color defaultForegroundColor = Color.WHITE;

    public JColorTextPane(){
        super();
    }

    public void setDefaultForegroundColor(Color color){
        defaultForegroundColor = color;
    }

    public void appendToPane(Color color, String txt){
        System.out.println("NO");
        StyleContext sc = StyleContext.getDefaultStyleContext();
        SimpleAttributeSet aset = new SimpleAttributeSet();
        StyleConstants.setForeground(aset, color);

        int len = this.getDocument().getLength();
        this.setCaretPosition(len);
        try {
            this.getDocument().insertString(len, txt + "\n", aset);  //Remove \n if for a different project
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void appendToPane(String txt){
        System.out.println("YES");
        this.appendToPane(defaultForegroundColor, txt);
    }
}
