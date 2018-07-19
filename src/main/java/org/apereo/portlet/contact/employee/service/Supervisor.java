package org.apereo.portlet.contact.employee.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Supervisor {

    @NonNull private String departmentName;
    @NonNull private String fullname;
    @NonNull private String networkuserID;
    @NonNull private String tartanId;
}
