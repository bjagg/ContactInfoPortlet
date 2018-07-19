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

import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.apereo.portlet.contact.common.entity.UserLastUpdate;
import org.apereo.portlet.contact.employee.entity.DirectoryInfo;
import org.apereo.portlet.contact.employee.entity.EmployeeInfo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

/** Main service class for employee directory information. */
@Transactional
@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @PersistenceContext private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public boolean infoRequiresUpdate(EmployeeRequestContext context) {
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
        final Date nextUpdate = addDays(lastUpdate, context.getDaysBetweenUpdate());
        return nextUpdate.before(new Date());
    }

    @Override
    public void refreshLastUpdate(String username) {
        em.merge(new UserLastUpdate(username));
    }

    /**
     * Add days to {@link Date} using Gregorian Calendar.
     *
     * @param date original date.
     * @param days number of days to add.
     * @return new {@link Date} that is +days in the future of date parameter.
     */
    public static Date addDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(GregorianCalendar.DATE, days);
        return cal.getTime();
    }

    @Transactional(readOnly = true)
    @Override
    public DirectoryInfo getDirectoryInfo(EmployeeRequestContext context) {
        final String url = calcDirectoryInfoUrl(context);
        DirectoryInfo info = new DirectoryInfo();
        try {
            final RestTemplate restTemplate = new RestTemplate();
            final EmployeeFeed feed = restTemplate.getForObject(url, EmployeeFeed.class);
            info = feed.toDirectoryInfo();
        } catch (Exception e) {
            log.error("error reading directory info url " + context.getDirectoryUrl(), e);
        }
        info.setUsername(context.getUsername());
        mergeSavedValues(info);
        log.debug(info.toString());
        return info;
    }

    private void mergeSavedValues(DirectoryInfo info) {
        assert info != null;
        assert info.getUsername() != null;
        DirectoryInfo savedInfo = em.find(DirectoryInfo.class, info.getUsername());
        if (savedInfo != null) {
            info.setHours(savedInfo.getHours());
            info.setTitle(savedInfo.getTitle());
            info.setDept(savedInfo.getDept());
            info.setPhone(savedInfo.getPhone());
            info.setLocation(savedInfo.getLocation());
            info.setFax(savedInfo.getFax());
            info.setSupervisor(savedInfo.getSupervisor());
        }
    }

    private String calcDirectoryInfoUrl(EmployeeRequestContext context) {
        String url = context.getDirectoryUrl();
        if (context.getUserId() != null) {
            final String userIdToken = "{" + context.getUserIdParam() + "}";
            url = url.replace(userIdToken, context.getUserId());
        }
        log.debug(url);
        return url;
    }

    @Transactional
    @Override
    public void saveDirectoryInfo(DirectoryInfo info) {
        em.merge(info);
    }

    @Transactional(readOnly = true)
    @Override
    public EmployeeInfo getEmployeeInfo(EmployeeRequestContext context) {
        final EmployeeInfo info = em.find(EmployeeInfo.class, context.getUsername());
        if (info != null) {
            em.detach(info);
        }
        return info;
    }

    @Transactional
    @Override
    public void saveEmployeeInfo(EmployeeInfo info) {
        em.merge(info);
    }

    @Override
    @Cacheable("departments")
    public Department[] getDepartmentList(EmployeeRequestContext context) {
        final String url = context.getDeptUrl();
        Department[] depts = new Department[0];
        try {
            final RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<DepartmentFeed> response =
                    restTemplate.getForEntity(url, DepartmentFeed.class);
            DepartmentFeed feed = response.getBody();
            depts = feed.getRows().toArray(depts);
        } catch (Exception e) {
            log.error("error reading departments url " + context.getDeptUrl(), e);
        }
        return depts;
    }

    @Override
    @Cacheable("locations")
    public Location[] getLocationList(EmployeeRequestContext context) {
        final String url = context.getLocUrl();
        Location[] locs = new Location[0];
        try {
            final RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<LocationFeed> response =
                    restTemplate.getForEntity(url, LocationFeed.class);
            LocationFeed feed = response.getBody();
            locs = feed.getRows().toArray(locs);
        } catch (Exception e) {
            log.error("error reading locations url " + context.getLocUrl(), e);
        }
        return locs;
    }

    @Override
    @Cacheable("supervisors")
    public Supervisor[] getSupervisorList(EmployeeRequestContext context) {
        final String url = context.getSupervisorUrl();
        Supervisor[] supervisors = new Supervisor[0];
        try {
            final RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<SupervisorFeed> response =
                    restTemplate.getForEntity(url, SupervisorFeed.class);
            SupervisorFeed feed = response.getBody();
            supervisors = feed.getRows().toArray(supervisors);
        } catch (Exception e) {
            log.error("error reading supervisors url " + context.getSupervisorUrl(), e);
        }
        return supervisors;
    }
}
