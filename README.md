BW14 Ready! (Ian Vernon)
cs56-games-pacman
=================

Pacman game, starting with code from http://zetcode.com/tutorials/javagamestutorial/pacman/, and improving. Ghosts currently randomly move around the board. Once the player has lost all the lives, the leaderboard will be shown and the player can enter his/her name. The leaderboard will display the top three high scores of all time and for that player.


![ScreenShot Of Pacman Main Menu](http://i.imgur.com/7vp2yM8.png)

_____________________
CONTROLS

	Title Screen:
		S - Start Single Player
		D - Start Co-op
		F - Start Versus
	While In-Game:
		Esc - Return to title screen
		P - Pause/Unpause the game
	Single Player:
		Pacman:
			Up Arrow - Move Up
			Down Arrow - Move Down
			Left Arrow - Move Left
			Right Arrow - Move Right
	Co-op:
		Pacman:
			Up Arrow - Move Up
			Down Arrow - Move Down
			Left Arrow - Move Left
			Right Arrow - Move Right
	
		Ms. Pacman:
			W - Move Up
			S - Move Down
			A - Move Left
			D - Move Right

	Versus:
		Pacman:
			Up Arrow - Move Up
			Down Arrow - Move Down
			Left Arrow - Move Left
			Right Arrow - Move Right

		Ghost 1:
			W - Move Up
			S - Move Down
			A - Move Left
			D - Move Right
		Ghost 2:
			Numpad 8 - Move Up
			Numpad 5 - Move Down
			Numpad 4 - Move Left
			Numpad 6 - Move Right
			
**Documentation**
* Pacman.java contains the `main` function and is the JFrame that displays the game
* The layout of each level is determined in Grid.java with arrays, where each number represents an element of the board. For example, level 1:
```java
 final short leveldata1[] =
    { 19, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
      21, 0,  0,  0,  17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
      21, 0,  0,  0,  17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 
      21, 0,  0,  0,  17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20, 
      17, 18, 18, 18, 16, 16, 20, 0,  17, 16, 16, 16, 16, 16, 20,
      17, 16, 16, 16, 16, 16, 20, 0,  17, 16, 16, 16, 16, 24, 20, 
      25, 16, 16, 16, 24, 24, 28, 0,  25, 24, 24, 16, 20, 0,  21, 
      1,  17, 16, 20, 0,  0,  0,  0,  0,  0,  0,  17, 20, 0,  21,
      1,  17, 16, 16, 18, 18, 22, 0,  19, 18, 18, 16, 20, 0,  21,
      1,  17, 16, 16, 16, 16, 20, 0,  17, 16, 16, 16, 20, 0,  21, 
      1,  17, 16, 16, 16, 16, 20, 0,  17, 16, 16, 16, 20, 0,  21,
      1,  17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20, 0,  21,
      1,  17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  21,
      1,  25, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 18, 20,
      9,  8,  8,  8,  8,  8,  8,  8,  8,  8,  25, 24, 24, 24, 28 };
```
* Most of the basic game functionality (start game, end game, detect collision, etc.) is in Board.java, which is the main JPanel where the game is displayed

			
**How to Run**<br>
For normal running (serialized file saved as "pacmanLeaderboard.ser"): `ant run`<br>
To set the serialized file, use command line arguments: `ant run-args -Darg0=filename`
