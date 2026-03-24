package com.petreg.prototype.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.petreg.prototype.model.type.RoleEnum;

@Component
@RequestScope
public class ActiveRoleContext {

    private RoleEnum activeRoleType;

    public RoleEnum getActiveRoleType() {
        return activeRoleType;
    }

    public void setActiveRoleType(RoleEnum activeRoleType) {
        this.activeRoleType = activeRoleType;
    }
}
