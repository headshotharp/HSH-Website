package de.headshotharp.web.database;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.headshotharp.web.database.generic.DataAccessObject;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "HSH_ROLE")
@Table(name = "HSH_ROLE")
public class Role implements DataAccessObject {

    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
    private String prefix;

    @Column(nullable = false, unique = true)
    private int power;

    @OneToMany(mappedBy = "role", cascade = { CascadeType.DETACH, CascadeType.PERSIST })
    private @Default Set<User> users = new HashSet<>();

    private @Default List<String> permissions = new LinkedList<>();

    @PreRemove
    private void preRemove() {
        for (User user : users) {
            user.setRole(null);
        }
    }
}
