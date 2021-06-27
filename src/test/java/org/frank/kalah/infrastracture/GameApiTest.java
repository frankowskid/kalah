package org.frank.kalah.infrastracture;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.net.URI;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"management.port=0"})
public class GameApiTest {

    private static final int NOT_EXISTING_GAME_ID = Integer.MAX_VALUE;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void shouldCreateNewGameAndReturn201() {
        ResponseEntity<Map> entity = testRestTemplate.postForEntity(
                "http://localhost:" + this.port + "/games", headers(), Map.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(entity.getBody()).isNotNull();
        assertThat(entity.getBody().get("id")).isNotNull();
        assertThat(entity.getBody().get("uri")).isNotNull();
        assertThatNoException().isThrownBy(() -> new URI((String) entity.getBody().get("uri")));

        assertThat(entity.getBody()).doesNotContainKey("status");
    }

    private HttpEntity<Void> headers() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(null, headers);
    }

    @Test
    void errorItGameNotExists() {
        int pitId = 1;

        ResponseEntity<String> entity = testRestTemplate.exchange(
                "http://localhost:" + this.port + "/games/" + NOT_EXISTING_GAME_ID + "/pits/" + pitId, HttpMethod.PUT, headers(), String.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(entity.getBody()).isEqualTo("Game does not exist");
    }

    @Test
    void createGameAndMakesMove() {
        int pitId = 2;
        GameStatus game = createGame();
        String gameId = game.getId();

        ResponseEntity<Map> entity = testRestTemplate.exchange(
                game.getUri() + "/pits/" + pitId, HttpMethod.PUT, headers(), Map.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody().get("id")).isEqualTo(gameId);
        assertThat(entity.getBody().get("uri")).isNotNull();
        assertThatNoException().isThrownBy(() -> new URI((String) entity.getBody().get("uri")));
        assertThat(entity.getBody().get("status")).isNotNull();

        ResponseEntity<GameStatus> move2 = testRestTemplate.exchange(
                game.getUri() + "/pits/" + 9, HttpMethod.PUT, headers(), GameStatus.class);
        assertThat(move2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(move2.getBody().getId()).isEqualTo(gameId);
        assertThat(move2.getBody().getUri()).isEqualTo(game.getUri());

        assertThat(move2.getBody().getStatus().get("7")).isEqualTo("1");
        assertThat(move2.getBody().getStatus().get("1")).isEqualTo("7");
        assertThat(move2.getBody().getStatus().get("14")).isEqualTo("1");

    }

    private GameStatus createGame() {
        ResponseEntity<GameStatus> entity = testRestTemplate.postForEntity(
                "http://localhost:" + this.port + "/games", headers(), GameStatus.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        return entity.getBody();
    }

}