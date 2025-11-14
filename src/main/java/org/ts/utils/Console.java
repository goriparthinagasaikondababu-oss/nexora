package org.ts.utils;

import java.util.Scanner;

public class Console {
    public final static String divider = "--------------------------------------------------";
    private static Scanner scanner;
    static {
        scanner = new Scanner(System.in);
    }
    public static String readInput(String prompt){
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static String readInput(){
        return scanner.nextLine();
    }
    public static void close(){
        if(scanner!=null){
            scanner.close();
            scanner = null;
        }
    }
}
