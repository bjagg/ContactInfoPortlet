package org.apereo.portlet.contact.student.service;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/** Feed for terms. - rows (list of {@Term}) - success - results */
@Data
@NoArgsConstructor
public class TermFeed {

    List<Term> rows;
    @NonNull Boolean success;
    Integer results;
}
