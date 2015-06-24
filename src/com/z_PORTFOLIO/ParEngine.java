package com.z_PORTFOLIO;

import java.util.ArrayList;


public class ParEngine{	
	
	private boolean 			 inactive;				// This EA can be active or already inactive.
	private double  			 multiplier;			// Each EA uses this constant to compute its own maximum execution time allowed in each iteration. NOTE: All multipliers are initialized by portRegister.	
	private long				 overTime;				// Each EA uses this constant to compute its own maximum execution time allowed in each iteration.
	private IEAlgorithm 		 parAlgorithm;			// Chosen EA to perform the parameterless strategy. Design Pattern Strategy. 
	private ParRegister 		 parRegister;			// Responsible for storing and processing all information related with this Parameterless Engine.
	private ArrayList<IEASolver> parSolvers;			// The array of solvers on which to perform PEBS.
	private int 				 solverPosition;		// Current Solver position.
	private int 				 highestN;				// Highest current population size.
	private int 				 lastSolver;			// Position of the last active Solver;
	private boolean				 currentSolverStopped;	// Current Solver has stopped.
	
	private long 				 relayTime;
	
	public ParEngine(int initPopSize, IEAlgorithm parAlgorithm, ParRegister parRegister){		
		this.inactive 	 		  = false;					
		this.parAlgorithm 		  = parAlgorithm;
		this.parRegister  		  = parRegister;	
		this.parSolvers   		  = new ArrayList<IEASolver>();				// Initialize the list of active Solvers;
		this.parSolvers.add(this.parAlgorithm.newIEASolver(initPopSize));	// Initialize the first Solver.
		this.solverPosition		  = 0;										// Initialize the current Solver position.				
		this.highestN 	    	  = initPopSize;							// Highest current population size.
		this.lastSolver 		  = 0;										// Initialize the position of the last active Solver;
		this.currentSolverStopped = false;
		this.relayTime 			  = 0;
	}
		
	public boolean     		         inactive(){return this.inactive;}
	public double 			    getMultiplier(){return this.multiplier;}
	public IEAlgorithm 		  getParAlgorithm(){return this.parAlgorithm;}
	public ParRegister 		   getParRegister(){return this.parRegister;}
	public ArrayList<IEASolver> getParSolvers(){return this.parSolvers;}
	public int 				getSolverPosition(){return this.solverPosition;}
	public int 					  getHighestN(){return this.highestN;}
	public int 					getLastSolver(){return this.lastSolver;}
	
	public void     setActive(boolean act){this.inactive = !act;}
	public void setMultiplier(double mult){this.multiplier = mult;}
	
	public long computeMaxTimeAllowed(long maxTime){return (long)(multiplier * maxTime) - overTime;}

	
//	DEPRECATED:
//	public long runForOneGeneration(){
//		IEASolver currentSolver = parSolvers.get(0);
//		long startGenerationTime = PortTime.getPEBSTime();			
//		currentSolver.nextGeneration();								
//		long generationTime = PortTime.getPEBSTime() - startGenerationTime;
//		return generationTime;
//	}
	
	public long runEAFor(long maxTimeAllowed){	
		long startGenerationTime = 0;
		long generationTime		 = 0;
		long startRelayTime	     = 0;
		long currentTime		 = 0;
		do{
			IEASolver currentSolver = parSolvers.get(solverPosition);
			if(currentSolver.getDummy()){									// This is a DUMMY solver.
				if(solverPosition == 0){
					deleteSolvers(0);
					if(parSolvers.isEmpty())								// If there are no active Solvers we must generate the next one. 
						addNextSolver();
				}
				else{														// The Dummy Solver is in the mid section. Keep it there.
					currentSolver.currentGeneration++;						// Perform a Dummy generation.											
					parRegister.incrementTotalIterations();
					PortPress.printSolverCurrentInfo(parRegister, currentSolver, 0, solverPosition);
				}
			}
			else{																							// This is NOT a dummy solver. Perform PEBS.
				startGenerationTime = PortTime.getPEBSTime();														
				currentSolver.nextGeneration();																// Perform the next generation for the current Solver. Class Stopper is responsible for computing the value of 'stopped' in each iteration.				
				generationTime = PortTime.getPEBSTime() - startGenerationTime;								// Time that takes to perform one generation. 
																																					
				startRelayTime = PortTime.getPEBSTime();				
				parRegister.updateCurrentData(currentSolver, currentTime + generationTime, solverPosition);	// NOTE: At this point 'relayTime' refers to the previous iteration of the do-while loop. This "currentTime" is used to update ParRegister.maxGenerationTime which in turn is used to compute PortEngine.maxTime.			
				parRegister.updateMaxGenerationTime(relayTime + generationTime);
				PortPress.printSolverCurrentInfo(parRegister, currentSolver, relayTime + generationTime, solverPosition);					
				this.currentSolverStopped = ParStopper.criteria(parRegister, currentSolver);				// Check if the currentSolver already satisfies its stop criteria. Also responsible for updating the value of PortStopper.success.				
				
				if(this.currentSolverStopped && ParStopper.foundOptimum()){									// The current Solver has stopped because the optimum was found.
					this.inactive = true;
					relayTime 	  = PortTime.getPEBSTime() - startRelayTime;
					currentTime  += relayTime + generationTime; 											// This time slot has ended, updated relayTime, currentTime and get out.
					parRegister.updateTotalParTime(currentTime);
					return currentTime;
				}
				
				if(this.currentSolverStopped && !ParStopper.foundOptimum()){								// The current Solver has stopped without finding the optimum solution.
					stopSolvers(solverPosition, solverPosition);	
					if(parSolvers.isEmpty())																// If there are no active Solvers we must generate the next one. 
						addNextSolver();	
				}
				else{																						// NOT this.currentSolverStopped
					if(solverPosition > 0){																	// Check Fitness Invariant.
						boolean invariant = true;
						int i = 0;
						do{
							IEASolver previousSolver = parSolvers.get(i);
							if(!previousSolver.getDummy())
								invariant = previousSolver.getAvgFitness()        > currentSolver.getAvgFitness() || 	
											previousSolver.getTotalFitnessCalls() < currentSolver.getTotalFitnessCalls();
							if(!invariant){																	// Current Solver has a better average fitness and performed fewer fitness calls.
								stopSolvers(i, solverPosition-1);											// Fitness Invariant does not hold for Solver 'i'. Stop Solver 'i' and all Solvers in between.
								break;																		// Found the first Solver that does not comply with 'invariant'. 
							}																				// Make all solvers from this position up to currentSolver (exclusive) Dummy.
							i++;
						}while(invariant && i < solverPosition);	
					}																						// END: if(solverPosition > 0)
				}																							// END: else (!this.currentSolverStopped)
			}																								// END: NOT a Dummy Solver.
			
			if(currentSolver.getCurrentGeneration() % PortEngine.nextSolver == 0){							// It is time to advance to the next Solver, using PEBS.
				solverPosition++;	
				if(solverPosition > lastSolver)																// If there is no active next Solver we must generate the next one.
					addNextSolver();
			}
			else																							// Keep using the first Solver.											
				solverPosition = 0;	
			
			relayTime    = PortTime.getPEBSTime() - startRelayTime;											// NOTE: At this point 'relayTime' refers to the current iteration of the do-while loop.			
			currentTime += generationTime + relayTime;														// NOTE: At this point 'currentTime' refers to the total time that took to perform all generations up to this moment.	 
			overTime 	 = currentTime - maxTimeAllowed;	
		}while(overTime < 0);																				// END: This time slot.
		
		parRegister.updateTotalParTime(currentTime);
		return currentTime;																					// This EA has yet to stop.
	}					 																					// END: runEAFor(...).								
	
	
	private void stopSolvers(int i, int j){
		if(i == 0)
			deleteSolvers(j);
		else
			for(int k = i; k <= j; k++)
				parSolvers.get(k).setDummy(true);
	}
	
	private void deleteSolvers(int j){
		parSolvers.subList(0, j+1).clear();																	// Keep Fitness Invariant.
		lastSolver -= (j+1);																				// Update the number of active Solvers.
		solverPosition = 0;																					// Update the current Solver position.
	}
	
	private void addNextSolver(){
		highestN *= 2;
		parSolvers.add(parAlgorithm.newIEASolver(highestN));			
		lastSolver++;
	} 
		
	public void reset(){
		this.inactive = false;
		parRegister.reset();
	}
	
}// END: ParEngine class.
















