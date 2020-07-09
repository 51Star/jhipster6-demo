package com.hugl.web.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.hugl.web.web.rest.TestUtil;

public class PublishTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Publish.class);
        Publish publish1 = new Publish();
        publish1.setId(1L);
        Publish publish2 = new Publish();
        publish2.setId(publish1.getId());
        assertThat(publish1).isEqualTo(publish2);
        publish2.setId(2L);
        assertThat(publish1).isNotEqualTo(publish2);
        publish1.setId(null);
        assertThat(publish1).isNotEqualTo(publish2);
    }
}
