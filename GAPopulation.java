/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package mosaicga;

/**
 *
 * @author reinh
 */
public interface GAPopulation {
    int getTopFitness();
    void repopulate();
    GAIndividual getFittestIndividual();
    int getPopulationFitness();
}
