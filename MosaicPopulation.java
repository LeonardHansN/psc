/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mosaicga;

import java.util.Random;

/**
 *
 * @author reinh
 */
public class MosaicPopulation {

    private GAIndividual[] population;
    private MosaicProblemBoard problemBoard;
    private int boardSize;
    Random rand;

    public MosaicPopulation(int populationSize, MosaicProblemBoard problemBoard, Random rand) {
        this.population = new GAIndividual[populationSize];
        
        this.rand = rand;
        this.problemBoard = problemBoard;
        this.boardSize = problemBoard.getBoardSize();
        this.generatePopulation();
        this.calculateChanceRoulette();
    }

    /**
     * Method yang berfungsi men-generate individu baru dengan kromosom yang
     * diisi secara acak. Digunakan ketika membuat populasi pertama.
     *
     * @return individu baru.
     */
    private GAIndividual generateIndividual() {
        char[][] chromosome = new char[this.boardSize][this.boardSize];
        for (int row = 0; row < this.boardSize; row++) {
            for (int col = 0; col < this.boardSize; col++) {
                if (rand.nextBoolean()) {
                    chromosome[row][col] = 'w';
                } else {
                    chromosome[row][col] = 'b';
                }
            }
        }
        return new MosaicIndividual(chromosome, problemBoard);
    }

    /**
     * Method untuk mengisi populasi pertama.
     */
    private void generatePopulation() {
        for (int i = 0; i < this.population.length; i++) {
            this.population[i] = this.generateIndividual();
        }
    }

    /**
     * Mengembalikan fitness dari sebuah individu pada index yang ditunjuk
     * dengan memanggil method getFitness() dari MosaicIndividual.
     *
     * @param idx index dari individu.
     * @return fitness dari individu pada posisi idx.
     */
    private int getIndividualFitness(int idx) {
        return this.population[idx].getFitness();
    }

    /**
     * Menghitung total fitness dari populasi ini.
     *
     * @return fitness dari populasi.
     */
    public int getPopulationFitness() {
        int totalFitness = 0;
        for (int i = 0; i < this.population.length; i++) {
            totalFitness += this.getIndividualFitness(i);
        }
        return totalFitness;
    }

    public double getPopulationNormalizedFitness() {
        double totalFitness = 0;
        int maxFitness = this.problemBoard.getSolutionFitness();
        int minFitness = this.problemBoard.getMinFitness();
        for (int i = 0; i < this.population.length; i++) {
            totalFitness += normalize(maxFitness, minFitness,
                    this.getIndividualFitness(i));
        }
        return totalFitness;
    }

    private double normalize(float max, float min, float x) {
        double normalized = (x - min) / (max - min);
        return normalized;
    }

    private GAIndividual pickIndividualRoulette(double[] rouletteChance) {
        double pickChance = rand.nextDouble();
        for (int i = 0; i < this.population.length; i++) {
            double individualChance = rouletteChance[i];
            if (pickChance <= individualChance) {
                return this.population[i];
            }
        }
        return null;
    }

    private double[] calculateChanceRoulette() {
        double[] rouletteChance = new double[this.population.length];
        double prevChance = 0;
        int maxFitness = this.problemBoard.getSolutionFitness();
        int minFitness = this.problemBoard.getMinFitness();
        double populationFitnessNormalized = this.getPopulationNormalizedFitness();
        System.out.println(populationFitnessNormalized);
        for (int i = 0; i < this.population.length; i++) {
            double individualFitnessNormalized = this.normalize(maxFitness,
                    minFitness, this.getIndividualFitness(i));
            double individualChance = individualFitnessNormalized
                    / populationFitnessNormalized + prevChance;
            prevChance = individualChance;
            rouletteChance[i] = individualChance;
        }
        return rouletteChance;
    }

    public GAIndividual[] pickParents() {
        double[] rouletteChance = this.calculateChanceRoulette();
        GAIndividual[] pickedParents = new GAIndividual[2];

        while (pickedParents[0] == null) {
            pickedParents[0] = this.pickIndividualRoulette(rouletteChance);
        }
        while (pickedParents[1] == null) {
            pickedParents[1] = this.pickIndividualRoulette(rouletteChance);
        }

        return pickedParents;
    }

    /**
     * Method yang berfungsi mengembalikan kromosom dari tiap individu pada
     * populasi untuk debugging.
     */
    public void printPopulation() {
        for (int i = 0; i < this.population.length; i++) {
            this.population[i].printChromosome();
            System.out.println("");
        }
    }
}
