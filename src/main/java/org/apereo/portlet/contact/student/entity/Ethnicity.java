package org.apereo.portlet.contact.student.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Entity
@Table(name = "contact_ethnicity")
public class Ethnicity {
    @Id @NonNull private String username;
    private boolean nativeAmerican;
    private boolean asian;
    private boolean african;
    private boolean hispanic;
    private boolean pacificIslander;
    private boolean white;
    private boolean notDisclosed = true;

    public Ethnicity(String username) {
        this.username = username;
    }
}
