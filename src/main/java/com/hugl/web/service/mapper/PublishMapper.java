package com.hugl.web.service.mapper;


import com.hugl.web.domain.*;
import com.hugl.web.service.dto.PublishDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Publish} and its DTO {@link PublishDTO}.
 */
@Mapper(componentModel = "spring", uses = {MemberMapper.class})
public interface PublishMapper extends EntityMapper<PublishDTO, Publish> {

    @Mapping(source = "member.id", target = "memberId")
    PublishDTO toDto(Publish publish);

    @Mapping(source = "memberId", target = "member")
    Publish toEntity(PublishDTO publishDTO);

    default Publish fromId(Long id) {
        if (id == null) {
            return null;
        }
        Publish publish = new Publish();
        publish.setId(id);
        return publish;
    }
}
