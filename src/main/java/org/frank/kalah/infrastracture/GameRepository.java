package org.frank.kalah.infrastracture;

import org.frank.kalah.domain.Game;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
class GameRepository {

    private final Map<Integer, Game> games = new HashMap<>();
    private int gameIdGenerator = 1;

    public Optional<Game> findGame(Integer gameId){
        return Optional.ofNullable(games.get(gameId));
    }

    public Integer newGame() {
        Integer gameId = newGameId();
        games.put(gameId, new Game());
        return gameId;
    }

    private Integer newGameId() {
        return gameIdGenerator++;
    }
}
