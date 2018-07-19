package org.apereo.portlet.contact.employee.service;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class DepartmentFeed {
    List<Department> rows;
    @NonNull Boolean success;
    Integer results;
}
