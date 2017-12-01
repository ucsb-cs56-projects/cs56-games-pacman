cs56-games-pacman
=================

Pac-Man game, starting with code from http://zetcode.com/tutorials/javagamestutorial/pacman/, and improving. Ghosts currently randomly move around the board. Once the player has lost all the lives, the leaderboard will be shown and the player can enter his/her name. The leaderboard will display the top three high scores of all time and for that player.


![Screenshot Of Pacman Main Menu](http://i.imgur.com/TODmNes.png)


Project History
===============
```
(ianvernon) Pac-Man game, starting with code from http://zetcode.com/tutorials/javagamestutorial/pacman/, and improving.
 W14 | bkiefer13 4pm | katfom, dmhartsook
 W15 | mliou 4pm | 2yangk23, jinzhu1993 
 W16 | hannavigil 5pm | ryantse, beserchris
```

**Documentation**
* PacMan.java contains the `main` function and is the JFrame that displays the game.
* Game controls are documented in the help menu of the game.
* Most of the basic game functionality (start game, end game, collision detection, etc.) is in Board.java, which is the main JPanel where the game is displayed.
* The layout of game levels are stored in serialized GridData files located in ``/assets/levels/``.
* Levels are editable by running the PacMan level editor that can be invoked with ``ant run-editor``.

**How to Run**
* For normal running (serialized file saved as "pacmanLeaderboard.ser"): ``ant run``.
* To set the serialized file, use command line arguments: ``ant run-args -Darg0=filename``.
![Image of Project Layout](https://github.com/UCSB-CS56-Projects/cs56-games-pacman/blob/master/Project-Layout.png)

**W16 Final Remarks** <br>
As of W16, there is now a level editor for the game. There is still work to do to allow loading of new levels outside of the five levels contained in ``/assets/levels``. Also, the ghosts still need additional work for movement. Additionally, a substantial rewrite could be undertaken to provide better support for player movement.

**F16 Final Remarks** <br>
As of F16, we have added another AI personality for the pink ghost, added multiple leaderboards for the different game modes, improved pacman's movement, improved versus mode so that it is now playable on laptops, and added "power pills" so that ghosts can now be eaten. We recommend that the easiest way to understand the code is to read the classes to get an idea of how they all interact with each other. Specifially, Board.java, PacPlayer.java, Ghost.java, and Character.java are ones we recommend to read up on.
