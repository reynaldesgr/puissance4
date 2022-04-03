package src;
public class HeuristicAlign extends Heuristic{
 
    /* Note une grille selon l'alignement des jetons du joueur p dans la grille */
    @Override
    public int noteGrid(Grid grid, Player p) {
        Player next_p = p.getName().equals(Game.J1.getName()) ? Game.J2 : Game.J1;
        if(grid.checkAlignementGridToken(3, next_p.getTokenColor())){
            return Heuristic.MIN_NOTE;
        }
        if(grid.checkAlignementGridToken(3, p.getTokenColor())){
            return Heuristic.MAX_NOTE;
        }

        Player.COLOR color = p.getTokenColor();
        int res = 0;
        for(int l = 0; l < Grid.MAX_LINE; l++){
            for(int c = 0; c < Grid.MAX_COLUMN; c++){
                res += this.checkAlignToken(grid, 4, color, l, c, 0,  1);
                res += this.checkAlignToken(grid, 4, color, l, c, 1,  1);
                res += this.checkAlignToken(grid, 4, color, l, c, 1,  0);
                res += this.checkAlignToken(grid, 4, color, l, c, 1, -1);
                res += this.checkAlignToken(grid, 4, color, l, c, 0, -1);
                res += this.checkAlignToken(grid, 4, color, l, c,-1, -1);
                res += this.checkAlignToken(grid, 4, color, l, c,-1,  0);
                res += this.checkAlignToken(grid, 4, color, l, c,-1,  1);
            }
        }
        return res;
    }

    /* Retourne un score selon l'alignement du jeton de couleur c. */
    private int checkAlignToken(Grid grid, int lengthAlign, Player.COLOR color, int c, int l, int H, int V){
        int res = 1;
        while(lengthAlign != 0 && res != 0){
            Token t = null;
            if(c < 0 || c > Grid.MAX_COLUMN - 1 || l < 0 || l > Grid.MAX_LINE - 1){
                res = 0;
            }else{
                t = grid.getGrid()[l][c];
            }

            if(t == null){
                res /=  2;
            }else if(t.getColor().equals(color)){
                res *=  2.;
            }else{
                res *=  0.;
            }
            c+=H;   l+=V;
            lengthAlign--;
        }
        return res;
    }
    
}
