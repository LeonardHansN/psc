/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mosaicga;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author reinh
 */
public class MosaicIndividual implements GAIndividual, Comparable<MosaicIndividual> {

    private char[][] blackTiles; // Kromosom
    private MosaicProblemBoard problemBoard;
    private ArrayList<int[]> numTileCoords;
    private int individualFitness;
    private final Random rand;

    public MosaicIndividual(char[][] blackTiles, MosaicProblemBoard problemBoard,
            Random rand) {
        this.blackTiles = blackTiles;
        this.problemBoard = problemBoard;
        this.numTileCoords = problemBoard.getNumTileCoords();
        this.rand = rand;
        this.individualFitness = 0;
        this.computeFitness();
    }

    @Override
    public int getFitness() {
        return this.individualFitness;
    }

    @Override
    public Object getChromosome() {
        return this.blackTiles;
    }

    @Override
    /**
     * Wrapper yang memanggil dan mengembalikan hasil dari method
     * singlePointCO().
     */
    public GAIndividual[] crossover(GAIndividual otherIndividual,
            double crossoverRate) {
        GAIndividual[] offsprings = this.singlePointCO(
                (MosaicIndividual) otherIndividual, crossoverRate);
        return offsprings;
    }

    @Override
    public void mutate(double rate) {
        double mutationChance = rand.nextDouble();
        if (mutationChance <= rate) {
            this.bitStringMutation();
        }
    }

    /**
     * Melakukan persilangan individu dengan individu lain.
     *
     * @param otherIndividual individu yang akan disilangkan dengan individu
     * ini.
     * @return array berisi 2 keturunan.
     */
    private GAIndividual[] singlePointCO(MosaicIndividual otherIndividual,
            double rate) {
        GAIndividual[] offsprings = new MosaicIndividual[2];
        double chance = rand.nextDouble();

        if (chance <= rate) {
            int size = this.problemBoard.getBoardSize();

            char[][] parentChromosome1 = this.blackTiles;
            char[][] parentChromosome2 = (char[][]) otherIndividual.getChromosome();

            char[][] childChromosome1 = new char[size][size];
            char[][] childChromosome2 = new char[size][size];

            int crossoverPointRow = this.rand.nextInt(size);
            int crossoverPointCol = this.rand.nextInt(size);

            // Isi kromosom dari keturunan dengan alel-alel induk bernomor sama hingga batas crossoverPointRow
            for (int row = 0; row < crossoverPointRow; row++) {
                for (int col = 0; col < size; col++) {
                    childChromosome1[row][col] = parentChromosome1[row][col];
                    childChromosome2[row][col] = parentChromosome2[row][col];
                }
            }

            // Mengisi row sampai batas crossoverPointCol dengan kromosom induk bernomor sesuai.
            for (int col = 0; col < crossoverPointCol; col++) {
                childChromosome1[crossoverPointRow][col] = parentChromosome1[crossoverPointRow][col];
                childChromosome2[crossoverPointRow][col] = parentChromosome2[crossoverPointRow][col];
            }

            // Mengisi row dari kolom crossoverPointCol sampai batas size dengan kromosom induk bernomor lain.
            for (int col = crossoverPointCol; col < size; col++) {
                childChromosome1[crossoverPointRow][col] = parentChromosome2[crossoverPointRow][col];
                childChromosome2[crossoverPointRow][col] = parentChromosome1[crossoverPointRow][col];
            }

            // Lanjutkan mengisi kromosom mulai dari batas crossoverPointRow hingga terisi penuh, namun kali ini dengan alel dari parent yang lain.
            for (int row = crossoverPointRow + 1; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    childChromosome1[row][col] = parentChromosome2[row][col];
                    childChromosome2[row][col] = parentChromosome1[row][col];
                }
            }

            offsprings[0] = new MosaicIndividual(childChromosome1, this.problemBoard,
                    this.rand);
            offsprings[1] = new MosaicIndividual(childChromosome2, this.problemBoard,
                    this.rand);

            return offsprings;
        }

        offsprings[0] = this;
        offsprings[1] = otherIndividual;

        return offsprings;
    }

    private void bitStringMutation() {
        int rowPos = rand.nextInt(this.problemBoard.getBoardSize());
        int colPos = rand.nextInt(this.problemBoard.getBoardSize());
        if (this.blackTiles[rowPos][colPos] == 'w') {
            this.blackTiles[rowPos][colPos] = 'b';
        } else {
            this.blackTiles[rowPos][colPos] = 'w';
        }
    }

    /**
     * Menghitung fitness dari individu dan menyimpannya ke atribut individual-
     * Fitness. Fitness disimpang di atribut tersebut karena akan sering dipang-
     * gil untuk komputasi. Method ini dipanggil ketika objek diinisialisasi.
     */
    private void computeFitness() {
        for (int[] coords : this.numTileCoords) {
            int row = coords[0];
            int col = coords[1];
            this.individualFitness += this.computeTileFitness(row, col);
        }
    }

    /**
     * Method yang berfungsi menghitung fitness dari tile pada posisi yang di-
     * tentukan. Dipanggil oleh method computeFitness()
     *
     * @param row posisi tile
     * @param col posisi tile
     * @return fitness pada tile yang ditunjuk.
     */
    private int computeTileFitness(int row, int col) {
        int blackCount = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && j >= 0 && i < blackTiles[0].length
                        && j < blackTiles[0].length && blackTiles[i][j] == 'b') {
                    blackCount += 1;
                    if (blackCount > this.problemBoard.getNumTile(row, col)) {
                        return -10;
                    }
                }
            }
        }
        if (blackCount < this.problemBoard.getNumTile(row, col)) {
            return 3*blackCount;
        }
        return this.problemBoard.getNumTile(row, col) * 5;
    }

    /**
     * Method sederhana yang mengembalikan kromosom ke layar untuk debugging.
     */
    public void printChromosome() {
        for (int i = 0; i < blackTiles.length; i++) {
            for (int j = 0; j < blackTiles.length; j++) {
                System.out.print(blackTiles[i][j]);
            }
            System.out.println();
        }
    }

    @Override
    /**
     * Method yang meng-override method dari interface Comparable agar array of
     * MosaicIndividual dapat di-sort.
     */
    public int compareTo(MosaicIndividual o) {
        return this.getFitness() - o.getFitness();
    }
}
