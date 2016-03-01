package pacman.controllers.Siyu_Qiu;

import pacman.game.Game;

//PacManPoint for A* Search

public class PacManPoint {
    //Parameters for A*
	Game gameState;
    PacManPoint parent;
    int index;
    int score;
    int depth=0;
  
    public PacManPoint(Game game,int depth)
    {
        this.gameState = game;
        this.index = game.getPacmanCurrentNodeIndex();
        this.depth = depth;
    }

}
