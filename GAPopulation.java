/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mosaicga;

/**
 * Interface untuk populasi pada algoritma genetik. Berisi method abstrak yang 
 * diperlukan pada algoritma genetik.
 * 
 * @author reinh
 */
public interface GAPopulation {
    /**
     * @return Fitness terbaik populasi.
     */
    int getTopFitness(); 
    
    /**
     * Mengisi populasi dengan populasi generasi berikutnya.
     */
    void repopulate();
    
    /**
     * @return individu dengan fitness terbaik.
     */
    GAIndividual getFittestIndividual();
    
    /*
     * @return Fitness total dari populasi.
     */
    int getPopulationFitness();
}
