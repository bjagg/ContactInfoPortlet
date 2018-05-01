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
package org.apereo.portlet.contact.student.service;

import org.apereo.portlet.contact.common.entity.UserLastUpdate;
import org.apereo.portlet.contact.common.util.CodeDesc;
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
     * Save {@link UserLastUpdate} with current time for this user.
     *
     * @param username identifier for user
     */
    void refreshLastUpdate(String username);

    /**
     * Based on {@link StudentRequestContext}, returns the current contact information for user.
     * This call merges the data from various sources, including local database.
     *
     * @param context collection of request values, such as username and contact info URL
     * @return {@link ContactInfo} populated with user information
     */
    ContactInfo getContactInfo(StudentRequestContext context);

    /**
     * Save {@link ContactInfo} to local database.
     *
     * @param info data to persist
     */
    void saveContactInfo(ContactInfo info);

    /**
     * Find communication preferences from various sources, including local database.
     *
     * @param context collection of request values, such as username and contact info URL
     * @return {@link CommunicationPreferences} or null
     */
    CommunicationPreferences getCommunicationPreferences(StudentRequestContext context);

    /**
     * Save {@link CommunicationPreferences} to local database.
     *
     * @param comPref data to persist
     */
    void saveCommunicationPreferences(CommunicationPreferences comPref);

    /**
     * Find ethnicity declaration from various sources, including local database.
     *
     * @param context collection of request values, such as username and contact info URL
     * @return {@link Ethnicity} or null
     */
    Ethnicity getEthnicity(StudentRequestContext context);

    /**
     * Save {@link Ethnicity} to local database.
     *
     * @param ethnicity data to persist
     */
    void saveEthnicity(Ethnicity ethnicity);

    /**
     * Get list of the race codes and descriptions.
     *
     * @param context collection of request values, such as username and contact info URL
     * @return {@link CodeDesc[]} of race
     */
    CodeDesc[] getRaceList(StudentRequestContext context);

    /**
     * Get list of ethnicity codes and descriptions.
     *
     * @param context collection of request values, such as username and contact info URL
     * @return {@link CodeDesc[]} of ethnicities
     */
    CodeDesc[] getEthnicityList(StudentRequestContext context);
}
