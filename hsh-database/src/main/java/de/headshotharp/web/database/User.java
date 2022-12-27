package de.headshotharp.web.database;

import com.fasterxml.jackson.annotation.JsonIgnore;

import de.headshotharp.web.database.generic.DataAccessObject;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "HSH_USER")
@Table(name = "HSH_USER")
public class User implements DataAccessObject {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String uuid;

    @JsonIgnore
    @Column(nullable = true)
    private String password;

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.PERSIST })
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    private @Default long placedBlocks = 0;
    private @Default long brokenBlocks = 0;

    public void setRole(Role role) {
        if (this.role != null) {
            this.role.getUsers().remove(this);
        }
        this.role = role;
        if (role != null) {
            role.getUsers().add(this);
        }
    }
}
