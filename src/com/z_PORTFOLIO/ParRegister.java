package com.z_PORTFOLIO;


public class ParRegister{
	
	private int 		 index;										// Each ParEngine knows its own index. This index does not depend on whether the ParEngine 
																	// is active or inactive and it remains constant throughout the entirety of the portfolio run.
	private String		 EAName;									// Each ParEngine knows the name of its own EAlgorithm.
	private long		 maxGenerationTime;							// Use this to compute next Ti.
	private long 	  	 totalParTime;
	private long 	  	 eliminationTime;
	private int  	  	 totalIterations;
	private long	  	 currentFitnessCalls;
	private long 	  	 totalFitnessCalls;	
	private ParBestSoFar parBestSoFar;


	public ParRegister(int index, String EAName){					// NOTE: All other fields are automatically initialized to 0, has intended.
		this.index		  = index;		
		this.EAName		  = EAName;
		this.parBestSoFar = new ParBestSoFar();						
	}	 
																	
	public int 				   getIndex(){return this.index;}
	public String			  getEAName(){return this.EAName;}
	public long	   getMaxGenerationTime(){return this.maxGenerationTime;}
	public long   	    getTotalParTime(){return this.totalParTime;}
	public long 	 getEliminationTime(){return this.eliminationTime;}
	public int  	 getTotalIterations(){return this.totalIterations;}	
	public long  getCurrentFitnessCalls(){return this.currentFitnessCalls;}
	public long    getTotalFitnessCalls(){return this.totalFitnessCalls;}	
	public ParBestSoFar getParBestSoFar(){return this.parBestSoFar;}
	
	
	public void     	   setTotalParTime(long time){this.totalParTime    = time;}
	public void  	    setEliminationTime(long time){this.eliminationTime = time;}	
	public void    setTotalIterations(int iterations){this.totalIterations = iterations;}
	public void setCurrentFitnessCalls(long fitCalls){this.currentFitnessCalls = fitCalls;}
	public void   setTotalFitnessCalls(long fitCalls){this.totalFitnessCalls   = fitCalls;}	
	
	public void              resetTotalRunTime(){this.totalParTime  = 0;}
	public void       resetCurrentFitnessCalls(){this.currentFitnessCalls = 0;}
	
	public void incrementTotalParTime(long time){this.totalParTime += time;}
	public void 	  incrementTotalIterations(){this.totalIterations++;}
	
	public void updateCurrentData(IEASolver currentSolver, long currentTime, int solverPosition){				
		this.totalIterations++;
		int currentFitCalls 	  = currentSolver.getCurrentFitnessCalls();
		this.currentFitnessCalls += currentFitCalls;																// NOTE: PortRegister is responsible for reseting this value to zero at the end of each time block.	
		currentSolver.resetCurrentFitnessCalls();
		this.totalFitnessCalls   += currentFitCalls;																// NOTE: This value is only reset at the end of each Portfolio run.		
		this.parBestSoFar.updateBest(currentSolver, 
									 this.totalParTime + currentTime, 
									 this.totalIterations, 
									 this.totalFitnessCalls, 
									 solverPosition);
	}	
	
	public void updateMaxGenerationTime(long generationTime){
//		PortPress.printString("CurrentGenerationTime = " + generationTime + 
//							  "\nMaxGenerationTime = " + this.maxGenerationTime);
		if(this.maxGenerationTime < generationTime)
			this.maxGenerationTime = generationTime;
	}
	
	public void updateTotalParTime(long currentTime){
		this.totalParTime += currentTime;
		this.eliminationTime = this.totalParTime;
	}
	
	public void updateEliminationTime(long currentTime){this.eliminationTime = this.totalParTime + currentTime;}	// NOTE: PortRegister.keepPortInvariant() is responsible for updating eliminationTime.
	
	public void reset(){
		this.maxGenerationTime   = 0;
		this.totalParTime        = 0;
		this.eliminationTime     = 0;
		this.totalIterations     = 0;
		this.currentFitnessCalls = 0;
		this.totalFitnessCalls   = 0;	
		this.parBestSoFar.reset();
	}
}











