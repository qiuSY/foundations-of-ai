package pacman.controllers.Siyu_Qiu;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;



public class Simulated_annealing_Controller extends Controller<MOVE> {

	public static StarterGhosts ghosts = new StarterGhosts();
	Random rnd=new Random();
		
	@Override
	public MOVE getMove(Game game, long timeDue) {

		int highScore=Integer.MIN_VALUE;
		MOVE bestMove=null;

		MOVE[] allMoves=MOVE.values();
		for(MOVE m:allMoves){

			Game gameTmp=game.copy();
			gameTmp.advanceGame(m,ghosts.getMove(gameTmp,timeDue));
			
			int tempHighScore=this.simulated_annealing(new PacManPoint(gameTmp,0));
			if (tempHighScore > highScore){
				highScore = tempHighScore;
				bestMove = m;
			}
			System.out.println("Trying Move: " + m + ", Score: " + tempHighScore);
		}
	
		System.out.println("High Score: " + highScore + ", High Move:" + bestMove);
		return bestMove;
	}
	
	
	public int simulated_annealing(PacManPoint startNode){
		double r=0.9;
		double t_min=1.0;
		double t =10.0;
		
		/*initial state*/
		PacManPoint currentNode = startNode;
		
		/*temperature keep decrease by some probility*/
		while(t>t_min){

			/*randomly select successor of current*/
			MOVE[] allMoves=MOVE.values();
			MOVE m = allMoves[rnd.nextInt(allMoves.length)];
			
				Game gameCopy = currentNode.gameState.copy();
				gameCopy.advanceGame(m,ghosts.getMove(gameCopy, 0));
				
				PacManPoint tempNode = new PacManPoint(gameCopy,0);
				
				int tempScore = tempNode.gameState.getScore();
				int currentScore = currentNode.gameState.getScore();

				int score_diff = tempScore-currentScore;
				
				/*if bigger than current score, keep forward*/
				if(score_diff > 0)
					currentNode = tempNode;
				/*else keep forward based on Math.exp(score_diff/t)*/
				else{
					double s= Math.random();
					if(Math.exp(score_diff/t) > s)
						currentNode = tempNode;
				}
				/*decrease the temperature*/
			t = r*t;
		}
		return currentNode.gameState.getScore();
	}

}
