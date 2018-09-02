package com.lskycity.support.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

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

        return brand.toLowerCase(Locale.US).contains(HUAWEI) || brand.toLowerCase(Locale.US).contains(HONOR) || model.toLowerCase(Locale.US).contains(HUAWEI);
    }

    public static boolean isSamsungDevice() {
        String brand = Build.BRAND;
        return BRAND.SAMSUNG.equalsIgnoreCase(brand);
    }

    /**
     * @return Serial Number, you may cannot get the right value if you are P, request read phone states permission first
     */
    @SuppressLint("HardwareIds")
    public static String getSerialNumber(){
        return Build.SERIAL;
    }

    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
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


    public static String getDeviceId(Context context) {
        String deviceId = getAndroidId(context);

        if(TextUtils.isEmpty(deviceId)) {
            deviceId = Build.SERIAL;
        }

        if(TextUtils.isEmpty(deviceId)) {
            deviceId = "";
        }
        return deviceId;
    }

    public static boolean isInArc() {
        // https://github.com/google/talkback/blob/master/src/main/java/com/google/android/marvin/talkback/TalkBackService.java#L1779-L1781
        // this is detect arc from google's app
        return Build.DEVICE != null && Build.DEVICE.matches(".+_cheets");
    }


}
