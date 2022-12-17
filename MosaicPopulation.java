/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mosaicga;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author reinh
 */
public class MosaicPopulation implements GAPopulation{

    private final double mutationRate;
    private final double crossoverRate;
    private final int carryOver;
    private GAIndividual[] population;
    private MosaicProblemBoard problemBoard;
    private int boardSize;
    private final double[] rouletteChance;
    Random rand;

    /**
     * Konstruktor
     * 
     * @param populationSize besar populasi.
     * @param crossoverRate peluang persilangan berhasil.
     * @param mutationRate peluang mutasi terjadi.
     * @param carryOver banyak individu yang akan dipertahankan ke generasi berikutnya.
     * @param problemBoard masalah yang ingin diselesaikan.
     * @param rand objek Random untuk menggenerasi angka-angka random.
     */
    public MosaicPopulation(int populationSize, double crossoverRate,
            double mutationRate, int carryOver,
            MosaicProblemBoard problemBoard, Random rand) {
        //Inisialisasi
        this.population = new MosaicIndividual[populationSize];
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.carryOver = carryOver;
        this.rand = rand;
        this.problemBoard = problemBoard;
        this.boardSize = problemBoard.getBoardSize();
        this.rouletteChance = new double[populationSize];

        this.generateFirstPopulation(); // Isi populasi untuk pertama kali.
    }

    /**
     * Method yang berfungsi men-generate individu baru dengan kromosom yang
     * diisi secara acak. Digunakan ketika membuat populasi pertama.
     *
     * @return individu baru.
     */
    private MosaicIndividual generateRandomIndividual() {
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
        return new MosaicIndividual(chromosome, this.problemBoard, this.rand);
    }

    /**
     * Method untuk mengisi populasi pertama.
     */
    private void generateFirstPopulation() {
        for (int i = 0; i < this.population.length; i++) {
            this.population[i] = this.generateRandomIndividual();
        }
        this.sortPopulationAsc();
        this.calculateChanceRoulette();
    }

    public int getTopFitness() {
        return this.population[this.population.length - 1].getFitness();
    }

    public void repopulate() {
        int populationSize = this.population.length;
        int limit = populationSize - this.carryOver;
        GAIndividual[] newPopulation = new MosaicIndividual[populationSize];
        for (int i = 0; i < limit; i += 2) {
            GAIndividual[] parents = this.pickParents();
            GAIndividual[] offsprings = this.reproduceOffsprings(parents);
            newPopulation[i] = offsprings[0];
            if ((i + 1) < limit) {
                newPopulation[i + 1] = offsprings[1];
            }
        };
        for(int i = limit; i < populationSize; i++){
            newPopulation[i] = this.population[i];
        }
        this.population = newPopulation;
        this.sortPopulationAsc();
        this.calculateChanceRoulette();
    }
    
    public GAIndividual getFittestIndividual(){
        return this.population[this.population.length - 1];
    }

    public GAIndividual[] reproduceOffsprings(GAIndividual[] parents) {
        GAIndividual[] offsprings = parents[0].crossover(parents[1],
                this.crossoverRate);
        offsprings[0].mutate(this.mutationRate);
        offsprings[1].mutate(this.mutationRate);
        return offsprings;
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

    private double getPopulationNormalizedFitness() {
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
        double prevChance = 0;
        int maxFitness = this.problemBoard.getSolutionFitness();
        int minFitness = this.problemBoard.getMinFitness();
        double populationFitnessNormalized = this.getPopulationNormalizedFitness();
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
        GAIndividual[] pickedParents = new MosaicIndividual[2];

        while (pickedParents[0] == null) {
            pickedParents[0] = this.pickIndividualRoulette(rouletteChance);
        }
        while (pickedParents[1] == null) {
            pickedParents[1] = this.pickIndividualRoulette(rouletteChance);
        }

        return pickedParents;
    }

    private void sortPopulationAsc() {
        Arrays.sort(this.population);
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
