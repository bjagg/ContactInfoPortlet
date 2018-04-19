package org.apereo.portlet.contact.common.entity;

import java.util.Date;
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
@Table(name = "contact_user_last_update")
public class UserLastUpdate {
    @Id @NonNull private String username;

    @Column(name = "last_update")
    @NonNull
    private Date lastUpdate;

    public UserLastUpdate(final String username) {
        this.username = username;
        this.lastUpdate = new Date();
    }

    public UserLastUpdate(final String username, final Date lastUpdate) {
        this.username = username;
        this.lastUpdate = new Date(lastUpdate.getTime());
    }
}
