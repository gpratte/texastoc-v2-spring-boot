package com.texastoc.cucumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.texastoc.model.game.Game;
import com.texastoc.model.season.QuarterlySeason;
import com.texastoc.model.season.Season;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.junit.Ignore;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;

@Ignore
public class GameStepdefs extends SpringBootBaseIntegrationTest {

    private Game gameToCreate;
    private Game gameCreated;
    private HttpClientErrorException exception;

    @Given("^the game starts now$")
    public void the_game_starts_now() throws Exception {
        // Arrange
        createSeason();

        gameToCreate = Game.builder()
            .date(LocalDate.now())
            .hostId(1)
            .doubleBuyIn(false)
            .transportRequired(false)
            .build();
    }

    @Given("^the double buy in game starts now$")
    public void the_double_buy_in_game_starts_now() throws Exception {
        // Arrange
        createSeason();

        gameToCreate = Game.builder()
            .date(LocalDate.now())
            .hostId(1)
            .doubleBuyIn(true)
            .transportRequired(false)
            .build();
    }

    @Given("^the game supplies need to be moved$")
    public void the_game_supplies_need_to_be_moved() throws Exception {
        // Arrange
        createSeason();

        gameToCreate = Game.builder()
            .date(LocalDate.now())
            .hostId(1)
            .doubleBuyIn(false)
            .transportRequired(true)
            .build();
    }

    @When("^the game is created$")
    public void the_game_is_created() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String gameToCreateAsJson = mapper.writeValueAsString(gameToCreate);
        HttpEntity<String> entity = new HttpEntity<>(gameToCreateAsJson ,headers);
        System.out.println(gameToCreateAsJson);

        gameCreated = restTemplate.postForObject(endpoint() + "/games", entity, Game.class);
    }

    @Then("^the game is normal$")
    public void the_game_is_normal() throws Exception {
        Assert.assertNotNull("game create should not be null", gameCreated);
        Assert.assertTrue("game season id should be greater than 0", gameCreated.getSeasonId() > 0);
        Assert.assertTrue("game quarterly season id should be greater than 0", gameCreated.getQSeasonId() > 0);
        Assert.assertEquals("game quarter should be 1", 1, gameCreated.getQuarter().getValue());

        Assert.assertEquals("game host id should be " + BRIAN_BAKER_PLAYER_ID, BRIAN_BAKER_PLAYER_ID, (int)gameCreated.getHostId());
        Assert.assertEquals("game host name should be " + BRIAN_BAKER_NAME, BRIAN_BAKER_NAME, gameCreated.getHostName());

        // Game setup variables
        Assert.assertFalse("double buy in should be false", gameCreated.getDoubleBuyIn());
        Assert.assertFalse("transport required should be false", gameCreated.getTransportRequired());
        Assert.assertEquals("kitty cost should come from season", KITTY_PER_GAME, (int)gameCreated.getKittyCost());
        Assert.assertEquals("buy in cost should come from season", GAME_BUY_IN, (int)gameCreated.getBuyInCost());
        Assert.assertEquals("re buy cost should come from season", GAME_REBUY, (int)gameCreated.getRebuyAddOnCost());
        Assert.assertEquals("re buy toc debit cost should come from season", GAME_REBUY_TOC_DEBIT, (int)gameCreated.getRebuyAddOnTocDebit());
        Assert.assertEquals("toc cost should come from season", TOC_PER_GAME, (int)gameCreated.getAnnualTocCost());
        Assert.assertEquals("quarterly toc cost should come from season", QUARTERLY_TOC_PER_GAME, (int)gameCreated.getQuarterlyTocCost());

        // Game time variables
        Assert.assertEquals("game number players should be zero", 0, (int)gameCreated.getNumPlayers());
        Assert.assertEquals("game kitty collected should be zero", 0, (int)gameCreated.getKittyCollected());
        Assert.assertEquals("game buy in should be zero", 0, (int)gameCreated.getBuyInCollected());
        Assert.assertEquals("game rebuy should be zero", 0, (int)gameCreated.getRebuyAddOnCollected());
        Assert.assertEquals("game annual toc collected should be zero", 0, (int)gameCreated.getAnnualTocCollected());
        Assert.assertEquals("game quarterly toc collected should be zero", 0, (int)gameCreated.getQuarterlyTocCollected());
        Assert.assertFalse("not finalized", gameCreated.getFinalized());
        Assert.assertNull("last calculated should be null", gameCreated.getLastCalculated());
        Assert.assertNull("started should be null", gameCreated.getStarted());
    }

    @Then("^the game is double buy in$")
    public void the_game_is_double_buy_in() throws Exception {
        Assert.assertNotNull("game create should not be null", gameCreated);

        // Game setup variables
        Assert.assertTrue("double buy in should be true", gameCreated.getDoubleBuyIn());
        Assert.assertFalse("transport required should be false", gameCreated.getTransportRequired());
        Assert.assertEquals("buy in cost should come from season", GAME_DOUBLE_BUY_IN, (int)gameCreated.getBuyInCost());
        Assert.assertEquals("re buy cost should come from season", GAME_DOUBLE_REBUY, (int)gameCreated.getRebuyAddOnCost());
        Assert.assertEquals("re buy toc debit cost should come from season", GAME_DOUBLE_REBUY_TOC_DEBIT, (int)gameCreated.getRebuyAddOnTocDebit());
    }

    @Then("^the game transport supplies flag is set$")
    public void the_game_transport_supplies_flag_is_set() throws Exception {
        Assert.assertNotNull("game create should not be null", gameCreated);

        // Game setup variables
        Assert.assertFalse("double buy in should be false", gameCreated.getDoubleBuyIn());
        Assert.assertTrue("transport required should be true", gameCreated.getTransportRequired());
    }

}
