\# UML-Klassendiagramm LockSnake



```mermaid

classDiagram

&#x20;   class Main {

&#x20;       +main(String\[] args)

&#x20;       -handleGameEnd(ActionEvent, GameState, JFrame)

&#x20;   }



&#x20;   class GameEngine {

&#x20;       -GameState state

&#x20;       -GamePanel gamePanel

&#x20;       +GameEngine(Level level)

&#x20;       +state() GameState

&#x20;       +setGamePanel(GamePanel panel)

&#x20;       +update(Direction direction)

&#x20;       +tick()

&#x20;   }



&#x20;   class GameState {

&#x20;       -Level level

&#x20;       -Snake snake

&#x20;       -List\~Pin\~ pins

&#x20;       -Status status

&#x20;       -Direction pendingDirection

&#x20;       +tick() GameState

&#x20;       +level() Level

&#x20;       +snake() Snake

&#x20;       +pins() List\~Pin\~

&#x20;       +status() Status

&#x20;       +pendingDirection() Direction

&#x20;   }



&#x20;   class GamePanel {

&#x20;       -GameState state

&#x20;       -GameRenderer renderer

&#x20;       -GameEngine gameEngine

&#x20;       +update(GameState newState)

&#x20;       +setGameEngine(GameEngine engine)

&#x20;   }



&#x20;   class Level {

&#x20;       -int width

&#x20;       -int height

&#x20;       -CellType\[]\[] cells

&#x20;       -List\~Pin\~ pins

&#x20;       -Position snakeStart

&#x20;   }



&#x20;   class Snake {

&#x20;       -List\~Position\~ body

&#x20;       +head() Position

&#x20;       +grow(Direction d) Snake

&#x20;       +occupies(Position position) boolean

&#x20;   }



&#x20;   class Pin {

&#x20;       -Position position

&#x20;       -State state

&#x20;       -Direction activationDirection

&#x20;       +withState(State newState) Pin

&#x20;   }



&#x20;   class Position {

&#x20;       -int x

&#x20;       -int y

&#x20;   }



&#x20;   class Direction {

&#x20;       <<enum>>

&#x20;       UP

&#x20;       DOWN

&#x20;       LEFT

&#x20;       RIGHT

&#x20;       NONE

&#x20;   }



&#x20;   class CellType {

&#x20;       <<enum>>

&#x20;       EMPTY

&#x20;       WALL

&#x20;       PIN\_SLOT

&#x20;   }



&#x20;   Main --> GameEngine

&#x20;   Main --> GamePanel

&#x20;   GameEngine --> GameState

&#x20;   GameEngine --> GamePanel

&#x20;   GamePanel --> GameState

&#x20;   GameState --> Level

&#x20;   GameState --> Snake

&#x20;   GameState --> Pin

&#x20;   Snake --> Position

&#x20;   Pin --> Position

&#x20;   Pin --> Direction

&#x20;   Level --> CellType

&#x20;   Level --> Pin

&#x20;   Level --> Position

