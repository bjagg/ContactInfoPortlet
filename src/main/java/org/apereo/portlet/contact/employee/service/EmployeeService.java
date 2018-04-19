package org.apereo.portlet.contact.employee.service;

import org.apereo.portlet.contact.employee.entity.DirectoryInfo;
import org.apereo.portlet.contact.employee.entity.EmployeeInfo;

public interface EmployeeService {

    boolean infoRequiresUpdate(EmployeeRequestContext context);

    void refreshLastUpdate(String username);

    DirectoryInfo getDirectoryInfo(EmployeeRequestContext context);

    void saveDirectoryInfo(DirectoryInfo info);

    EmployeeInfo getEmployeeInfo(EmployeeRequestContext context);

    void saveEmployeeInfo(EmployeeInfo info);
}
