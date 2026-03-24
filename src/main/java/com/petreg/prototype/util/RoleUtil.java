package com.petreg.prototype.util;

import java.util.Optional;
import java.util.Set;

import com.petreg.prototype.model.Role;
import com.petreg.prototype.model.type.RoleEnum;

public class RoleUtil {

    static public Optional<Role> getRoleByTypeFromRoles(Set<Role> roles, RoleEnum roleType) {
        return roles.stream()
           .filter(role -> role.getName() == roleType)
           .findFirst();
    }
}
