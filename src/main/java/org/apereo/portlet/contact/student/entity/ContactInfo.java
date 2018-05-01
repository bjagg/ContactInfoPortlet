/**
 * Licensed to Apereo under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright ownership. Apereo
 * licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the License at the
 * following location:
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apereo.portlet.contact.student.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
    private String name;
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

    @ElementCollection
    @CollectionTable(name = "contact_info_race")
    private List<String> race = new ArrayList();

    @ElementCollection
    @CollectionTable(name = "contact_info_ethnicity")
    private List<String> ethnicity = new ArrayList();
}
