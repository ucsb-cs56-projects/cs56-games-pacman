BW14 Ready! (Ian Vernon)
cs56-games-pacman
=================

Pacman game, starting with code from http://zetcode.com/tutorials/javagamestutorial/pacman/, and improving

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
			W - Move Up
			S - Move Down
			A - Move Left
			D - Move Right
	Co-op:
		Pacman:
			W - Move Up
			S - Move Down
			A - Move Left
			D - Move Right
		Ms. Pacman:
			Up Arrow - Move Up
			Down Arrow - Move Down
			Left Arrow - Move Left
			Right Arrow - Move Right
	Versus:
		Pacman:
			W - Move Up
			S - Move Down
			A - Move Left
			D - Move Right
		Ghost 1:
			Up Arrow - Move Up
			Down Arrow - Move Down
			Left Arrow - Move Left
			Right Arrow - Move Right
		Ghost 2:
			Numpad 8 - Move Up
			Numpad 5 - Move Down
			Numpad 4 - Move Left
			Numpad 6 - Move Right
**How to Run**<br>
For normal running (serialized file saved as "pacmanLeaderboard.ser"): `ant run`<br>
To set the serialized file, use command line arguments: `ant run-args -Darg0=filename`
