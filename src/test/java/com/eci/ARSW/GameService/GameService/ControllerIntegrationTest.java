package com.eci.ARSW.GameService.GameService;

import com.eci.ARSW.GameService.GameService.model.GameState;
import com.eci.ARSW.GameService.GameService.model.Player;
import com.eci.ARSW.GameService.GameService.model.Position;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test for GameManagerController - initialize game
     */
    @Test
    public void testInitializeGame() throws Exception {
        mockMvc.perform(post("/gameManager/init")
                        .param("rows", "5")
                        .param("cols", "5")
                        .param("globalMines", "10")
                        .param("minesPerPlayer", "3"))
                .andExpect(status().isOk());
    }

    /**
     * Test for GameManagerController - create player
     */
    @Test
    public void testCreatePlayer() throws Exception {
        mockMvc.perform(post("/gameManager/init")
                        .param("rows", "5")
                        .param("cols", "5")
                        .param("globalMines", "10")
                        .param("minesPerPlayer", "3"))
                .andExpect(status().isOk());

        Position pos = new Position(2, 2);

        mockMvc.perform(post("/gameManager/player")
                        .param("mines", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.position.x", is(2)))
                .andExpect(jsonPath("$.position.y", is(2)))
                .andExpect(jsonPath("$.mines", is(2)));
    }

    /**
     * Test for PlayerController - save and get player
     */
    @Test
    public void testSaveAndGetPlayer() throws Exception {
        Player player = new Player(new Position(1, 1), 3);
        player.setSymbol("P");
        player.setMode('T');
        player.setState(true);

        String playerJson = objectMapper.writeValueAsString(player);

        // Save
        String content = mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(playerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol", is("P")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract ID
        Player savedPlayer = objectMapper.readValue(content, Player.class);

        // Get by ID
        mockMvc.perform(get("/players/" + savedPlayer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol", is("P")));
    }

    /**
     * Test for GameStateController - save and get game state
     */
    @Test
    public void testCreateAndGetGameState() throws Exception {
        String[][] matrix = {{"P1", "M"}, {"F", "T"}};
        GameState state = new GameState(matrix);
        state.setStatus("IN_PROGRESS");

        String stateJson = objectMapper.writeValueAsString(state);

        // Save
        String response = mockMvc.perform(post("/api/game")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stateJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        GameState savedState = objectMapper.readValue(response, GameState.class);

        // Get by ID
        mockMvc.perform(get("/api/game/" + savedState.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")));
    }
}
