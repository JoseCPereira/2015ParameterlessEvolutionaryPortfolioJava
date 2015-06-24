package com.z_PORTFOLIO;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;


class PortParameter{
	
	// NOTE!! optionNames must coincide exactly with the option names in the 'Parameters.txt' file.
	private static final String optionNames[] =     
	{"portRuns", "T0", "alpha", "nextSolver", "parParamFile",
	 "problemType", "stringSize", "optimumValue", "kay", "sigmaK",
	 "maxIterations", "maxFitnessCalls", "maxTimeAllowed", "verbose"
	 };
	
	private static int    problemType;											// Problem type. Check 'TemplateParameters.txt' for the option menu.
	private static int    stringSize;											// Size of each individual. 	NOTE: This parameter must be set accordingly to the problem type.
	private static float  optimumValue;											// Best fitness. 				NOTE: This parameter must be set accordingly to the problem type.
	private static int 	  kay;													// Order of TRAP-K problems.	NOTE: This is only for the TRAP-K problems.
	private static double sigmaK;												// Standard deviation of noise. NOTE: Set sigma = 0 for non-NOISY problems.
	private static String parParamFile; 										// Name of the input file where all necessary parameters for the parameterless method are set.
	
	public static int getProblemType(){return problemType;}						// NOTE: This is for testing only!!
	public static double getSigmaK(){return sigmaK;}							// 		 Check ParEngine.parametrize(...)
	
	
	
	public static void initializeParameters(String parameterFile){				// NOTE: Execute this initialization PRIOR to any other.		
		try{
			FileInputStream fstream = new FileInputStream(parameterFile);		// Open the file to be read			
			DataInputStream in = new DataInputStream(fstream);					// Create an object of DataInputStream
			BufferedReader buff = new BufferedReader(new InputStreamReader(in));
			int nLine = 0; 														// Line number
			String line;
			while((line = buff.readLine())!= null){
				nLine++; 														// Increment line number
				if(line.length() > 0){		   									// Ignore empty lines
					if(line.charAt(0) != '#'){ 									// Ignore comments
						Scanner scanner = new Scanner(line);
						scanner.useDelimiter("=");
						
						String optionName = scanner.next().trim();				// Get option name and value. If not valid, exit program!
						validateOptionName(line, optionName, nLine);
						String optionValue = scanner.next().trim(); 
						validateOptionValue(optionName, optionValue, nLine); 
					}
				}
			}
			ParParameter.initializeParameters(parParamFile);					// Initialize all necessary parameters for the Parameterless method.
			in.close();
		}  
		catch(Exception e){														// Catch open file  error.
			System.err.println("Error: " + e.getMessage());
		}
	}	
	
	private static void validateOptionName(String line, String option, int nLine){
		if(option.length() >= line.length())
			exitError("Line " + nLine + " --> Missing equal sign '='");
		if(!validateName(option))
			exitError("Line " + nLine + " --> INVALID OPTION NAME '" + option + "'");
	}
	
	private static boolean validateName(String name){
		for(int i = 0; i < optionNames.length; i++)
			if(name.equals((String)optionNames[i]))
				return true;
		return false;
	}
	
	private static void validateOptionValue(String optionName,String optionValue, int nLine)	// NOTE: Usage of 'switch' with 'String' only legal for Java SE 7 or later!
	throws NumberFormatException{
		if(optionName.equals("portRuns")){
			PORTFOLIO.portRuns = Integer.parseInt(optionValue);
			if(PORTFOLIO.portRuns < 0)
				exitError("Line " + nLine + " --> Number of runs to perform must be a POSITIVE integer.");
			return; 																			// Option validated!!
		}
		if(optionName.equals("T0")){
			PortEngine.T0 = Long.parseLong(optionValue);
			if(PortEngine.T0 <= 0)
				exitError("Line " + nLine + " --> Initial time slot, T0, must be a POSITIVE integer.");
			return;  																			// Option validated!!
		}
		if(optionName.equals("alpha")){
			PortEngine.alpha = Double.parseDouble(optionValue);
			if(PortEngine.alpha < 1)
				exitError("Line " + nLine + " --> Time multiplier must be a constant greater than one.");
			return;  																			// Option validated!!
		}
		if(optionName.equals("N0")){
			PortEngine.N0 = Integer.parseInt(optionValue);										// NOTE: PortEngine.N1 is initialized along with stringSize.				
			if(PortEngine.N0 <= 0)
				exitError("Line " + nLine + " --> Population size must be a POSITIVE integer.");
			return;																				// Option validated!!
		}
		if(optionName.equals("nextSolver")){
			PortEngine.nextSolver = Integer.parseInt(optionValue);
			if(PortEngine.nextSolver <= 1 && PortEngine.nextSolver != 0)
				exitError("Line " + nLine + " --> Number of generations until next Solver must be a positive integer " +
												 "greater than one (PEBS) or 0 (STANDALONE)");
			return; // Option validated!!
		}
		if(optionName.equals("parParamFile")){
			parParamFile = optionValue;
			return; 																			// Option validated!!
		}
		if(optionName.equals("problemType")){
			problemType = Integer.parseInt(optionValue);										// NOTE: Add new option for each new problem
			if(!((problemType >= 0 && problemType <= 7) || (problemType >= 10 && problemType <= 17) || problemType == 21 || problemType == 22 || problemType == 30)) 
				exitError("Line " + nLine + " --> INVALID Problem Type. Please check 'BParameters.txt' for the complete list of valid problems.");
			return; 																			// Option validated!!
		}		
		if(optionName.equals("stringSize")){													// NOTE: Link string size to problem type!
			stringSize = Integer.parseInt(optionValue);
			PortEngine.N1 = stringSize;															// NOTE: N1 is to be used only by the P-ECGA and P-HBOA algorithms.
			if(stringSize <= 0)
				exitError("Line " + nLine + " --> Individual string size must be a POSITIVE integer.");
			return; 																			// Option validated!!
		}
		if(optionName.equals("optimumValue")){
			optimumValue = Float.parseFloat(optionValue);										// NOTE: 'optimumValue' is problem dependent, there are no restrictions on possible values.																									
			return;  																			// Option validated!!  
		}		
		if(optionName.equals("kay")){															// NOTE: This is only for the TRAP-K problems.
			kay = Integer.parseInt(optionValue);
			if(kay <= 1)
				exitError("Line " + nLine + " --> Order of TRAP-K must be an integer greater than one.");
			return;  																			// Option validated!!
		}
		if(optionName.equals("sigmaK")){
			sigmaK = Double.parseDouble(optionValue);
			if(sigmaK < 0)
				exitError("Line " + nLine + " --> Noise standard deviation must be a NON-NEGATIVE Number.");
			return;  																			// Option validated!!  
		}
		if(optionName.equals("maxIterations")){
			PortStopper.maxIterations = Integer.parseInt(optionValue);
			if(PortStopper.maxIterations <= 0 && PortStopper.maxIterations != -1)
				exitError("Line " + nLine + " --> Maximal number of portfolio iterations must be either '-1' or a POSITIVE integer.");
			return;  																			// Option validated!!
		}
		if(optionName.equals("maxFitnessCalls")){
			PortStopper.maxFitnessCalls = Long.parseLong(optionValue);
			if(PortStopper.maxFitnessCalls <= 0 && PortStopper.maxFitnessCalls != -1)
				exitError("Line " + nLine + " --> Maximal number of fitness calls must be either '-1' or a POSITIVE integer.");
			return;  																			// Option validated!!
		}
		if(optionName.equals("maxTimeAllowed")){
			PortStopper.maxTimeAllowed = Long.parseLong(optionValue);
			if(PortStopper.maxTimeAllowed <= 0 && PortStopper.maxTimeAllowed != -1)
				exitError("Line " + nLine + " --> 'PortStopper.maxTime' must be a positive number.");
			return;  																			// Option validated!!
		}
		if(optionName.equals("verbose")){
			PortPress.verbose = Boolean.parseBoolean(optionValue);
			if(PortPress.verbose != false && PortPress.verbose != true)
				exitError("Line " + nLine + " --> Verbose option must be either 'false' or 'true'.");
			return;  																			// Option validated!!
		}
		if(true)
			exitError("Line" + nLine +
					  " --> If you are reading this message something is FUNDAMENTALLY WRONG with 'validateOptionValue(String, String, int)'.\n" + 
					  "You may contact the author at 'unidadeimaginaria@gmail.com'\n" +
					  "Sorry for the inconvenience!");
	} 																							// END: validateOptionValue(...)
	
	
	
	public static Problem initializeProblem(){													// Design Pattern Strategy
		switch(problemType){																	// NOTE: When implementing a new problem one must add a corresponding new case.
			// ZERO Problems		
			case 0: return new Problem(new ZeroMax(), stringSize, optimumValue, sigmaK);
			case 1: return new Problem(new ZeroQuadratic(), stringSize, optimumValue, sigmaK);
			case 2: return new Problem(new ZeroThreeDeceptive(), stringSize, optimumValue, sigmaK);
			case 3: return new Problem(new ZeroThreeDeceptiveBiPolar(), stringSize, optimumValue, sigmaK);
			case 4: return new Problem(new ZeroThreeDeceptiveOverlapping(), stringSize, optimumValue, sigmaK);
			case 5: return new Problem(new ZeroTrapK(kay), stringSize, optimumValue, sigmaK);
			case 6: return new Problem(new ZeroUniformSixBlocks(), stringSize, optimumValue, sigmaK);
			// ONE Problems
			case 10: return new Problem(new OneMax(), stringSize, optimumValue, sigmaK);
			case 11: return new Problem(new Quadratic(), stringSize, optimumValue, sigmaK);
			case 12: return new Problem(new ThreeDeceptive(), stringSize, optimumValue, sigmaK);
			case 13: return new Problem(new ThreeDeceptiveBiPolar(), stringSize, optimumValue, sigmaK);
			case 14: return new Problem(new ThreeDeceptiveOverlapping(), stringSize, optimumValue, sigmaK);
			case 15: return new Problem(new TrapK(kay), stringSize, optimumValue, sigmaK);
			case 16: return new Problem(new UniformSixBlocks(), stringSize, optimumValue, sigmaK);
			// OTHER Problems
			case 21: return new Problem(new HierarchicalTrapOne(), stringSize, optimumValue, sigmaK);
			case 22: return new Problem(new HierarchicalTrapTwo(), stringSize, optimumValue, sigmaK);
			case 30: return new Problem(new IsingSpin(), stringSize, optimumValue, sigmaK);
			default: exitError("If you are reading this message something is FUNDAMENTALLY WRONG with the validation of the 'problemType' value.\n" + 
					   		"You may contact the author at 'unidadeimaginaria@gmail.com'\n" +
					   		"Sorry for the inconvenience!");
			 		 // This line is never executed!
					 return new Problem(new OneMax(), stringSize, optimumValue, sigmaK);
		}
	}
	
	public static void exitError(String message){	// Input error found!! Exit program!
		System.err.println(new Error(message));
		System.exit(1);
	}
	
}// End of class Parameter





