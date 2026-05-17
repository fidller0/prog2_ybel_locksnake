package de.hsbi.lockgame.logic;

import de.hsbi.lockgame.model.*;
import java.util.List;

public final class GameState {
    private final Level level;
    private final Snake snake;
    private final List<Pin> pins;
    private final Status status;
    private final Direction pendingDirection;

    public GameState(
            Level level, Snake snake, List<Pin> pins, Status status, Direction pendingDirection) {
        this.level = level;
        this.snake = snake;
        this.pins = List.copyOf(pins);
        this.status = status;
        this.pendingDirection = pendingDirection;
        // TODO: lege einen neuen GameState mit den übergebenen Informationen an

    }

    public Level level() {
        // TODO: Getter
        return level;
    }

    public Snake snake() {
        // TODO: Getter
        return snake;
    }

    public List<Pin> pins() {
        // TODO: Getter
        return pins;
    }

    public Status status() {
        // TODO: Getter
        return status;
    }

    public Direction pendingDirection() {
        // TODO: Getter
        return pendingDirection;
    }

    public GameState tick() {
        if (!status.isRunning() || pendingDirection == Direction.NONE) {
            return this;
        }

        Position nextHead = snake.nextHead(pendingDirection);

        if (!level.isInside(nextHead)) {
            return new GameState(level, snake, pins, Status.LOST_OUT_OF_BOUNDS, Direction.NONE);
        }

        if (snake.occupies(nextHead)) {
            return new GameState(level, snake, pins, Status.LOST_SELF_COLLISION, Direction.NONE);
        }

        if (level.cellAt(nextHead) == CellType.WALL) {
            return new GameState(level, snake, pins, status, Direction.NONE);
        }

        Pin pin = pinAt(nextHead);

        if (pin != null) {
            if (pin.state().isSet()) {
                return new GameState(level, snake, pins, status, Direction.NONE);
            }

            if (pin.activationDirection() != pendingDirection) {
                return new GameState(level, snake, pins, status, Direction.NONE);
            }

            List<Pin> newPins =
                    pins.stream().map(p -> p == pin ? p.withState(Pin.State.HIGH) : p).toList();

            Status newStatus =
                    newPins.stream().allMatch(p -> p.state().isSet()) ? Status.WON : Status.RUNNING;

            return new GameState(level, snake, newPins, newStatus, Direction.NONE);
        }

        Snake newSnake = snake.grow(pendingDirection);

        return new GameState(level, newSnake, pins, status, Direction.NONE);
    }

    private Pin pinAt(Position position) {
        return pins.stream()
                .filter(
                        pin ->
                                pin.position().x() == position.x()
                                        && pin.position().y() == position.y())
                .findFirst()
                .orElse(null);
    }

    public enum Status {
        RUNNING,
        WON,
        LOST_SELF_COLLISION,
        LOST_OUT_OF_BOUNDS;

        public boolean isRunning() {
            return this == RUNNING;
        }
    }
}
