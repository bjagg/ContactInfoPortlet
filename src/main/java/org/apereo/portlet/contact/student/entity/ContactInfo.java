package org.apereo.portlet.contact.student.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contact_info")
public class ContactInfo {
    @Id @NonNull private String username;
    private String address;
    private String city;
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    private String county;

    @Column(name = "phone")
    private String phoneNumber;

    private Boolean mobile = Boolean.FALSE;

    @Column(name = "alt_phone")
    private String altPhone;
}
