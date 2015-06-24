package com.z_PORTFOLIO;



/* All stop criteria by Martin Pelikan (2000)
 
 {PARAM_LONG,"maxNumberOfGenerations",&boaParams.maxGenerations,"200","Maximal Number of Generations to Perform",NULL},
 {PARAM_LONG,"maxFitnessCalls",&boaParams.maxFitnessCalls,"-1","Maximal Number of Fitness Calls (-1 when unbounded)",NULL},
 {PARAM_FLOAT,"epsilon",&boaParams.epsilon,"0.01","Termination threshold for the univ. freq. (-1 is ignore)",NULL},
 {PARAM_CHAR,"stopWhenFoundOptimum",&boaParams.stopWhenFoundOptimum,"-1","Stop if the optimum was found?", &yesNoDescriptor},
 {PARAM_FLOAT,"maxOptimal",&boaParams.maxOptimal,"-1","Percentage of opt. & nonopt. ind. threshold (-1 is ignore)",NULL},
 */

public class ParStopper{
	
	public static int      allFitnessEqual;					// Stop if all individuals have the same fitness. 			Default = -1 (ignore)
	public static float    epsilon;							// Termination threshold for the univariate frequencies. 	Default = -1 (ignore)
	public static float    maxOptimal;						// Proportion of optimal individuals threshold. 			Default = -1 (ignore)
	public static int      foundBestFit;  					// Stop if the optimum was found? 							Default = -1 (-1 -> no; 1 -> yes)
	public static float    foundOnes;						// Found individual with all ones (or zeros). 				Default = -1 (-1 -> ignore; 0 -> ZERO; 1 -> ONE)
								
	private static boolean success = false;					// NOTE: ParStopper.foundOnes() and ParStopper.foundOptimum() 
															//		 are responsible for updating the value of success.

	
	public static boolean criteria(ParRegister parRegister, IEASolver currentSolver){
		Population currentPopulation = currentSolver.getCurrentPopulation();
		return allFitnessEqual(currentPopulation)    || 
			   uniFreqConvergence(currentPopulation) || 
			   optimalThreshold(currentPopulation)   || 
			   foundBestFit(currentPopulation)		 ||     // NOTE: Responsible for updating the value of success.
			   foundOnes(currentPopulation); 				// NOTE: Responsible for updating the value of success.
	}
	
	
	private static boolean allFitnessEqual(Population population){
		return allFitnessEqual == -1 ? false : population.getBestFit() == population.getAvgFit();
	}
	
	private static boolean uniFreqConvergence(Population population){
		int N = population.getN();
		if(epsilon == -1)
			return false;
		for(int j = 0; j < Problem.n; j++){
			float uniFrequency = ((float)(population.getUniFrequencies(j)))/((float)N);
			if(uniFrequency < epsilon && uniFrequency > 1-epsilon)
				return false;
		}
		return true;
	}	
	
	private static boolean optimalThreshold(Population population){	// NOTE: optimalThreshold criteria assumes that stringSize is always the optimum value!
		int N = population.getN();
		if(maxOptimal == -1)
			return false;
		double[] fitness = population.getFitness();
		int nOptimum = 0;
		for(int i = 0; i < N; i++)
			if(fitness[i] == Problem.optimumValue)
				nOptimum++;
		return ((float)nOptimum)/((float)N) >= maxOptimal;
	}	
	
	public static boolean foundBestFit(Population population){ 		// NOTE: foundBestFit criteria assumes that stringSize is always the optimum value!
		if(foundBestFit == -1)
			return false;
		else if(population.getBestFit() == Problem.optimumValue){
			ParStopper.success = true;
			PortStopper.setSuccess(true);
			return true;
		}
		else return false;
	}
		
	public static boolean foundOnes(Population population){
		int N = population.getN();	
		if(foundOnes == 0){
			for(int i = 0; i < N; i++)
				if(population.getIndividual(i).isZero()){
					ParStopper.success = true;
					PortStopper.setSuccess(true);
					return true;
				}
		}
		else if(foundOnes == 1)
			for(int i = 0; i < N; i++)
				if(population.getIndividual(i).isOne()){
					ParStopper.success = true;
					PortStopper.setSuccess(true);
					return true;
				}
		return false;												// if(foundOnes == -1)
	}
	
	public static boolean foundOptimum(){return success;}
	
	public static void setSuccess(boolean suc){success = suc;}
	
}









