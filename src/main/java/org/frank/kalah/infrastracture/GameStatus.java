package org.frank.kalah.infrastracture;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import org.frank.kalah.infrastracture.json.GameStatusView;
import org.frank.kalah.infrastracture.json.NewGameView;

import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class GameStatus {

    @JsonView(value = {NewGameView.class, GameStatusView.class})
    private final String id;
    @JsonView(value = {NewGameView.class, GameStatusView.class})
    private final String uri;
    @JsonView(value = {GameStatusView.class})
    private final Map<String,String> status;


    @JsonCreator
    public GameStatus(@JsonProperty("id") Integer gameId, String uri, Map<String,String> status) {
        this.id = gameId.toString();
        this.uri = uri;
        this.status = status;
    }
    public GameStatus(Integer gameId, String uri) {
        this.id = gameId.toString();
        this.uri = uri;
        this.status = null;
    }

    public String getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public Map<String,String> getStatus() {
        return status;
    }
}
