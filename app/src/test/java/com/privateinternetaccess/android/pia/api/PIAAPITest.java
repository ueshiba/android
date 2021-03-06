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

package com.privateinternetaccess.android.pia.api;

import android.test.mock.MockContext;

import com.privateinternetaccess.android.pia.api.AccountApi;
import com.privateinternetaccess.android.pia.api.IpApi;
import com.privateinternetaccess.android.pia.api.LocationApi;
import com.privateinternetaccess.android.pia.api.MaceApi;
import com.privateinternetaccess.android.pia.api.PiaApi;
import com.privateinternetaccess.android.pia.api.PortForwardApi;
import com.privateinternetaccess.android.pia.api.PurchasingApi;
import com.privateinternetaccess.android.pia.api.ReportingApi;
import com.privateinternetaccess.android.pia.api.ServerAPI;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import okhttp3.mock.MockInterceptor;
import okhttp3.mock.Rule;

import static org.hamcrest.CoreMatchers.instanceOf;

@RunWith(MockitoJUnitRunner.class)
public class PIAAPITest {

    @Mock
    MockContext context;

    MockInterceptor interceptor;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        interceptor = new MockInterceptor();
    }

    @Test
    public void creationTest_accountApi(){
        Assert.assertThat(new AccountApi(context), instanceOf(AccountApi.class));
    }

    @Test
    public void creationTest_ipApi(){
        Assert.assertThat(new IpApi(context), instanceOf(IpApi.class));
    }

    @Test
    public void creationTest_localtionApi(){
        Assert.assertThat(new LocationApi(), instanceOf(LocationApi.class));
    }

    @Test
    public void creationTest_maceApi(){
        Assert.assertThat(new MaceApi(), instanceOf(MaceApi.class));
    }

    @Test
    public void creationTest_piaApi(){
        Assert.assertThat(new PiaApi(), instanceOf(PiaApi.class));
    }

    @Test
    public void creationTest_portForwardApi(){
        Assert.assertThat(new PortForwardApi(), instanceOf(PortForwardApi.class));
    }

    @Test
    public void creationTest_purchasingApi(){
        Assert.assertThat(new PurchasingApi(context), instanceOf(PurchasingApi.class));
    }

    @Test
    public void creationTest_reportingApi(){
        Assert.assertThat(new ReportingApi(context), instanceOf(ReportingApi.class));
    }
    @Test
    public void creationTest_serverApi(){
        Assert.assertThat(new ServerAPI(context), instanceOf(ServerAPI.class));
    }

    @Test
    public void selectBaseURLTest_defaultAvailable() {
        interceptor.addRule(new Rule.Builder()
                .get()
                .url(PiaApi.PROXY_PATHS.get(0))
                .respond(200));
        Assert.assertEquals(PiaApi.getBaseURL(context), PiaApi.PROXY_PATHS.get(0));
    }

    @Test
    public void selectBaseURLTest_defaultUnavailable() {
        interceptor.reset();
        interceptor.addRule(new Rule.Builder()
                .get()
                .url(PiaApi.PROXY_PATHS.get(0))
                .respond(401));
        Assert.assertNotEquals(PiaApi.getBaseURL(context), PiaApi.PROXY_PATHS.get(1));
    }

    @Test
    public void selectBaseURLTest_allUnavailable() {
        interceptor.reset();

        for (String path : PiaApi.PROXY_PATHS) {
            interceptor.addRule(new Rule.Builder()
                    .get()
                    .url(path)
                    .respond(401));
        }

        Assert.assertEquals(PiaApi.getBaseURL(context), PiaApi.PROXY_PATHS.get(0));
    }

}
