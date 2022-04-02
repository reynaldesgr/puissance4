package src;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import src.Grid.DIRECTION;
import src.Player.COLOR;

/* Classe dédiée aux opérations de sauvegarde de la grille. */
public class FileGridSaver {

    /* Sauvegarde de la grille dans un fichier .txt contenu dans "path" */
    public void save(String path, Grid grid, Player old_J1, Player old_J2){
        try{
            RandomAccessFile fw = new RandomAccessFile(path, "rw");
            if(fw.length() != 0) fw.setLength(0);
            fw.writeChars(old_J1.getTokenColor().toString() + " " + old_J2.getTokenColor().toString() + "\n");
            fw.writeChars(grid.findNeighborsGrid());
            fw.seek(0);
            fw.close();
        }catch(IOException ioe){
            System.err.println("IOException: " + ioe.getMessage());
        }
    }

    /* Chargement de la grille d'un fichier de sauvegarde .txt contenu dans "path" */
    public Grid load(String path, Player new_J1, Player new_J2){
        Grid grid = new Grid();
        try{
            RandomAccessFile fw = new RandomAccessFile(path, "r");
            fw.seek(0);
            String line;
            line = fw.readLine().replace("\u0000", "");
            String[] line_sep = line.split(" ");
            List<String> line_sep_list = Arrays.asList(line_sep);

            Iterator<String> it = line_sep_list.iterator();
            if(it != null) new_J1.setColor(COLOR.valueOf(it.next()));
            if(it != null) new_J2.setColor(COLOR.valueOf(it.next()));
           
            if(fw.length() != 0){
                Grid loaded_grid = new Grid();
                while(fw.getFilePointer() != fw.length()){
                    line = fw.readLine().replace("\u0000", ""); 
                    line_sep = line.split(" ");
                    line_sep_list = Arrays.asList(line_sep);
                    it = line_sep_list.iterator();

                    int l = Integer.parseInt(it.next().trim());
                    int c = Integer.parseInt(it.next().trim());

                    Token t = new Token(COLOR.valueOf(it.next()));
                    if(loaded_grid.getGrid()[l][c] == null){ loaded_grid.getGrid()[l][c] = t;}
                    System.out.println(line_sep_list);
                    while(it.hasNext()){
                        COLOR C = COLOR.valueOf(it.next()); 
                        DIRECTION D = DIRECTION.valueOf(it.next());   
                        Token tok_nbhood = new Token(C);        
                        if(D.equals(DIRECTION.LEFT)){ 
                            loadNeighbors(t, tok_nbhood, D, DIRECTION.RIGHT, loaded_grid, l, c, 0, -1);
                        }else if(D.equals(DIRECTION.RIGHT)){
                            loadNeighbors(t, tok_nbhood, D, DIRECTION.LEFT, loaded_grid, l, c, 0, 1);
                        }else if(D.equals(DIRECTION.UP)){
                            loadNeighbors(t, tok_nbhood, D, DIRECTION.DOWN, loaded_grid, l, c, -1, 0);
                        }else if(D.equals(DIRECTION.DOWN)){
                            loadNeighbors(t, tok_nbhood, D, DIRECTION.UP, loaded_grid, l, c, 1, 0);
                        }else if(D.equals(DIRECTION.UP_RIGHT)){
                            loadNeighbors(t, tok_nbhood, D, DIRECTION.DOWN_LEFT, loaded_grid, l, c, -1, 1);
                        }else if(D.equals(DIRECTION.UP_LEFT)){
                            loadNeighbors(t, tok_nbhood, D, DIRECTION.DOWN_RIGHT, loaded_grid, l, c, -1, -1);
                        }else if(D.equals(DIRECTION.DOWN_RIGHT)){
                            loadNeighbors(t, tok_nbhood, D, DIRECTION.UP_LEFT, loaded_grid, l, c, 1, 1);
                        }else if(D.equals(DIRECTION.DOWN_LEFT)){
                            loadNeighbors(t, tok_nbhood, D, DIRECTION.UP_RIGHT, loaded_grid, l, c, 1, -1);
                        }
                    }    
                }  
                grid = loaded_grid;
                fw.close(); 
            }
        }catch(IOException ioe){
            System.err.println("IOException: " + ioe.getMessage());
        }
        return grid;
    }

    /* Permet de réinitialiser les voisins de chaque jeton d'une grille qui vient d'être chargé */
    public void loadNeighbors(Token t, Token tok_nbhood, DIRECTION D, DIRECTION D_nbhood, Grid loaded_grid, int l, int c, int offx, int offy){
        if(loaded_grid.getGrid()[l + offx][c + offy] == null){
            loaded_grid.getGrid()[l][c] = t;
            loaded_grid.getGrid()[l + offx][c + offy] = tok_nbhood;
            t.setNeighbors(D, tok_nbhood);
            tok_nbhood.setNeighbors(D_nbhood, loaded_grid.getGrid()[l][c]);
        }else{
            t.setNeighbors(D, loaded_grid.getGrid()[l + offx][c + offy]);
            loaded_grid.getGrid()[l + offx][c + offy].setNeighbors(D_nbhood, t);
        }

    }  
    
    /* Permet de décharger la sauvegarde (nettoie le fichier le sauvegarde) */
    public void unload(String path){
        try{
            RandomAccessFile fw = new RandomAccessFile(path, "rw");
            fw.setLength(0);
            fw.close();
        }catch(IOException ioe){
            System.err.println("IOException: " + ioe.getMessage());
        }
    } 
}
