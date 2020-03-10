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

import android.content.Context;

import com.privateinternetaccess.android.pia.impl.AccountImpl;
import com.privateinternetaccess.android.pia.impl.ConnectionImpl;
import com.privateinternetaccess.android.pia.impl.VPNImpl;
import com.privateinternetaccess.android.pia.interfaces.IAccount;
import com.privateinternetaccess.android.pia.interfaces.IConnection;
import com.privateinternetaccess.android.pia.interfaces.IFactory;
import com.privateinternetaccess.android.pia.interfaces.IVPN;

/**
 *
 * Factory and constructor handling of everything PIA vpn and account related.
 *
 * Created by arne on 07.10.17.
 */

public class PIAFactory implements IFactory {

    public static IFactory getInstance(){
        return new PIAFactory();
    }

    @Override
    public IConnection getConnection(Context context){
        return new ConnectionImpl(context);
    }

    @Override
    public IVPN getVPN(Context context){
        return new VPNImpl(context);
    }

    public IAccount getAccount(Context context) {
        return new AccountImpl(context);
    }

}
