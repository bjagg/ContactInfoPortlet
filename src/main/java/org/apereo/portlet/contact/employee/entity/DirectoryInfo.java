package org.apereo.portlet.contact.employee.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Entity
@Table(name = "contact_dir_info")
public class DirectoryInfo {
    @Id @NonNull private String username;
    private String title;
    private String dept;
    private String phone;
    private String location;
    private String hours;
    private String fax;
    private String supervisor;

    public DirectoryInfo(String username) {
        this.username = username;
    }
}
