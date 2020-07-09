package com.hugl.web.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PubMapperTest {

    private PubMapper pubMapper;

    @BeforeEach
    public void setUp() {
        pubMapper = new PubMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(pubMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(pubMapper.fromId(null)).isNull();
    }
}
