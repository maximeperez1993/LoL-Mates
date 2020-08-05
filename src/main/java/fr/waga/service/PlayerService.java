package fr.waga.service;

import fr.waga.persistence.Player;
import fr.waga.persistence.PlayerEntity;
import fr.waga.persistence.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class PlayerService {

    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional
    @Consumes("application/json")
    @PostMapping(value = "/player", consumes = "application/json")
    public void players(@RequestBody Player player) {
        Optional<PlayerEntity> playerEntityOptional = this.playerRepository.findById(player.getName());
        if (playerEntityOptional.isPresent()) {
            PlayerEntity playerEntity = playerEntityOptional.get();
            playerEntity.addGames(player.getGames());
            this.playerRepository.save(playerEntity);
        } else {
            this.playerRepository.save(new PlayerEntity(player));
        }
    }

    @Transactional
    @Consumes("application/json")
    @PostMapping(value = "/players", consumes = "application/json")
    public void players(@RequestBody List<Player> players) {
        this.playerRepository.saveAll(players.stream().map(PlayerEntity::new).collect(Collectors.toList()));
    }

    @Transactional
    @GetMapping(value = "/players")
    public List<Player> players() {
        return this.playerRepository.findAll().stream()
                .map(PlayerEntity::toPlayer)
                .collect(Collectors.toList());
    }
}
