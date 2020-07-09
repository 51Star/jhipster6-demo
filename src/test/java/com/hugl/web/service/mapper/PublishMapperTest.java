package com.hugl.web.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PublishMapperTest {

    private PublishMapper publishMapper;

    @BeforeEach
    public void setUp() {
        publishMapper = new PublishMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(publishMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(publishMapper.fromId(null)).isNull();
    }
}
