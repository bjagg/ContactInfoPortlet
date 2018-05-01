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

import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.apereo.portlet.contact.common.entity.UserLastUpdate;
import org.apereo.portlet.contact.common.util.CodeDesc;
import org.apereo.portlet.contact.student.entity.CommunicationPreferences;
import org.apereo.portlet.contact.student.entity.ContactInfo;
import org.apereo.portlet.contact.student.entity.Ethnicity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/** Main service class for student contact info. */
@Service
@Slf4j
public class StudentServiceImpl implements StudentService {

    @PersistenceContext private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public boolean infoRequiresUpdate(StudentRequestContext context) {
        log.debug("Checking if user should be prompted for contact info");
        if (context.isOverrideWindow()) {
            // should we override the check against term dates? (usually used for testing)
            return true;
        }

        final UserLastUpdate userLastUpdate = em.find(UserLastUpdate.class, context.getUsername());
        final Date lastUpdate = (userLastUpdate == null) ? null : userLastUpdate.getLastUpdate();
        if (lastUpdate == null) {
            log.debug("user has never saved contact info");
            return true;
        }

        final String url = context.getTermsUrl();
        if (url == null) {
            log.error("Terms URL is null!!");
            return true;
        }
        final RestTemplate restTemplate = new RestTemplate();
        final TermFeed feed = restTemplate.getForObject(url, TermFeed.class);
        if (feed == null) {
            log.error("Parsing JSON from {} failed", url);
            return true;
        }

        log.debug(feed.toString());

        final Term currentTerm = calcCurrentTerm(feed);
        if (currentTerm == null) {
            log.debug("Between terms");
            return false;
        }
        return currentTerm.getStartDate().after(lastUpdate);
    }

    @Transactional
    @Override
    public void refreshLastUpdate(String username) {
        em.merge(new UserLastUpdate(username));
    }

    private Term calcCurrentTerm(TermFeed feed) {
        final Date today = new Date();
        for (Term term : feed.getRows()) {
            if (term.getStartDate().before(today) && term.getEndDate().after(today)) {
                log.debug("Current term: {}", term.toString());
                return term;
            }
        }
        return null;
    }

    @Override
    public ContactInfo getContactInfo(StudentRequestContext context) {
        final String url = calcContactInfoUrl(context);
        final RestTemplate restTemplate = new RestTemplate();
        final ContactInfo info = restTemplate.getForObject(url, ContactInfo.class);
        info.setUsername(context.getUsername());
        log.debug(info.toString());
        mergeUnfetchedValues(info);
        return info;
    }

    private String calcContactInfoUrl(StudentRequestContext context) {
        String url = context.getContactInfoUrl();
        if (context.getUserId() != null) {
            final String userIdToken = "{" + context.getUserIdParam() + "}";
            url = url.replace(userIdToken, context.getUserId());
        }
        log.debug(url);
        return url;
    }

    @Transactional(readOnly = true)
    private void mergeUnfetchedValues(ContactInfo info) {
        assert info != null;
        assert info.getUsername() != null;
        ContactInfo savedInfo = em.find(ContactInfo.class, info.getUsername());
        if (savedInfo != null) {
            info.setMobile(savedInfo.getMobile());
            info.setAltPhone(savedInfo.getAltPhone());
        }
    }

    @Transactional
    @Override
    public void saveContactInfo(ContactInfo info) {
        em.merge(info);
    }

    @Transactional(readOnly = true)
    @Override
    public CommunicationPreferences getCommunicationPreferences(StudentRequestContext context) {
        // no external system of record
        final CommunicationPreferences comPref =
                em.find(CommunicationPreferences.class, context.getUsername());
        if (comPref != null) {
            em.detach(comPref);
        }
        return comPref;
    }

    @Transactional
    @Override
    public void saveCommunicationPreferences(CommunicationPreferences comPref) {
        em.merge(comPref);
    }

    @Transactional(readOnly = true)
    @Override
    public Ethnicity getEthnicity(StudentRequestContext context) {
        // will be included in contact info feed
        final Ethnicity ethnicity = em.find(Ethnicity.class, context.getUsername());
        if (ethnicity != null) {
            em.detach(ethnicity);
        }
        return ethnicity;
    }

    @Transactional
    @Override
    public void saveEthnicity(Ethnicity ethnicity) {
        em.merge(ethnicity);
    }

    @Override
    public CodeDesc[] getRaceList(StudentRequestContext context) {
        final String url = context.getRaceListUrl();
        final RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CodeDesc[]> response = restTemplate.getForEntity(url, CodeDesc[].class);
        return response.getBody();
    }

    @Override
    public CodeDesc[] getEthnicityList(StudentRequestContext context) {
        final String url = context.getEthnicityListUrl();
        final RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CodeDesc[]> response = restTemplate.getForEntity(url, CodeDesc[].class);
        return response.getBody();
    }
}
