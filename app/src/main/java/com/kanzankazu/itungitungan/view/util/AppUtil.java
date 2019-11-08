package com.kanzankazu.itungitungan.view.util;

public class AppUtil {
    /**
     * @param b
     * @return 1=true, 0=false
     */
    public static int saveModelBoolInt(boolean b) {
        if (!b) {
            return 0;
        } else {
            return 1;
        }
    }
}
