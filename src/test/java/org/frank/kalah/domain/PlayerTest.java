package org.frank.kalah.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class PlayerTest {


    @Test
    void south() {
        assertThat(Player.SOUTH.nextHouseAfter(1)).isEqualTo(2);
        assertThat(Player.SOUTH.nextHouseAfter(2)).isEqualTo(3);
        assertThat(Player.SOUTH.nextHouseAfter(6)).isEqualTo(7);
        assertThat(Player.SOUTH.nextHouseAfter(7)).isEqualTo(8);
        assertThat(Player.SOUTH.nextHouseAfter(12)).isEqualTo(13);
        assertThat(Player.SOUTH.nextHouseAfter(13)).isEqualTo(1);
        assertThat(Player.SOUTH.nextHouseAfter(14)).isEqualTo(1);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy( () -> Player.SOUTH.nextHouseAfter(15));


        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy( () -> Player.SOUTH.nextHouseAfter(0));


        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy( () -> Player.SOUTH.nextHouseAfter(-1));

    }
    @Test
    void north() {
        assertThat(Player.NORTH.nextHouseAfter(1)).isEqualTo(2);
        assertThat(Player.NORTH.nextHouseAfter(2)).isEqualTo(3);

        assertThat(Player.NORTH.nextHouseAfter(6)).isEqualTo(8);

        assertThat(Player.NORTH.nextHouseAfter(7)).isEqualTo(8);

        assertThat(Player.NORTH.nextHouseAfter(12)).isEqualTo(13);
        assertThat(Player.NORTH.nextHouseAfter(13)).isEqualTo(14);
        assertThat(Player.NORTH.nextHouseAfter(14)).isEqualTo(1);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy( () -> Player.NORTH.nextHouseAfter(15));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy( () -> Player.NORTH.nextHouseAfter(0));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy( () -> Player.NORTH.nextHouseAfter(-1));
    }


}