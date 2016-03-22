package pacman.controllers.Siyu_Qiu;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Game;
import pacman.game.Constants.MOVE;

public class Evolution_Controller extends Controller<MOVE> {
	public static StarterGhosts ghosts = new StarterGhosts();
	private Random rnd = new Random();
	private MOVE[] allMoves = MOVE.values();
	int maxDepth = 30;

	@Override
	public MOVE getMove(Game game, long timeDue) {
		MOVE[] allMoves = game.getPossibleMoves(game
				.getPacmanCurrentNodeIndex());

		int highScore = Integer.MIN_VALUE;
		MOVE bestMove = null;

		for (MOVE m : allMoves) {
			System.out.println("Trying Move: " + m);
			Game gameTmp = game.copy();
			gameTmp.advanceGame(m, ghosts.getMove(gameTmp, timeDue));

			// depth is represent the number of mutation
			int tempHighScore = this.evolution(game, maxDepth);

			if (tempHighScore > highScore) {
				highScore = tempHighScore;
				bestMove = m;
			}
			System.out.println("Trying Move: " + m + ", Score: "
					+ tempHighScore);
		}
		System.out.println("High Score: " + highScore + ", High Move:"
				+ bestMove);
		return bestMove;
	}

	public int evolution(Game game, int maxDepth) {
		Queue<PacManNode_evo> population = new PriorityQueue<PacManNode_evo>(5,
				new Comparator<PacManNode_evo>() {
					//descending order for get highest score
					@Override
					public int compare(PacManNode_evo o1, PacManNode_evo o2) {
						return -(Evolution_Controller.getScore(o1)
								- Evolution_Controller.getScore(o2));
					}
				});

		// initialize 5 population
		for (int i = 0; i < 5; i++)
			population.offer(new PacManNode_evo(game));

		for (int i = 0; i < maxDepth; i++) {
			Queue<PacManNode_evo> children = new PriorityQueue<PacManNode_evo>(5,
					new Comparator<PacManNode_evo>() {
						//ascending order for muatation
						@Override
						public int compare(PacManNode_evo o1, PacManNode_evo o2) {
							return Evolution_Controller.getScore(o1)
									- Evolution_Controller.getScore(o2);
						}
					});
//			children = population;
			
			for(PacManNode_evo m : population){
				children.add(m);
				System.out.println("population:before mutate"+m.moves+"score" + Evolution_Controller.getScore(m));
			}
			for(PacManNode_evo m : children)
				System.out.println("children:before mutate"+m.moves+"score" + Evolution_Controller.getScore(m));
			
			//choose two nodes that have lowest score to mutate
			PacManNode_evo m1 = mutate(children.poll());
			PacManNode_evo m2 = mutate(children.poll());
			//add back to pq
			children.add(m1);
			children.add(m2);
			
			//replace population with new generation
			population.clear();
			for(PacManNode_evo m : children){
				population.add(m);
				System.out.println("after mutate, before assign "+m.moves+"score" + Evolution_Controller.getScore(m));
			}
			
			
//			population = children;
			
			for(PacManNode_evo m : population)
			System.out.println("after mutate"+m.moves+"score" + Evolution_Controller.getScore(m));
			
		}
		for(PacManNode_evo m : population)
			System.out.println("after all"+m.moves+"score" + Evolution_Controller.getScore(m));
		System.out.println("highest score" + Evolution_Controller.getScore(population.peek())+population.peek().moves);
		return Evolution_Controller.getScore(population.peek());
		
	}

	// we replace one of moves by a new move for mutation
	public PacManNode_evo mutate(PacManNode_evo self) {
		// randomly choose one move to mutation
		// size is 5
		int index = rnd.nextInt(5);
		// also randomly choose a move from all move,
		// which should be different from the previous one
		MOVE newmove = self.moves.get(index);
		while (newmove.equals(self.moves.get(index)))
			newmove = allMoves[rnd.nextInt(allMoves.length)];

		self.moves.set(index, newmove);
		// after mutation, score should be reset to 0
		self.score = Evolution_Controller.getScore(self);
		
		return self;
	}
	
	
	public static int getScore(PacManNode_evo node) {
		Game gameCopy = node.gameState.copy();
		for(MOVE m : node.moves){
			gameCopy.advanceGame(m,ghosts.getMove(gameCopy, 0));
		}
		return  gameCopy.getScore();
	}
	
}
