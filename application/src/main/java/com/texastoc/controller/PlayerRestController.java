package com.texastoc.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.texastoc.model.user.Player;
import com.texastoc.service.PlayerService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@SuppressWarnings("unused")
@RestController
public class PlayerRestController {

  private final PlayerService playerService;

  public PlayerRestController(PlayerService playerService) {
    this.playerService = playerService;
  }

  @PostMapping("/api/v2/players")
  public Player createPlayer(@RequestBody Player player) {
    return playerService.create(player);
  }

  @PutMapping("/api/v2/players/{id}")
  public void updatePlayer(@PathVariable("id") int id, @RequestBody @Valid Player player, HttpServletRequest request) {
    if (!request.isUserInRole("ADMIN")) {
      Principal principal = request.getUserPrincipal();
      Player playerThatIsLoggedIn = playerService.getByEmail(principal.getName());
      if (playerThatIsLoggedIn.getId() != id) {
        throw new AccessDeniedException("A player that is not an admin cannot update another player");
      }
    }
    player.setId(id);
    playerService.update(player);
  }

  @GetMapping("/api/v2/players")
  public List<Player> getPlayers() {
    return playerService.get();
  }

  @GetMapping("/api/v2/players/{id}")
  public Player getPlayer(@PathVariable("id") int id) {
    return playerService.get(id);
  }

  @PostMapping(value = "/password/reset", consumes = "application/vnd.texastoc.password-forgot+json")
  public void forgot(@RequestBody Forgot forgot) {
    // TODO send code via email
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  static class Forgot {
    private String email;
  }
}
