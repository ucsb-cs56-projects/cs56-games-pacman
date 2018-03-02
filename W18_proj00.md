### Name: 
#### Areg Nersisyan, github:aregluss
#### Vicki Chen, github: vickiic

a. This is a remake of the Pac-Man game in which the user avoids "ghosts" and moves around to eat pellets and gain points.

b. 
* As a single user, I can control pacman to move around the maze to eat pellets and avoid the ghosts. 
* As two players, I can control Pacman or Mrs.Pacman and play in either co-op or vs. mode with another user who can be either Pacman,Mrs.Pacman or a ghost. 
* As three users, one user must be Pacman and the other two can choose between ghost or pacman and play in vs. or co-op mode 

c. 
* The software runs smoothly upon start. The user is presented with a menu and options to choose game mode by corresponding key.
* During gameplay, the player can move Pac-man with up-down-left-right arrow keys. A score is also used to keep track of the points each player can receive

d. As of now, the game can only be played if the user has all the necessary files. 
* As a developer, we could make this into a web app and that way anyone with the URL could go and play it. This will definitely increase the number of people who would play it. 
* As a player, I should be able to play is a 2 player vs. mode since the current versus mode is meant to be played by three players(2 ghosts and 1 pacman)
* As a Ghost player,  I should be able to control one ghost and uses a different key in order to change the behavior of the 2nd ghost, which is controlled by the AI. 
* As a developer, changing the size of the UI would also improve the player's experience with the game and is worth spending the time to add customization to the UI.


 e. 
* The readme.md file is in excellent state. It contains a diagram/chart which explained different game modes/classes.
* If new classes are added, the diagram can be updated to keep the relationship between all the classes clear for future contributors


f. The build.xml is in a good state. All the targets are clear and contain descriptions.
* One thing that could be fixed in the build.xml is giving it a path to access javadocs through a docs folder

g. Yes, there are currently enough issues to earn 1000 points. The issues have detailed explainations of the problem and expectations of fixed solutions.

h. 
* One issue involves correcting the timing of the pacman and ghost when it disappears off screen and enters through the other side.
* Another issue is to add attributes to make the ghost characters run away from pac-man once it gets bigger. Theres also a slight lag while the user is trying to turn in the maze.

i. The current code is very well organized. The diagram presents the relationships between the classes clearly. The methods within these files are also clear. There are three .java files that make up the main classes and contain subclasses in each:
* Pacman.java: Driver file which contains most of the gameplay and creates the PacMan object that sets up the game. This class contains the main function and is the JFrame that displays the game
* Board.java: Contains all of the logic used during gameplay. Attributes such as movements of different objects are located in this file as well as music. This class extends Panel and gets added to the PacMan JFrame as a component. This class draws data from Grid.java which provides the map layout along with methods to load level data and spawn in random fruits. Grid.java obtains data from GridData.java which includes information of grid size and other grid data. This class also receives data from Ghost.java and PacPlayer.java which are both actors in the game. These files contain methods on movements of Ghost and Pacman characters. The board class also obtains information from a audio class which allows sound effects to take place during gameplay.
* LeaderBoardGUI.java: contains implementation of the leaderboard/user interface. Shows score at the end of the game and the points a player received.  Draws information from Leaderboard.java which provides the logic behind the leaderboard and game info from Gameplayed.java

j. There are currently several tests which involves testing out the character, ghost, ghost house, leaderboard,gameplayed and PacPlayer. This is definitely a enough coverage to test individual functionalities of actor classes.
* one thing we can improve would be looking into implementing a test for the ghost’s movements in the game to target lagging issues.

