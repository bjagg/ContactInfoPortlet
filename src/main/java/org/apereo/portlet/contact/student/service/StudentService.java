package org.apereo.portlet.contact.student.service;

import org.apereo.portlet.contact.student.entity.CommunicationPreferences;
import org.apereo.portlet.contact.student.entity.ContactInfo;
import org.apereo.portlet.contact.student.entity.Ethnicity;

/** Main service class for student contact info. */
public interface StudentService {

    /**
     * Check if this is the scheduled calendar window that requires users to update their contact
     * info.
     *
     * <p>An override option is available in the context.
     *
     * @param context collection of request values, such as username and contact info URL
     * @return boolean if contact information should be gathered now
     */
    boolean infoRequiresUpdate(StudentRequestContext context);

    /**
     * Save {@UserLastUpdate} with current time for this user.
     *
     * @param username identifier for user
     */
    void refreshLastUpdate(String username);

    /**
     * Based on {@context}, returns the current contact information for user. This call merges the
     * data from various sources, including local database.
     *
     * @param context collection of request values, such as username and contact info URL
     * @return {@ContactInfo} populated with user information
     */
    ContactInfo getContactInfo(StudentRequestContext context);

    /**
     * Save {@ContactInfo} to local database.
     *
     * @param info data to persist
     */
    void saveContactInfo(ContactInfo info);

    /**
     * Find communication preferences from various sources, including local database.
     *
     * @param context collection of request values, such as username and contact info URL
     * @return {@CommunicationPreferences} or null
     */
    CommunicationPreferences getCommunicationPreferences(StudentRequestContext context);

    /**
     * Save {@CommunicationPreferences} to local database.
     *
     * @param comPref data to persist
     */
    void saveCommunicationPreferences(CommunicationPreferences comPref);

    /**
     * Find ethnicity declaration from various sources, including local database.
     *
     * @param context collection of request values, such as username and contact info URL
     * @return {@Ethnicity} or null
     */
    Ethnicity getEthnicity(StudentRequestContext context);

    /** Save {@Ethnicity} to local database. @Param ethnicity data to presist */
    void saveEthnicity(Ethnicity ethnicity);
}
