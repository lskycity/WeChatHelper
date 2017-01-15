package com.zhaofliu.wechathelper.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class DeviceUtils {
    public static final String GALAXY_NOTE_II = "t03g";

    public static final String GALAXY_NOTE_II_VERIZON = "t0ltevzw"; // Galaxy Note II

    public static final String LG_OPTIMUS_G_PRO = "geefhd";

    public static final String LG_G2 = "g2"; // Galaxy Note II

    public static final String GOOGLE_NEXUS_4 = "mako";

    public static final String SONY_XPERIA_A = "C5502";

    public static final String SONY_XPERIA_Z = "C6602";

    public static final String SONY_XPERIA_Z_LTE = "C6603";

    public static final String[] BB_SUPPORT_DEVICE = {"Z30", "passport", "classic"};

    public static final String BLACKBERRY = "BlackBerry";

    private static final String MATE_8_MODEL = "HUAWEI NXT-L29";

    private static final String HUAWEI = "huawei";

    private static final String HONOR = "honor";

    private static final String MC_DEVICE_PREFIX = "MC";


    /**
     * Model	    FCC id	    Carriers/regions
     * C6902/L39h	PY7PM-0500	Worldwide
     * C6906	    PY7PM-0460	North America
     * C6916(Z1s)	PY7PM-0590	T-Mobile US
     * C6903	    PY7PM-0450	Worldwide
     * C6943	    PY7PM-0650	Brazil
     * SOL23	    PY7PM-0470	au by KDDI
     * SO-01F	    PY7PM-0440	NTT DoCoMo
     * L39t		                China(TD-SCDMA)
     */
    public static final String SONY_XPERIA_Z1 = "C6902";

    public static final String SONY_XPERIA_Z1_LTE = "C6903";

    public static final String SONY_XPERIA_Z1_LTE_NORTH_AMERICA = "C6906";

    public static final String SONY_XPERIA_Z1_LTE_BRAZIL = "C6943";

    public static final String SONY_XPERIA_Z1S = "C6916";

    public static final String SONY_XPERIA_Z1_LTE_KDDI = "SOL23";

    public static final String SONY_XPERIA_Z1_LTE_NTT_DOCOMO = "SO-01F";

    public static final String SONY_XPERIA_Z1_LTE_TD = "L39t";


    private DeviceUtils() {
    }

    public static final class BRAND {
        public static final String SONY = "SONY";
        public static final String LGE = "LGE";
        public static final String SAMSUNG = "SAMSUNG";
    }

    public static boolean isANRDevice() {
        final String[] NOTE4_MODELS = new String[]
                {"SM-N9100", "SM-N9106W", "SM-N9108V", "SM-N9108W", "SM-N9109W", "SM-N910A", "SM-N910C", "SCL24", "SC-01G", "SM-N910F", "SM-N910FD",
                        "SM-N910FQ", "SM-N910G", "SM-N910H", "SM-N910K", "SM-N910L", "SM-N910P", "SM-N910R4", "SM-N910S", "SM-N910T", "SM-N910T1",
                        "SM-N910U", "SM-N910V", "SM-N910W8", "SM-N910X"};

        final String[] MATE7_MODELS = new String[]
                {"MT7-UL00", "mt7-ul00"};

        final String[] HONOR6_MODELS = new String[]
                {"Mulan", "H60-L01", "H60-L02", "H60-L11", "H60-L12", "Glory 6"};

        String model = android.os.Build.MODEL;

        for (String note4 : NOTE4_MODELS) {
            if (model.contains(note4)) {
                return true;
            }
        }

        for (String mate7 : MATE7_MODELS) {
            if (model.contains(mate7)) {
                return true;
            }
        }

        for (String honor6 : HONOR6_MODELS) {
            if (model.contains(honor6)) {
                return true;
            }
        }
        return false;

    }

    public static boolean isBlackBerryDevice() {
        String model = Build.MODEL;
        String product = Build.PRODUCT;
        String manufacture = Build.MANUFACTURER;

        //first check the model
        for (String bb_model : BB_SUPPORT_DEVICE) {
            if (bb_model.equalsIgnoreCase(model)) {
                return true;
            }
        }

        //then check the product
        if (BLACKBERRY.equalsIgnoreCase(product)) {
            return true;
        }

        //then check the manufacture
        return BLACKBERRY.equalsIgnoreCase(manufacture);
    }

    public static boolean isHuaweiMate8Device() {
        return Build.MODEL.equalsIgnoreCase(MATE_8_MODEL);
    }

    public static boolean isHuaWeiDevice() {
        String model = Build.MODEL;
        String brand = Build.BRAND;

        if (brand.toLowerCase(Locale.US).contains(HUAWEI) || brand.toLowerCase(Locale.US).contains(HONOR)) {
            return true;
        }
        return model.toLowerCase(Locale.US).contains(HUAWEI);
    }

    public static boolean isNexus6P() {
        String model = Build.MODEL;
        return model.equalsIgnoreCase("Nexus 6P");
    }

    public static boolean isSamsungDevice() {
        String brand = Build.BRAND;
        return BRAND.SAMSUNG.equalsIgnoreCase(brand);
    }

    public static boolean isMCDevice()
    {
        String model = Build.MODEL;
        return model.startsWith(MC_DEVICE_PREFIX);
    }

    /**
     * getSerialNumber
     * @return result is same to getSerialNumber1()
     */
    public static String getSerialNumber(){
        String serial = null;
        try {
            Class<?> c =Class.forName("android.os.SystemProperties");
            Method get =c.getMethod("get", String.class);
            serial = (String)get.invoke(c, "ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }

    @SuppressLint("HardwareIds")
    public static String getIMEI(Context context) {
        return ((TelephonyManager)context.getSystemService(
                Context.TELEPHONY_SERVICE)).getDeviceId();
    }


    @SuppressLint("HardwareIds")
    public static String getAndroidId(Context context) {
        try{
            return android.provider.Settings.Secure.getString(
                    context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            return null;
        }

    }

    public static String getSerialNumber1() {
        return android.os.Build.SERIAL;
    }


    public static String getDeviceId(Context context) {
        String deviceId = getAndroidId(context);

        if(TextUtils.isEmpty(deviceId)) {
            deviceId = getSerialNumber1();
        }

        if(TextUtils.isEmpty(deviceId)) {
            deviceId = "";
        }
        return deviceId;
    }


}
