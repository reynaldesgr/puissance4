package src;

import java.util.Random;


class IA extends Player
{
    private AlphaBetaIA alphaBetaIA = new AlphaBetaIA(new HeuristicAlign());

    public IA()
    { 
        super("IA"); 
    }

    public void chooseTokenColor(Player J)
    {
        Random  rand                = new Random();
        int     numberColorPlayer   = J.getNumberColor() + 1,
                numberIAColor       = rand.nextInt(5) + 1;

        while(numberIAColor == numberColorPlayer)
        {
            numberIAColor = rand.nextInt(5) + 1;
        }   
        this.setANSIColor(numberIAColor- 1); 
    }

    public void placeTokenOnGrid(Grid gameGrid)
    {
        gameGrid.placeToken( this, alphaBetaIA.bestColumnToPlay( gameGrid, this ), false );
    }
}