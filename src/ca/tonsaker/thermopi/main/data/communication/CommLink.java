package ca.tonsaker.thermopi.main.data.communication;

import ca.tonsaker.thermopi.main.Debug;
import ca.tonsaker.thermopi.main.Main;
import ca.tonsaker.thermopi.main.data.ConfigFile;
import com.pi4j.io.serial.*;
import org.jetbrains.annotations.Contract;

import java.io.IOException;

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
            //TODO Debug Error
            e.printStackTrace();
        }catch (InterruptedException e2) {
            //TODO Debug Error
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
            int timeout = 1000;
            while(true){
                String received = new String(serial.read());
                if(!received.equals("")){
                    if(received.contains("DENIED")) return false;
                    if(received.contains("ACCEPTED")) return true;
                }
                timeout--;
                if(timeout <= 0){
                    Debug.println(Debug.ERROR, "Never received a response from ThermoHQ!");
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
        if(String.copyValueOf(newCode).equals("")) return false; //TODO Redundant???
        if(String.copyValueOf(oldCode).equals("")) return false; //TODO Redundant???
        if(String.copyValueOf(oldCode).equals(String.copyValueOf(newCode))) return false; //TODO Redundant??? Place elsewhere???
        String command = "<PASSCHANGE:" + String.copyValueOf(oldCode) + ":" + String.copyValueOf(newCode) + ">";
        sendData(command);
        return waitForResponse();
    }

    public static boolean sendUnarm(char[] code){
        if(String.copyValueOf(code).equals("")) return false; //TODO Redundant???
        String command = "<UNARM:" + String.copyValueOf(code) + ">";
        sendData(command);
        return waitForResponse();
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
