package pacman.controllers.Siyu_Qiu;

import java.util.ArrayList;
import java.util.List;

import pacman.controllers.Controller;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class KNN_Controller extends Controller<MOVE> {
	private GHOST closestGhost;
	private int closestPill;
	private int closestGhostDist;
	private int closestPillDist;
	private static int K = 3;

	// The first index: the distance of the nearest edible ghost
	// The second index: the distance of the nearest pill
	// The third index: decision (), 0: escape from ghost, 1: eat pill
	// pill's distance < ghost distance, eat pill first
	// otherwise escape from ghost
	private static int[][] trainingData = { { 5, 2, 0 }, { 5, 5, 0 },
			{ 5, 10, 0 }, { 5, 15, 0 }, { 5, 20, 0 }, { 10, 2, 1 },
			{ 10, 5, 1 }, { 10, 10, 0 }, { 10, 15, 0 }, { 10, 20, 0 },
			{ 15, 2, 1 }, { 15, 5, 1 }, { 15, 10, 1 }, { 15, 15, 0 },
			{ 15, 20, 0 }, { 20, 2, 1 }, { 20, 5, 1 }, { 20, 10, 1 },
			{ 20, 15, 1 }, { 20, 20, 0 }, { 30, 5, 1 }, { 30, 10, 1 },
			{ 30, 15, 1 }, { 30, 20, 1 } };

	@Override
	public MOVE getMove(Game game, long timeDue) {
		int currentPacmanIndex = game.getPacmanCurrentNodeIndex();

		initialization(game);

		if (training() == false) {
			// escape from ghost
			System.out.println("ghost");
			return game.getNextMoveAwayFromTarget(currentPacmanIndex,
					game.getGhostCurrentNodeIndex(closestGhost), DM.PATH);
		} else {
			// go for pill
			System.out.println("pill");
			return game.getNextMoveTowardsTarget(currentPacmanIndex,
					closestPill, DM.PATH);
		}
	}

	public void initialization(Game game) {
		int currentPacmanIndex = game.getPacmanCurrentNodeIndex();

		// Get the closest Ghost
		int ghostDist = Integer.MAX_VALUE;
		for (GHOST ghost : GHOST.values()) {
			if (game.getGhostEdibleTime(ghost) == 0
					&& game.getGhostLairTime(ghost) == 0) {
				int currentGhostIndex = game.getGhostCurrentNodeIndex(ghost);
				int tempDist = game.getShortestPathDistance(currentPacmanIndex,
						currentGhostIndex);
				if (tempDist < ghostDist)
					ghostDist = tempDist;
				closestGhost = ghost;
			}
		}
		closestGhostDist = ghostDist;

		// Get the closest pill
		int[] pills = game.getActivePillsIndices();
		int[] powerPills = game.getActivePowerPillsIndices();

		List<Integer> allPills = new ArrayList<Integer>();
		for (int pill : pills)
			allPills.add(pill);
		for (int powerPill : powerPills)
			allPills.add(powerPill);
		int[] allPillsarray = new int[allPills.size()];
		for (int i = 0; i < allPills.size(); i++) {
			allPillsarray[i] = allPills.get(i);
		}

		closestPill = game.getClosestNodeIndexFromNodeIndex(currentPacmanIndex,
				allPillsarray, DM.PATH);
		closestPillDist = game.getShortestPathDistance(currentPacmanIndex,
				closestPill);
	}

	// train Ms.Pacman
	private boolean training() {
		int[] distance = new int[K];
		int[] index = new int[K];

		// initialization
		for (int i = 0; i < K; i++) {
			distance[i] = Integer.MAX_VALUE;
		}

		// Evaluate distance
		for (int i = 0; i < trainingData.length; i++) {
			int temp = (int) Math.sqrt(Math.pow(trainingData[i][0]
					- closestGhostDist, 2)
					+ Math.pow(trainingData[i][1] - closestPillDist, 2));

			if (temp <= distance[0]) {
				// update distance[0]
				distance[2] = distance[1];
				distance[1] = distance[0];
				distance[0] = temp;
				index[2] = index[1];
				index[1] = index[0];
				index[0] = i;
			} else if (temp <= distance[1]) {
				// update distance[1]
				distance[2] = distance[1];
				distance[1] = temp;
				index[2] = index[1];
				index[1] = i;
			} else if (temp <= distance[2]) {
				distance[2] = temp;
				index[2] = i;
			}
		}

		int count = 0;
		for (int i = 0; i < K; i++) {
			if (trainingData[index[i]][2] == 1)
				count++;
		}
		System.out.println(count);
		return count >= 2;
	}
}
