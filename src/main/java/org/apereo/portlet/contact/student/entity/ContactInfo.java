package org.apereo.portlet.contact.student.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contact_info")
public class ContactInfo {
    @Id private String username;
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
