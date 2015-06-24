package com.z_PORTFOLIO;


class ParBestSoFar{
	private long	   bestIndividualTime;		
	private int 	   bestIndividualIteration;
	private int 	   bestIndividualSolverPosition;
	private int 	   bestIndividualGeneration;
	private Population bestIndividualPopulation;
	private int 	   bestIndividualPosition;
	private double 	   bestIndividualFitness;
	private long	   bestIndividualFitnessCalls;
	
	private long	   bestAverageTime;		
	private int 	   bestAverageIteration;
	private int 	   bestAverageSolverPosition;
	private int 	   bestAverageGeneration;
	private Population bestAveragePopulation;
	private double 	   bestAverageFitness;
	private long	   bestAverageFitnessCalls;
	
	
	public ParBestSoFar(){
		this.bestIndividualFitness = Double.NEGATIVE_INFINITY;		// NOTE: All other variables are automatically
		this.bestAverageFitness    = Double.NEGATIVE_INFINITY;    	//		 initialized to 0, has intended. 
	}														
	
	public long		        getBestIndividualTime(){return this.bestIndividualTime;}
	public long		   getBestIndividualIteration(){return this.bestIndividualIteration;}
	public int    getBestIndividualSolverPosition(){return this.bestIndividualSolverPosition;}
	public int        getBestIndividualGeneration(){return this.bestIndividualGeneration;}
	public Population getBestIndividualPopulation(){return this.bestIndividualPopulation;}
	public int          getBestIndividualPosition(){return this.bestIndividualPosition;}
	public double        getBestIndividualFitness(){return this.bestIndividualFitness;}
	public long     getBestIndividualFitnessCalls(){return this.bestIndividualFitnessCalls;}
	
	public long		        getBestAverageTime(){return this.bestAverageTime;}
	public long		   getBestAverageIteration(){return this.bestAverageIteration;}
	public int    getBestAverageSolverPosition(){return this.bestAverageSolverPosition;}
	public int        getBestAverageGeneration(){return this.bestAverageGeneration;}
	public Population getBestAveragePopulation(){return this.bestAveragePopulation;}
	public double        getBestAverageFitness(){return this.bestAverageFitness;}
	public long     getBestAverageFitnessCalls(){return this.bestAverageFitnessCalls;}
	
	public void updateBest(IEASolver currentSolver, long bestTime, int iteration, long totalFitnessCalls, int solverPosition){
		Population currentPopulation = currentSolver.getCurrentPopulation();
		double currentBestIndividualFit = currentPopulation.getBestFit();
		if(currentBestIndividualFit > this.bestIndividualFitness){
			this.bestIndividualTime       	  = bestTime;
			this.bestIndividualIteration	  = iteration;			
			this.bestIndividualSolverPosition = solverPosition;
			this.bestIndividualGeneration     = currentSolver.getCurrentGeneration();
			this.bestIndividualPopulation 	  = currentPopulation;
			this.bestIndividualPosition   	  = currentPopulation.getBestPos();
			this.bestIndividualFitness 		  = currentBestIndividualFit;
			this.bestIndividualFitnessCalls	  = totalFitnessCalls;
		}
		double currentBestAverageFit = currentPopulation.getAvgFit();
		if(currentBestAverageFit > this.bestAverageFitness){
			this.bestAverageTime       	   = bestTime;
			this.bestAverageIteration	   = iteration;			
			this.bestAverageSolverPosition = solverPosition;
			this.bestAverageGeneration     = currentSolver.getCurrentGeneration();
			this.bestAveragePopulation 	   = currentPopulation;
			this.bestAverageFitness 	   = currentBestAverageFit;
			this.bestAverageFitnessCalls   = totalFitnessCalls;
		}
		
	}
	
	public void reset(){
		this.bestIndividualTime           = 0;		
		this.bestIndividualIteration      = 0;
		this.bestIndividualSolverPosition = 0;
		this.bestIndividualGeneration     = 0;
		this.bestIndividualPopulation     = null;
		this.bestIndividualPosition       = 0;
		this.bestIndividualFitness        = Double.NEGATIVE_INFINITY;;
	
		this.bestAverageTime           = 0;		
		this.bestAverageIteration      = 0;
		this.bestAverageSolverPosition = 0;
		this.bestAverageGeneration     = 0;
		this.bestAveragePopulation     = null;
		this.bestAverageFitness        = Double.NEGATIVE_INFINITY;;
	}
	
}






