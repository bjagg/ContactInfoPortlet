package org.apereo.portlet.contact.employee.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Entity
@Table(name = "contact_emp_info")
public class EmployeeInfo {
    @Id @NonNull private String username;
    private String emergencyPhone;
    private String bio;
    private String credentials;
    private String personalUrl;

    public EmployeeInfo(String username) {
        this.username = username;
    }
}
