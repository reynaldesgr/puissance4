package src;


import static src.Heuristic.MAX_NOTE;
import static src.Heuristic.MIN_NOTE;

import java.util.ArrayList;

/* Elegage Alpha Beta */
public class AlphaBetaIA {
    private static final int DEPTH_EXPLORATION = 4;
    private Heuristic heuristic;

    public AlphaBetaIA(Heuristic Heuristic){
        this.heuristic = Heuristic;
    }

    double valGame = MIN_NOTE;

    /* Renvoie la meilleure colonne à jouer */
    public int bestColumnToPlay(Grid grid, Player p){
        ArrayList<Integer> columnToPlay = new ArrayList<>();

        for(int col = 1; col <= Grid.MAX_COLUMN; col++){
            if(!grid.isColumnFull(col - 1)){
                columnToPlay.add(col);
            }
        }
        
        int alpha = MIN_NOTE; int beta = MAX_NOTE;
        int action = 1;
        for(int m : columnToPlay){
            Grid clone_grid = new Grid();
            clone_grid.getCopy(grid);
            clone_grid.placeToken(p, m, true);
            int val = alphaBeta(clone_grid, p, alpha, beta, false, DEPTH_EXPLORATION);
            if(val > alpha){
                action = m;
                alpha = val;
            }
        }
        printAction(action);
        return action; 
    }
    
    /** ALGORITHME ALPHA-BETA **/

    /* Consiste à minimiser la perte maximum. L'élègage alpha-beta accèle la routine */
    /* de recherche minimax en éliminant les cas qui ne seront pas utilisés. */
    /* Elle s'appuie sur le fait que tous les autres niveaux de l'arbre seront maximisés et */
    /* que tous les autres niveaux seront minimisés. */

    private int alphaBeta(Grid grid, Player j, int alpha, int beta, boolean maxState, int depth){
        Player next_j = j == Game.J1 ? Game.J2 : Game.J1; // Prochain joueur 

        if(depth == 0){ /* Maximum de profondeur atteint. On calcule le score obtenu. */
            return this.heuristic.noteGrid(grid, j);
        }else{
            if(maxState){ /* Si état MAX */
                for(int c = 1; c <= Grid.MAX_COLUMN; c++){ /* Coups possibles */
                    if(!grid.isColumnFull(c - 1)){
                        Grid clone_grid = new Grid();
                        clone_grid.getCopy(grid);
                        clone_grid.placeToken(j, c, true);
                        alpha = Math.max(alpha, alphaBeta(clone_grid, j, alpha, beta, !maxState, depth - 1));
                        if(alpha >= beta){
                            return alpha; /** coupure beta **/
                        }
                    }
                }
                return alpha;
            }else{  /* Si état MIN => joueur adverse */
                for(int c = 1; c <= Grid.MAX_COLUMN; c++){ /* Coups possibles */
                    if(!grid.isColumnFull(c - 1)){
                        Grid clone_grid = new Grid();
                        clone_grid.getCopy(grid);
                        clone_grid.placeToken(next_j, c, true);
                        beta = Math.min(beta, alphaBeta(clone_grid, j, alpha, beta, !maxState, depth - 1));
                        if(beta <= alpha){
                            return beta; /** coupure alpha **/
                        }
                    }
                }
                return beta;
            }
        }
    }

    /* Procédure permettant d'afficher l'action choisi dans la console */
    private void printAction(int action){
        System.out.println( Game.ANSI_RED + "IA joue en : " + Game.ANSI_WHITE + action);
    }
}

