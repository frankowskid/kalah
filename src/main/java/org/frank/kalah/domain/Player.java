package org.frank.kalah.domain;

import com.google.common.base.Preconditions;

public enum Player implements NextHouse {
    SOUTH(7, 14) {
        public int nextHouseAfter(int houseId) {
            Preconditions.checkArgument(validHouseId(houseId), "No such house Id: " + houseId);
            return (houseId + 1 == opponentStoreId ? houseId + 2 : houseId + 1) % Board.NO_OF_HOUSES;
        }
    },
    NORTH(14, 7) {
        public int nextHouseAfter(int houseId) {
            Preconditions.checkArgument(validHouseId(houseId), "No such house Id: " + houseId);
            int candidate = (houseId + 1 == opponentStoreId ? houseId + 2 : houseId + 1) % Board.NO_OF_HOUSES;
            return candidate == 0 ? Board.NO_OF_HOUSES : candidate;
        }
    };

    private static boolean validHouseId(int houseId) {
        return houseId <= Board.NO_OF_HOUSES && houseId > 0;
    }

    final int opponentStoreId;
    final int storeId;

    Player(int storeId, int opponentStoreId) {
        this.storeId = storeId;
        this.opponentStoreId = opponentStoreId;
    }

    public static Player findByHouseId(int houseId) {
        if( houseId > 0 && houseId <= Player.SOUTH.storeId) {
            return SOUTH;
        } else if (houseId > Player.SOUTH.storeId && houseId <= Player.NORTH.storeId ) {
            return NORTH;
        } else {
            throw new IllegalArgumentException("No such house: " + houseId);
        }
    }

    public boolean isStore(int houseNumber) {
        return houseNumber == storeId;
    }
}
