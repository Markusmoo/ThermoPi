package ca.tonsaker.thermopi.main;

import ca.tonsaker.thermopi.main.gui.DebugGUI;

import java.awt.*;

/**
 * Created by Marku on 2017-03-09.
 */
public abstract class Debug {

    private static boolean showHigh = true;
    private static boolean showMedium = true;
    private static boolean showLow = true;
    private static boolean showDebug = true;

    //If adding a new constant, change lvl scope in println()
    public static final int HIGH = 2;
    public static final int MEDIUM = 1;
    public static final int LOW = 0;
    public static final int DEBUG = -1;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    private static boolean showConsoleColors = true;

    private static DebugGUI debugGUI;

    public static void setDebugGUI(DebugGUI debugGUI){
        if(debugGUI == null) throw new NullPointerException();
        Debug.debugGUI = debugGUI;
    }

    public static void println(int lvl, String txt){
        if(lvl > 2 || lvl < -1){ //lvl scope
            System.err.println(lvl + " -Is not a valid lvl for println!");
        }

        if(lvl == Debug.DEBUG && Debug.showDebug){
            if(showConsoleColors){
                System.out.println(Debug.ANSI_CYAN + "[DEBUG]: " + txt + Debug.ANSI_RESET);
                debugGUI.getConsole().appendToPane(Config.COLOR_TEXT_CYAN, "[DEBUG]: " + txt);
            }else{
                System.out.println("[DEBUG]: " + txt);
                debugGUI.getConsole().appendToPane("[DEBUG]: " + txt);
            }
        }else if(lvl == Debug.LOW && Debug.showLow) {
            if(showConsoleColors){
                System.out.println(Debug.ANSI_BLUE + "[LOW]: " + txt + Debug.ANSI_RESET);
                debugGUI.getConsole().appendToPane(Config.COLOR_TEXT_BLUE, "[LOW]: " + txt);
            }else{
                System.out.println("[LOW]: " + txt);
                debugGUI.getConsole().appendToPane("[LOW]: " + txt);
            }
        }else if(lvl == Debug.MEDIUM && Debug.showMedium) {
            if(showConsoleColors){
                System.out.println(ANSI_GREEN + "[MEDIUM]: " + txt + Debug.ANSI_RESET);
                debugGUI.getConsole().appendToPane(Config.COLOR_TEXT_GREEN, "[MEDIUM]: " + txt);
            }else{
                System.out.println("[MEDIUM]: " + txt);
                debugGUI.getConsole().appendToPane("[MEDIUM]: " + txt);
            }
        }else if(lvl == Debug.HIGH && Debug.showHigh){
            if(showConsoleColors){
                System.out.println(Debug.ANSI_YELLOW + "[HIGH]: " + txt + Debug.ANSI_RESET);
                debugGUI.getConsole().appendToPane(Config.COLOR_TEXT_YELLOW, "[HIGH]: " + txt);
            }else{
                System.out.println("[HIGH]: " + txt);
                debugGUI.getConsole().appendToPane("[HIGH]: " + txt);
            }
        }
    }

    public static void setFilters(boolean showDebug, boolean showHigh, boolean showMedium, boolean showLow){
        Debug.showDebug = showDebug;
        Debug.showHigh = showHigh;
        Debug.showMedium = showMedium;
        Debug.showLow = showLow;
    }

    public static void consoleColorVisible(boolean isVisible){
        Debug.showConsoleColors = isVisible;
    }

}