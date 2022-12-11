/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mosaicga;

import java.util.ArrayList;

/**
 *
 * @author reinh
 */
public class MosaicIndividual implements GAIndividual {

    private char[][] blackTiles; // Kromosom
    private MosaicProblemBoard problemBoard;
    private ArrayList<int[]> numTileCoords;
    private int individualFitness;

    public MosaicIndividual(char[][] blackTiles, MosaicProblemBoard problemBoard) {
        this.blackTiles = blackTiles;
        this.problemBoard = problemBoard;
        this.numTileCoords = problemBoard.getNumTileCoords();
        this.individualFitness = 0;
        this.computeFitness();
        
    }

    @Override
    public int getFitness() {
        return this.individualFitness;
    }

    private void computeFitness() {
        for (int[] coords : this.numTileCoords) {
            int row = coords[0];
            int col = coords[1];
            this.individualFitness += this.computeTileFitness(row, col);
        }
    }

    private int computeTileFitness(int row, int col) {
        int blackCount = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 &&  j >= 0 && i < blackTiles[0].length && 
                        j < blackTiles[0].length && blackTiles[i][j] == 'b') { 
                    blackCount += 1;
                    if (blackCount > this.problemBoard.getNumTile(row, col)){
                        return -10;
                    }
                }
            }
        }
        return blackCount * 5;
    }

    public void printChromosome() {
        for (int i = 0; i < blackTiles.length; i++) {
            for (int j = 0; j < blackTiles.length; j++) {
                System.out.print(blackTiles[i][j]);
            }
            System.out.println();
        }
    }
}
