/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mosaicga;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author reinh
 */
public class MosaicGA {
    
    
    private int computeTileMaxFitness(char tile) {     //method untuk memberi perkiraan nilai fitness pada tiap sel boardSetup 
        for (int i = 0; i < MosaicProblemBoard.TILE_NUMBERS.length - 1; i++) {        //iterasi untuk pengecekan sel jika merupakan sel hitam berangka 0 sampai 4
            if (tile == MosaicProblemBoard.TILE_NUMBERS[i]) {                    //jika merupakan sel hitam berangka,
                return ((tile - 48) * 5); //kembalikan 5 point berdasarkan pengali angka pada sel hitam tsb
            }
        }                    //jika bukan sel yang masuk dalam kriteria perhitungan
        return 0;               //kembalikan 0
    }

    public static char[][] reader(String path) { //method unuk membaca puzzle dengan memasukkan parameter path dari file
        char[][] board = new char[5][5];
        try {
            
            FileReader file = new FileReader(path); //instansiasi variable file dengan objek FileReader yang menerima parameter path dari file
            BufferedReader document = new BufferedReader(file); //instansiasi variable document dengan objek BufferedReader dengan parameter file
            String line;
            int i = 0;
            while ((line = document.readLine()) != null) {
                for (int x = 0; x < 5; x++) { //iterasi untuk memasukkan nilai tiap grid puzzle kedalam array
                    board[i][x] = line.charAt(x);
                }
                i++;
            }
        } catch (Exception e) {
            System.out.println("File: " + path + " tidak ditemukan");// ketika file gagal dibaca atau path tidak dapat ditemukan
            return new char[5][5];
        }
        return board; //kembalikkan array puzzle
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        char[][] board;
        String thisFilePath = new File("").getAbsolutePath();
        String boardPath = "\\src\\mosaicga\\board.txt";
        System.out.println(thisFilePath);
        board = reader(thisFilePath.concat(boardPath));
        MosaicProblemBoard mpb = new MosaicProblemBoard(board);
        System.out.println(mpb.getSolutionFitness());
        System.out.println(mpb.getMinFitness());
//        
//        String solPath = "\\src\\mosaicga\\sol.txt";
//        System.out.println(thisFilePath);
//        char[][] sol = reader(thisFilePath.concat(solPath));
//        MosaicIndividual mi = new MosaicIndividual(sol, mpb);
//        System.out.println(mi.getFitness());

        Random rand = new Random(3);

        MosaicPopulation mp = new MosaicPopulation(10, mpb, rand);
        mp.printPopulation();
        
        System.out.println("Parents");
        GAIndividual[] mparents = mp.pickParents();
        mparents[0].printChromosome();
        System.out.println("");
        mparents[1].printChromosome();
    }

}
