package com.z_PORTFOLIO;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;


class ParParameter{
	
	// NOTE: optionNames must coincide exactly with the option names in the 'Parameters.txt' file.
	private static final String optionNames[] =     
		{"N0", "nextSolver",
		 "allFitnessEqual", "epsilon", "maxOptimal", "foundBestFit", "foundOnes"};
	
	// NOTE: Execute this initialization PRIOR to any other.
	public static void initializeParameters(String parParameterFile){		
		try{
			// Open the file to be read
			FileInputStream fstream = new FileInputStream(parParameterFile);
			// Create an object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader buff = new BufferedReader(new InputStreamReader(in));
			int nLine = 0; // Line number
			String line;
			while((line = buff.readLine())!= null){
				nLine++; 						// Increment line number
				if(line.length() > 0){		   	// Ignore empty lines
					if(line.charAt(0) != '#'){ 	// Ignore comments
						Scanner scanner = new Scanner(line);
						scanner.useDelimiter("=");
						// Get option name and value. If not valid, exit program!
						String optionName = scanner.next().trim();
						validateOptionName(line, optionName, nLine);
						String optionValue = scanner.next().trim(); 
						validateOptionValue(optionName, optionValue, nLine); 
					}
				}
			}
			in.close();
		}  // Catch open file  error.
		catch(Exception e){
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
	
	// Usage of 'switch' with 'String' only legal for Java SE 7 or later!
	private static void validateOptionValue(String optionName,String optionValue, int nLine)
	throws NumberFormatException{
		if(optionName.equals("N0")){
			PortEngine.N0 = Integer.parseInt(optionValue);
			if(PortEngine.N0 <= 0)
				exitError("Line " + nLine + " --> Population size must be a POSITIVE integer.");
			return; // Option validated!!
		}
		if(optionName.equals("nextSolver")){
			PortEngine.nextSolver = Integer.parseInt(optionValue);
			if(PortEngine.nextSolver < 2)
				exitError("Line " + nLine + " --> Number of generations until next Solver must be a positive integer " +
												 "greater than one (PEBS)");
			return; // Option validated!!
		}
		if(optionName.equals("allFitnessEqual")){
			ParStopper.allFitnessEqual = Integer.parseInt(optionValue);
			if(!(ParStopper.allFitnessEqual == -1 || ParStopper.allFitnessEqual == 1))
				exitError("Line " + nLine + " --> 'allFitnessEqual' must be either '-1' or '1'.");
			return; // Option validated!!
		}
		if(optionName.equals("epsilon")){
			ParStopper.epsilon = Float.parseFloat(optionValue);
			if((ParStopper.epsilon < 0 || ParStopper.epsilon > 1) && ParStopper.epsilon != -1)
				exitError("Line " + nLine + " --> Termination threshold for the univariate frequencies must be either '-1' or a number BETWEEN '0' and '1'.");
			return; // Option validated!!
		}
		if(optionName.equals("maxOptimal")){
			ParStopper.maxOptimal = Float.parseFloat(optionValue);
			if((ParStopper.maxOptimal < 0 || ParStopper.maxOptimal > 1) && ParStopper.maxOptimal != -1)
				exitError("Line " + nLine + " --> Optimal and non-optimal ind. threshold must be either '-1' or a number BETWEEN '0' and '1'.");
			return; // Option validated!!
		}
		if(optionName.equals("foundBestFit")){
			ParStopper.foundBestFit = Integer.parseInt(optionValue);
			if(ParStopper.foundBestFit != -1 && ParStopper.foundBestFit != 1)
				exitError("Line " + nLine + " --> 	INVALID option for stopWhenFoundOptimum.");
			return; // Option validated!!
		}
		if(optionName.equals("foundOnes")){
			ParStopper.foundOnes = Integer.parseInt(optionValue);
			if(ParStopper.foundOnes != -1 && ParStopper.foundOnes != 0 && ParStopper.foundOnes != 1)
				exitError("Line " + nLine + " --> 	INVALID option for stopWhenFoundOnes.");
			return; // Option validated!!
		}
		if(true)
			exitError("Line" + nLine +
					  " --> If you are reading this message something is FUNDAMENTALLY WRONG with 'validateOptionValue(String, String, int)'.\n" + 
					  "You may contact the author at 'unidadeimaginaria@gmail.com'\n" +
					  "Sorry for the inconvenience!");
	}// END: validateOptionValue(...)	
	
	
	// Input error found!! Exit program!
	public static void exitError(String message){
		System.err.println(new Error(message));
		System.exit(1);
	}
	
}// End of class Parameter





