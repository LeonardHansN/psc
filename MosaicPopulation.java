/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mosaicga;

import java.util.Arrays;
import java.util.Random;

/**
 * Kelas untuk menyimulasikan populasi solusi puzzle mosaic dengan algoritma
 * genetik (GA).
 *
 * @author reinh
 */
public class MosaicPopulation implements GAPopulation {

    private final double mutationRate; // Tingkat terjadinya mutasi.
    private final double crossoverRate; // Tingkat terjadinya persilangan (crossover).
    private final int carryOver; // Banyak individu yang akan dipertahankan ke generasi berikutnya.
    private GAIndividual[] population; // Populasi pada satu waktu.
    private MosaicProblemBoard problemBoard; // Masalah yang ingin diselesaikan.
    private int boardSize; // Panjang dari board.
    Random rand; // Objek random untuk menggenerasi variabel-variabel primitif secara acak.

    /*
    Array yang menampung range peluang terpilihnya suatu individu dalam pemilih-
    an dengan roulette. 
    
    Contoh:
    Misalkan array ini menampung nilai nilai {0.3, 0.7, 1}. Maka peluang terpilih-
    nya individu 1 adalah 0.3 - 0 = 0.3, dan individu 1 akan terpilih jika angka
    double yang dipilih secara acak berada pada range [0, 0.3]. Demikian peluang 
    terpilihnya individu 2 adalah 0.7 - 0.3 - 0.4, dan individual 2 akan terpilih
    jika angka double yang dipilih secara acak berada pada range (0.3, 0.7].
     */
    private final double[] rouletteChance;

    /**
     * Konstruktor
     *
     * @param populationSize besar populasi.
     * @param crossoverRate peluang persilangan berhasil.
     * @param mutationRate peluang mutasi terjadi.
     * @param carryOver banyak individu yang akan dipertahankan ke generasi
     * berikutnya.
     * @param problemBoard masalah yang ingin diselesaikan.
     * @param rand objek Random untuk menggenerasi variabel-variabel secara
     * acak.
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
        char[][] chromosome = new char[this.boardSize][this.boardSize]; // Kromosom dari individu baru.
        for (int row = 0; row < this.boardSize; row++) { // Looping untuk mengisi semua gen kromosom.
            for (int col = 0; col < this.boardSize; col++) {
                if (rand.nextBoolean()) { // Memilih alel secara acak.
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
        this.sortPopulationAsc(); // Urutkan populasi.
        this.calculateChanceRoulette(); // Hitung peluang terpilih pada pemilihan roulette.
    }

    /**
     * Mengembalikan fitness terbaik dari sebuah individu pada populasi, yaitu
     * individu pada index terakhir (karena sudah diurutkan menaik berdasarkan
     * fitness)
     *
     * @return fitness individu.
     */
    public int getTopFitness() {
        return this.population[this.population.length - 1].getFitness();
    }

    /**
     * Method untuk mengganti populasi dengan populasi generasi berikutnya.
     */
    public void repopulate() {
        int populationSize = this.population.length;
        int limit = populationSize - this.carryOver; // Batas akhir iterasi. 
        GAIndividual[] newPopulation = new MosaicIndividual[populationSize]; // Array untuk menampung populasi baru sementara.

        /*
        Lakukan iterasi sampai limit. Index limit sampai terakhir pada 
        newPopulation akan diisi kemudian. Dipilih pertambahan i dengan 2 karena
        offspring yang dihasilkan tiap persilangan adalah 2.
         */
        for (int i = 0; i < limit; i += 2) { // 
            GAIndividual[] parents = this.pickParents(); // Memilih parent.
            GAIndividual[] offsprings = this.reproduceOffsprings(parents); // Rekombinasi gen parent.
            while (offsprings[0] == null || offsprings[1] == null) { // Lakukan rekombinasi sampai berhasil.
                parents = this.pickParents(); // Memilih parent.
                offsprings = this.reproduceOffsprings(parents);
            }
            newPopulation[i] = offsprings[0]; // Mengisi newPopulation pada index i dengan turunan pada index 0.
            if ((i + 1) < limit) { // Jika tidak lewat batas limit. Pengecekan dilakukan di sini karena i di-increment dengan 2. 
                newPopulation[i + 1] = offsprings[1]; // Mengisi newPopulation pada index i+1 dengan turunan pada index 1.
            }
        };

        for (int i = limit; i < populationSize; i++) { // Isi newPopulation pada index limit sampai terakhir dengan individu-individu terbaik. 
            newPopulation[i] = this.population[i];
        }

        this.population = newPopulation;
        this.sortPopulationAsc(); // Populasi perlu diurutkan kembali.
        this.calculateChanceRoulette(); // Dan juga dihitung kembali peluangnya.
    }

    /**
     * Mengembalikan individual dengan fitness terbaik.
     *
     * @return individual dengan fitness terbaik.
     */
    public GAIndividual getFittestIndividual() {
        return this.population[this.population.length - 1]; // Individu dengan fitness terbaik adalah individu di akhir array.
    }

    /**
     * Method untuk menyilangkan 2 individu untuk menghasilkan keturunan.
     *
     * @param parents Array 1d berisi 2 individu yang akan disilangkan.
     * @return Array 1d berisi 2 individu baru.
     */
    public GAIndividual[] reproduceOffsprings(GAIndividual[] parents) {
        GAIndividual[] offsprings = parents[0].crossover(parents[1],
                this.crossoverRate); // Pertama-tama lakukan persilangan.
        if (offsprings[0] != null & offsprings[1] != null) { // Jika berhasil, lakukan mutasi dengan peluang mutationRate
            offsprings[0].mutate(this.mutationRate);
            offsprings[1].mutate(this.mutationRate);
        }
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

    /**
     * Menghitung fitness total populasi yang sudah dinormalisasi. Digunakan
     * saat perhitungan peluang. Normalisasi dilakukan agar peluang berada pada
     * range [0.0, 1.0]
     *
     * @return
     */
    private double getPopulationNormalizedFitness() {
        double totalFitness = 0;
        int maxFitness = this.problemBoard.getSolutionFitness(); // Max fitness yaitu fitness solusi.
        int minFitness = this.problemBoard.getMinFitness(); // Min fitness sesuai penjelasan pada MosaicProblemBoard.java.
        for (int i = 0; i < this.population.length; i++) { // Loop untuk menambahkan totalFitness dengan setiap fitness individu pada populasi.
            totalFitness += normalize(maxFitness, minFitness,
                    this.getIndividualFitness(i));
        }
        return totalFitness;
    }

    /**
     * Method yang menghitung hasil normalisasi berdasarkan rumus normalisasi.
     *
     * @param max batas bawah
     * @param min batas atas
     * @param x bilangan yang akan dinormalisasi
     * @return
     */
    private double normalize(float max, float min, float x) {
        double normalized = (x - min) / (max - min);
        return normalized;
    }

    /**
     * Memilih individu acak menggunakan metode roulette.
     *
     * @return Individu terpilih.
     */
    private GAIndividual pickIndividualRoulette() {
        double pickChance = rand.nextDouble();
        for (int i = 0; i < this.population.length; i++) {
            double individualChance = rouletteChance[i];
            if (pickChance <= individualChance) {
                return this.population[i];
            }
        }
        return null;
    }

    /**
     * Method untuk menghitung peluang terpilihnya masing-masing individu sesuai
     * penjelasan di atas.
     */
    private void calculateChanceRoulette() {
        double prevChance = 0; // Variabel untuk menampung peluang individu sebelumnya sementara.
        int maxFitness = this.problemBoard.getSolutionFitness(); // Max fitness yaitu fitness solusi.
        int minFitness = this.problemBoard.getMinFitness(); // Min fitness sesuai penjelasan pada MosaicProblemBoard.java.
        double populationFitnessNormalized = this.getPopulationNormalizedFitness(); // Menyimpan fitness populasi ternormalisasi.
        for (int i = 0; i < this.population.length; i++) {
            double individualChance; // Variabel untuk menyimpan peluang individu terpilih.

            if (i < this.population.length - 1) { // Iterasi untuk mengisi array rouletteChance
                double individualFitnessNormalized = this.normalize(maxFitness,
                        minFitness, this.getIndividualFitness(i)); // Fitness individu harus dinormalisasi karena ada kemungkinan fitness yang diperoleh bernilai negatif.
                individualChance = individualFitnessNormalized
                        / populationFitnessNormalized + prevChance; // individualChance = (fitness individu) / (fitness populasi). Ditambahkan dengan peluang sebelumnya karena 
            } else { // Jika individual terakhir, maka masukkan 1 ke array. Hal ini karena biasanya hasil perhitungan tidak genap menjadi 1 karena representasi double yang terbatas.
                individualChance = 1; 
            }
            rouletteChance[i] = individualChance;

            prevChance = individualChance;
        }
    }

    /**
     * Method untuk memilih parent dengan memanfaatkan pickIndividualRoulette();
     * 
     * @return array 1d berisi 2 individu.
     */
    public GAIndividual[] pickParents() {
        GAIndividual[] pickedParents = new MosaicIndividual[2];

        while (pickedParents[0] == null) { // Pilih sampai berhasil.
            pickedParents[0] = this.pickIndividualRoulette();
        }
        while (pickedParents[1] == null) { // Pilih sampai berhasil.
            pickedParents[1] = this.pickIndividualRoulette();
        }

        return pickedParents;
    }

    /**
     * Method untuk menyortir array berdasarkan fitness secara menaik.
     */
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
