package com.hugl.web.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.hugl.web.web.rest.TestUtil;

public class PublishDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PublishDTO.class);
        PublishDTO publishDTO1 = new PublishDTO();
        publishDTO1.setId(1L);
        PublishDTO publishDTO2 = new PublishDTO();
        assertThat(publishDTO1).isNotEqualTo(publishDTO2);
        publishDTO2.setId(publishDTO1.getId());
        assertThat(publishDTO1).isEqualTo(publishDTO2);
        publishDTO2.setId(2L);
        assertThat(publishDTO1).isNotEqualTo(publishDTO2);
        publishDTO1.setId(null);
        assertThat(publishDTO1).isNotEqualTo(publishDTO2);
    }
}
