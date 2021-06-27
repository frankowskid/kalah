package org.frank.kalah.domain;

import com.google.common.base.Preconditions;

import java.util.Map;

public class Game {
    private final Board board;
    private Player currentPlayer;

    Game(Board board) {
        this.board = board;
        currentPlayer = Player.SOUTH;
    }

    public Game() {
        board = new Board();
        currentPlayer = Player.SOUTH;
    }

    public Board result() {
        return board;
    }

    public void move(Player player, int houseNo) {
        Preconditions.checkArgument(isGameInProgress(), "Game Over");
        Preconditions.checkArgument(currentPlayer == player, currentPlayer + " player turn");
        Preconditions.checkArgument(currentPlayer == Player.findByHouseId(houseNo),"Cannot move stones from other player houses");
        Preconditions.checkArgument(currentPlayer.storeId != houseNo,"Can not move stones from store");

        int lastHouse = board.moveStones(player, houseNo);
        steelStones(lastHouse);
        nextPlayer(lastHouse);
    }

    int stonesInHouse(int houseNumber) {
        return board.stonesInHouse(houseNumber);
    }

    private boolean isGameInProgress() {
        return !(playerStones(Player.SOUTH) == 0 || playerStones(Player.NORTH) == 0);
    }

    private int playerStones(Player player) {
        int sum = 0;
        for(int i = 1; i < board.housesOnOneSide(); i++) {
            sum += board.stonesInHouse(player.storeId - i);
        }
        return sum;
    }

    private void steelStones(int lastHouse) {
        if(!moveEndedInStore(lastHouse) && oneStoneInHouse(lastHouse)) {
            steel(lastHouse);
        }
    }

    private boolean oneStoneInHouse(int lastHouse) {
        return board.stonesInHouse(lastHouse) == 1;
    }

    private boolean moveEndedInStore(int lastHouse) {
        return currentPlayer.isStore(lastHouse);
    }

    private void steel(int lastHouse) {
        board.steelStones(currentPlayer,lastHouse);
    }

    private void nextPlayer(int lastHouse) {
        if (!moveEndedInStore(lastHouse)) {
            currentPlayer = currentPlayer == Player.SOUTH ? Player.NORTH : Player.SOUTH;
        }
    }

    public void print() {
        System.out.print(board.toString());
    }

    public Map<String,String> boardStatus() {
        return board.boardStatus();
    }
}
