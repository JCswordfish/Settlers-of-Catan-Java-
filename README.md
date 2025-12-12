# Settlers of Catan in Java

Rundown of each file's purpose:

## Networking
- `ClientScreen.java` - runs the GUI of each player, as well as inputs/outputs.
- `Client.java`, `Server.java`, `ServerScreen.java`, `ServerThread.java`, `Manager.java` - work together to connect players to the server and manages sending information between them, mainly updates to the situation of the game.

## The Game
- `Game.java` - manages the workings of the game.

- `Player.java` - manages player info.

- `Tile.java`, `TileType.java`, `Trade.java`, `Devcard.java`, `Settlement.java` - Game objects (tiles, trade requests, development cards, settlements)


##Data Structures
- `MyArrayList.java`, `MyDLList.java`, `Node.java`:  Helpful and advanced data structures which optimize game performance.





