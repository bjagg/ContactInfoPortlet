package org.apereo.portlet.contact.employee.service;

import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.apereo.portlet.contact.common.entity.UserLastUpdate;
import org.apereo.portlet.contact.employee.entity.DirectoryInfo;
import org.apereo.portlet.contact.employee.entity.EmployeeInfo;
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
     * Add days to {@Date} using Gregorian Calendar.
     *
     * @param date original date.
     * @param days number of days to add.
     * @return new {@Date} that is +days in the future of date parameter.
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
        final RestTemplate restTemplate = new RestTemplate();
        final EmployeeFeed feed = restTemplate.getForObject(url, EmployeeFeed.class);
        final DirectoryInfo info = feed.toDirectoryInfo();
        info.setUsername(context.getUsername());
        mergeSavedValues(info);
        log.debug(info.toString());
        return info;
    }

    private void mergeSavedValues(DirectoryInfo info) {
        DirectoryInfo savedInfo = em.find(DirectoryInfo.class, info.getUsername());
        if (savedInfo != null) {
            info.setHours(savedInfo.getHours());
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
}
