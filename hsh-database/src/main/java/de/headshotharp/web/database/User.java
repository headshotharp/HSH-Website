package de.headshotharp.web.database;

import java.util.HashSet;
import java.util.Set;

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
import jakarta.persistence.OneToMany;
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

    private @Default double money = 0;
    private @Default long placedBlocks = 0;
    private @Default long brokenBlocks = 0;

    @OneToMany(mappedBy = "user", cascade = { CascadeType.ALL })
    private @Default Set<UserValueHistory> valueHistory = new HashSet<>();

    public void setRole(Role role) {
        if (this.role != null) {
            this.role.getUsers().remove(this);
        }
        this.role = role;
        if (role != null) {
            role.getUsers().add(this);
        }
    }

    public UserValueHistory createValueHistory() {
        UserValueHistory history = new UserValueHistory();
        history.setMoney(money);
        history.setBrokenBlocks(brokenBlocks);
        history.setPlacedBlocks(placedBlocks);
        history.setUser(this);
        valueHistory.add(history);
        return history;
    }
}
