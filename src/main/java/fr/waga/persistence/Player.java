package fr.waga.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Player {
    private final String name;
    private final List<Game> games;

    public Player(@JsonProperty("name") String name,
                  @JsonProperty("games") List<Game> games) {
        this.name = name;
        this.games = games;
    }

    public String getName() {
        return name;
    }

    public List<Game> getGames() {
        return games;
    }


}
