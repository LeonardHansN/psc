/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mosaicga;

/**
 * Interface untuk individu pada algoritma genetik. Berisi method abstrak yang 
 * diperlukan pada algoritma genetik.
 * 
 * @author reinh
 */
public interface GAIndividual {
    /**
     * @return Fitness individu
     */
    int getFitness();
    
    /**
     * @return mengembalikan kromosom individu.
     */
    Object getChromosome();
    
    /**
     * Method untuk melakukan persilangan
     * 
     * @param otherIndividual individu yang akan disilangkan dengan individu 
     *                        dari mana method dipanggil.
     * @param crossoverRate Tingkat keberhasilan persilangan.
     * @return array berisi null jika gagal, array berisi 2 individu baru jika 
     *         berhasil
     */
    GAIndividual[] crossover(GAIndividual otherIndividual, double crossoverRate);
    
    /**
     * Method untuk melakukan mutasi
     * 
     * @param rate Tingkat terjadinya mutasi.
     */
    void mutate(double rate);
    
    /**
     * Method debugging dengan mengembalikan chromosome ke layar.
     */
    void printChromosome();
}
