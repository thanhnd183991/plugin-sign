package vt.thanhnd58.cmd;

import java.io.IOException;

public class AppCmd {
    public static void runCmd(String cmd) {
        System.out.println(cmd);
        Runtime rt = Runtime.getRuntime();
        try {
            Process pr = rt.exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void exit() {
        System.exit(0);
    }
}
