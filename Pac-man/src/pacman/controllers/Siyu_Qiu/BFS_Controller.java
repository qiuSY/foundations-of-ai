package pacman.controllers.Siyu_Qiu;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class BFS_Controller extends Controller<MOVE> {

	public static StarterGhosts ghosts = new StarterGhosts();

	@Override
	public MOVE getMove(Game game, long timeDue) {
		
		MOVE[] allMoves=MOVE.values();
		int highScore=Integer.MIN_VALUE;
		MOVE bestMove=null;
		Random rnd=new Random();
		List<MOVE> bestMoves=new ArrayList<MOVE>();
		
		for(MOVE m: allMoves){
			System.out.println("Trying Move: " + m);
			//In order of get the best move, we need to 
			//copy a new game object, decide which one is
			//best and return to initial state.
			Game gameTmp=game.copy();

			
			//Game would be controlled by controller
			gameTmp.advanceGame(m,ghosts.getMove(gameTmp,timeDue));
			
			//for example maxdepth is 7
			int tempHighScore=this.bfs_PacMan(new PacManNode(gameTmp,0),7);
			
			if (tempHighScore>highScore){
				highScore=tempHighScore;
				bestMove=m;
			}
//			else if(tempHighScore==highScore){
			//Add this statement to choose move from the list of bestMoves
//				bestMoves.add(m);
//				}
//			
			System.out.println("Trying Move: " + m + ", Score: " + tempHighScore);
		}
		//Only choose when we found multiple bestMoves
//		if(bestMoves.size()>0)
//			bestMove=bestMoves.get(rnd.nextInt(bestMoves.size()));
		
		System.out.println("High Score: " + highScore + ", High Move:" + bestMove);
		return bestMove;
		}
			
		public int bfs_PacMan(PacManNode rootState, int maxDepth){
			MOVE[] allMoves=Constants.MOVE.values();
			int highScore=Integer.MIN_VALUE;
			//Queue for BFS
			Queue<PacManNode> queue = new LinkedList<PacManNode> ();
			
			//Enqueue the start state of Pac-Man
			queue.offer(rootState);
			
			while(!queue.isEmpty()){
				PacManNode node = queue.poll();
				if(node.depth>=maxDepth){
					//get Score when we meet max Depth
					int score=node.gameState.getScore();
					if(highScore<score) highScore=score;
					
				}
				else{
					//else we find best Move in children
					for(MOVE m: allMoves){
						//Similarly, we need more gameState to
						//get best Moves
						Game gameCopy=node.gameState.copy();
						gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));
						PacManNode nextNode=new PacManNode(gameCopy,node.depth+1);
						queue.offer(nextNode);
					}
				}
			}
			return highScore;
		}
}