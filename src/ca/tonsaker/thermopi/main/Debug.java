package ca.tonsaker.thermopi.main;

/**
 * Created by Marku on 2017-03-09.
 */
public class Debug {

    public static boolean showHigh = true;
    public static boolean showMedium = true;
    public static boolean showLow = true;
    public static boolean showDebug = true;

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

    public static void println(int lvl, String txt){
        if(lvl > 2 || lvl < -1){ //lvl scope
            System.err.println(lvl + " -Is not a valid lvl for println!");
        }

        if(lvl == Debug.DEBUG && Debug.showDebug){
            System.out.println(Debug.ANSI_CYAN + "[DEBUG]: " + txt + Debug.ANSI_RESET);
        }else if(lvl == Debug.LOW && Debug.showLow) {
            System.out.println(Debug.ANSI_BLUE + "[LOW]: " + txt + Debug.ANSI_RESET);
        }else if(lvl == Debug.MEDIUM && Debug.showMedium) {
            System.out.println(ANSI_GREEN + "[MEDIUM]: " + txt + Debug.ANSI_RESET);
        }else if(lvl == Debug.HIGH && Debug.showHigh){
            System.out.println(Debug.ANSI_YELLOW + "[HIGH]: " + txt + Debug.ANSI_RESET);
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
