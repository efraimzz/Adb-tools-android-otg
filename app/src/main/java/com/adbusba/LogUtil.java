package com.adbusba;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.preference.PreferenceManager;

public class LogUtil {
    private static final String LOG_PATH = "/storage/emulated/0/log.txt";
    public static void logToFile(Throwable msg) {logToFile(msg.toString()
                                                           +msg.getStackTrace()[0].getFileName()
                                                           +msg.getStackTrace()[0].getClassName()
                                                           +msg.getStackTrace()[0].getMethodName()
                                                           +msg.getStackTrace()[0].getLineNumber()
                                                           );
    }
    public static void logToFile(Throwable msg,boolean stackTrack) {
        String st="";
        if(stackTrack){
            if(msg.getStackTrace().length>0)
                for(StackTraceElement ste: msg.getStackTrace()){
                    st+="\n"+ste.toString();
                }
        }
        logToFile(msg.toString()
                  +msg.getStackTrace()[0].getFileName()
                  +msg.getStackTrace()[0].getClassName()
                  +msg.getStackTrace()[0].getMethodName()
                  +msg.getStackTrace()[0].getLineNumber()
                  +st);
    }
    public static void logToFile(String msg) {
            try {
                FileWriter writer = new FileWriter(LOG_PATH, true);
                String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
                writer.write("[" + time + "] " + msg + "\n");
                writer.close();
            } catch (IOException e) {
                // silent
            }
    }
    public static void i(String a, String b){logToFile(a+" "+b);}
    public static void e(String a, String b){logToFile(a+" "+b);}
    public static void w(String a, String b){logToFile(a+" "+b);}
}



