package org.apereo.portlet.contact.student.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/** Data object for Term with start and end dates. - id - termName - startDate - endDate - toView */
@Data
@NoArgsConstructor
public class Term {

    @NonNull private String id;
    @NonNull private String termName;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Date startDate;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
    private Date endDate;

    private String toView;
}
