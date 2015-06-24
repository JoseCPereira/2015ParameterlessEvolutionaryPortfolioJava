package com.z_PORTFOLIO;

import java.util.ArrayList;

public class PortEngine{	
	public static long T0;									// Maximum reference time for the initial time slot, in milliseconds.
	public static double  			   alpha;				// Time multiplier for each algorithm in the portfolio.
	public static int 	  			   N0, N1;				// Initial population sizes to be used by the EDAs: N0 => SGA, UMDA; N1 => ECGA, HBOA.
	public static int 	  			   nextSolver;			// Number of generations until next Solver. 			
	public static Problem 			   problem;				// Problem to be solved.  
	public static ArrayList<ParEngine> portfolio;			// Array of all EA algorithms. This is tightly linked to 'portRegister'
	public static PortRegister         portRegister;		// Responsible for computing and storing all information necessary for a PortEngine run. 
															// To do so, portRegister communicates intensively with each EARegister.
	
	private long currentMaxTime;							// Necessary for updating the value of maxTime after each ParEngine iteration.
	private long maxTime;									// T0, T1, T2, ..., maxTime are the maximum reference times in each time slot.
	
	
	public PortEngine(String portParamFile){
		PortParameter.initializeParameters(portParamFile); 	// Initialize and validate Portfolio parameters.		
		problem = PortParameter.initializeProblem();		// Design Pattern Strategy.
		PortPress.initializePress();
		PortPress.printInitialInfo();
	}															
	
	private void initializePortfolio(){
		portfolio = new ArrayList<ParEngine>();
		//portfolio.add(new ParEngine(N0, new SGA("SGAParameters.txt"),   new ParRegister(0)));				// Each EA "knows" its own index in the portfolio.
		portfolio.add(new ParEngine(N0, new UMDA("UMDAParameters.txt"), new ParRegister(0, "P-UMDA")));		// NOTE: When integrating a new EA to the portfolio, it is only necessary to add a new line to this block,	
		portfolio.add(new ParEngine(N1, new ECGA("ECGAParameters.txt"), new ParRegister(1, "P-ECGA")));		// 	 	 choose the correct initial population size (N0 or N1),
		portfolio.add(new ParEngine(N1, new HBOA("HBOAParameters.txt"), new ParRegister(2, "P-HBOA")));		//		 and adjust the indexes (0, 1, 2, ...) accordingly.
		
		portRegister 		= new PortRegister();															// NOTE: PortRegister() is responsible for initializing the time multipliers of every ParEngine.		
		// DEPRECATED: T0   = 2 * portfolio.get(portRegister.getNEA() - 1).runForOneGeneration();
		// DEPRECATED: T0 = 2000;																			// NOTE: Change this initial value according to the dimension of the problem (n) and the size of selectedSet (NS). 
		this.currentMaxTime = 0;
		maxTime             = T0;
		ParStopper.setSuccess(false);
		PortStopper.setSuccess(false);
	}
	
	//public long      getT0(){return this.T0;}
	public long getMaxTime(){return this.maxTime;}
		
	public void updateCurrentMaxTime(long maxGenerationTime){
		if(this.currentMaxTime < maxGenerationTime)
			this.currentMaxTime = maxGenerationTime;
	}
		
	public void updateMaxTime(){
		maxTime      		= 2 * this.currentMaxTime;													// NOTE: Doubling currentMaxTime gives some leeway for the EAs to perform some more generations.
		this.currentMaxTime = 0;																		//		 Check 'Cinoc/Desktop/Project EAs/Parameterless EA/Parameterless Source&Tests/Optimize_Lobo2014/auto_eda_selection.cpp'
	}
		
		
	public void RUN(int nRun){		
		initializePortfolio();																			// Initialize the array of EAlagorithms and respective ParEngines.
		PortPress.printRunInitialInfo(nRun);
		this.runPortfolio(nRun);		
		PortPress.printRunFinalInfo(nRun);	
	} 
	
	public void runPortfolio(int nRun){		
		int timeSlot = 0;
		do{																								// Main loop: run all EAs in the Portfolio for each time slot.
			PortPress.printTimeSlotInitialInfo(timeSlot, maxTime, nRun);
			for(int i = 0; i < portRegister.getNEA(); i++){												// Secondary loop: run each EA in the Portfolio for the current max time allowed. 
				ParEngine currentParEngine = portfolio.get(i);
				long maxTimeAllowed = currentParEngine.computeMaxTimeAllowed(maxTime);		   			// Compute the maximum execution time allowed to each ParEngine in each iteration.
				PortPress.printParEngineInitialInfo(currentParEngine, maxTimeAllowed);
				
				if(!currentParEngine.inactive()){														// If not stopped, perform the current time slot with the current ParEngine.
					long currentTime = currentParEngine.runEAFor(maxTimeAllowed);							
					portRegister.updateCurrentData(currentParEngine.getParRegister(), currentTime);		// Responsible for reseting the value of currentFitCalls of the currentParEngine.				
					PortPress.printParEngineCurrentInfo(currentParEngine, maxTimeAllowed, currentTime);
					if(currentParEngine.inactive() && PortStopper.foundOptimum()){						// The optimum was found, increment by one the number of successes and proceed to the next run.
						PORTFOLIO.nSuccess++;						
						return;
					}
					if(currentParEngine.inactive() && !PortStopper.foundOptimum()){						// The currentParEngine stopped without finding the optimum. The run will continue with adjusted multipliers.
						PortEngine.portRegister.decrementNActiveParEngines();							// Decrement by one the number of active ParEngines in the portfolio.  
						PortEngine.portRegister.updateFirstActiveParEngine();
						PortEngine.portRegister.computeMultipliers();									// Recompute the time multipliers.
					}				
					portRegister.keepPortInvariant(i, currentTime);										// Keep INVARIANT: EAs Best Average Fitness must be in monotonic decreasing order in the Portfolio.
					updateCurrentMaxTime(currentParEngine.getParRegister().getMaxGenerationTime());		// NOTE: maxTime = max{(1/a^2)T(UMDA), (1/a)T(ECGA), T(HBOA)}										// Update the next value of maxTime.
				}																						// END: This timeSlot with current ParEngine, go to next ParEngine.
			}																							// END: This timeSlot altogether.
			updateMaxTime();
			PortPress.printTimeSlotFinalInfo(timeSlot);
			timeSlot++;		
		}while(!PortStopper.criteria(portRegister));													// END: This portfolio run.
	}
	
}// END: PortEngine class.



















