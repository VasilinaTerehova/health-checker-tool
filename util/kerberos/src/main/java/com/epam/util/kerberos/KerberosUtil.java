/*
 * ******************************************************************************
 *  *
 *  * Pentaho Big Data
 *  *
 *  * Copyright (C) 2002-2018 by Hitachi Vantara : http://www.pentaho.com
 *  *
 *  *******************************************************************************
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with
 *  * the License. You may obtain a copy of the License at
 *  *
 *  *    http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *  *****************************************************************************
 */

package com.epam.util.kerberos;

import com.sun.security.auth.module.Krb5LoginModule;
import org.apache.hadoop.security.UserGroupInformation;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.Map;

public class KerberosUtil {
    public static LoginContext getLoginContextFromUsernamePassword(String principal, String password)
            throws LoginException {
        return new LoginContext("app_name", createSubject(), callbacks -> {
            for (Callback callback : callbacks) {
                if (callback instanceof NameCallback) {
                    ((NameCallback) callback).setName(principal);
                } else if (callback instanceof PasswordCallback) {
                    ((PasswordCallback) callback).setPassword(password.toCharArray());
                } else {
                    throw new UnsupportedCallbackException(callback);
                }
            }
        }, new KerberosConfiguration(createAppConfigurationEntry(
                createBaseKerberosUserLoginOptions("principal", principal))));
    }

    private static Subject createSubject() {
        return new Subject();
    }

    private static AppConfigurationEntry[] createAppConfigurationEntry(Map<String, String> options) {
        return new AppConfigurationEntry[]{
                new AppConfigurationEntry(Krb5LoginModule.class.getName(),
                        AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options),
                new AppConfigurationEntry(UserGroupInformation.HadoopLoginModule.class.getName(),
                        AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options)
        };
    }

    private static Map<String, String> createBaseKerberosUserLoginOptions(String optKey, String optValue) {
        Map<String, String> options = new HashMap<>(createBaseKerberosUserLoginOptions());
        options.put(optKey, optValue);

        return options;
    }

    private static Map<String, String> createBaseKerberosUserLoginOptions() {
        Map<String, String> options = new HashMap<>(createBaseLoginConfigMap());

        options.put("useTicketCache", Boolean.TRUE.toString());
        options.put("renewTGT", Boolean.TRUE.toString());

        return options;
    }


    private static Map<String, String> createBaseLoginConfigMap() {
        Map<String, String> configBaseMap = new HashMap<>();

        configBaseMap.put("debug", Boolean.TRUE.toString());
        configBaseMap.put("refreshKrb5Config", Boolean.TRUE.toString());

        return configBaseMap;
    }
}
