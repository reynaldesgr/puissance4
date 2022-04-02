package src;


import static src.Heuristic.MAX_NOTE;
import static src.Heuristic.MIN_NOTE;

import java.util.ArrayList;

/* Elegage Alpha Beta */
public class AlphaBetaIA {
    private static final int DEPTH_EXPLORATION = 4;
    private Heuristic Heuristic;

    public AlphaBetaIA(Heuristic Heuristic){
        this.Heuristic = Heuristic;
    }

    double valGame = MIN_NOTE;

    /* Renvoie la meilleure colonne à jouer */
    public int bestColumnToPlay(Grid grid, Player p){
        ArrayList<Integer> columnToPlay = new ArrayList<>();

        for(int col = 1; col <= Grid.MAX_COLUMN; col++){
            if(!grid.isColumnFull(col - 1)){
                columnToPlay.add(col);
                break;
            }
        }

        for(int col = 1; col <= Grid.MAX_COLUMN; col++){
            if(!grid.isColumnFull(col - 1)){
                Grid grid_IA = new Grid();
                grid_IA.getCopy(grid);
                grid_IA.placeToken(p, col, true);
                double valCurrentGame = alphaBeta(grid_IA, p, DEPTH_EXPLORATION);

                if(valCurrentGame == valGame){
                    columnToPlay.add(col);
                }else if(valCurrentGame > valGame){
                    columnToPlay.clear();
                    valGame = valCurrentGame;
                    columnToPlay.add(col);
                }
            }
        }

        int numColumnToPlay = (int) (Math.random() * columnToPlay.size());
        return columnToPlay.get(numColumnToPlay);
    }
    
    private double alphaBeta(Grid grid, Player p, int depth){
        double alpha = MIN_NOTE;
        double beta  = MAX_NOTE;

        return this.max(grid, p, depth, alpha, beta);
    }

    /* Min */
    private double min(Grid grid, Player p, int depth, double alpha, double beta){
        if(depth != 0){
            double valCurrentGame = MAX_NOTE;
            for(int col = 1; col <= Grid.MAX_COLUMN; col++){
                if(!grid.isColumnFull(col - 1)){
                    Grid cloneGrid = new Grid();
                    cloneGrid.getCopy(grid);
                    cloneGrid.placeToken(Game.nextPlayer(), col, true);
                    valCurrentGame = Math.min(valGame, this.max(cloneGrid, p, depth - 1, alpha, beta));
                    if(alpha >= valCurrentGame){
                        return valCurrentGame;
                    }
                    beta = Math.min(beta, valCurrentGame);
                }
            }
            return valCurrentGame;
        }else{
            return this.Heuristic.noteGrid(grid, p);
        }

    }

    /* Max */
    private double max(Grid grid, Player p, int depth, double alpha, double beta){
        if(depth != 0){
            double valCurrentGame = MIN_NOTE;
            for(int col = 1; col <= Grid.MAX_COLUMN; col++){
                if(!grid.isColumnFull(col - 1)){
                    Grid cloneGrid = new Grid();
                    cloneGrid.getCopy(grid);
                    cloneGrid.placeToken(p, col, true);
                    valCurrentGame = Math.max(valCurrentGame, this.min(cloneGrid, p, depth - 1, alpha, beta));

                    if(valCurrentGame >= beta){
                        return valCurrentGame;
                    }
                    alpha = Math.max(alpha, valCurrentGame);
                }
            }
            return valCurrentGame;
        }else{
            return this.Heuristic.noteGrid(grid, p);
        }
    }
}
