/*
 *  Copyright (c) 2020 Private Internet Access, Inc.
 *
 *  This file is part of the Private Internet Access Android Client.
 *
 *  The Private Internet Access Android Client is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  The Private Internet Access Android Client is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License along with the Private
 *  Internet Access Android Client.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.privateinternetaccess.android.pia;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.privateinternetaccess.android.PIACallbacks;
import com.privateinternetaccess.android.PIAOpenVPNTunnelLibrary;
import com.privateinternetaccess.android.pia.handlers.PiaPrefHandler;
import com.privateinternetaccess.android.pia.interfaces.IBuilder;
import com.privateinternetaccess.android.pia.model.PurchasingTestingData;
import com.privateinternetaccess.android.pia.services.PIATileService;
import com.privateinternetaccess.android.pia.utils.DLog;
import com.privateinternetaccess.android.pia.utils.NotificationHelper;
import com.privateinternetaccess.android.pia.utils.Prefs;
import com.privateinternetaccess.android.pia.vpn.PiaLibVpnLibrary;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.blinkt.openvpn.core.VPNNotifications;
import de.blinkt.openvpn.core.VpnStatus;

/**
 * Builds and initializes the VPN, our app and notifications all in one class.
 *
 * Has convenience methods to help setup the app in whatever way you need and first launch methods to help set the user up right.
 *
 * Created by hfrede on 11/28/17.
 */

public class PIABuilder implements IBuilder {

    public static final String PIA_FIRST_BUILT = "pia_first_built";
    public Context context;
    private boolean firstLaunch;

    private PIABuilder(Context context) {
        this.context = context;
        firstLaunch = Prefs.with(context).get(PIA_FIRST_BUILT, true);
        if(firstLaunch){
            Prefs.with(context).set(PIA_FIRST_BUILT, false);
        }
    }

    public static IBuilder init(Context context){
        return new PIABuilder(context);
    }

    @Override
    public IBuilder createNotificationChannel(String channelName, String channelDescription) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationHelper.createPiaNotificationChannel(context, NotificationHelper.NOTIFICATION_CHANNEL_ID, channelName, channelDescription);
        }
        return this;
    }

    @Override
    public IBuilder enabledTileService() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            context.getPackageManager().setComponentEnabledSetting(new ComponentName(context, PIATileService.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0);
        return this;
    }

    @Override
    public IBuilder initVPNLibrary(VPNNotifications notifications, PIACallbacks callbacks, VpnStatus.StateListener listener){
        // add this listener first to handle the first response from the library.
        VpnStatus.initLogCache(context.getCacheDir());
        VpnStatus.addStateListener(listener);
        PIAOpenVPNTunnelLibrary.init(context, notifications, callbacks);
        PiaLibVpnLibrary.init();
        return this;
    }

    @Override
    public IBuilder setPortForwardingEnabled(boolean portForwarding) {
        if(firstLaunch)
            PiaPrefHandler.setPortForwardingEnabled(context, portForwarding);
        return this;
    }

    @Override
    public IBuilder setIPTrackingEnabled(boolean ipTracking) {
        if(firstLaunch)
            PiaPrefHandler.setIPTracking(context, ipTracking);
        return this;
    }

    @Override
    public IBuilder setMACEEnabled(boolean maceEnabled) {
        if(firstLaunch)
            PiaPrefHandler.setMaceActive(context, maceEnabled);
        return this;
    }

    @Override
    public IBuilder setOnlyAllowedApp(String... packageNames) {
        PiaPrefHandler.setVPNPerAppAllowed(context, true);
        Set<String> apps = new HashSet<>();
        Collections.addAll(apps, packageNames);
        PiaPrefHandler.setVPNPerAppPackages(context, apps);
        return this;
    }

    @Override
    public IBuilder setPurchaseTest(PurchasingTestingData data) {
        PiaPrefHandler.setPurchaseTesting(context, data.isTesting());
        PiaPrefHandler.setPurchaseTestingUsername(context, data.getUsername());
        PiaPrefHandler.setPurchaseTestingPassword(context, data.getPassword());
        PiaPrefHandler.setPurchaseTestingException(context, data.getException());
        PiaPrefHandler.setPurchaseTestingStatus(context, data.getResponseCode());
        return this;
    }

    @Override
    public IBuilder addVPNListener(VpnStatus.StateListener listener) {
        VpnStatus.addStateListener(listener);
        return this;
    }

    @Override
    public IBuilder removeVPNListener(VpnStatus.StateListener listener) {
        VpnStatus.removeStateListener(listener);
        return this;
    }

    @Override
    public IBuilder setDebugParameters(boolean debugMode, int debugLevel, File filesDir) {
        DLog.DEBUG_MODE = debugMode;
        DLog.DEBUG_LEVEL = debugLevel;
        DLog.base = filesDir;
        DLog.d("PIAApplication", "base = " + DLog.base + " debug mode = " + DLog.DEBUG_MODE + " debug level = " + DLog.DEBUG_LEVEL);
        return this;
    }

    @Override
    public IBuilder enableLogging(boolean logging) {
        DLog.DEV_MODE = logging;
        return this;
    }
}