/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mosaicga;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author reinh
 */
public class MosaicGA {

    public static char[][] boardReader(String path) { //method unuk membaca puzzle dengan memasukkan parameter path dari file
        char[][] board = null;
        try {
            FileReader file = new FileReader(path); //instansiasi variable file dengan objek FileReader yang menerima parameter path dari file
            BufferedReader document = new BufferedReader(file); //instansiasi variable document dengan objek BufferedReader dengan parameter file
            String line = document.readLine();
            int length = line.length();
            board = new char[length][length];
            int row = 0;
            while (line != null) {
                for (int i = 0; i < length; i++) { //iterasi untuk memasukkan nilai tiap grid puzzle kedalam array
                    board[row][i] = line.charAt(i);
                }
                row ++;
                line = document.readLine();
            }
        } catch (Exception e) {
            System.out.println("File: " + path + " tidak ditemukan");// ketika file gagal dibaca atau path tidak dapat ditemukan
            return new char[5][5];
        }
        return board; //kembalikkan array puzzle
    }

    public static void main(String[] args) {
        String thisFilePath = new File("").getAbsolutePath();

        //Inisialisasi problem
        String boardPath = "/board.txt";
        char[][] problemBoardArray = boardReader(thisFilePath.concat(boardPath));
        MosaicProblemBoard mpb = new MosaicProblemBoard(problemBoardArray);
        int solutionFitness = mpb.getSolutionFitness();
        
        
        //Inisialisasi populasi.
        int populationSize = 100;
        double crossoverRate = 0.8;
        double mutationRate = 0.005;
        int carryOver = 5;
        Random rand = new Random(100);
        GAPopulation mp = new MosaicPopulation(populationSize, crossoverRate,
                mutationRate, carryOver, mpb, rand);
        
        String solPath = "/sol.txt";
        MosaicIndividual mi2 = new MosaicIndividual(boardReader(thisFilePath.concat(solPath)), mpb, rand);
        System.out.println(mi2.getFitness());
        
        mpb.debug();

        // Penyelesaian
        GAIndividual sol = null;
        int iterLimit = 5000000;
        int i;
        for (i = 0; i < iterLimit; i++) {
            int topFitness = mp.getTopFitness();
            //System.out.println("Iterasi ke-" + i + ", fitness terbaik = " + mp.getTopFitness());
            if (topFitness >= solutionFitness) {
                sol = mp.getFittestIndividual();
                break;
            }
            mp.repopulate();
        }
        if (sol == null) {
            sol = mp.getFittestIndividual();
        }

        sol.printChromosome();

        // char[][] tempSol = (char[][]) sol.getChromosome();
        // String filename = "solution.txt";
        
        // PrintWriter pw = new PrintWriter(filename, tempSol);
        // pw.save();
        
        System.out.println("Fitness dari solusi: " + solutionFitness);
    }

}
