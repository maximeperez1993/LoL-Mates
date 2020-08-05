package fr.waga.persistence;

import javax.persistence.*;
import java.util.Objects;


@Entity(name = "Game")
@Table(name = "GAME")
public class GameEntity {

    @Id
    @Column(nullable = false, name = "ID")
    @GeneratedValue
    private long id;

    @Column(nullable = false, name = "GAME_ID")
    private long gameId;

    @Column(name = "IS_ALLY")
    private boolean isAlly;

    @Column(name = "IS_WON")
    private boolean isWon;

    public GameEntity() {
    }

    public GameEntity(Game game) {
        this.gameId = game.getGameId();
        this.isAlly = game.isAlly();
        this.isWon = game.isWon();
    }

    public long getId() {
        return id;
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

    public Game toGame() {
        return new Game(this.gameId, this.isAlly, this.isWon);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameEntity that = (GameEntity) o;
        return gameId == that.gameId &&
                isAlly == that.isAlly &&
                isWon == that.isWon;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, isAlly, isWon);
    }
}
