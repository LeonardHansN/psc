package mosaicga;

import java.util.ArrayList;

/**
 * Kelas yang merepresentasikan papan (board) puzzle yang akan diselesaikan
 * dengan genetic algorithm. Pada sebuah papan, tile angka direpresentasikan
 * dengan karakter angka tersebut, sedangkan tile yang kosong direpresentasikan
 * dengan karakter 'w', yang diambil dari inisial kata "white".
 *
 * @author reinh
 */
public class MosaicProblemBoard {

    public static final char[] TILE_NUMBERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8'}; // TILE_NUMBERS adalah nomor-nomor yang mungkin ada pada sebuah tile. Digunakan untuk perhitungan fitness.
    private final char[][] boardSetup; // Array yang merepresentasikan papan puzzle yang akan diselesaikan sesuai penjelasan kelas. 
    private int solutionFitness;  // Fitness yang diharapkan dari solusi untuk papan.
    private int minFitness; //Fitness minimal dari board, yaitu ketika jumlah tile hitam pada tiap tile angka melebihi angka pada tile angka tersebut.
    private int boardSize;  // Ukuran dari board.
    private ArrayList<int[]> numTileCoords;

    /**
     * Konstruktor.
     *
     * @param boardSetup merupakan kondisi awal board yang akan diselesaikan.
     */
    public MosaicProblemBoard(char[][] boardSetup) {
        this.numTileCoords = new ArrayList();
        this.boardSetup = boardSetup;
        this.boardSize = boardSetup[0].length;
        this.computeMaxMinFitness();
    }

    public char[][] getBoardSetup() {  // Getter array boardSetup
        return boardSetup;
    }

    public int getBoardSize() { // Getter ukuran board
        return this.boardSize;
    }

    public int getSolutionFitness() {// Getter solutionFitness
        return this.solutionFitness;
    }

    public int getMinFitness(){
        return this.minFitness;
    }
    
    public int getNumTile(int row, int col) {
        return boardSetup[row][col] - 48;
    }
    
    public ArrayList<int[]> getNumTileCoords(){
        return this.numTileCoords;
    }

    private void computeMaxMinFitness() {
        int maxFitness = 0;
        int minFitness = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (Character.isDigit(boardSetup[i][j])) {                    
                    maxFitness += ((boardSetup[i][j] - 48) * 5); 
                    minFitness -= 10;
                    this.numTileCoords.add(new int[]{i,j});
                }
            }
        }
        this.solutionFitness = maxFitness; 
        this.minFitness = minFitness;
    }
    

    public void debug() {
        System.out.println("Puzzle: ");
        for (int i = 0; i < boardSetup.length; i++) {
            for (int j = 0; j < boardSetup.length; j++) {
                System.out.print(boardSetup[i][j]);
            }
            System.out.println();
        }
    }
}
