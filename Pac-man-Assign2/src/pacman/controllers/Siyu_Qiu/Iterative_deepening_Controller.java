package pacman.controllers.Siyu_Qiu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class Iterative_deepening_Controller extends Controller <MOVE> {
	
	public static StarterGhosts ghosts = new StarterGhosts();
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		MOVE[] allMoves=MOVE.values();
		
		/*We want to get the bestMove and highest Score 
		 * for each depth, and determin which depth has the highest
		 * score.*/
		int highScore=Integer.MIN_VALUE;
		MOVE bestMove=null;
		int bestDepth=0;
		Random rnd=new Random();
		List<MOVE> bestMoves=new ArrayList<MOVE>();
		
		for(int i=0; i<15;i++){
			for(MOVE m:allMoves){
				System.out.println("Trying to move: "+m);
				Game gameCopy=game.copy();
				//Make a move
				gameCopy.advanceGame(m, ghosts.getMove(gameCopy, timeDue));
				DFS_Controller dls=new DFS_Controller();
				
				//Get highest score of each possible move in its children for each depth
				int tempHighScore=dls.dfs_pacMan(new PacManNode(gameCopy,0), i);
				if(highScore<tempHighScore) {
					highScore=tempHighScore;
					bestMove=m;
					bestDepth=i;
				}
//				else if(tempHighScore==highScore){
//				//Add this statement to choose move from the list of bestMoves
//					bestMoves.add(m);
//					}
				
				System.out.println("tempHighScore="+tempHighScore+"  ,move"+m);
			}
//			//Only choose when we found multiple bestMoves
//			if(bestMoves.size()>0)
//				bestMove=bestMoves.get(rnd.nextInt(bestMoves.size()));
			
			System.out.println("depth:"+i+"  ,highScore="+highScore+"  ,bestMove"+bestMove);
		}
		System.out.println("BESTDEPTH:"+bestDepth+"  BESTSCORE:"+highScore+"  BESTMOVE:"+bestMove);
		return bestMove;
	}
}
