package ca.tonsaker.thermopi.main.gui.helper;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * Created by Markus Tonsaker on 2017-03-13.
 */
public class JColorTextPane extends JTextPane{

    private Color defaultForegroundColor = Color.WHITE;

    public JColorTextPane(){
        super();
    }

    public void setDefaultForegroundColor(Color color){
        defaultForegroundColor = color;
    }

    public void appendLnToPane(Color color, String txt){
        this.appendToPane(color, txt+"\n");
    }

    public void appendLnToPane(String txt){ this.appendToPane(txt+"\n"); }

    public void appendToPane(Color color, String txt){
        StyleContext sc = StyleContext.getDefaultStyleContext();
        SimpleAttributeSet aset = new SimpleAttributeSet();
        StyleConstants.setForeground(aset, color);

        int len = this.getDocument().getLength();
        this.setCaretPosition(len);
        try {
            this.getDocument().insertString(len, txt, aset);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void appendToPane(String txt){
        this.appendToPane(defaultForegroundColor, txt);
    }
}
