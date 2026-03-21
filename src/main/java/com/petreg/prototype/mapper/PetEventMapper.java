package com.petreg.prototype.mapper;

import org.springframework.stereotype.Component;

import com.petreg.prototype.dto.PetEventResponseDto;
import com.petreg.prototype.model.PetEvent;

@Component
public class PetEventMapper {

    private final PetMapper petMapper;
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    public PetEventMapper(
            PetMapper petMapper,
            UserMapper userMapper,
            RoleMapper roleMapper) {
        this.petMapper = petMapper;
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }

    public PetEventResponseDto toDto(PetEvent event) {
        return new PetEventResponseDto(
            event.getId(),
            petMapper.toDto(event.getPet()),
            userMapper.toDto(event.getUser()),
            roleMapper.toDto(event.getUserRole()),
            event.getEventType(),
            event.getTimestamp(),
            event.getDescription()
        );
    }
}
