package org.apereo.portlet.contact.employee.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Location {
    @NonNull private String roomNumber;
}
