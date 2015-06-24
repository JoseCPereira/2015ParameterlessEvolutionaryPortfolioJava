package com.z_PORTFOLIO;

//DESIGN PATTERN STRATEGY
public abstract class IEASolver{
	
	protected int 		 N;
	protected Population currentPopulation;
	protected int 		 currentGeneration;						// Current generation for this solver. This is updated by each nextGeneration() call.
	protected int    	 currentFitnessCalls;					// Number of fitness calls for this solver. This must be updated for all RandomPopulation() and replace() calls.
	protected long		 totalFitnessCalls;						// Total number of fitness calls for this solver. NOTE: This is not necessary. It's just extra information.
	protected double 	 avgFitness;							// Average fitness of the current population for this Solver. This is updated by each nextGeneration() call.
	protected boolean 	 dummy;	
	
	protected IEASolver(int currentN){
		this.N                   = currentN;
		this.currentPopulation   = new RandomPopulation(N);		// Initial random population. Fitness and statistics are automatically computed.
		this.currentGeneration   = 0;
		this.currentFitnessCalls = N;	
		this.totalFitnessCalls   = N;
		this.avgFitness 	     = 0;
		this.dummy 			     = false;
	}
	
	protected void updateFitnessCalls(int fitCalls){
		this.currentFitnessCalls += fitCalls;					// NOTE: ParRegister is responsible for reseting this value to zero at the end of each IEASolver generation.
		this.totalFitnessCalls   += fitCalls;					// NOTE: replace() computes the fitness only of the newIndividuals.
	}
	
	public int 	   					  getN(){return this.N;}
	public Population getCurrentPopulation(){return this.currentPopulation;}
	public int 		  getCurrentGeneration(){return this.currentGeneration;}
	public int      getCurrentFitnessCalls(){return this.currentFitnessCalls;}
	public long       getTotalFitnessCalls(){return this.totalFitnessCalls;}
	public double 	  		 getAvgFitness(){return this.avgFitness;}
	public boolean 			      getDummy(){return this.dummy;}
	
	public void resetCurrentFitnessCalls(){this.currentFitnessCalls = 0;}
	
	public void incrementCurrentGeneration(){currentGeneration++;}
	
	public void setDummy(boolean dum){dummy = dum;}
	
	public abstract void nextGeneration();						// This function computes the next 'currentPopulation'.  
																// It is also responsible for incrementing the value of 
																// 'currentGeneration' and updating the value of 'avgFitness'.	
}















