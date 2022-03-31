package src;

import java.util.Random;

import src.Grid.DIRECTION;

class IA extends Player{
    public IA(){ super("IA"); }

    public void chooseTokenColor(Player J){
        int numberColorPlayer = J.getNumberColor() + 1;
        Random rand = new Random();

        int numberIAColor = rand.nextInt(5) + 1;
        while(numberIAColor == numberColorPlayer){
            numberIAColor = rand.nextInt(5) + 1;
        }   this.setANSIColor(numberIAColor- 1); 
    }

    public void placeTokenOnGrid(Grid gameGrid){
        searchTokenAlignment(gameGrid);   
    }

    void placeOnLastFreeCase(Grid gamegrid){
        if(canPlaceToken(gamegrid, Grid.last_token_pos_l -1, Grid.last_token_pos_c)){
            gamegrid.placeToken(this, Grid.last_token_pos_c + 1);
        }else if(canPlaceToken(gamegrid, Grid.last_token_pos_l, Grid.last_token_pos_c - 1)){
            gamegrid.placeToken(this, Grid.last_token_pos_c);
        }else if(canPlaceToken(gamegrid, Grid.last_token_pos_l, Grid.last_token_pos_c + 1)){
            gamegrid.placeToken(this, Grid.last_token_pos_c + 2);
        }
    }

    public boolean canPlaceToken(Grid gameGrid, int l, int c){
        int last_l = Grid.last_token_pos_l;
        Token[][] current_grid = gameGrid.getGrid();
        if(c < 0 || c > Grid.MAX_COLUMN - 1 || l < 0 || l > Grid.MAX_LINE - 1){
            return false;
        } 
        if(l != last_l){
            if(l != last_l - 1) return false;
        }
        if(current_grid[l][c] != null){
            return false;
        }
        return true;
    }

    public void searchTokenAlignment(Grid gameGrid){
        int score[] = new int[6]; int pos_c = 0; int pos_l = 0; int best_token_score = 0;
        int left, right, up, down, down_right, down_left;
        for(int l = 0; l < Grid.MAX_LINE; l++){
            for(int c = 0; c < Grid.MAX_COLUMN; c++){
                Token[][] grid = gameGrid.getGrid();
                Token t = gameGrid.getGrid()[l][c];
                if(t != null){
                    if((c != 0 && grid[l][c - 1] == null) || (c!= Grid.MAX_COLUMN -1 && grid[l][c+1] == null)){
                        left =       t.isAlign(t, DIRECTION.LEFT, 0);
                        right =      t.isAlign(t, DIRECTION.RIGHT, 0);
                        up =         t.isAlign(t, DIRECTION.UP, 0);
                        down =       t.isAlign(t, DIRECTION.DOWN, 0);
                        down_right = t.isAlign(t, DIRECTION.DOWN_RIGHT, 0);
                        down_left =  t.isAlign(t, DIRECTION.DOWN_LEFT, 0);

                        if(score[0] < left)       score[0] = left;
                        if(score[1] < right)      score[1] = right;
                        if(score[2] < down)       score[2] = down;
                        if(score[3] < up)         score[3] = up;
                        if(score[4] < down_right) score[4] = down_right;
                        if(score[5] < down_left)  score[5] = down_left;

                        if(left + right >= best_token_score && c != 0 && c != Grid.MAX_COLUMN - 1){
                            best_token_score = left + right;
                            pos_l = l; pos_c = c;
                        }else if( down + up >= best_token_score && l != 0){
                            best_token_score = up + down;
                            pos_l = l; pos_c = c;
                        }else if(down_right + down_left >= best_token_score && c != Grid.MAX_COLUMN - 1 && l != 0 && c!= 0){
                            best_token_score = down_right + down_left;
                            pos_l = l; pos_c = c;
                        }
                    }
                }
            }
        }

        int max_score_alignment = 0;
        int alignement_max = - 1;
        for(int x = 0; x < score.length; x++){
            if(max_score_alignment < score[x]){
                max_score_alignment = score[x];
                alignement_max = x;
            }
        }
        
        if(alignement_max != -1){
            DIRECTION d_max = DIRECTION.values()[alignement_max];
            if(d_max.equals(DIRECTION.RIGHT) && canPlaceToken(gameGrid, pos_l, pos_c  - 1)){
                gameGrid.placeToken(this, pos_c);
            }else if(d_max.equals(DIRECTION.LEFT) && canPlaceToken(gameGrid, pos_l, pos_c + 1)){
                gameGrid.placeToken(this, pos_c + 2);
            }else if(d_max.equals(DIRECTION.DOWN) && canPlaceToken(gameGrid, pos_l - 1, pos_c)){
                gameGrid.placeToken(this, pos_c + 1);
            }else if(d_max.equals(DIRECTION.DOWN_LEFT) && canPlaceToken(gameGrid, pos_l - 1, pos_c + 1)){
                gameGrid.placeToken(this, pos_c + 2);
            }else if(d_max.equals(DIRECTION.DOWN_RIGHT) && canPlaceToken(gameGrid, pos_l - 1, pos_c - 1)){
                gameGrid.placeToken(this, pos_c);
            }else{
                placeOnLastFreeCase(gameGrid);
            }
        }else{
            placeOnLastFreeCase(gameGrid);
        }
    }
}