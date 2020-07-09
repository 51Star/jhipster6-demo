package com.hugl.web.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.hugl.web.web.rest.TestUtil;

public class PubTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pub.class);
        Pub pub1 = new Pub();
        pub1.setId(1L);
        Pub pub2 = new Pub();
        pub2.setId(pub1.getId());
        assertThat(pub1).isEqualTo(pub2);
        pub2.setId(2L);
        assertThat(pub1).isNotEqualTo(pub2);
        pub1.setId(null);
        assertThat(pub1).isNotEqualTo(pub2);
    }
}
