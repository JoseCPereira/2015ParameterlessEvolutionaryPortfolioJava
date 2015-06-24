package com.z_PORTFOLIO;

public class PortRegister{
	private int    nEA;
	private int    nActiveParEngines;
	private int	   firstActiveParEngine;
	private int    totalIterations;
	private long   totalFitnessCalls;
	private long   totalRunTime;
	
	private String bestIndividualEA;
	private int    bestIndividualEAIndex;
	private long   bestIndividualFitnessCalls;
	private long   bestIndividualTime;
	private double bestIndividualFitness;
	private long   bestIndividualTotalTime;
	private int	   bestIndividualPopSize;
	
	private String bestAverageEA;
	private int    bestAverageEAIndex;
	private long   bestAverageFitnessCalls;
	private long   bestAverageTime;
	private double bestAverageFitness;
	private long   bestAverageTotalTime;
	private int	   bestAveragePopSize;
	
	private long   untilEliminationTime;								// NOTE: This is used by this.keepPortInvariant() to compute the elimination time of a ParEngine.
	
	
	public PortRegister(){
		this.nEA 			       = PortEngine.portfolio.size();
		this.nActiveParEngines     = nEA;
		this.firstActiveParEngine  = 0;
		this.totalIterations       = 0;
		this.totalFitnessCalls     = 0;
		this.totalRunTime          = 0;
		this.untilEliminationTime  = 0;
		this.bestIndividualFitness = Double.NEGATIVE_INFINITY;
		this.bestAverageFitness    = Double.NEGATIVE_INFINITY;
		this.computeMultipliers();										// NOTE: portRegister is responsible for initializing the time multipliers for every ParEngine.
	}																	// NOTE: The update of the multipliers is made by a ParEngine calling computeMultipliers() 
																		//		 the moment it becomes inactive.
	public int                  getNEA(){return this.nEA;}
	public int    getNActiveParEngines(){return this.nActiveParEngines;}
	public int getFirstActiveParEngine(){return this.firstActiveParEngine;}
	public int      getTotalIterations(){return this.totalIterations;}	
	public long   getTotalFitnessCalls(){return this.totalFitnessCalls;}
	public long        getTotalRunTime(){return this.totalRunTime;}	
	
	public String	      getBestIndividualEA(){return this.bestIndividualEA;}
	public int	     getBestIndividualEAIndex(){return this.bestIndividualEAIndex;}
	public long getBestIndividualFitnessCalls(){return this.bestIndividualFitnessCalls;}
	public long 	    getBestIndividualTime(){return this.bestIndividualTime;}
	public double    getBestIndividualFitness(){return this.bestIndividualFitness;}
	public long    getBestIndividualTotalTime(){return this.bestIndividualTotalTime;}
	public long 	 getBestIndividualPopSize(){return this.bestIndividualPopSize;}
	
	public String	      getBestAverageEA(){return this.bestAverageEA;}
	public int	     getBestAverageEAIndex(){return this.bestAverageEAIndex;}
	public long getBestAverageFitnessCalls(){return this.bestAverageFitnessCalls;}
	public long	   	    getBestAverageTime(){return this.bestAverageTime;}
	public double    getBestAverageFitness(){return this.bestAverageFitness;}
	public long    getBestAverageTotalTime(){return this.bestAverageTotalTime;}
	public long 	 getBestAveragePopSize(){return this.bestAveragePopSize;}
	
	public void decrementNActiveParEngines(){this.nActiveParEngines--;}
	public void   	   incrementIterations(){this.totalIterations++;}
	public void incrementTotalFitnessCalls(){this.totalFitnessCalls++;}												
	
	public void updateCurrentData(ParRegister currentParRegister, long currentTime){
		this.totalIterations++;		
		this.totalRunTime      += currentTime;
		this.totalFitnessCalls += currentParRegister.getCurrentFitnessCalls();						
		currentParRegister.resetCurrentFitnessCalls();					// NOTE: This value is reset to zero at the end of each time block.
		updateBest(currentParRegister);
	}
	
	private void updateBest(ParRegister currentParRegister){
		ParBestSoFar parBestSoFar = currentParRegister.getParBestSoFar();
		double currentBestIndividualFit = parBestSoFar.getBestIndividualFitness();
		if(currentBestIndividualFit > this.bestIndividualFitness){
			this.bestIndividualEA	   		= currentParRegister.getEAName();
			this.bestIndividualEAIndex		= currentParRegister.getIndex();
			this.bestIndividualFitnessCalls = parBestSoFar.getBestIndividualFitnessCalls();
			this.bestIndividualTime	   		= parBestSoFar.getBestIndividualTime();
			this.bestIndividualFitness 		= currentBestIndividualFit;
			this.bestIndividualTotalTime	= currentParRegister.getTotalParTime();
			this.bestIndividualPopSize		= parBestSoFar.getBestIndividualPopulation().getN();
		}
		double currentBestAverageFit = parBestSoFar.getBestAverageFitness();
		if(currentBestAverageFit > this.bestAverageFitness){
			this.bestAverageEA			 = currentParRegister.getEAName();
			this.bestAverageEAIndex 	 = currentParRegister.getIndex();
			this.bestAverageFitnessCalls = parBestSoFar.getBestAverageFitnessCalls();
			this.bestAverageTime		 = parBestSoFar.getBestAverageTime();
			this.bestAverageFitness		 = currentBestAverageFit;
			this.bestAverageTotalTime	 = currentParRegister.getTotalParTime();
			this.bestAveragePopSize		 = parBestSoFar.getBestAveragePopulation().getN();
		}		
	}
	
	public void keepPortInvariant(int index, long currentTime){
		if(index == this.firstActiveParEngine)
			this.untilEliminationTime = 0;															// This is the beginning of the current timeSlot. Just reset untilEliminationTime. 
		else 
			if(index > this.firstActiveParEngine && this.nActiveParEngines > 1){
				this.untilEliminationTime += currentTime;
				double currentAvgFitness = PortEngine.portfolio.get(index).getParRegister().getParBestSoFar().getBestAverageFitness();
				int prev 				 = index - 1;
				do{
					ParEngine previousParEngine = PortEngine.portfolio.get(prev);
					if(!previousParEngine.inactive()){
						double previousAvgFitness = previousParEngine.getParRegister().getParBestSoFar().getBestAverageFitness();
						if(previousAvgFitness <= currentAvgFitness){
							previousParEngine.setActive(false);										// Responsible for updating the value of 'inactive'
							previousParEngine.getParRegister().updateEliminationTime(this.untilEliminationTime);	
							PortPress.printParEngineEliminationInfo(previousParEngine);				// NOTE: Do not change this order!
							decrementNActiveParEngines();
						}
					}
					prev--;
				}while(prev >= this.firstActiveParEngine && this.nActiveParEngines > 1);
				updateFirstActiveParEngine();														// NOTE: Do not change this order!
				computeMultipliers();
			}																						// END: if(index > first...)
	}
	
//	public void reset(){
//		for(int i = 0; i < nEA; i++)
//			PortEngine.portfolio.get(i).reset();
//		this.nActiveParEngines 	   = nEA;
//		this.firstActiveParEngine  = 0;
//		this.totalIterations   	   = 0;
//		this.totalFitnessCalls 	   = 0;
//		this.totalRunTime      	   = 0;	
//		this.untilEliminationTime  = 0;
//		this.bestIndividualFitness = Double.NEGATIVE_INFINITY;
//		this.bestAverageFitness    = Double.NEGATIVE_INFINITY;
//		this.computeMultipliers();
//	}
	
	public void updateFirstActiveParEngine(){
		for(int i = this.firstActiveParEngine; i < nEA; i++)
			if(!PortEngine.portfolio.get(i).inactive()){
				this.firstActiveParEngine = i;
				return;
			}
	}
	
	public void computeMultipliers(){																// NOTE: This method is called by a ParEngine, the moment the latter becomes inactive.
		ParEngine parEngine;
		int nActive = 0;
		for(int i = this.firstActiveParEngine; i < nEA; i++){
			parEngine = PortEngine.portfolio.get(i);
			if(!parEngine.inactive()){
				nActive++;
				parEngine.setMultiplier(Math.pow(PortEngine.alpha, nActiveParEngines - nActive));
			}
		}
	}
	
}









