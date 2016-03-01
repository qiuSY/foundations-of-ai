package pacman.controllers.Siyu_Qiu;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class DFS_Controller extends Controller<MOVE> {

	public static StarterGhosts ghosts = new StarterGhosts();

	@Override
	public MOVE getMove(Game game, long timeDue) {
		int highScore=Integer.MIN_VALUE;
		MOVE[] allMoves=MOVE.values();
		MOVE bestMove=null;
		Random rnd=new Random();
		List<MOVE> bestMoves=new ArrayList<MOVE>();
		
		for(MOVE m:allMoves){
			System.out.println("Trying Move: " + m);
			Game gameCopy=game.copy();
			//Make a move
			gameCopy.advanceGame(m, ghosts.getMove(gameCopy, timeDue));
			int tempHighscore=dfs_pacMan(new PacManNode(gameCopy,0),15);
			
			if(tempHighscore>highScore) {
				highScore=tempHighscore;
				bestMove=m;
			}
//			else if(tempHighscore==highScore){
////			Add this statement to choose move from the list of bestMoves
//				bestMoves.add(m);
//				}
			System.out.println("Trying Move: " + m + ", Score: " + tempHighscore);
		}
		//Only choose when we found multiple bestMoves
//		if(bestMoves.size()>0)
//			bestMove=bestMoves.get(rnd.nextInt(bestMoves.size()));
//		
		System.out.println("High Score: " + highScore + ", High Move:" + bestMove);
		return bestMove;
	}
	
	public int dfs_pacMan(PacManNode rootState,int maxDepth){
		int highScore=Integer.MIN_VALUE;
		MOVE[] allMoves=MOVE.values();
		System.out.println(rootState.depth+1);
		
		for(MOVE m:allMoves){
			Game gameCopy=rootState.gameState.copy();
			gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));
			if(rootState.depth>=maxDepth){
				int score=rootState.gameState.getScore();
				if(score>highScore) highScore=score;
			}
			else
				//call dfs_pacMan recursively to go deeper and find the best Move
				return dfs_pacMan(new PacManNode(gameCopy,rootState.depth+1),maxDepth);
		}
		return highScore;
	}
}
