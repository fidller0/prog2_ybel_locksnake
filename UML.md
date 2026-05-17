# UML-Klassendiagramm LockSnake

```mermaid
classDiagram
    class Main {
        +main()
        -handleGameEnd()
    }

    class GameEngine {
        -state
        -gamePanel
        +GameEngine()
        +state()
        +setGamePanel()
        +update()
        +tick()
    }

    class GameState {
        -level
        -snake
        -pins
        -status
        -pendingDirection
        +tick()
        +level()
        +snake()
        +pins()
        +status()
        +pendingDirection()
    }

    class GamePanel {
        -state
        -renderer
        -gameEngine
        +update()
        +setGameEngine()
    }

    class Level {
        -width
        -height
        -cells
        -pins
        -snakeStart
    }

    class Snake {
        -body
        +head()
        +grow()
        +occupies()
    }

    class Pin {
        -position
        -state
        -activationDirection
        +withState()
    }

    class Position {
        -x
        -y
    }

    class Direction {
        UP
        DOWN
        LEFT
        RIGHT
        NONE
    }

    class CellType {
        EMPTY
        WALL
        PIN_SLOT
    }

    Main --> GameEngine
    Main --> GamePanel
    GameEngine --> GameState
    GameEngine --> GamePanel
    GamePanel --> GameState
    GameState --> Level
    GameState --> Snake
    GameState --> Pin
    Snake --> Position
    Pin --> Position
    Pin --> Direction
    Level --> CellType
    Level --> Pin
    Level --> Position
```

## Kurze Analyse

`Main` startet das Spiel und verbindet `GameEngine` mit `GamePanel`.

`GameEngine` verwaltet den aktuellen Spielzustand. Sie reagiert auf Tastatureingaben und führt bei jedem Timer-Schritt einen neuen Spielschritt aus.

`GameState` enthält die eigentliche Spiellogik. Dort wird geprüft, ob die Schlange sich bewegt, gegen eine Wand läuft, aus dem Spielfeld fällt, sich selbst trifft oder einen Pin aktiviert.

`GamePanel` zeigt den Spielzustand an. Wenn sich der Zustand ändert, bekommt das Panel den neuen `GameState` und zeichnet das Spielfeld neu.

Die Modellklassen `Level`, `Snake`, `Pin`, `Position`, `Direction` und `CellType` beschreiben die Daten des Spiels.
