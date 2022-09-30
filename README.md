# SurviveIt
**What will the application do?**  \
The application is a luck based game where the user will
control a player to destroy a set of enemies. 
Your player and enemy will have a set of health, and damage.
The point of the game is to destroy the many enemies within the game before dying. 
User can buy more players to attack from coins dropped from enemies.
Enemies may spawn and attack after each user interaction. 
When defeated the user will lose a bit of stats, and will need to
start a new game. To win the user must defeat all current enemies in game.


**Who will use it?** \
The target audience is anyone interested within
strategy games. 

**Why is this project of interest to you?** \
I have a great interest in creating a video game. I have
created a couple games in Unity, but with little original code.
Creating a game in java will give me more insight in to
game development and also coding.

###User Story
- As a user, I want to be able to add another attacker when I have enough coins. (Add player to GameBoard)
- As a user, I want to be able to attack an enemy. (Remove enemy from GameBoard)
- As a user, I want to be able to restart the game when defeated.
- As a user, I want to be able to dodge an attack.
- As a user, I want to be able to save my Game any time I want.
- As a user, I want to be given the option to load my previous game from file.

###Phase 4: Task 2
Tue Mar 29 19:39:17 PDT 2022
Defeated enemy
Tue Mar 29 19:39:18 PDT 2022
Defeated enemy
Tue Mar 29 19:39:18 PDT 2022
Defeated enemy
Tue Mar 29 19:39:19 PDT 2022
Defeated enemy
Tue Mar 29 19:39:20 PDT 2022
Added player

Here we can see that the user attacked enough times to defeat 4 enemies, and also bought a player with the required coins.

###Phase 4: Task 3:
If I had more time to refactor, I would create an abstract class for my UI for GameGUI and StartEndGUI
because GameGUI and StartEndGameGUI has many elements similar to each other. This would include the window listener that 
displays the eventLog when closing the game. I would also make a design pattern (observer design pattern) to notify when events changes. This would
include activities such as defeating an enemy, and buying a player. This would make the program easier to follow and reduce coupling .
