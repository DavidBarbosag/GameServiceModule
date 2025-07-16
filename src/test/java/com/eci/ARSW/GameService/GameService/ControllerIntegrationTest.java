package com.eci.ARSW.GameService.GameService;

import com.eci.ARSW.GameService.GameService.model.GameState;
import com.eci.ARSW.GameService.GameService.model.Player;
import com.eci.ARSW.GameService.GameService.model.Position;
import com.eci.ARSW.GameService.GameService.repository.GameStateRepository;
import com.eci.ARSW.GameService.GameService.repository.PlayerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameStateRepository gameStateRepository;

    @TestConfiguration
    static class MockedRepositoriesConfig {

        @Bean
        @Primary
        public PlayerRepository mockPlayerRepository() {
            return mock(PlayerRepository.class);
        }

        @Bean
        @Primary
        public GameStateRepository mockGameStateRepository() {
            return mock(GameStateRepository.class);
        }
    }

    @Test
    public void testSaveAndGetPlayer() throws Exception {
        Player player = new Player(new Position(1, 1), 3);
        player.setSymbol("P");
        player.setMode('T');
        player.setState(true);
        player.setId("mockedId");

        when(playerRepository.save(any(Player.class))).thenReturn(player);
        when(playerRepository.findById("mockedId")).thenReturn(Optional.of(player));

        String playerJson = objectMapper.writeValueAsString(player);

        String content = mockMvc.perform(post("/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(playerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol", is("P")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Player savedPlayer = objectMapper.readValue(content, Player.class);

        mockMvc.perform(get("/players/" + savedPlayer.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol", is("P")));
    }

    @Test
    public void testCreateAndGetGameState() throws Exception {
        String[][] matrix = {{"P1", "M"}, {"F", "T"}};
        GameState state = new GameState(matrix);
        state.setStatus("IN_PROGRESS");
        state.setId("mockedGameId");

        when(gameStateRepository.save(any(GameState.class))).thenReturn(state);
        when(gameStateRepository.findById("mockedGameId")).thenReturn(Optional.of(state));

        String stateJson = objectMapper.writeValueAsString(state);

        String response = mockMvc.perform(post("/api/game")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stateJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        GameState savedState = objectMapper.readValue(response, GameState.class);

        mockMvc.perform(get("/api/game/" + savedState.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("IN_PROGRESS")));
    }
}
