package com.kanzankazu.itungitungan.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkUtil {

    /*
     * These constants aren't yet available in my API level (7), but I need to
     * handle these cases if they come up, on newer versions
     */
    private static final int NETWORK_TYPE_EHRPD = 14; // Level 11
    private static final int NETWORK_TYPE_EVDO_B = 12; // Level 9
    private static final int NETWORK_TYPE_HSPAP = 15; // Level 13
    private static final int NETWORK_TYPE_IDEN = 11; // Level 8
    private static final int NETWORK_TYPE_LTE = 13; // Level 11
    private static final String IPV4_BASIC_PATTERN_STRING =
            "(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" + // initial 3 fields, 0-255 followed by .
                    "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])"; // final field, 0-255
    private static final Pattern IPV4_PATTERN = Pattern.compile("^" + IPV4_BASIC_PATTERN_STRING + "$");
    private static int LoopCurrentIP;

    /**
     * CHECK CONNECTION
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isInternetConnect(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isInternetWorking() {
        boolean success = false;
        try {
            URL url = new URL("https://google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.connect();
            success = connection.getResponseCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    public static boolean isOnline1() {
        try {
            Process p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            return (returnVal == 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * CHECK CONNECTION type
     */
    public static boolean isOnline2() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isOnlineByPort() {
        try {
            int timeoutMs = 2000;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);
            sock.connect(sockaddr, timeoutMs);
            sock.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static int getConnectionMainType(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if ((info != null && info.isConnected())) {
            return info.getType();
        }
        return -1;
    }

    public static String getSubTypeConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();

        if ((info != null && info.isConnected())) {
            return NetworkUtil.getSubType(info.getType(), info.getSubtype());
        } else {
            return "No NetWork Access";
        }

    }

    private static String getSubType(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            //System.out.println("CONNECTED VIA WIFI");
            return "CONNECTED VIA WIFI";
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return "NETWORK type 1xRTT, Speed: 50-100 kbps"; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return "NETWORK type CDMA (3G), Speed: 2 Mbps"; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return "NETWORK type EDGE (2.75G), Speed: 100-120 Kbps"; // ~// 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return "NETWORK type EVDO_0, Speed: 400-1000 kbps"; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return "NETWORK type EVDO_A, Speed: 600-1400 kbps"; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return "NETWORK type GPRS (2.5G), Speed: 40-50 Kbps"; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return "NETWORK type HSDPA (4G), Speed: 2-14 Mbps"; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return "NETWORK type HSPA (4G), Speed: 0.7-1.7 Mbps"; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return "NETWORK type HSUPA (3G), Speed: 1-23 Mbps"; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return "NETWORK type UMTS (3G), Speed: 0.4-7 Mbps"; // ~ 400-7000 kbps
                // NOT AVAILABLE YET IN API LEVEL 7
                case NetworkUtil.NETWORK_TYPE_EHRPD:
                    return "NETWORK type EHRPD, Speed: 1-2 Mbps"; // ~ 1-2 Mbps
                case NetworkUtil.NETWORK_TYPE_EVDO_B:
                    return "NETWORK_TYPE_EVDO_B, Speed: 5 Mbps"; // ~ 5 Mbps
                case NetworkUtil.NETWORK_TYPE_HSPAP:
                    return "NETWORK type HSPA+ (4G), Speed: 10-20 Mbps"; // ~ 10-20 Mbps
                case NetworkUtil.NETWORK_TYPE_IDEN:
                    return "NETWORK type IDEN, Speed: 25 kbps"; // ~25 kbps
                case NetworkUtil.NETWORK_TYPE_LTE:
                    return "NETWORK type LTE (4G), Speed: 10+ Mbps"; // ~ 10+ Mbps Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    return "NETWORK type UNKNOWN";
                default:
                    return "";
            }
        } else {
            return "";
        }
    }

    /**
     * GET SIGNAL STRENGHT
     */
    public static String getNetworkType(final Context activity) {
        String networkStatus = "";
        final ConnectivityManager connMgr = (ConnectivityManager)
                activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        // check for wifi
        final NetworkInfo wifi =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        // check for mobile data
        final NetworkInfo mobile =
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isAvailable()) {
            networkStatus = "Wifi";
        } else if (mobile.isAvailable()) {
            networkStatus = getDataType(activity);
        } else {
            networkStatus = "noNetwork";
        }
        return networkStatus;
    }

    public static String getDataType(Context activity) {
        String type = "Mobile Data";
        TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
        switch (tm.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                type = "Mobile Data 3G";
                Log.i("Type", "3g");
                // for 3g HSDPA networktype will be return as
                // per testing(real) in device with 3g enable
                // data
                // and speed will also matters to decide 3g network type
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                type = "Mobile Data 4G";
                Log.i("Type", "4g");
                // No specification for the 4g but from wiki
                // i found(HSPAP used in 4g)
                break;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                type = "Mobile Data GPRS";
                Log.i("Type", "GPRS");
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                type = "Mobile Data EDGE 2G";
                Log.i("Type", "EDGE 2g");
                break;
        }
        return type;
    }

    public static int checkNetworkStrengh(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo Info = cm.getActiveNetworkInfo();
        if (Info == null || !Info.isConnectedOrConnecting()) {
            //Log.i(TAG, "No connection");
        } else {
            int netType = Info.getType();
            int netSubtype = Info.getSubtype();

            if (netType == ConnectivityManager.TYPE_WIFI) {

                WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                return wifiManager.getConnectionInfo().getRssi();

            } /*else if (netType == ConnectivityManager.TYPE_MOBILE) {
                SubscriptionManager subManager = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                    subManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);

                    List<SubscriptionInfo> subInfoList = subManager.getActiveSubscriptionInfoList();

                    for (int i = 0; i < subInfoList.size(); i++) {
                        int subID = subInfoList.get(i).getSubscriptionId();
                        int simPosition = subInfoList.get(i).getSimSlotIndex();
                        String number = String.valueOf(subInfoList.get(i).getCarrierName());

                        if (subManager.isNetworkRoaming(subID))
                        else
                    }
                }

                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                int type = telephonyManager.getNetworkType();
                List<CellInfo> all = telephonyManager.getAllCellInfo();
                int strenght = 0;
                if (type == 1 || type == 4 || type == 7 || type == 11 || type == 2) {
                    CellInfoGsm cellinfogsm = (CellInfoGsm) all.get(0);
                    CellSignalStrengthGsm cellSignalStrengthGsm = cellinfogsm.getCellSignalStrength();
                    strenght = cellSignalStrengthGsm.getDbm();
                } else if (type == 3 || type == 5 || type == 6 || type == 8 || type == 9 || type == 10 || type == 14 || type == 15) {
                    CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) all.get(0);
                    CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
                    strenght = cellSignalStrengthWcdma.getDbm();
                } else if (type == 13) {
                    CellInfoLte cellInfoLte = (CellInfoLte) all.get(0);
                    CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                    strenght = cellSignalStrengthLte.getDbm();
                }

                return strenght;
            }*/ else {
                return -1;
            }
        }
        return 0;
    }

    public static int getWifiSignalStrengthIndBm(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return (2 * wifiInfo.getRssi()) - 113;
    }

    public static int checkNetworkStatus(final Context context) {
        int networkStatus = 3;
        // Get connect mangaer
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        // check for wifi
        final NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        // check for mobile data
        final NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable()) {
            networkStatus = 1;//wifi
        } else if (mobile.isAvailable()) {
            networkStatus = 2;//mobile network
        } else {
            networkStatus = 3;//no network
        }

        return networkStatus;

    }

    /**
     * ON OFF MOBILE DATA & WIFI
     */
    public static int checkNetworkStatusInt(final Context context) {
        int networkStatus = 3;
        // Get connect mangaer
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        // check for wifi
        final NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        // check for mobile data
        final NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable()) {
            networkStatus = 1;//wifi
        } else if (mobile.isAvailable()) {
            networkStatus = 2;//mobile network
        } else {
            networkStatus = 3;//no network
        }

        return networkStatus;

    }

    public static String checkNetworkStatusString(final Context activity) {
        String networkStatus = "";
        try {
            // Get connect mangaer
            final ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            // // check for wifi
            final NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            // // check for mobile data
            final NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifi.isAvailable()) {
                networkStatus = "Wifi";
            } else if (mobile.isAvailable()) {
                networkStatus = getDataType(activity);
            } else {
                networkStatus = "noNetwork";
                //networkStatus = "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
            networkStatus = "0";
        }
        return networkStatus;
    }

    public static boolean isWifiEnabled(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiInfo.getState() == NetworkInfo.State.CONNECTED;
    }

    public static void setWifiEnabled(Context context) {
        try {
            WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            WifiConfiguration wc = new WifiConfiguration();
            wc.SSID = "\"SSIDName\"";
            wc.preSharedKey = "\"password\"";
            wc.hiddenSSID = true;
            wc.status = WifiConfiguration.Status.ENABLED;

            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            wc.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);

            boolean b = wifi.isWifiEnabled();
            if (b) {
                wifi.setWifiEnabled(false);
                Toast.makeText(context, "Wifi Off", Toast.LENGTH_SHORT).show();
            } else {
                wifi.setWifiEnabled(true);
                Toast.makeText(context, "Wifi On", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static boolean isMobileDataEnabled(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return mobileInfo.getState() == NetworkInfo.State.CONNECTED;
    }

    public static void setMobileDataEnabled(Context context, boolean enabled) {
        final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        try {
            final Class conmanClass = Class.forName(conman.getClass().getName());
            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
            final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static boolean isToggleMobileDataConnection(Context context) {

        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // method is callable
            mobileDataEnabled = (Boolean) method.invoke(cm);
        } catch (Exception e) {
        }
        return mobileDataEnabled;
    }

    public static void setToggleMobileDataConnection(boolean b, Context context) {
        try {
            final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final Class conmanClass = Class.forName(conman.getClass().getName());
            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(b);
            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
            final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(b);
            setMobileDataEnabledMethod.invoke(iConnectivityManager, b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            boolean isEnabled;
            if (telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED) {
                isEnabled = true;
            } else {
                isEnabled = false;
            }

            Class<?> telephonyManagerClass = Class.forName(telephonyManager.getClass().getName());
            Method getITelephonyMethod = telephonyManagerClass.getDeclaredMethod("getITelephony");
            getITelephonyMethod.setAccessible(true);
            Object ITelephonyStub = getITelephonyMethod.invoke(telephonyManager);
            Class<?> ITelephonyClass = Class.forName(ITelephonyStub.getClass().getName());

            Method dataConnSwitchmethod;
            if (isEnabled) {
                dataConnSwitchmethod = ITelephonyClass.getDeclaredMethod("disableDataConnectivity");
            } else {
                dataConnSwitchmethod = ITelephonyClass.getDeclaredMethod("enableDataConnectivity");
            }
            dataConnSwitchmethod.setAccessible(true);
            dataConnSwitchmethod.invoke(ITelephonyStub,true);
        } catch (Exception e) {
            //
        }*/

    }

    private static boolean isMobileDataEnabledFromLollipop(Context context) {
        boolean state = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            state = Settings.Global.getInt(context.getContentResolver(), "mobile_data", 0) == 1;
        }
        return state;
    }

    /*public static void setMobileNetworkfromLollipop(Context context) throws Exception {
        String command = null;
        int state = 0;
        try {
            // Get the current state of the mobile network.
            state = isMobileDataEnabledFromLollipop(context) ? 0 : 1;
            // Get the value of the "TRANSACTION_setDataEnabled" field.
            String transactionCode = getTransactionCode(context);
            // Android 5.1+ (API 22) and later.
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                SubscriptionManager mSubscriptionManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
                // Loop through the subscription list i.e. SIM list.
                for (int i = 0; i < mSubscriptionManager.getActiveSubscriptionInfoCountMax(); i++) {
                    if (transactionCode != null && transactionCode.length() > 0) {
                        // Get the active subscription ID for a given SIM card.
                        int subscriptionId = mSubscriptionManager.getActiveSubscriptionInfoList().get(i).getSubscriptionId();
                        // Execute the command via `su` to turn off
                        // mobile network for a subscription service.
                        command = "service call phone " + transactionCode + " i32 " + subscriptionId + " i32 " + state;
                        executeCommandViaSu(context, "-c", command);
                    }
                }
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                // Android 5.0 (API 21) only.
                if (transactionCode != null && transactionCode.length() > 0) {
                    // Execute the command via `su` to turn off mobile network.
                    command = "service call phone " + transactionCode + " i32 " + state;
                    executeCommandViaSu(context, "-c", command);
                }
            }
        } catch (Exception e) {
            // Oops! Something went wrong, so we throw the exception here.
            throw e;
        }
    }*/

    private static String getTransactionCode(Context context) throws Exception {
        try {
            final TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final Class<?> mTelephonyClass = Class.forName(mTelephonyManager.getClass().getName());
            final Method mTelephonyMethod = mTelephonyClass.getDeclaredMethod("getITelephony");
            mTelephonyMethod.setAccessible(true);
            final Object mTelephonyStub = mTelephonyMethod.invoke(mTelephonyManager);
            final Class<?> mTelephonyStubClass = Class.forName(mTelephonyStub.getClass().getName());
            final Class<?> mClass = mTelephonyStubClass.getDeclaringClass();
            final Field field = mClass.getDeclaredField("TRANSACTION_setDataEnabled");
            field.setAccessible(true);
            return String.valueOf(field.getInt(null));
        } catch (Exception e) {
            // The "TRANSACTION_setDataEnabled" field is not available,
            // or named differently in the current API level, so we throw
            // an exception and inform users that the method is not available.
            throw e;
        }
    }

    private static void executeCommandViaSu(Context context, String option, String command) {
        boolean success = false;
        String su = "su";
        for (int i = 0; i < 3; i++) {
            // Default "su" command executed successfully, then quit.
            if (success) {
                break;
            }
            // Else, execute other "su" commands.
            if (i == 1) {
                su = "/system/xbin/su";
            } else if (i == 2) {
                su = "/system/bin/su";
            }
            try {
                // Execute command as "su".
                Runtime.getRuntime().exec(new String[]{su, option, command});
            } catch (IOException e) {
                success = false;
                // Oops! Cannot execute `su` for some reason.
                // Log error here.
            } finally {
                success = true;
            }
        }
    }

    /**
     * GET IP MAC
     */
    public static boolean isAirplaneModeOn(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    public static boolean isIPv4Address(final String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    public static String getSSID(Context context) {
        if (isWifiConnected(context)) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifiManager.getConnectionInfo();
            String aremail = info.getSSID().split("\"")[1];
            return aremail;
        } else {
            return "mobile";
        }
    }

    public static String getMacId(Context context) {

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getBSSID();
    }

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public static String getMACAddress(String interfaceName) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName))
                        continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null)
                    return "";
                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0)
                    buf.deleteCharAt(buf.length() - 1);
                return buf.toString();
            }
        } catch (Exception ex) {
            return "";
        } // for now eat exceptions
        return "";
        /*
         * try { // this is so Linux hack return
         * loadFileAsString("/sys/class/net/" +interfaceName +
         * "/address").toUpperCase().trim(); } catch (IOException ex) { return
         * null; }
         */
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = NetworkUtil.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port
                                // suffix
                                return delim < 0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    //The return is defined in RILConstants.java, e.g. RILConstants.NETWORK_MODE_WCDMA_PREF
    public static int getPreferredNetwork(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Method method = getHiddenMethod("getPreferredNetworkType", TelephonyManager.class, null);
        int preferredNetwork = -1000;
        try {
            preferredNetwork = (int) method.invoke(mTelephonyManager);
            //Log.i(TAG, "Preferred Network is ::: " + preferredNetwork);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return preferredNetwork;
    }

    /**
     * CHANGE PREFERENCE NETWORK*/

    /**
     * // NETWORK_MODE_* See ril.h RIL_REQUEST_SET_PREFERRED_NETWORK_TYPE
     * int NETWORK_MODE_WCDMA_PREF              = 0;   // GSM/WCDMA (WCDMA preferred)
     * int NETWORK_MODE_GSM_ONLY                = 1;   // GSM only
     * int NETWORK_MODE_WCDMA_ONLY              = 2;   // WCDMA only
     * int NETWORK_MODE_GSM_UMTS                = 3;   // GSM/WCDMA (auto mode, according to PRL)**
     * int NETWORK_MODE_CDMA                    = 4;   // CDMA and EvDo (auto mode, according to PRL)**
     * int NETWORK_MODE_CDMA_NO_EVDO            = 5;   // CDMA only
     * int NETWORK_MODE_EVDO_NO_CDMA            = 6;   // EvDo only
     * int NETWORK_MODE_GLOBAL                  = 7;   // GSM/WCDMA, CDMA, and EvDo (auto mode, according to PRL)**
     * int NETWORK_MODE_LTE_CDMA_EVDO           = 8;   // LTE, CDMA and EvDo
     * int NETWORK_MODE_LTE_GSM_WCDMA           = 9;   // LTE, GSM/WCDMA
     * int NETWORK_MODE_LTE_CDMA_EVDO_GSM_WCDMA = 10;  // LTE, CDMA, EvDo, GSM/WCDMA
     * int NETWORK_MODE_LTE_ONLY                = 11;  // LTE Only mode.
     * int NETWORK_MODE_LTE_WCDMA               = 12;  // LTE/WCDMA
     * int NETWORK_MODE_TDSCDMA_ONLY            = 13;  // TD-SCDMA only
     * int NETWORK_MODE_TDSCDMA_WCDMA           = 14;  // TD-SCDMA and WCDMA
     * int NETWORK_MODE_LTE_TDSCDMA             = 15;  // TD-SCDMA and LTE
     * int NETWORK_MODE_TDSCDMA_GSM             = 16;  // TD-SCDMA and GSM
     * int NETWORK_MODE_LTE_TDSCDMA_GSM         = 17;  // TD-SCDMA,GSM and LTE
     * int NETWORK_MODE_TDSCDMA_GSM_WCDMA       = 18;  // TD-SCDMA, GSM/WCDMA
     * int NETWORK_MODE_LTE_TDSCDMA_WCDMA       = 19;  // TD-SCDMA, WCDMA and LTE
     * int NETWORK_MODE_LTE_TDSCDMA_GSM_WCDMA   = 20;  // TD-SCDMA, GSM/WCDMA and LTE
     * int NETWORK_MODE_TDSCDMA_CDMA_EVDO_GSM_WCDMA     = 21;  // TD-SCDMA,EvDo,CDMA,GSM/WCDMA
     * int NETWORK_MODE_LTE_TDSCDMA_CDMA_EVDO_GSM_WCDMA = 22;  // TD-SCDMA/LTE/GSM/WCDMA, CDMA, and EvDo
     */

    //The parameter must be based on RILConstants.java, e.g.: RILConstants.NETWORK_MODE_LTE_ONLY
    public static void setPreferredNetwork(Context context, int networkType) {
        try {
            Method setPreferredNetwork = getHiddenMethod("setPreferredNetworkType", TelephonyManager.class, new Class[]{int.class});
            TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Boolean success = (Boolean) setPreferredNetwork.invoke(mTelephonyManager, networkType);
            //Log.i(TAG, "Could set Network Type ::: " + (success.booleanValue() ? "YES" : "NO"));
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static Method getHiddenMethod(String methodName, Class fromClass, Class[] params) {
        Method method = null;
        try {
            Class clazz = Class.forName(fromClass.getName());
            method = clazz.getMethod(methodName, params);
            method.setAccessible(true);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return method;
    }

    public static ArrayList<InetAddress> getConnectedDevices(String YourPhoneIPAddress) {
        ArrayList<InetAddress> ret = new ArrayList<InetAddress>();

        LoopCurrentIP = 0;

        //        String IPAddress = "";
        String[] myIPArray = YourPhoneIPAddress.split("\\.");
        InetAddress currentPingAddr;

        for (int i = 0; i <= 255; i++) {
            try {

                // build the next IpModel address
                currentPingAddr = InetAddress.getByName(myIPArray[0] + "." + myIPArray[1] + "." + myIPArray[2] + "." + Integer.toString(LoopCurrentIP));

                // 50ms Timeout for the "ping"
                if (currentPingAddr.isReachable(50)) {
                    if (currentPingAddr.getHostAddress() != YourPhoneIPAddress) {
                        ret.add(currentPingAddr);
                    }
                }
            } catch (IOException ex) {
            }

            LoopCurrentIP++;
        }
        return ret;
    }

    /*get all ipString scan*/

    public static int getDefaultDataSubscriptionId(SubscriptionManager subscriptionManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            int nDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
            if (nDataSubscriptionId != SubscriptionManager.INVALID_SUBSCRIPTION_ID) {
                return (nDataSubscriptionId);
            }
        }
        try {
            Class<?> subscriptionClass = Class.forName(subscriptionManager.getClass().getName());

            try {
                Method getDefaultDataSubscriptionId = subscriptionClass.getMethod("getDefaultDataSubId");

                try {
                    return ((int) getDefaultDataSubscriptionId.invoke(subscriptionManager));
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                }
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            }
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        return (SubscriptionManager.INVALID_SUBSCRIPTION_ID);
    }

    /*public boolean isSimAvailable(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager sManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            SubscriptionInfo infoSim1 = sManager.getActiveSubscriptionInfoForSimSlotIndex(0);
            SubscriptionInfo infoSim2 = sManager.getActiveSubscriptionInfoForSimSlotIndex(1);
            if (infoSim1 != null || infoSim2 != null) {
                return true;
            }
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager.getSimSerialNumber() != null) {
                return true;
            }
        }
        return false;
    }

    public String getUIText22(Context context) {
        SubscriptionManager subscriptionManager = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            subscriptionManager = (SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            int nDataSubscriptionId = getDefaultDataSubscriptionId(subscriptionManager);
            if (nDataSubscriptionId != SubscriptionManager.INVALID_SUBSCRIPTION_ID) {
                SubscriptionInfo si = subscriptionManager.getActiveSubscriptionInfo(nDataSubscriptionId);
                if (si != null) {
                    return (si.getCarrierName().toString());
                }
            }
        }

        return "null";
    }*/










    /*CHECK CONNECTION type*/



    /*GET SIGNAL STRENGHT*/



    /*GET IP MAC*/


}
