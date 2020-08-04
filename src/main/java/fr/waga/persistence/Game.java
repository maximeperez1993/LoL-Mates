package fr.waga.persistence;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Game {
    private final long gameId;
    private final boolean isAlly;
    private final boolean isWon;

    public Game(@JsonProperty("gameId") long gameId,
                @JsonProperty("isAlly") boolean isAlly,
                @JsonProperty("isWon") boolean isWon) {
        this.gameId = gameId;
        this.isAlly = isAlly;
        this.isWon = isWon;
    }

    public long getGameId() {
        return gameId;
    }

    public boolean isAlly() {
        return isAlly;
    }

    public boolean isWon() {
        return isWon;
    }

}
