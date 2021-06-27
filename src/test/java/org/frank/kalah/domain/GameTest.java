package org.frank.kalah.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


public class GameTest {
    private Game game;

//    At the beginning of the game, four seeds are placed in each house. This is the traditional method.
//    Each player controls the six houses and their seeds on the player's side of the board. The player's score is the number of seeds in the store to their right.
//    Players take turns sowing their seeds. On a turn, the player removes all seeds from one of the houses under their control. Moving counter-clockwise, the player drops one seed in each house in turn, including the player's own store but not their opponent's.
//    If the last sown seed lands in an empty house owned by the player, and the opposite house contains seeds, both the last seed and the opposite seeds are captured and placed into the player's store.
//    If the last sown seed lands in the player's store, the player gets an additional move. There is no limit on the number of moves a player can make in their turn.
//    When one player no longer has any seeds in any of their houses, the game ends. The other player moves all remaining seeds to their store, and the player with the most seeds in their store wins.

//           <--- North
// ------------------------
//         13  12  11  10   9   8
//
//         14                   7
//
//         1   2   3   4   5   6
//         ------------------------
//    South --->

    @BeforeEach
    void setup() {
        game = new Game();
    }

    @Test
    void gameResultForNewGame() {
        assertThat(game.result()).isEqualTo(Board.of().build());
    }


    @Test
    void stonesAreMovedBetweenHouses() {
        game = new Game(Board.of().with(1, 14).build());
        game.print();
        game.move(Player.SOUTH, 1);
        game.print();

        assertThat(game.result()).isEqualTo(Board.of()
                // @formatter:off
        .with(1, 1).with(2, 8).with(3, 7).with(4, 7).with(5, 7)
        .with(6, 7)                                                                               .with(14,0)
        .with(7, 1).with(8,7).with(9,7).with(10,7).with(11,7).with(12,7).with(13,7)
        .build());
        // @formatter:on
    }

    @Test
    void playerDoesNotAddStonesToOpponentsStore() {
        game.move(Player.SOUTH, 2);
        game.print();
        game.move(Player.NORTH, 8);
        game.print();

        assertThat(game.stonesInHouse(14)).isEqualTo(1);

        game.move(Player.SOUTH, 3);
        game.print();
        game.move(Player.NORTH, 8);
        game.print();
        game.move(Player.SOUTH, 6);
        game.print();

        assertThat(game.stonesInHouse(14)).isEqualTo(1);
        assertThat(game.stonesInHouse(1)).isEqualTo(8);
    }

    @Test
    void playerCannotMoveStonesFromEmptyHouse() {
        Board boardState = Board.of().with(1, 0).build();
        game = new Game(boardState);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> game.move(Player.SOUTH, 1)).withMessage("Cannot move from empty house");

        assertThat(game.result()).isEqualTo(boardState);

        assertThatNoException().isThrownBy(() -> game.move(Player.SOUTH, 2));
    }

    @Test
    void southPlayerHasFirstMove() {//this is not explicitly in game rules but convention
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> game.move(Player.NORTH, 7))
                .withMessage("SOUTH player turn");

        assertThat(game.result()).isEqualTo(Board.of().build());

    }

    @Test
    void playerMoveDoesNotInOwnStoreThenOpponentsTurn() {
        game.move(Player.SOUTH, 2);
        game.print();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> game.move(Player.SOUTH, 1))
                .withMessage("NORTH player turn");
    }

    @Test
    void playerHasExtraMoveIfEndsMoveInOwnStore() {
        game.move(Player.SOUTH, 1);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> game.move(Player.NORTH, 8));

        game.move(Player.SOUTH, 2);
    }

    @Test
    void playersGetsOpponentStonesIfEndsMoveInOwnEmptyHouse() {
        game = new Game(Board.of().with(1, 1).with(2, 0).build());
        game.print();
        game.move(Player.SOUTH, 1);
        game.print();

        assertThat(game.stonesInHouse(Player.SOUTH.storeId)).isEqualTo(7);
    }

    @Test
    void thereIsNoMoveAfterGameIsOver() {
        game = new Game(Board.of().with(1, 0).with(2, 0).with(3, 0).with(4, 0)
                .with(5, 0).with(6, 0).build());

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> game.move(Player.SOUTH, 1))
                .withMessage("Game Over");

        game = new Game(Board.of().with(8, 0).with(9, 0).with(10, 0).with(11, 0)
                .with(12, 0).with(13, 0).build());

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> game.move(Player.SOUTH, 1))
                .withMessage("Game Over");
    }

    @Test
    void playerCannotMoveStonesFromOpponentsHouse() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> game.move(Player.SOUTH, 8))
                .withMessage("Cannot move stones from other player houses");
    }

    @Test
    void playerCannotMoveStonesFromFromNotExistingHouses() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> game.move(Player.SOUTH, 15))
                .withMessage("No such house: 15");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> game.move(Player.SOUTH, -2))
                .withMessage("No such house: -2");
    }

    @Test
    void playerCannotMoveStonesFromOwnHouseStore() {
        game = new Game(Board.of().with(Player.SOUTH.storeId, 1).build());

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> game.move(Player.SOUTH, Player.SOUTH.storeId))
                .withMessage("Can not move stones from store");
    }
    //southTryToMoveFromStore
}