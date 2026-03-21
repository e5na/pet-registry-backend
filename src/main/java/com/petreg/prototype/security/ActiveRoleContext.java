package com.petreg.prototype.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.petreg.prototype.model.type.RoleEnum;

@Component
@RequestScope
public class ActiveRoleContext {

    private RoleEnum activeRole;

    public RoleEnum getActiveRole() {
        return activeRole;
    }

    public void setActiveRole(RoleEnum activeRole) {
        this.activeRole = activeRole;
    }
}
