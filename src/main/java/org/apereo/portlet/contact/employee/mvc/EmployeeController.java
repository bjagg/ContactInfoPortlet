package org.apereo.portlet.contact.employee.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.portlet.ActionRequest;
import javax.portlet.RenderRequest;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import lombok.extern.slf4j.Slf4j;
import org.apereo.portlet.contact.employee.entity.DirectoryInfo;
import org.apereo.portlet.contact.employee.entity.EmployeeInfo;
import org.apereo.portlet.contact.employee.service.EmployeeRequestContext;
import org.apereo.portlet.contact.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

@Controller
@RequestMapping("VIEW")
@Slf4j
public class EmployeeController {

    @Autowired private EmployeeService service;

    @Autowired private ObjectMapper mapper;

    @Transactional(readOnly = true)
    @RenderMapping
    public ModelAndView showEmployeeForm(final RenderRequest request) {
        log.debug("Processing employee view for {}", request.getRemoteUser());

        final EmployeeRequestContext context = new EmployeeRequestContext(request);

        final ModelAndView mav = new ModelAndView("employeeInfo");

        final boolean doUpdate = service.infoRequiresUpdate(context);
        mav.addObject("updateRequired", doUpdate);
        if (doUpdate) {
            log.debug("Employee directory info update required");

            DirectoryInfo directoryInfo = service.getDirectoryInfo(context);
            if (directoryInfo != null) {
                log.debug(directoryInfo.toString());
            } else {
                log.debug("No directory info found");
                directoryInfo = new DirectoryInfo();
            }
            mav.addObject(directoryInfo);

            EmployeeInfo employeeInfo = service.getEmployeeInfo(context);
            if (employeeInfo != null) {
                log.debug(employeeInfo.toString());
            } else {
                log.debug("No employee info found");
                employeeInfo = new EmployeeInfo();
            }
            mav.addObject(employeeInfo);
        } else {
            log.debug("No employee directory info update required");
        }

        return mav;
    }

    @Transactional(readOnly = true)
    @ResourceMapping(value = "emp-info")
    public void employeeInfoResource(final ResourceRequest request, ResourceResponse response)
            throws IOException {
        log.debug("Processing AJAX resource request");

        final EmployeeRequestContext context = new EmployeeRequestContext(request);

        final Map<String, Object> results = new HashMap<>();

        final boolean doUpdate = service.infoRequiresUpdate(context);
        results.put("updateRequired", doUpdate);
        if (doUpdate) {
            log.debug("Employee directory info update required");

            DirectoryInfo directoryInfo = service.getDirectoryInfo(context);
            if (directoryInfo != null) {
                log.debug(directoryInfo.toString());
            } else {
                log.debug("No directory info found");
                directoryInfo = new DirectoryInfo();
            }
            results.put("directoryInfo", directoryInfo);

            EmployeeInfo employeeInfo = service.getEmployeeInfo(context);
            if (employeeInfo != null) {
                log.debug(employeeInfo.toString());
            } else {
                log.debug("No employee info found");
                employeeInfo = new EmployeeInfo();
            }
            results.put("employeeInfo", employeeInfo);
        } else {
            log.debug("No employee directory info update required");
        }

        final String json = mapper.writeValueAsString(results);
        response.getWriter().write(json);
    }

    @Transactional
    @ActionMapping
    public void saveEmployeeAction(final ActionRequest request) {
        log.debug("Processing save employee action");
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            log.debug(entry.toString());
        }
        if (!request.getRemoteUser().equals(request.getParameter("username"))) {
            log.warn(
                    "RemoteUser {} does not match username {}!",
                    request.getRemoteUser(),
                    request.getParameter("username"));
        }

        final EmployeeRequestContext context = new EmployeeRequestContext(request);

        // update directory info
        DirectoryInfo directoryInfo = service.getDirectoryInfo(context);
        if (directoryInfo == null) {
            directoryInfo = new DirectoryInfo();
            directoryInfo.setUsername(context.getUsername());
        }
        directoryInfo.setTitle(request.getParameter("title"));
        directoryInfo.setDept(request.getParameter("dept"));
        directoryInfo.setPhone(request.getParameter("phone"));
        directoryInfo.setLocation(request.getParameter("location"));
        directoryInfo.setHours(request.getParameter("hours"));
        directoryInfo.setFax(request.getParameter("fax"));
        directoryInfo.setSupervisor(request.getParameter("supervisor"));
        log.debug(directoryInfo.toString());
        log.debug("Saving directory info ...");
        service.saveDirectoryInfo(directoryInfo);

        // update employee info
        EmployeeInfo employeeInfo = service.getEmployeeInfo(context);
        if (employeeInfo == null) {
            employeeInfo = new EmployeeInfo();
            employeeInfo.setUsername(context.getUsername());
        }
        employeeInfo.setEmergencyPhone(request.getParameter("emergencyPhone"));
        employeeInfo.setBio(request.getParameter("bio"));
        employeeInfo.setCredentials(request.getParameter("credentials"));
        employeeInfo.setPersonalUrl(request.getParameter("url"));
        log.debug(employeeInfo.toString());
        log.debug("Saving employee info ... ");
        service.saveEmployeeInfo(employeeInfo);

        log.debug("Refreshing last update for {} ... ", context.getUsername());
        service.refreshLastUpdate(context.getUsername());
    }
}
