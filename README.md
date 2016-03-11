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

**W16 Final Remarks** <br>
As of W16, there is now a level editor for the game. There is still work to do to allow loading of new levels outside of the five levels contained in ``/assets/levels``. Also, the ghosts still need additional work for movement. Additionally, a substantial rewrite could be undertaken to provide better support for player movement.
