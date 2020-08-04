package fr.waga.persistence;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "Player")
@Table(name = "PLAYER")
//@TypeDefs({@TypeDef(name="jsonb", typeClass = JsonBinaryType.class)})
public class PlayerEntity {

    @Id
    @Column(nullable = false, name = "NAME")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "player_id")
    private List<GameEntity> games = new ArrayList<>();

    public PlayerEntity() {
    }

    public PlayerEntity(String name, List<Game> games) {
        this.name = name;
        this.games = games.stream().map(GameEntity::new).collect(Collectors.toList());
    }

    public PlayerEntity(Player player) {
        this(player.getName(), player.getGames());
    }


    public String getName() {
        return name;
    }

    public List<GameEntity> getGames() {
        return games;
    }

    public Player toPlayer(){
        return new Player(this.name, this.games.stream().map(GameEntity::toGame).collect(Collectors.toList()));
    }
}
