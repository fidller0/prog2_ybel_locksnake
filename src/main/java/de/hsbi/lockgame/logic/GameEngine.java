package de.hsbi.lockgame.logic;

import de.hsbi.lockgame.logic.GameState.Status;
import de.hsbi.lockgame.model.Direction;
import de.hsbi.lockgame.model.Level;
import de.hsbi.lockgame.model.Snake;
import de.hsbi.lockgame.ui.GamePanel;
import java.util.List;

public final class GameEngine {
    private GameState state;
    private GamePanel gamePanel;

    public GameEngine(Level level) {
        this.state =
                new GameState(
                        level,
                        new Snake(List.of(level.snakeStart())),
                        level.pins(),
                        Status.RUNNING,
                        Direction.NONE);
    }

    public GameState state() {
        return state;
    }

    public void setGamePanel(GamePanel panel) {
        this.gamePanel = panel;
        notifyGamePanel();
    }

    public void update(Direction direction) {
        if (direction == null) {
            return;
        }

        this.state =
                new GameState(
                        state.level(), state.snake(), state.pins(), state.status(), direction);

        notifyGamePanel();
    }

    public void tick() {
        this.state = state.tick();
        notifyGamePanel();
    }

    private void notifyGamePanel() {
        if (gamePanel != null) {
            gamePanel.update(state);
        }
    }
}
