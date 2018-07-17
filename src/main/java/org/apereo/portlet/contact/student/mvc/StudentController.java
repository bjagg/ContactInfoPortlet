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
package org.apereo.portlet.contact.student.mvc;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apereo.portlet.contact.common.util.CodeDesc;
import org.apereo.portlet.contact.student.entity.CommunicationPreferences;
import org.apereo.portlet.contact.student.entity.ContactInfo;
import org.apereo.portlet.contact.student.service.ContactPreferences;
import org.apereo.portlet.contact.student.service.StudentRequestContext;
import org.apereo.portlet.contact.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

/** Main portlet view for Students */
@Controller
@RequestMapping("VIEW")
@Slf4j
public class StudentController {

    @Autowired private StudentService service;

    @Autowired private ObjectMapper mapper;

    @Transactional(readOnly = true)
    @RenderMapping
    public ModelAndView showStudentView(final RenderRequest request) {
        log.debug("Processing student view for {}", request.getRemoteUser());

        final StudentRequestContext context = new StudentRequestContext(request);

        final ModelAndView mav = new ModelAndView("studentInfo");

        final boolean doUpdate = service.infoRequiresUpdate(context);
        mav.addObject("updateRequired", doUpdate);
        if (doUpdate) {
            log.debug("Contact info update required");

            final ContactInfo info = service.getContactInfo(context);
            log.debug(info.toString());
            mav.addObject("contactInfo", info);

            final CommunicationPreferences comPref = service.getCommunicationPreferences(context);
            if (comPref != null) {
                log.debug(comPref.toString());
            } else {
                log.debug("No communications preferences found");
            }
            final Boolean showComPref = comPref == null;
            log.debug("Prompting for communication preferences: {}", showComPref);
            mav.addObject("showCommunicationPreferences", showComPref);

            mav.addObject("races", service.getRaceList(context));
            mav.addObject("ethnicities", service.getEthnicityList(context));
        } else {
            log.debug("No contact info update required");
        }

        return mav;
    }

    @ResourceMapping(value = "race-list")
    public void raceListResource(ResourceRequest request, ResourceResponse response)
            throws IOException {
        log.debug("Processing AJAX resource request for race list");

        final StudentRequestContext context = new StudentRequestContext(request);
        final CodeDesc[] races = service.getRaceList(context);
        final String json = mapper.writeValueAsString(races);
        log.debug("json of race list: {}", json);
        response.getWriter().write(json);
    }

    @ResourceMapping(value = "ethnicity-list")
    public void ethnicityListResource(ResourceRequest request, ResourceResponse response)
            throws IOException {
        log.debug("Processing AJAX resource request for ethnicity list");

        final StudentRequestContext context = new StudentRequestContext(request);
        final CodeDesc[] ethnicities = service.getEthnicityList(context);
        final String json = mapper.writeValueAsString(ethnicities);
        log.debug("json of ethnicity list: {}", json);
        response.getWriter().write(json);
    }

    @Transactional(readOnly = true)
    @ResourceMapping(value = "student-info")
    public void studentInfoResource(final ResourceRequest request, final ResourceResponse response)
            throws IOException {
        log.debug("Processing AJAX resource request");

        final StudentRequestContext context = new StudentRequestContext(request);

        final Map<String, Object> results = new HashMap<>();

        final boolean doUpdate = service.infoRequiresUpdate(context);
        results.put("updateRequired", doUpdate);
        if (doUpdate) {
            log.debug("Contact info update required");

            final ContactInfo info = service.getContactInfo(context);
            log.debug(info.toString());
            results.put("contactInfo", info);

            final CommunicationPreferences comPref = service.getCommunicationPreferences(context);
            if (comPref != null) {
                log.debug(comPref.toString());
            } else {
                log.debug("No communications preferences found");
            }
            final Boolean showComPref = comPref == null;
            log.debug("Prompting for communication preferences: {}", showComPref);
            results.put("showCommunicationPreferences", showComPref);

        } else {
            log.debug("No contact info update required");
        }

        final String json = mapper.writeValueAsString(results);
        response.getWriter().write(json);
    }

    @Transactional
    @ActionMapping
    public void saveStudentAction(final ActionRequest request) {
        log.debug("Processing save action");
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            log.debug(entry.toString());
        }
        if (!request.getRemoteUser().equals(request.getParameter("username"))) {
            log.warn(
                    "RemoteUser {} does not match username {}!",
                    request.getRemoteUser(),
                    request.getParameter("username"));
        }

        final StudentRequestContext context = new StudentRequestContext(request);

        // update contact info
        final ContactInfo info = service.getContactInfo(context);
        info.setAddress(request.getParameter("address"));
        info.setCity(request.getParameter("city"));
        info.setState(request.getParameter("state"));
        info.setZipCode(request.getParameter("zip"));
        info.setCounty(request.getParameter("county"));
        info.setPhoneNumber(request.getParameter("phoneNumber"));
        info.setMobile(request.getParameter("is-mobile") != null);
        info.setAltPhone(request.getParameter("altPhone"));
        List<String> race = info.getRace();
        race.clear();
        String[] raceValues = request.getParameterValues("race");
        if (raceValues != null) {
            race.addAll(Arrays.asList(raceValues));
        }
        List<String> ethnicity = info.getEthnicity();
        ethnicity.clear();
        String ethnicityValue = request.getParameter("ethnicity");
        if (ethnicityValue != null) {
            ethnicity.add(ethnicityValue);
        }
        log.debug(info.toString());
        log.debug("Saving contact info...");
        service.saveContactInfo(info);

        // communication preference
        // business rule: only 1 update ever
        CommunicationPreferences comPref = service.getCommunicationPreferences(context);
        if (comPref != null) {
            log.debug("Communication preferences already saved -- skipping");
        } else {
            comPref = new CommunicationPreferences(context.getUsername());
            comPref.setSms(request.getParameter("sms") != null);
            comPref.setMobileApp(request.getParameter("mobile-app") != null);
            comPref.setPortal(request.getParameter("portal") != null);
            comPref.setNone(request.getParameter("none") != null);
            log.debug(comPref.toString());
            log.debug("saving communication preferences ...");
            service.saveCommunicationPreferences(comPref);
        }

        if (context.getPrefsRemoteUrl() != null && !context.getPrefsRemoteUrl().isEmpty()) {
            final String mobilePhone =
                    info.getMobile() ? info.getPhoneNumber() : info.getAltPhone();
            final String remoteJson = updateRemoteService(request, context, mobilePhone);
        }

        log.debug("Refreshing last update for {} ... ", context.getUsername());
        service.refreshLastUpdate(context.getUsername());
    }

    private String updateRemoteService(
            ActionRequest request, StudentRequestContext context, String mobilePhone) {
        // update remote service
        ContactPreferences remotePrefs = new ContactPreferences();
        remotePrefs.setStudentId(context.getUserId());
        remotePrefs.setUserName(context.getUsername());
        remotePrefs.setReceiveSms(request.getParameter("sms"));
        remotePrefs.setReceivePushNotification(request.getParameter("mobile-app"));
        remotePrefs.setReceivePortalNotification(request.getParameter("portal"));
        remotePrefs.setPhoneNumber(mobilePhone);
        log.debug(remotePrefs.toString());
        String json;
        try {
            json = mapper.writeValueAsString(remotePrefs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            json = "{}";
        }
        log.debug(json);
        return json;
    }
}
