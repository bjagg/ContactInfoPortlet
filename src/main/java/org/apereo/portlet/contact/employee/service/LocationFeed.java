package org.apereo.portlet.contact.employee.service;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class LocationFeed {

    List<Location> rows;
    @NonNull Boolean success;
    Integer results;
}
