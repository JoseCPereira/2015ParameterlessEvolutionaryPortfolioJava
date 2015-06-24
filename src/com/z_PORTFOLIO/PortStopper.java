package com.z_PORTFOLIO;


public class PortStopper{
	
	public static int      maxIterations;
	public static long     maxFitnessCalls;
	public static long     maxTimeAllowed;
	
	private static boolean success = false;			// NOTE: ParStopper.foundOnes() and ParStopper.foundOptimum() 
													//		 are responsible for updating the value of success;
	
	public static boolean criteria(PortRegister portRegister){ 
		return (portRegister.getNActiveParEngines() == 0) 									            ||
			   ((maxIterations   == -1)? false : portRegister.getTotalIterations()   > maxIterations)   ||
			   ((maxFitnessCalls == -1)? false : portRegister.getTotalFitnessCalls() > maxFitnessCalls) ||
			   ((maxTimeAllowed  == -1)? false : portRegister.getTotalRunTime()      > maxTimeAllowed);
	}

	public static int    getMaxIterations(){return maxIterations;}
	public static long getMaxFitnessCalls(){return maxFitnessCalls;}
	public static long  getMaxTimeAllowed(){return maxTimeAllowed;}
	
	public static void setSuccess(boolean suc){success = suc;}
	
	public static boolean foundOptimum(){return success;}
	
}