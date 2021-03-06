/**
 * Licensed to Apereo under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright ownership. Apereo
 * licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the License at the
 * following location:
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apereo.portlet.contact.employee.service;

import java.util.Map;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

/** Pertinent data for the specific employee user request. Includes preferences and user details. */
@Slf4j
@Value
public class EmployeeRequestContext {
    /* following must match portlet preference names */
    private static final String OVERRIDE_WINDOW = "override.check.window";
    private static final String DAYS_BETWEEN_UPDATE = "cycle.days";
    private static final String DIR_INFO_URL = "directoryInfo.url";
    private static final String USER_ID_FOR_URL = "directoryInfo.userId";
    private static final String DEPARTMENTS_URL = "departments.url";
    private static final String LOCATIONS_URL = "locations.url";
    private static final String SUPERVISORS_URL = "supervisors.url";

    private int daysBetweenUpdate;
    private String directoryUrl;
    private boolean overrideWindow;
    private String username;
    private String userIdParam;
    private String userId;
    private String deptUrl;
    private String locUrl;
    private String supervisorUrl;

    public EmployeeRequestContext(final PortletRequest request) {
        // Capture portlet preferences
        final PortletPreferences prefs = request.getPreferences();
        this.daysBetweenUpdate =
                Integer.parseUnsignedInt(prefs.getValue(DAYS_BETWEEN_UPDATE, "90"));
        this.directoryUrl = prefs.getValue(DIR_INFO_URL, null);
        this.overrideWindow = Boolean.parseBoolean(prefs.getValue(OVERRIDE_WINDOW, "false"));
        this.userIdParam = prefs.getValue(USER_ID_FOR_URL, "username");
        this.deptUrl = prefs.getValue(DEPARTMENTS_URL, null);
        this.locUrl = prefs.getValue(LOCATIONS_URL, null);
        this.supervisorUrl = prefs.getValue(SUPERVISORS_URL, null);

        // Get the USER_INFO from portlet.xml,
        // which gets it from personDirectoryContext.xml
        @SuppressWarnings("unchecked")
        final Map<String, String> userInfo =
                (Map<String, String>) request.getAttribute(PortletRequest.USER_INFO);
        this.username = request.getRemoteUser();
        this.userId = userInfo.get(this.userIdParam);

        log.debug(this.toString());
    }
}
