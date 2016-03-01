package pacman.controllers.Siyu_Qiu;


import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;


//Hill_climbing algorithm
public class Hill_climbing_controller extends Controller<MOVE>{
	
	public static StarterGhosts ghosts = new StarterGhosts();
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		PacManPoint startNode = new PacManPoint(game,0);
		int highScore = Integer.MIN_VALUE;
		MOVE bestMove = null;
		
		MOVE[] allMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());
		
		System.out.println("dasda");
		for(MOVE m:allMoves){
			Game gameCopy = startNode.gameState.copy();
			gameCopy.advanceGame(m, ghosts.getMove(gameCopy, timeDue));
			
			int temphighScore = this.hillClimbing(new PacManPoint(gameCopy,0),7);
			if(temphighScore > highScore){
				highScore = temphighScore;
				bestMove = m;
			}

			System.out.println("Trying Move: " + m + ", Score: " + temphighScore);
		}
	
		System.out.println("High Score: " + highScore + ", High Move:" + bestMove);
		return bestMove;
	}
	
	public int hillClimbing(PacManPoint startNode,int maxDepth){
		int highScore=Integer.MIN_VALUE;
		PacManPoint currentNode = startNode;
		
		while(currentNode!=null){
			
			int counts =0;
			MOVE[] allMoves = currentNode.gameState.getPossibleMoves(currentNode.index);

			for(MOVE m:allMoves){
				Game gameCopy = startNode.gameState.copy();
				gameCopy.advanceGame(m,ghosts.getMove(gameCopy, 0));

				PacManPoint tempNode = new PacManPoint(gameCopy,currentNode.depth+1);
				int tempScore = tempNode.gameState.getScore();
				
				if(tempScore <= currentNode.gameState.getScore()){
					counts++;
				}
				else
					currentNode = tempNode;
			}
			
			if(counts==allMoves.length||currentNode.depth>=maxDepth){
				System.out.println(counts);
				highScore = currentNode.gameState.getScore();
				return highScore;
			}
			
		}
		return highScore;
	}
	
}
