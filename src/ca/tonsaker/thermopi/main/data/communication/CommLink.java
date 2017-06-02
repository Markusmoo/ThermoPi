package ca.tonsaker.thermopi.main.data.communication;

import ca.tonsaker.thermopi.main.Debug;
import ca.tonsaker.thermopi.main.Main;
import ca.tonsaker.thermopi.main.Utilities;
import ca.tonsaker.thermopi.main.data.ConfigFile;
import com.pi4j.io.serial.*;
import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Markus Tonsaker on 2017-03-19.
 *
 * By default, the serial port on the Raspberry Pi is configured as a console port for communicating with the
 * Linux OS shell. If you want to use the serial port in a software program,
 * you must disable the OS from using this port. Please see this blog article by Clayton Smith
 * for step-by-step instructions on how to disable the OS console for this port:
 * http://www.irrational.net/2012/04/19/using-the-raspberry-pis-serial-port/
 */
public class CommLink implements SerialDataEventListener{

    public static final int HOME = 32;
    public static final int AWAY = 33;

    private static Queue<String> last10SerialCommands = new LinkedList<>();

    static Serial serial;
    static SerialConfig serialConfig;
    Main main;

    public CommLink(Main main){
        this.main = main;
        if(ConfigFile.serialOn) {
            serial = SerialFactory.createInstance();
            setupSerial();
        }
    }

    public void setupSerial(){
        serialConfig = new SerialConfig();
        try {
            serialConfig.device(SerialPort.getDefaultPort()).baud(Baud._9600)
                    .dataBits(DataBits._8)
                    .parity(Parity.NONE)
                    .stopBits(StopBits._1)
                    .flowControl(FlowControl.NONE);
        }catch (IOException e){
            Debug.println(Debug.ERROR, "Failed to setup serial!");
            e.printStackTrace();
        }catch (InterruptedException e2) {
            Debug.println(Debug.ERROR, "Failed to setup serial!");
            e2.printStackTrace();
        }
        serial.addListener(this);
        try {
            serial.open(serialConfig);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean waitForResponse(){
        try {
            long timeout = 5000;
            long currTime = System.currentTimeMillis();
            while(true){
                for(String lc : last10SerialCommands) {
                    if (lc.equals("DENIED")){
                        last10SerialCommands.remove();
                        return false;
                    }
                    if (lc.equals("ACCEPTED")){
                        last10SerialCommands.remove();
                        return true;
                    }
                }
                if(System.currentTimeMillis() - currTime > timeout){
                    serial.close();
                    Debug.println(Debug.ERROR, "Never received a response from ThermoHQ in 5 seconds.  Exiting ThermoPi..");
                    System.exit(-1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void sendTestAlarm(){
        sendData("<TEST:ALARM>");
    }

    public static void sendTestFurnace(){
        sendData("<TEST:FURNACE>");
    }


    public static void sendTemperatureSet(byte temp){
        String command = "<TEMP:SET:" + Math.abs(temp) + ">";
        sendData(command);
    }

    public static boolean sendPassChange(char[] newCode, char[] oldCode){
        if(String.copyValueOf(newCode).equals("")) return false;
        if(String.copyValueOf(oldCode).equals("")) return false;
        if(String.copyValueOf(oldCode).equals(String.copyValueOf(newCode))) return false;
        String command = "<PASSCHANGE:" + String.copyValueOf(oldCode) + ":" + String.copyValueOf(newCode) + ">";
        sendData(command);
        return waitForResponse();
    }

    public static boolean sendArm(int status){
        if(status == HOME) sendData("<ARM:HOME>"); else
        if(status == AWAY) sendData("<ARM:AWAY>");
        return waitForResponse();
    }

    public static boolean sendUnarm(char[] code){
        if(String.copyValueOf(code).equals("")) return false;
        String command = "<UNARM:" + String.copyValueOf(code) + ">";
        sendData(command);
        return waitForResponse();
    }

    public static void sendRefresh(){
        sendData("<REFRESH>");
    }

    public static void sendData(String str){
        try {
            if(!ConfigFile.serialOn) return;
            Debug.println(Debug.DEBUG, "Sent: "+str);
            serial.write(str);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    @Contract(pure = true)
    private boolean integerToBoolean(int i){
        if(i == 0) return false; else return true;
    }

    /**
     * TODO * - Means done
     * TODO
     * TODO * Temperature IN
     * TODO * Temperature OUT
     * TODO * Temperature SET
     * TODO * ZONES
     * TODO Armed - AWAY
     * TODO Armed - HOME
     * TODO Unarmed
     *
     * @param s
     */
    public void applyFunction(String s){
        String[] commands = s.split(":");
        if (commands.length <= 0){
            Debug.println(Debug.ERROR, "Error, serial received does not contain anything therefore it could not be applied.");
            return;
        }

        last10SerialCommands.add(commands[0]);

        if(commands[0].contains("ZONE")){ //Set which zones are activated
            for(int i = 0; i < commands[1].toCharArray().length; i++){
                main.securityGUI.highlightZone(i, integerToBoolean(Character.getNumericValue(commands[1].toCharArray()[i])));
            }
        }else if(commands[0].contains("TEMP")){ //Set the temperature displays
            if(commands[1].contains("IN")) { //Inside temperature
                if (isInteger(commands[2])) {
                    main.thermostatGUI.setTemperatureInside(Integer.parseInt(commands[2]));
                } else {
                    Debug.println(Debug.ERROR, "Serial received a corrupted temperature command: " + s);
                }
            }else if(commands[1].contains("OUT")){ //Outside temperature
                if (isInteger(commands[2])) {
                    main.thermostatGUI.setTemperatureOutside(Integer.parseInt(commands[2]));
                } else {
                    Debug.println(Debug.ERROR, "Serial received a corrupted temperature command: " + s);
                }
            }else if(commands[1].contains("SET")){ //Desired temperature
                if (isInteger(commands[2])) {
                    main.thermostatGUI.setTemperatureSet(Integer.parseInt(commands[2]));
                } else {
                    Debug.println(Debug.ERROR, "Serial received a corrupted temperature command: " + s);
                }
            }else{
                Debug.println(Debug.ERROR, "Serial received a corrupted temperature command: " + s);
            }
        }else if(commands[0].contains("BEEP")) { //Beep
            Utilities.buttonTone(); //TODO add way to turn off
        }else if(commands[0].contains("ARMED")){ //Armed
            if(commands[1].contains("HOME")){
                Utilities.armHome();
            }else if(commands[1].contains("AWAY")){
                Utilities.armAway();
            }else if(commands[1].contains("UNARMED")){
                Utilities.unarm();
            }
        }

    }
    @Override
    public void dataReceived(SerialDataEvent e) {
        try {
            byte[] received = e.getSerial().read();
            String rStr = new String(received);
            Debug.println(Debug.DEBUG, "Serial Port Received: "+rStr);

            String[] commands = rStr.split("<");
            for(String command : commands) applyFunction(command.replace(">", ""));

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
