package com.tpd.twitch.external;

import com.tpd.twitch.external.model.Game;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * GameController handles the function of getting top games and game information.
 * It specifically provides REST mapping for retrieving games from the Twitch API
 */
@RestController
public class GameController {
    private final TwitchService twitchService;
    public GameController(TwitchService twitchService) {
        this.twitchService = twitchService;
    }

    @GetMapping("/game")
    public List<Game> getGames(@RequestParam(value = "game_name", required = false) String gameName) {
        if (gameName == null) {
            return twitchService.getTopGames();
        } else {
            return twitchService.getGames(gameName);
        }
    }
}
