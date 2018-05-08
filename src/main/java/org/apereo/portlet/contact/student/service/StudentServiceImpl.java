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
import org.springframework.cache.annotation.Cacheable;
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

        TermFeed feed = getTermFeed(context);
        if (feed == null) {
            log.error("Parsing JSON for termfeed failed");
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

    @Cacheable("terms")
    private TermFeed getTermFeed(StudentRequestContext context) {
        TermFeed feed = null;
        final String url = context.getTermsUrl();
        if (url == null) {
            log.error("Terms URL is null!!");
        } else {
            try {
                final RestTemplate restTemplate = new RestTemplate();
                feed = restTemplate.getForObject(url, TermFeed.class);
            } catch (Exception e) {
                log.error("error reading terms from " + context.getTermsUrl(), e);
            }
        }
        return feed;
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
        ContactInfo info = new ContactInfo();
        try {
            final RestTemplate restTemplate = new RestTemplate();
            info = restTemplate.getForObject(url, ContactInfo.class);
        } catch (Exception e) {
            log.error("error reading student contact info from " + context.getContactInfoUrl(), e);
        }
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
            // populating from db
            info.setAddress(savedInfo.getAddress());
            info.setCity(savedInfo.getCity());
            info.setState(savedInfo.getState());
            info.setZipCode(savedInfo.getZipCode());
            info.setCounty(savedInfo.getCounty());
            info.setPhoneNumber(savedInfo.getPhoneNumber());
            info.setMobile(savedInfo.getMobile());
            info.setAltPhone(savedInfo.getAltPhone());
            info.getRace().clear();
            info.getRace().addAll(savedInfo.getRace());
            info.getEthnicity().clear();
            info.getEthnicity().addAll(savedInfo.getEthnicity());
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

    @Override
    @Cacheable("races")
    public CodeDesc[] getRaceList(StudentRequestContext context) {
        final String url = context.getRaceListUrl();
        CodeDesc[] codes = new CodeDesc[0];
        try {
            final RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<CodeDesc[]> response = restTemplate.getForEntity(url, CodeDesc[].class);
            codes = response.getBody();
        } catch (Exception e) {
            log.error("error reading races url " + context.getRaceListUrl(), e);
        }
        return codes;
    }

    @Override
    @Cacheable("ethnicities")
    public CodeDesc[] getEthnicityList(StudentRequestContext context) {
        final String url = context.getEthnicityListUrl();
        CodeDesc[] codes = new CodeDesc[0];
        try {
            final RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<CodeDesc[]> response = restTemplate.getForEntity(url, CodeDesc[].class);
            codes = response.getBody();
        } catch (Exception e) {
            log.error("error reading ethnicities url " + context.getEthnicityListUrl(), e);
        }
        return codes;
    }
}
