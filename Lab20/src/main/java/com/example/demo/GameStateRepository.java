package com.example.demo;
import org.springframework.stereotype.Repository;

@Repository
public class GameStateRepository {
    private GameState gameState;

    public GameState getGameState() {
        if (gameState == null) {
            gameState = new GameState();
        }
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
