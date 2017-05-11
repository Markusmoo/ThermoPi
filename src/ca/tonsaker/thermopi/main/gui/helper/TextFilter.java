package ca.tonsaker.thermopi.main.gui.helper;

import ca.tonsaker.thermopi.main.Utilities;
import ca.tonsaker.thermopi.main.data.ConfigFile;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/**
 * Created by Markus Tonsaker on 5/10/2017.
 */
public class TextFilter extends DocumentFilter {

    protected boolean numOnly = false;
    protected int maxLength = -1;

    public TextFilter(boolean numOnly, int maxLength){
        super();
        this.numOnly = numOnly;
        this.maxLength = maxLength;
    }

    public boolean isNumOnly() {
        return numOnly;
    }

    public void setNumOnly(boolean numOnly) {
        this.numOnly = numOnly;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if(numOnly) {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.insert(offset, string);

            if (test(sb.toString()) && (maxLength < 0 || string.length() <= maxLength)) {
                super.insertString(fb, offset, string, attr);
            } else {
                if (!ConfigFile.debugMode) Utilities.buttonTone();
            }
        }else{
            if(maxLength < 0 || string.length() < maxLength){
                super.insertString(fb, offset, string, attr);
            } else {
                if (!ConfigFile.debugMode) Utilities.buttonTone();
            }
        }
    }

    private boolean test(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if(numOnly) {
            Document doc = fb.getDocument();
            StringBuilder sb = new StringBuilder();
            sb.append(doc.getText(0, doc.getLength()));
            sb.replace(offset, offset + length, text);

            if (test(sb.toString()) && (maxLength < 0 || text.length() <= maxLength)) {
                super.replace(fb, offset, length, text, attrs);
            } else {
                if (!ConfigFile.debugMode) Utilities.buttonTone();
            }
        }else{
            if(maxLength < 0 || text.length() < maxLength){
                super.replace(fb, offset, length, text, attrs);
            } else {
                if (!ConfigFile.debugMode) Utilities.buttonTone();
            }
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.delete(offset, offset + length);

        if(maxLength < 0 || sb.toString().length() <= maxLength){
            super.remove(fb, offset, length);
        } else {
            if (!ConfigFile.debugMode) Utilities.buttonTone();
        }
    }
}