package de.headshotharp.web.database.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.headshotharp.web.database.Role;
import de.headshotharp.web.database.User;
import de.headshotharp.web.database.generic.DataAccessObject;

public class EntityRegister {

    public static final List<Class<? extends DataAccessObject>> DAO_CLASSES = Collections
            .unmodifiableList(Arrays.asList(User.class, Role.class));

    private EntityRegister() {
    }
}
