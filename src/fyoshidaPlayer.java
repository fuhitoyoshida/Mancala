/****************************************************************
 * studPlayer.java
 * Implements MiniMax search with A-B pruning and iterative deepening search (IDS). The static board
 * evaluator (SBE) function is simple: the # of stones in studPlayer's
 * mancala minue the # in opponent's mancala.
 * -----------------------------------------------------------------------------------------------------------------
 * Licensing Information: You are free to use or extend these projects for educational purposes provided that
 * (1) you do not distribute or publish solutions, (2) you retain the notice, and (3) you provide clear attribution to UW-Madison
 *
 * Attribute Information: The Mancala Game was developed at UW-Madison.
 *
 * The initial project was developed by Chuck Dyer(dyer@cs.wisc.edu) and his TAs.
 *
 * Current Version with GUI was developed by Fengan Li(fengan@cs.wisc.edu).
 * Some GUI componets are from Mancala Project in Google code.
 */




//################################################################
// studPlayer class
//################################################################

/**
 * provides a copy of the mancala board as an array of integers where the bin
 * numbers are indexed in the following way:<pre>
     -----------------------------------------
     |    | 12 | 11 | 10 |  9 |  8 |  7 |    |
     | 13 |-----------------------------|  6 |
     |    |  0 |  1 |  2 |  3 |  4 |  5 |    |
     -----------------------------------------</pre>
 * @return an array of the number of stones in each bin
 */

public class fyoshidaPlayer extends Player {


	/*Use IDS search to find the best move. The step starts from 1 and keeps incrementing by step 1 until
	 * interrupted by the time limit. The best move found in each step should be stored in the
	 * protected variable move of class Player.
	 */
	public void move(GameState state) {
		int maxDepth = 1;
		while(true){
			move = maxAction(state, maxDepth++);
		}
	}

	// Return best move for max player. Note that this is a wrapper function created for ease to use.
	// In this function, you may do one step of search. Thus you can decide the best move by comparing the 
	// sbe values returned by maxSBE. This function should call minAction with 5 parameters.
	public int maxAction(GameState state, int maxDepth) {
		int alpha = Integer.MIN_VALUE, beta = Integer.MAX_VALUE, bestMove = -1, maxV = alpha, tempV = 0;
		for(int i = 0; i < 6; i++){
			if(state.state[i] != 0){
				GameState cp = new GameState(state);
				boolean next = cp.applyMove(i);
				if(next){
					tempV = maxAction(cp, 1, maxDepth, alpha, beta);
				}else{
					tempV = minAction(cp, 1, maxDepth, alpha, beta);
				}
				if(tempV > maxV){
					maxV = tempV;
					bestMove = i;
				}
			}
		}
		return bestMove;
	}

	//return sbe value related to the best move for max player
	//at max value, change alpha, use beta to prune, 
	public int maxAction(GameState state, int currentDepth, int maxDepth, int alpha, int beta) {
		if(currentDepth == maxDepth || !hasMove(0, state)) return sbe(state);
		int maxV = Integer.MIN_VALUE;
		for(int i = 0; i < 6; i++){
			if(state.state[i] != 0){
				GameState cp = new GameState(state);
				boolean next = cp.applyMove(i);
				if(next){
					maxV = Math.max(maxV, maxAction(cp, currentDepth+1, maxDepth, alpha, beta));
					if(maxV >= beta){
						return maxV;
					}
					alpha = Math.max(maxV, alpha);
				}else{
					maxV = Math.max(maxV, minAction(cp, currentDepth+1, maxDepth, alpha, beta));
					if(maxV >= beta){
						return maxV;
					}
					alpha = Math.max(maxV, alpha);
				}
			}
		}
		return maxV;
	}



	//return sbe value related to the bset move for min player
	//at min level, update beta, use alpha to prune
	public int minAction(GameState state, int currentDepth, int maxDepth, int alpha, int beta) {
		if(currentDepth == maxDepth || !hasMove(1, state)) return sbe(state);
		int minV = Integer.MAX_VALUE;
		for(int i = 7; i < 13; i++){
			if(state.state[i] != 0){
				GameState cp = new GameState(state);
				boolean next = cp.applyMove(i);
				if(next){
					minV = Math.min(minV, minAction(cp, currentDepth+1, maxDepth, alpha, beta));
					if(minV <= alpha){
						return minV;
					}
					beta = Math.min(minV, beta);
				}else{
					minV = Math.min(minV, maxAction(cp, currentDepth+1, maxDepth, alpha, beta));
					if(minV <= alpha){
						return minV;
					}
					beta = Math.min(minV, beta);
				}
			}
		}
		return minV;
	}


	//the sbe function for game state. Note that in the game state, the bins for current player are always in the bottom row.
	private int sbe(GameState state) {
		//simple sbe, own mancala - opp mancala
		return state.state[6] - state.state[13];
	}

	private boolean hasMove(int side, GameState state){
		for(int i = 7*side; i < 6 + side*7; i++){
			if(state.state[i] != 0) return true;
		}
		return false;
	}
}
