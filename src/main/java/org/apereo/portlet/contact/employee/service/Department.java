package org.apereo.portlet.contact.employee.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Department {

    @NonNull private String departmentName;
    @NonNull private String departmentId;
}
