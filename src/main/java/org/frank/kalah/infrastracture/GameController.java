package org.frank.kalah.infrastracture;

import com.fasterxml.jackson.annotation.JsonView;
import org.frank.kalah.domain.Player;
import org.frank.kalah.infrastracture.json.GameStatusView;
import org.frank.kalah.infrastracture.json.NewGameView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@Validated
public class GameController {

    private final GameRepository gameRepository;

    @Autowired
    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @PostMapping(value = "/games", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @JsonView(NewGameView.class)
    public GameStatus creteGame() {
        Integer gameId = gameRepository.newGame();
        return new GameStatus(gameId, generateUrl(gameId));
    }

    private String generateUrl(Integer gameId) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/games/" + gameId).build().toUriString();
    }

    @PutMapping(value = "/games/{gameId}/pits/{pitId}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @JsonView(GameStatusView.class)
    public GameStatus creteGame(@Valid @PathVariable @Positive Integer gameId, @PathVariable @Positive Integer pitId) {
        return gameRepository.findGame(gameId)
                .map(game -> {
                    game.move( Player.findByHouseId(pitId), pitId);
                    return new GameStatus(gameId, generateUrl(gameId), game.boardStatus());
                })
                .orElseThrow(
                        () -> new IllegalArgumentException("Game does not exist"));
    }
}
