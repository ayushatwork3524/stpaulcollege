package com.main.stpaul.helper;

public class StudentHelper {
    
    public static String sessionIncrementer(String session) {
        String[] sessionArray = session.split("-");
        int sessionStart = Integer.parseInt(sessionArray[0]);
        int sessionEnd = Integer.parseInt(sessionArray[1]);
        sessionStart++;
        sessionEnd++;
        return sessionStart + "-" + sessionEnd;
    }
}
