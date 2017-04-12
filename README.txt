Ritwik Dixit, Shangyu Hsu, Bennett Cotter

Introduction:
Our game is a 2-player (although you could control both players yourself) platformer. It is a multi-level, scrolling game. In each level, 2 players work together to reach the goal. However, to reach the finish, the players must find certain buttons that open gates and reveal the path to the finish. It it is a combination of a collaborative platformer 
and a puzzle game.

Instructions:
WASD: Move Player 1
Up/Down/Right/Left Arrow: Move Player 2
R: Restart Level
ESC: Back to Menu

Classes:
Entity - Anything on the level that can collide
Player extends Entity-represents a player with x,y,img,vx,vy
Obstacle - Anything the player can jump collide with
Gate (extends Obstacle)-horizontal/vertical platforms that can be opened and closed by buttons
Button-binds to certain gates and opens/closes its gates depending on the mode
Level(extends JPanel)-Holds all the objects in a level
LevelLoader - Static utility class parses text files into a running level
LevelScreen- level selection screen JPanel
Game-JFrame that controls which level the player is in, has main method the KeyListener & Game Loop 

Responsibilites:
Ritwik: Text file parsing to contruct levels, Level, Gate, Player, Level Design

Shangyu: Gate, Button, Menu, Game, LevelScreen

Bennett: Game, Entity, Player, Collisions, Obstacle

Everyone contributed to:
Level, Player, Game, Entity

