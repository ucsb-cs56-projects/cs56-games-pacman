cs56-games-pacman
=================

Pac-Man game, starting with code from http://zetcode.com/tutorials/javagamestutorial/pacman/, and improving. Ghosts currently randomly move around the board. Once the player has lost all the lives, the leaderboard will be shown and the player can enter his/her name. The leaderboard will display the top three high scores of all time and for that player.


![Screenshot Of Pacman Main Menu](http://i.imgur.com/TODmNes.png)

**Documentation**
* PacMan.java contains the `main` function and is the JFrame that displays the game.
* Game controls are documented in the help menu of the game.
* Most of the basic game functionality (start game, end game, collision detection, etc.) is in Board.java, which is the main JPanel where the game is displayed.
* The layout of game levels are stored in serialized GridData files located in ``/assets/levels/``.
* Levels are editable by running the PacMan level editor that can be invoked with ``ant run-editor``.

![Image of Project Layout](https://github.com/UCSB-CS56-Projects/cs56-games-pacman/blob/master/Project-Layout.png)

**How to Run**
* For normal running (serialized file saved as "pacmanLeaderboard.ser"): ``ant run``.
* To set the serialized file, use command line arguments: ``ant run-args -Darg0=filename``.
* To run level editor, use `ant run-editor`.
* Developer mode can be run using `ant run-dev`.


Project History
===============
```
(ianvernon) Pac-Man game, starting with code from http://zetcode.com/tutorials/javagamestutorial/pacman/, and improving.
 W14 | bkiefer13 4pm | katfom, dmhartsook
 W15 | mliou 4pm | 2yangk23, jinzhu1993 
 W16 | hannavigil 5pm | ryantse, beserchris
```

**W16 Final Remarks** <br>
As of W16, there is now a level editor for the game. There is still work to do to allow loading of new levels outside of the five levels contained in ``/assets/levels``. Also, the ghosts still need additional work for movement. Additionally, a substantial rewrite could be undertaken to provide better support for player movement.

**F16 Final Remarks** <br>
As of F16, we have added another AI personality for the pink ghost, added multiple leaderboards for the different game modes, improved pacman's movement, improved versus mode so that it is now playable on laptops, and added "power pills" so that ghosts can now be eaten. We recommend that the easiest way to understand the code is to read the classes to get an idea of how they all interact with each other. Specifially, Board.java, PacPlayer.java, Ghost.java, and Character.java are ones we recommend to read up on.

**F17 Final Remarks** <br>
For F17 we added several features that may be helpful for the next programmers: the dev-tool (accessed with **ant run-dev**) is a tool that allows you, as a developer, to test out many of the game functions such as respawning enemies, going to the next level, making pacman invincible, etc. to aid in testing. Along the same line we have added unit tests and extended the javadoc documentation to almost every method in the project. We have also added a **"GhostHouse"** so that ghosts spawning is controlled in an orderly manner. Some quick tips: start with the javadoc. It includes information on most of the project's various methods and is a great way to get a quick grasp of the projects overall framework. Another resource to check out is the class diagram picture included in the directory that holds details about the various classes and how they relate. On that topic, the classes to pay attention to are **Board.java**, **PacPlayer.java**, **Ghost.java**, and **PacMan.java**. Additionally, something to keep in mind would be ending the game after the player clears level 6. At present, the game just repeats continually, but it might be an interesting feature to add a "win" screen or something to end the game after level 6 is cleared. Last note, although we have already refactored a lot of the codes, a 2nd round of refactoring will be very helpful if possible, to keep the codes clean.
