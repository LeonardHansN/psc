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

    private final char[][] boardSetup; // Array yang merepresentasikan papan puzzle yang akan diselesaikan sesuai penjelasan kelas. 
    private int solutionFitness;  // Fitness yang diharapkan dari solusi untuk papan.
    private int minFitness; //Fitness minimal dari board, yaitu ketika jumlah tile hitam pada tiap tile angka melebihi angka pada tile angka tersebut.
    private int boardSize;  // Ukuran dari board.
    private ArrayList<int[]> numTileCoords; // ArrayList yang menampung koordinat tile yang berisi angka.

    /**
     * Konstruktor.
     *
     * @param boardSetup merupakan kondisi awal board yang akan diselesaikan.
     */
    public MosaicProblemBoard(char[][] boardSetup) {
        // Inisialisasi
        this.numTileCoords = new ArrayList(); 
        this.boardSetup = boardSetup;
        this.boardSize = boardSetup[0].length;
        
        this.computeMaxMinFitness(); // Menghitung fitness solusi dan fitness terkecil yang dapat diperoleh
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

    public int getMinFitness(){ // Getter fitness terendah.
        return this.minFitness;
    }
    
    public int getNumTile(int row, int col) { // Getter isi dari tile yang ditunjuk pada index row dan col yang diberikan
        return boardSetup[row][col] - 48;
    }
    
    public ArrayList<int[]> getNumTileCoords(){ // Mengembalikan ArrayList berisi koordinat-koordinat tile yang berisi angka.
        return this.numTileCoords;
    }

    /**
     * Menghitung fitness solusi(max) dan fitness minimal yang dapat diperoleh, 
     * dan menempatkannya ke atribut solutionFitness dan minFitness. Method ini
     * selalu dipanggil ketika objek MosaicProblemBoard dibuat.
     */
    private void computeMaxMinFitness() { 
        int maxFitness = 0;
        int minFitness = 0;
        for (int i = 0; i < boardSize; i++) { // Looping untuk mengakses semua tile pada papan.
            for (int j = 0; j < boardSize; j++) {
                /*
                Jika tile adalah digit, kurangi minFitness dengan 10, dan kali-
                kan angka dengan 5, lalu tambahkan ke maxFitness. Dengan begini,
                fitness dari solusi adalah 5 * (jumlah seluruh angka pada papan),
                dan fitness terkecil adalah -10 * (jumlah seluruh angka pada pa-
                pan)
                */
                if (Character.isDigit(boardSetup[i][j])) {                   
                    maxFitness += ((boardSetup[i][j] - 48) * 5); 
                    minFitness -= 10;
                    this.numTileCoords.add(new int[]{i,j}); // Tambahkan koordinat tile angka ke numTileCoords
                }
            }
        }
        this.solutionFitness = maxFitness; 
        this.minFitness = minFitness;
    }
    
    /**
     * Method untuk mengembalikan isi board ke papan. Hanya berfungsi untuk de-
     * bugging.
     */
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
