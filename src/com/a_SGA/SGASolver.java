package com.a_SGA;

import com.z_PORTFOLIO.Individual;
import com.z_PORTFOLIO.IEASolver;
import com.z_PORTFOLIO.SelectedSet;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Each concrete Solver inherits the following fields from the abstract class IEASolver:
//
// 		int 	   N;
//		Population currentPopulation;
//		int 	   currentGeneration;		// Current generation for this solver. This is updated by each nextGeneration() call.
//		int    	   urrentFitnessCalls;		// Number of fitness calls for this solver. This must be updated for all RandomPopulation() and replace() calls.
//		double 	   avgFitness;				// Average fitness of the current population for this Solver. This is updated by each nextGeneration() call.
//		boolean    dummy;					// Used by PEBS to determine if a parSolver is still active.
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class SGASolver extends IEASolver{
	private Selection 	 selection;		 				// NOTE: Use Parameter.initializeSelector()    to generate the chosen selector type.
	private Crossover 	 crossover;						// NOTE: Use Parameter.initializeCrossover()   to generate the chosen crossover type.
	private Mutation 	 mutation;						// NOTE: Use Parameter.initializeMutation()    to generate the chosen mutation type.
	private IReplacement replacement; 					// NOTE: Use Parameter.initializeReplacement() to generate the chosen replacement type.
	
	
	public SGASolver(String paramFile, int currentN){		
		super(currentN);								// Call constructor in abstract class IEASolver
		Parameter.initializeParameters(paramFile);  	// Initialize and validate SGA parameters
		selection   = Parameter.initializeSelection(N);
		crossover   = Parameter.initializeCrossover();
		mutation    = Parameter.initializeMutation();
		replacement = Parameter.initializeReplacement(currentN);
	}
	
	public void nextGeneration(){
		currentGeneration++;
		SelectedSet selectedSet = selection.select(currentPopulation);		// 1. SELECTION.
		Individual[] newIndividuals = crossover.cross(selectedSet);			// 2. CROSSOVER.
		mutation.mutate(newIndividuals);									// 3. MUTATION.	 
		replacement.replace(currentPopulation, newIndividuals);				// 4. REPLACEMENT. NOTE: This function is responsible for updating the information about the best individual. 
		
		updateFitnessCalls(newIndividuals.length);							// NOTE: replace() computes the fitness only of the newIndividuals.
		currentPopulation.computeUnivariateFrequencies();						
		avgFitness = currentPopulation.computeAvgFitness();					// NOTE: Every nextGeneration() must compute the average fitness of its current Population!
																			//		 No need to update information about the best individual. Replacement is responsible for that.		 		
	}
}







