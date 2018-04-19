package org.apereo.portlet.contact.student.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Entity
@Table(name = "contact_com_prefs")
public class CommunicationPreferences {
    @Id @NonNull private String username;
    private boolean sms;

    @Column(name = "mobile_app")
    private boolean mobileApp;

    private boolean portal;
    private boolean none = true;

    public CommunicationPreferences(String username) {
        this.username = username;
    }
}
