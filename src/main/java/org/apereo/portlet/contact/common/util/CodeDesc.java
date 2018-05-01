package org.apereo.portlet.contact.common.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeDesc {

    @NonNull
    @JsonProperty("Code")
    private String code;

    @NonNull
    @JsonProperty("Description")
    private String description;
}
