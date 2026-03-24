package com.petreg.prototype.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.petreg.prototype.model.User;
import com.petreg.prototype.model.type.RoleEnum;

@Component
@RequestScope
public class ActiveUserContext {

    private RoleEnum activeRoleType;
    private User user;

    public RoleEnum getActiveRoleType() {
        return activeRoleType;
    }

    public void setActiveRoleType(RoleEnum activeRoleType) {
        this.activeRoleType = activeRoleType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
