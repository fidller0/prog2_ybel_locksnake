package de.hsbi.lockgame.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import de.hsbi.lockgame.logic.GameState.Status;
import de.hsbi.lockgame.model.CellType;
import de.hsbi.lockgame.model.Direction;
import de.hsbi.lockgame.model.Level;
import de.hsbi.lockgame.model.Pin;
import de.hsbi.lockgame.model.Position;
import de.hsbi.lockgame.model.Snake;
import java.util.List;
import org.junit.jupiter.api.Test;

class GameStateTest {

    @Test
    void givenRunningStateWithNoDirection_whenTick_thenStateDoesNotChange() {
        GameState state = newState(Direction.NONE);

        GameState result = state.tick();

        assertSame(state, result);
    }

    @Test
    void givenNotRunningState_whenTick_thenStateDoesNotChange() {
        GameState state =
                new GameState(
                        emptyLevel(5, 5, new Position(1, 1), List.of()),
                        new Snake(List.of(new Position(1, 1))),
                        List.of(),
                        Status.WON,
                        Direction.RIGHT);

        GameState result = state.tick();

        assertSame(state, result);
    }

    @Test
    void givenSnakeWithRightDirection_whenTick_thenSnakeMovesRight() {
        GameState state = newState(Direction.RIGHT);

        GameState result = state.tick();

        assertEquals(new Position(2, 1), result.snake().head());
        assertEquals(Status.RUNNING, result.status());
        assertEquals(Direction.NONE, result.pendingDirection());
    }

    @Test
    void givenSnakeMovesOutOfBounds_whenTick_thenGameIsLost() {
        Level level = emptyLevel(2, 2, new Position(1, 0), List.of());
        GameState state =
                new GameState(
                        level,
                        new Snake(List.of(new Position(1, 0))),
                        List.of(),
                        Status.RUNNING,
                        Direction.RIGHT);

        GameState result = state.tick();

        assertEquals(Status.LOST_OUT_OF_BOUNDS, result.status());
    }

    @Test
    void givenWallInFrontOfSnake_whenTick_thenSnakeDoesNotMove() {
        CellType[][] cells = emptyCells(5, 5);
        cells[2][1] = CellType.WALL;

        Level level = new Level(5, 5, cells, List.of(), new Position(1, 1));
        GameState state =
                new GameState(
                        level,
                        new Snake(List.of(new Position(1, 1))),
                        List.of(),
                        Status.RUNNING,
                        Direction.RIGHT);

        GameState result = state.tick();

        assertEquals(new Position(1, 1), result.snake().head());
        assertEquals(Status.RUNNING, result.status());
    }

    @Test
    void givenSnakeMovesIntoItself_whenTick_thenGameIsLost() {
        Level level = emptyLevel(5, 5, new Position(1, 1), List.of());
        Snake snake = new Snake(List.of(new Position(1, 1), new Position(2, 1)));
        GameState state = new GameState(level, snake, List.of(), Status.RUNNING, Direction.RIGHT);

        GameState result = state.tick();

        assertEquals(Status.LOST_SELF_COLLISION, result.status());
    }

    @Test
    void givenPinWithWrongDirection_whenTick_thenPinIsNotActivated() {
        Pin pin = new Pin(new Position(2, 1), Pin.State.LOW, Direction.LEFT);
        Level level = emptyLevel(5, 5, new Position(1, 1), List.of(pin));
        GameState state =
                new GameState(
                        level,
                        new Snake(List.of(new Position(1, 1))),
                        List.of(pin),
                        Status.RUNNING,
                        Direction.RIGHT);

        GameState result = state.tick();

        assertEquals(Pin.State.LOW, result.pins().getFirst().state());
        assertEquals(Status.RUNNING, result.status());
    }

    @Test
    void givenOnePinWithCorrectDirection_whenTick_thenPinIsActivatedAndGameWon() {
        Pin pin = new Pin(new Position(2, 1), Pin.State.LOW, Direction.RIGHT);
        Level level = emptyLevel(5, 5, new Position(1, 1), List.of(pin));
        GameState state =
                new GameState(
                        level,
                        new Snake(List.of(new Position(1, 1))),
                        List.of(pin),
                        Status.RUNNING,
                        Direction.RIGHT);

        GameState result = state.tick();

        assertEquals(Pin.State.HIGH, result.pins().getFirst().state());
        assertEquals(Status.WON, result.status());
    }

    @Test
    void givenTwoPinsAndOnlyOneActivated_whenTick_thenGameKeepsRunning() {
        Pin firstPin = new Pin(new Position(2, 1), Pin.State.LOW, Direction.RIGHT);
        Pin secondPin = new Pin(new Position(4, 4), Pin.State.LOW, Direction.UP);
        Level level = emptyLevel(5, 5, new Position(1, 1), List.of(firstPin, secondPin));
        GameState state =
                new GameState(
                        level,
                        new Snake(List.of(new Position(1, 1))),
                        List.of(firstPin, secondPin),
                        Status.RUNNING,
                        Direction.RIGHT);

        GameState result = state.tick();

        assertEquals(Pin.State.HIGH, result.pins().get(0).state());
        assertEquals(Pin.State.LOW, result.pins().get(1).state());
        assertEquals(Status.RUNNING, result.status());
    }

    @Test
    void givenLastMissingPinIsActivated_whenTick_thenGameIsWon() {
        Pin firstPin = new Pin(new Position(0, 0), Pin.State.HIGH, Direction.RIGHT);
        Pin secondPin = new Pin(new Position(2, 1), Pin.State.LOW, Direction.RIGHT);
        Level level = emptyLevel(5, 5, new Position(1, 1), List.of(firstPin, secondPin));
        GameState state =
                new GameState(
                        level,
                        new Snake(List.of(new Position(1, 1))),
                        List.of(firstPin, secondPin),
                        Status.RUNNING,
                        Direction.RIGHT);

        GameState result = state.tick();

        assertEquals(Status.WON, result.status());
    }

    private static GameState newState(Direction direction) {
        Level level = emptyLevel(5, 5, new Position(1, 1), List.of());
        return new GameState(
                level,
                new Snake(List.of(new Position(1, 1))),
                List.of(),
                Status.RUNNING,
                direction);
    }

    private static Level emptyLevel(int width, int height, Position start, List<Pin> pins) {
        return new Level(width, height, emptyCells(width, height), pins, start);
    }

    private static CellType[][] emptyCells(int width, int height) {
        CellType[][] cells = new CellType[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = CellType.EMPTY;
            }
        }

        return cells;
    }
}
