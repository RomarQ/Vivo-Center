package pt.ubi.pdm.vivo;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;

import pt.ubi.pdm.vivo.Users.User;
import pt.ubi.pdm.vivo.Visit.Visit;
import pt.ubi.pdm.vivo.profile.Profile;

import static android.content.ContentValues.TAG;

public class session {

    // API address
    public static String API_host = "https://vivomountaincenter.herokuapp.com";

    public static Profile profile = new Profile();
    public static ArrayList<Visit> visits = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<>();

    static WifiManager.WifiLock mWifiLock = null;

    /***
     * Calling this method will aquire the lock on wifi. This is avoid wifi
     * from going to sleep as long as <code>releaseWifiLock</code> method is called.
     **/
    public static void holdWifiLock(Context mContext) {
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);

        if( mWifiLock == null )
            mWifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, TAG);

        mWifiLock.setReferenceCounted(false);

        if( !mWifiLock.isHeld() )
            mWifiLock.acquire();
    }

    /***
     * Calling this method will release if the lock is already help. After this method is called,
     * the Wifi on the device can goto sleep.
     **/
    public static void releaseWifiLock() {

        if( mWifiLock == null )
            Log.w(TAG, "#releaseWifiLock mWifiLock was not created previously");

        if( mWifiLock != null && mWifiLock.isHeld() ){
            mWifiLock.release();
        }

    }

}
