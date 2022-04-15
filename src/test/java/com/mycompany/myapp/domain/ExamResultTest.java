package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExamResultTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExamResult.class);
        ExamResult examResult1 = new ExamResult();
        examResult1.setId(1L);
        ExamResult examResult2 = new ExamResult();
        examResult2.setId(examResult1.getId());
        assertThat(examResult1).isEqualTo(examResult2);
        examResult2.setId(2L);
        assertThat(examResult1).isNotEqualTo(examResult2);
        examResult1.setId(null);
        assertThat(examResult1).isNotEqualTo(examResult2);
    }
}
