package org.frank.kalah.domain;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;


//           <--- North
// ------------------------
//         13  12  11  10   9   8
//
//         14                   7
//
//         1   2   3   4   5   6
//         ------------------------
//    South --->
public class Board {

    public static final int NO_OF_HOUSES = 14;
    private static final int STONES_IN_HOUSE = 6;
    private static final int SOUTH_STORE_POSITION = 7;
    private static final int NORTH_STORE_POSITION = 14;

    private final int[] board;

    Board() {
        Preconditions.checkArgument(NO_OF_HOUSES > 2, "Must have. more that 2 houses, but has " + NO_OF_HOUSES);
        Preconditions.checkArgument(NO_OF_HOUSES % 2 == 0, "Total number of houses must be even, but is " + NO_OF_HOUSES);
        this.board = new int[NO_OF_HOUSES];
        initBoard(STONES_IN_HOUSE);
    }

    public static Builder of() {
        return new Builder();
    }

    private void initBoard(int stonesInHouse) {
        for (int i = 0; i < NO_OF_HOUSES; i++) {
            this.board[i] = stonesInHouse;
        }
        clearStores();
    }

    private void clearStores() {
        clearSouthStore();
        clearNorthStore();
    }

    private void clearNorthStore() {
        this.board[NORTH_STORE_POSITION - 1] = 0;
    }

    private void clearSouthStore() {
        this.board[SOUTH_STORE_POSITION - 1] = 0;
    }

    public int moveStones(Player player, int fromHouse) {
        checkIfValidMove(fromHouse);

        int stonesToAdd = pickStonesFrom(fromHouse);
        return addStones(player, fromHouse, stonesToAdd);
    }

    private int addStones(Player player, int fromHouse, int stonesToAdd) {
        int addToHouse = fromHouse;

        while (stonesToAdd-- > 0) {
            addToHouse = player.nextHouseAfter(addToHouse);
            board[toIndex(addToHouse)] += 1;
        }

        return addToHouse;
    }

    private int pickStonesFrom(int i) {
        int stonesInHouse = board[toIndex(i)];
        board[toIndex(i)] = 0;
        return stonesInHouse;
    }

    private void checkIfValidMove(int i) {
        Preconditions.checkArgument(board[toIndex(i)] != 0, "Cannot move from empty house");
    }

    private int toIndex(int houseNo) {
        return houseNo - 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board that = (Board) o;
        return Arrays.equals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(board);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        printNorthSide(b);
        printStores(b);
        printSouthSide(b);
        return b.toString();
    }

    private void printNorthSide(StringBuilder b) {
        for (int i = toIndex(NO_OF_HOUSES) - 1; i > housesOnOneSide(); i--) {
            b.append(board[i]).append(',');
        }
        b.deleteCharAt(b.length() - 1);
        b.append("\n");
    }

    private void printStores(StringBuilder b) {
        b.append(board[toIndex(NORTH_STORE_POSITION)])
                .append("  ".repeat(housesOnOneSide() - 2))
                .append(" ")
                .append(board[toIndex(SOUTH_STORE_POSITION)]);
        b.append("\n");
    }

    private void printSouthSide(StringBuilder b) {
        for (int i = toIndex(1); i < housesOnOneSide(); i++) {
            b.append(board[i]).append(',');
        }
        b.deleteCharAt(b.length() - 1);
        b.append("\n");
    }

    public int housesOnOneSide() {
        return (NO_OF_HOUSES - 2) >> 1;
    }

    public int stonesInHouse(int houseNumber) {
        return board[toIndex(houseNumber)];
    }

    public void steelStones(Player player, int lastHouse) {
        board[toIndex(player.storeId)] += pickStonesFrom(lastHouse);
        board[toIndex(player.storeId)] += pickStonesFrom(oppositeHouse(lastHouse));
    }

    private int oppositeHouse(int lastHouse) {
        return NO_OF_HOUSES - lastHouse;
    }

    public Map<String, String> boardStatus() {
        Map<String, String> status = new LinkedHashMap<>();
        for (int i = 0; i < NO_OF_HOUSES; i++) {
            status.put(String.valueOf(i+1), String.valueOf(board[i]));
        }
        return status;
    }

    static class Builder {

        private final Board result;

        Builder() {
            result = new Board();
        }

        Board build() {
            return result;
        }

        public Builder with(int houseNo, int stones) {
            result.board[houseNo - 1] = stones;
            return this;
        }
    }
}
