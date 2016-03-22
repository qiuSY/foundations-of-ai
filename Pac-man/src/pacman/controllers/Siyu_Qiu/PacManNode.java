/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.controllers.Siyu_Qiu;

import pacman.game.Game;

/**
 *
 * PacManNode for BFS,DFS,IDFS
 */
public class PacManNode 
{
	//Parameters for BFS,DFS,IDFS
	Game gameState;
    int depth;
    int score;
    
    public PacManNode(Game game, int depth)
    {
        this.gameState = game;
        this.depth = depth;
		this.score=game.getScore();
    }
}