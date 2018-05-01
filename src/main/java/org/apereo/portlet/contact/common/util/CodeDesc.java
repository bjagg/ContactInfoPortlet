package org.apereo.portlet.contact.common.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.Value;

@Value
@AllArgsConstructor
public class CodeDesc {

    @NonNull
    @JsonProperty("Code")
    private String code;

    @NonNull
    @JsonProperty("Description")
    private String description;
}
