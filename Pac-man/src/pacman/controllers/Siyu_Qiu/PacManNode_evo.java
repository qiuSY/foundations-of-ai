package pacman.controllers.Siyu_Qiu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class PacManNode_evo {
	public Game gameState;
	public int score;
	private Random rnd=new Random();
	private MOVE[] allMoves=MOVE.values();
	
	//representation is an array of moves
	//For example : size = 5,  <LEFT, UP, RIGHT, DOWN, UP >
	List<MOVE> moves = new ArrayList<MOVE>();
	
	//used for initialization
	public PacManNode_evo(Game game){
		this.gameState = game;
		
		//when initialize a PacManNode_evo, randomly generate a set of moves
		//for example, size is 5
		for(int i=0; i<5; i++){
			moves.add(allMoves[rnd.nextInt(allMoves.length)]);
		}
		//get score
		this.score=game.getScore();
	}
	
	//used for mutation
	public PacManNode_evo(Game game, List<MOVE> m, int depth){
		this.gameState = game;
		this.moves = m;
	}
}
