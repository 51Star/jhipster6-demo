package com.hugl.web.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.hugl.web.web.rest.TestUtil;

public class PubDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PubDTO.class);
        PubDTO pubDTO1 = new PubDTO();
        pubDTO1.setId(1L);
        PubDTO pubDTO2 = new PubDTO();
        assertThat(pubDTO1).isNotEqualTo(pubDTO2);
        pubDTO2.setId(pubDTO1.getId());
        assertThat(pubDTO1).isEqualTo(pubDTO2);
        pubDTO2.setId(2L);
        assertThat(pubDTO1).isNotEqualTo(pubDTO2);
        pubDTO1.setId(null);
        assertThat(pubDTO1).isNotEqualTo(pubDTO2);
    }
}
