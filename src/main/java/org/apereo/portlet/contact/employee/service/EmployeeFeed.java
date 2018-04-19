package org.apereo.portlet.contact.employee.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apereo.portlet.contact.employee.entity.DirectoryInfo;

/** Data object that maps to the JSON feed. */
@Data
@NoArgsConstructor
public class EmployeeFeed {
    private String name;
    private String positionTitle;
    private String department;
    private String location;
    private String sinclairPhone;
    private String sinclairFax;
    private String supervisorID;

    public DirectoryInfo toDirectoryInfo() {
        DirectoryInfo info = new DirectoryInfo();
        info.setTitle(positionTitle);
        info.setDept(department);
        info.setLocation(location);
        info.setPhone(sinclairPhone);
        info.setFax(sinclairFax);
        info.setSupervisor(supervisorID);
        return info;
    }
}
