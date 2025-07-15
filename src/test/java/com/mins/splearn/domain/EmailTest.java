package com.mins.splearn.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EmailTest {
    @Test
    void equality() {
        var email1 = new Email("min@splearn.com");
        var email2 = new Email("min@splearn.com");

        assertThat(email1).isEqualTo(email2);
    }

}