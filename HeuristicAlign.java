package src;
public class HeuristicAlign extends Heuristic{
 
    @Override
    public double noteGrid(Grid grid, Player p) {
        if(grid.checkAlignementGridToken(4, Game.nextPlayer().getTokenColor())){
            return Heuristic.MIN_NOTE;
        }
        if(grid.checkAlignementGridToken(4, p.getTokenColor())){
            return Heuristic.MAX_NOTE;
        }

        Player.COLOR color = p.getTokenColor();
        double res = 0;
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

    private double checkAlignToken(Grid grid, int lengthAlign, Player.COLOR color, int c, int l, int H, int V){
        double res = 1;
        while(lengthAlign != 0 && res != 0){
            Token t = null;
            if(c < 0 || c > Grid.MAX_COLUMN - 1 || l < 0 || l > Grid.MAX_LINE - 1){
                res = 0;
            }else{
                t = grid.getGrid()[l][c];
            }

            if(t == null){
                res *= 0.5;
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
