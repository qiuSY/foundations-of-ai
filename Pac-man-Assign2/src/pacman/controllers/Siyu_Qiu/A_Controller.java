package pacman.controllers.Siyu_Qiu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

//A* search algorithm

public class A_Controller extends Controller<MOVE> {
	
	public static StarterGhosts ghosts = new StarterGhosts();
	
	@Override
	public MOVE getMove(Game game, long timeDue) {
		PacManPoint startNode = new PacManPoint(game,0);
		int highScore = Integer.MIN_VALUE;
		MOVE bestMove = null;
			
		MOVE[] allMoves=MOVE.values();
		for(MOVE m:allMoves){
			System.out.println("Trying Move: " + m);
			Game gameCopy = startNode.gameState.copy();
			gameCopy.advanceGame(m,ghosts.getMove(gameCopy,timeDue));
			//Return value for AstartSearch is the distance from startNode to goal
			//Prevent move reversely
			int temphighScore = this.AstarSearch(new PacManPoint(gameCopy,0), 7);
			if(highScore <= temphighScore){
				highScore = temphighScore;
				bestMove = m;
			}
			System.out.println("Trying Move(out): " + m + ", Score: " + temphighScore);
		}
		System.out.println("High Score: " + highScore + ", High Move:" + bestMove);
		return bestMove;
	}
	
	public int AstarSearch(PacManPoint start, int maxDepth){
		//Set up openlist and closelist, add the startnode into openlist
		List<PacManPoint> openList = new ArrayList<PacManPoint>();
		List<PacManPoint> closeList = new ArrayList<PacManPoint>();
		
		int highScore = 0;
		
		//Add the startnode into openlist
		openList.add(start);
		while(openList.size()!=0){
			//Get mininum F from openList
			//Remove node and put node into closelist
			PacManPoint tempStart=maxNode(openList);
			openList.remove(0);
			closeList.add(tempStart);

			if(tempStart.depth>=maxDepth){
				//get Score when we meet max Depth
				highScore=tempStart.score;
			}
			else{
				//Get all possibleMoves from tempStart
				MOVE[] allMoves=MOVE.values();
				for(MOVE m:allMoves){

					//Advance Game, update game status
					Game gameCopy = tempStart.gameState.copy();
					gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));
					
					//Update currentNode after Move m
					PacManPoint currentNode=new PacManPoint(gameCopy,tempStart.depth+1);

					if(containsPoint(currentNode,openList)!=null)
						foundPoint(tempStart,currentNode,containsPoint(currentNode,openList));

					else
						notfoundPoint(tempStart, currentNode, openList);
//					System.out.println("Trying Move: " + m + ", Score: " + currentNode.gameState.getScore());
				}
			}
	
		}
		System.out.println("in: highScore"+highScore);
		return highScore;
	}
	
	//Get minimum F in the openList
	public PacManPoint maxNode(List<PacManPoint> openList){
		//firstly, sort from min to max
		Collections.sort(openList, new Comparator<PacManPoint>() {   
		    public int compare(PacManPoint o1, PacManPoint o2) {      
		    	return (o1.score-o2.score);
		    }
		});
		return openList.get(0);
	}
	
	//The node is indicated as index.
	public PacManPoint containsPoint(PacManPoint key, List<PacManPoint> openList){
		for(int i=0;i<openList.size();i++){
			if(openList.get(i).index == key.index)
				return openList.get(i);
		}
		return null;
	}
	
	//If found currentNode in openList, re-calculate F
	public void foundPoint(PacManPoint tempStart, PacManPoint currentNode, PacManPoint pastNode){
		int F = currentNode.gameState.getScore();
		
		if(pastNode.score < F){
			pastNode.parent = tempStart;
			pastNode.score = F;
		}
	}
	
	//If not found currentNode in openList
	//add it into openlist,update parent, F
	public void notfoundPoint(PacManPoint tempStart, PacManPoint currentNode,List<PacManPoint> openList){
		currentNode.parent = tempStart;
		currentNode.score = currentNode.gameState.getScore();
		openList.add(currentNode);
	}
	
}
