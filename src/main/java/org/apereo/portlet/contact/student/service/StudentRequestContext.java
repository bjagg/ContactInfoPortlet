package org.apereo.portlet.contact.student.service;

import java.util.Map;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

/** Pertinent data for the specific user request. Includes current preferences and user details. */
@Slf4j
@Value
public class StudentRequestContext {
    /* following must match portlet preference names */
    private static final String OVERRIDE_WINDOW = "override.check.window";
    private static final String TERMS_URL = "terms.url";
    private static final String CONTACT_INFO_URL = "contactInfo.url";
    private static final String USER_ID_FOR_URL = "contactInfo.userId";

    private String termsUrl;
    private String contactInfoUrl;
    private boolean overrideWindow;
    private String username;
    private String userIdParam;
    private String userId;

    public StudentRequestContext(final PortletRequest request) {
        // Capture portlet preferences
        final PortletPreferences prefs = request.getPreferences();
        this.termsUrl = prefs.getValue(TERMS_URL, null);
        this.contactInfoUrl = prefs.getValue(CONTACT_INFO_URL, null);
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
