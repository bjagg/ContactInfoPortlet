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

    private int daysBetweenUpdate;
    private String directoryUrl;
    private boolean overrideWindow;
    private String username;
    private String userIdParam;
    private String userId;

    public EmployeeRequestContext(final PortletRequest request) {
        // Capture portlet preferences
        final PortletPreferences prefs = request.getPreferences();
        this.daysBetweenUpdate =
                Integer.parseUnsignedInt(prefs.getValue(DAYS_BETWEEN_UPDATE, "90"));
        this.directoryUrl = prefs.getValue(DIR_INFO_URL, null);
        this.overrideWindow = Boolean.parseBoolean(prefs.getValue(OVERRIDE_WINDOW, "false"));
        this.userIdParam = prefs.getValue(USER_ID_FOR_URL, "username");

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
