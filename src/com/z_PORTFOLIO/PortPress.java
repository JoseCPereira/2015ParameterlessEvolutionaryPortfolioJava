package com.z_PORTFOLIO;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class PortPress{
	
	public static boolean		  verbose;				// Verbose mode prints all currentSolver info in each generation. Default = false.	
														// NOTE: 'verbose' is initialized by PortParameter.validateOptionValue().
	
	private static String         testFileName;			// This file stores all the information that is also printed in the console during an entire run.
	private static FileWriter     fstreamTest;
	private static BufferedWriter testFileOut;
	
	// TODO: Write STATS file!!
//	private static String         testFileNameStats;	// This file stores only the statistics necessary to generate graphics.
//	private static FileWriter     fstreamTestStats;
//	private static BufferedWriter testFileOutStats;
	
	
	private static int			  indentSize = 7;
	private static String		  indent;
	
	public static void initializePress(){
		indent   = "";
		for(int i = 0; i < indentSize; i++)
			indent += " ";
		String str = "Runs" + PORTFOLIO.portRuns + "_T0" + PortEngine.T0 + "_alpha" + PortEngine.alpha + 
				     "_" + Problem.problemName + "_n" + Problem.n + ".txt";
		if(PortEngine.nextSolver > 99){
			testFileName      = "PORT-STANDALONE_"       + str;
			//testFileNameStats = "STATS_PORT-STANDALONE_" + str;
		}
		else{
			testFileName      = "PORTFOLIO_"       + str;
			//testFileNameStats = "STATS_PORTFOLIO_" + str;
		}
		try{
			fstreamTest 	 = new FileWriter(testFileName);			// 'true' => Append to file.
			testFileOut 	 = new BufferedWriter(fstreamTest);
//			fstreamTestStats = new FileWriter(testFileNameStats, true);	// 'true' => Append to file.
//			testFileOutStats = new BufferedWriter(fstreamTest);
		}catch(Exception e){System.err.println("ERROR: " + e.getMessage());}
	}
	
	public static void printString(String str){							// NOTE: Use this method to print simultaneous in 
		System.out.println(str);										//	 	 the console and in the testFileOut.
		try{	
			testFileOut.write("\n" + str);
		}catch(Exception e){System.err.println("ERROR: " + e.getMessage());} 
	}
	
	
	public static void printInitialInfo(){
		int indentSize = 30;
		String  indent = "";
		while(indent.length() < indentSize)
			indent += " ";
		String str0;
		if(PortEngine.nextSolver > 99)
				str0 = "############ - PORTFOLIO STANDALONE METHOD - ############ - PORTFOLIO STANDALONE METHOD - ############\n#";
		else
				str0 = "############ - PORTFOLIO PEBS METHOD - ############ - PORTFOLIO PEBS METHOD - ############\n#";
		String str1  = "\n#   PORTFOLIO:  [ P-UMDA P-ECGA P-HBOA ]" +	   
// DEPRECATED:		   "\n#           ->             T0 = " + PortEngine.getT0() +
					   "\n#           ->          alpha = " + PortEngine.alpha +
					   "\n#" +
					   "\n#        PEBS:" +
					   "\n#           ->             N0 = " + PortEngine.N0 + 
					   "\n#           ->     nextSolver = " + PortEngine.nextSolver +
					   "\n#" +
					   "\n#      STOPPER:" + 
					   "\n#           ->  maxIterations = " + PortStopper.getMaxIterations() +
					   "\n#           ->    maxFitCalls = " + PortStopper.getMaxFitnessCalls() +
					   "\n#           -> maxTimeAllowed = " + PortStopper.getMaxTimeAllowed() +
					   "\n#" +
					   "\n# ------------------------------------------------------" + 
					   "\n#" +
					   "\n#   PROBLEM:" +
					   "\n#      -> Name = " + Problem.problemName +
					   "\n#      ->    n = " + Problem.n +
					   "\n#\n########################################################################################\n\n";
		
		String str = str0 + str1;
		printString(str);		
	}
	
	public static void printRunInitialInfo(int r){
		printString("\n##### RUN " + (r+1) + "/" + PORTFOLIO.portRuns + " #####" + 
			 	      "##### RUN " + (r+1) + "/" + PORTFOLIO.portRuns + " #####" +
			 	      "##### RUN " + (r+1) + "/" + PORTFOLIO.portRuns + " #####");
	}
	
	public static void printRunFinalInfo(int r){
		printString("\n##### RUN " + (r+1) + "/" + PORTFOLIO.portRuns + " #####" + 
					  "##### RUN " + (r+1) + "/" + PORTFOLIO.portRuns + " #####" +
					  "##### RUN " + (r+1) + "/" + PORTFOLIO.portRuns + " #####");
		String str = "\n# " + "                      Success: " + PortStopper.foundOptimum() + 
					 "\n# " + "         Current Success Rate: " + PORTFOLIO.nSuccess + "/" + (r+1) +
					 "\n# " + "     Final Active EAlgorithms: [ ";
		for(int i = 0; i < PortEngine.portRegister.getNEA(); i++){
			ParEngine parEngine = PortEngine.portfolio.get(i);
				if(!parEngine.inactive())
					str += parEngine.getParRegister().getEAName() + " ";
		}
			  str += "]"    +
					 "\n# " + "-------------------------------";
		for(int i = 0; i < PortEngine.portRegister.getNEA(); i++){
			ParRegister parRegister = PortEngine.portfolio.get(i).getParRegister();
			str +=   "\n# " + "   Total " + parRegister.getEAName() + " Fitness Calls: " + parRegister.getTotalFitnessCalls(); 
		}		
			  str += "\n# " + "Total PORTFOLIO Fitness Calls: " + PortEngine.portRegister.getTotalFitnessCalls() + // DEPRECATED: " (+ " + PortEngine.N0 + ")" + 
					 "\n# " + "-------------------------------";
		for(int i = 0; i < PortEngine.portRegister.getNEA(); i++){
			ParRegister parRegister = PortEngine.portfolio.get(i).getParRegister();
			str +=   "\n# " + "    Total " + parRegister.getEAName() + " Running Time: " + parRegister.getTotalParTime(); 
		}
			  str += "\n# " + " Total PORTFOLIO Running Time: " + PortEngine.portRegister.getTotalRunTime() + // DEPRECATED: " (+ " + PortEngine.T0 + ")" +					 					 
					 "\n# " + "-------------------------------" +
					 "\n# " + "      Best Individual Fitness: " + PortEngine.portRegister.getBestIndividualFitness() +
					 "\n# " + "   Best Individual EAlgorithm: " + PortEngine.portRegister.getBestIndividualEA() +
					 //"\n# " + "           Best EA Total Time: " + PortEngine.portRegister.getBestIndividualTotalTime() +
					 "\n# " + "    Best Individual Pop. Size: " + PortEngine.portRegister.getBestIndividualPopSize() +
					 "\n# " + "Best Individual Fitness Calls: " + PortEngine.portRegister.getBestIndividualFitnessCalls() +
					 "\n# " + "         Best Individual Time: " + PortEngine.portRegister.getBestIndividualTime() +
					 "\n# " + "-------------------------------" +
					 "\n# " + "         Best Average Fitness: " + PortEngine.portRegister.getBestAverageFitness() +
					 "\n# " + "      Best Average EAlgorithm: " + PortEngine.portRegister.getBestAverageEA() +
					 //"\n# " + "   Best Average EA Total Time: " + PortEngine.portRegister.getBestAverageTotalTime() +
					 "\n# " + "       Best Average Pop. Size: " + PortEngine.portRegister.getBestAveragePopSize() +
					 "\n# " + "   Best Average Fitness Calls: " + PortEngine.portRegister.getBestAverageFitnessCalls() +
					 "\n# " + "            Best Average Time: " + PortEngine.portRegister.getBestAverageTime() +
					 "\n#\n###############################################################################################";
		printString(str);
	}
	
	public static void printTimeSlotInitialInfo(int timeSlot, long maxTime, int r){
		String str = //"================================================================================================" +
					 "\n RUN: " + (r+1) + "====>> STARTING TIME SLOT: T" + timeSlot + " = " + maxTime;
//					 "\n=> " +
//					 "\n=> " + "Currently Active EAlgorithms: [ ";
//		for(int i = 0; i < PortEngine.portRegister.getNEA(); i++){
//			ParEngine parEngine = PortEngine.portfolio.get(i);
//			if(!parEngine.inactive())
//				str += parEngine.getParRegister().getEAName() + " ";
//		}
//		str += 		 "]";
		printString(str);
	}
	
	public static void printTimeSlotFinalInfo(int timeSlot){
		String str = "================================================================================================" +
					 "\n====>> ENDING Time Slot: T" + timeSlot +
					 "\n=" +
					 "\n=> " + " Currently Active EAlgorithms: [ ";
		for(int i = 0; i < PortEngine.portRegister.getNEA(); i++){
			ParEngine parEngine = PortEngine.portfolio.get(i);
			if(!parEngine.inactive())
				str += parEngine.getParRegister().getEAName() + " ";
		}
		str += 		 "]" +
					 "\n=> " + "          Total Fitness Calls: " + PortEngine.portRegister.getTotalFitnessCalls() +
					 "\n=> " + "           Total Running Time: " + PortEngine.portRegister.getTotalRunTime() +
					 "\n=> " + "          Max Generation Time: " + PORTFOLIO.portEngine.getMaxTime() +
					 "\n=> " + "----------------------------" +
					 "\n=> " + "      Best Individual Fitness: " + PortEngine.portRegister.getBestIndividualFitness() +
					 "\n=> " + "   Best Individual EAlgorithm: " + PortEngine.portRegister.getBestIndividualEA() +
					 "\n=> " + "Best Individual Fitness Calls: " + PortEngine.portRegister.getBestIndividualFitnessCalls() +
					 "\n=> " + "         Best Individual Time: " + PortEngine.portRegister.getBestIndividualTime() +
					 "\n=> " + "----------------------------" +
					 "\n=> " + "         Best Average Fitness: " + PortEngine.portRegister.getBestAverageFitness() +
					 "\n=> " + "      Best Average EAlgorithm: " + PortEngine.portRegister.getBestAverageEA() +
					 "\n=> " + "   Best Average Fitness Calls: " + PortEngine.portRegister.getBestAverageFitnessCalls() +
					 "\n=> " + "            Best Average Time: " + PortEngine.portRegister.getBestAverageTime() +
					 "\n================================================================================================";
		printString(str);
	}		
	
	public static void printParEngineInitialInfo(ParEngine currentParEngine, long maxTimeAllowed){
		ParRegister currentParRegister = currentParEngine.getParRegister();
		String str;
		if(currentParEngine.inactive())
			str = "=>" + 
				  "\n" + "==>> INACTIVE: " + currentParRegister.getEAName() + "(" + currentParRegister.getIndex() + ")";
		else{		
			str = "=>" + 
			      "\n" + "==>> RUNNING: " + currentParRegister.getEAName() + "(" + currentParRegister.getIndex() + ")" +
					 		 " for a maximum allowed time of << " + maxTimeAllowed + " >> milliseconds."+
				  "\n" +
				  "\n" + indent + "Iteration      GenerationTime      Pop. Size      Generation      Avg. Fitness      BestCurrentFitness      BestFitnessSoFar";
		}
		printString(str);
	}
	
	public static void printParEngineCurrentInfo(ParEngine currentParEngine, long maxTimeAllowed, long currentTime){
		ParRegister currentParRegister = currentParEngine.getParRegister();
		String str = "================================================================================================" +
					 "\n====>> STOPPING: " + currentParRegister.getEAName() +
					 "\n=> " +
					 "\n=> " + "             Currently Active: " + !currentParEngine.inactive() +
					 "\n=> " + "      CurrentTime/AllowedTime: " + currentTime + "/" + maxTimeAllowed + " milliseconds" + 
					 "\n=> " + "                    TotalTime: " + currentParRegister.getTotalParTime() + " milliseconds" +
					 "\n=> " + "          Max Generation Time: " + currentParRegister.getMaxGenerationTime();
		if(currentParEngine.inactive())
			  str += "\n=> " + "             Elimination Time: " + currentParRegister.getEliminationTime();
		ParBestSoFar parBestSoFar = currentParRegister.getParBestSoFar();
			  str += "\n=> " + "          Total Fitness Calls: " + currentParRegister.getTotalFitnessCalls() +
					 "\n=> " + "-------------------------------" +
					 "\n=> " + "      Best Individual Fitness: " + parBestSoFar.getBestIndividualFitness() +
					 "\n=> " + "    Best Individual Pop. Size: " + parBestSoFar.getBestIndividualPopulation().getN() +
					 "\n=> " + "Best Individual Fitness Calls: " + parBestSoFar.getBestIndividualFitnessCalls() +
					 "\n=> " + "         Best Individual Time: " + parBestSoFar.getBestIndividualTime() +
					 "\n=> " + "-------------------------------" +
					 "\n=> " + "         Best Average Fitness: " + parBestSoFar.getBestAverageFitness() +
					 "\n=> " + "       Best Average Pop. Size: " + parBestSoFar.getBestAveragePopulation().getN() +
					 "\n=> " + "   Best Average Fitness Calls: " + parBestSoFar.getBestAverageFitnessCalls() +
					 "\n=> " + "            Best Average Time: " + parBestSoFar.getBestAverageTime();					 
		str +=       "\n================================================================================================";
		printString(str);   			   
	}
	
	public static void printParEngineEliminationInfo(ParEngine currentParEngine){
		ParRegister currentParRegister = currentParEngine.getParRegister();
		String str = "================================================================================================" +
					 "\n====>> DEACTIVATING: " + currentParRegister.getEAName() +
					 "\n=> " +
					 "\n=> " + "             Currently Active: " + !currentParEngine.inactive() +
					 "\n=> " + "                    TotalTime: " + currentParRegister.getTotalParTime() + " milliseconds" +
					 "\n=> " + "             Elimination Time: " + currentParRegister.getEliminationTime();
		ParBestSoFar parBestSoFar = currentParRegister.getParBestSoFar();
			  str += "\n=> " + "          Total Fitness Calls: " + currentParRegister.getTotalFitnessCalls() +
					 "\n=> " + "-------------------------------" +
					 "\n=> " + "      Best Individual Fitness: " + parBestSoFar.getBestIndividualFitness() +
					 "\n=> " + "    Best Individual Pop. Size: " + parBestSoFar.getBestIndividualPopulation().getN() +
					 "\n=> " + "Best Individual Fitness Calls: " + parBestSoFar.getBestIndividualFitnessCalls() +
					 "\n=> " + "         Best Individual Time: " + parBestSoFar.getBestIndividualTime() +
					 "\n=> " + "-------------------------------" +
					 "\n=> " + "         Best Average Fitness: " + parBestSoFar.getBestAverageFitness() +
					 "\n=> " + "       Best Average Pop. Size: " + parBestSoFar.getBestAveragePopulation().getN() +
					 "\n=> " + "   Best Average Fitness Calls: " + parBestSoFar.getBestAverageFitnessCalls() +
					 "\n=> " + "            Best Average Time: " + parBestSoFar.getBestAverageTime() +					 
					 "\n================================================================================================";
		printString(str);   			   
	}
	
	public static void printSolverCurrentInfo(ParRegister currentParRegister, IEASolver currentSolver, long generationTime, int solverPosition){		
		if(verbose){
			int currentIteration      = currentParRegister.getTotalIterations();
			ParBestSoFar parBestSoFar = currentParRegister.getParBestSoFar();
			if(currentIteration%30 == 0)
				printString(indent + "Iteration      GenerationTime      Pop. Size      Generation      Avg. Fitness      BestCurrentFitness      BestFitnessSoFar");
			if(currentSolver.getDummy())   
				printString(String.format(indent + "%6d %16d %16d (%d) %11d %18s %18s %21.2f (%d)",
							currentIteration,
							generationTime,
							currentSolver.getN(), solverPosition,
							currentSolver.getCurrentGeneration(),
							"DUMMY", "DUMMY", 
							parBestSoFar.getBestIndividualFitness(),
							parBestSoFar.getBestIndividualPopulation().getN()							
							)
						);
		
			else
				printString(String.format(indent + "%6d %16d %16d (%d) %11d %18.2f %18.2f %21.2f (%d)",
							currentIteration, 
							generationTime,
							currentSolver.getN(), solverPosition,
							currentSolver.getCurrentGeneration(),
							currentSolver.getAvgFitness(),
							currentSolver.getCurrentPopulation().getBestFit(),
							parBestSoFar.getBestIndividualFitness(),
							parBestSoFar.getBestIndividualPopulation().getN()							
							)	
						);
		}
	}
	
	public static void printFinalInfo(){
		printString("\nSUCCESS RATE = " + PORTFOLIO.nSuccess + "/" + PORTFOLIO.portRuns + "\n");
	}
	
	public static void closeTestFileOut(){		
		try{testFileOut.close();}
		catch(Exception e){System.err.println("ERROR: " + e.getMessage());}
	}		
	
}













