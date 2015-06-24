package com.z_PORTFOLIO;

import java.util.Random;


public class PORTFOLIO{
	private static String 	 portParamFile;						// Name of the main parameters file.
	public  static int    	 portRuns;							// Number of runs to perform with the same problem.
	public  static Random 	 random = new Random();				// Responsible for all random number operations.
	
	public static int		 nSuccess;							// Number of successful runs;
	public static PortEngine portEngine;
	public static void main(String[] args){		
		//random.setSeed(654324);								// This will fix the sequence of seeds that will be used on each run of the Portfolio.
		
		portParamFile = args[0];	
		nSuccess 	  = 0;
		portEngine    = new PortEngine(portParamFile);			// Initialize the Portfolio Engine.
		
		for(int nRun = 0; nRun < PORTFOLIO.portRuns; nRun++)	// Perform all runs of the Portfolio.
			portEngine.RUN(nRun);								
		
		PortPress.printFinalInfo();
		PortPress.closeTestFileOut();
	}		
}		
		

							
		





