package com.a_SGA;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import com.z_PORTFOLIO.Problem;



public class Parameter{
	
	// NOTE: optionNames must coincide exactly with the option names in the 'Parameters.txt' file.
	private static final String optionNames[] =     
		{"selectionMethod", "tourSize", "offspringSize", "pCrossover", 
		 "nCrossover", "pSwap", "pMutation", "replacementType", "windowSize"
		};
	
	// Selector parameters
	private static int selectionMethod;	// Selection method. Default = 1 (Tournament selection)
	private static int tourSize;		// Size of tournament. Use only with Tournament Selection (selection = 1 or 2)
	private static float pOffspringSize;// Size of the Selection Set and the Offspring Set. Default = 1 (The same value as N)

	// Operators parameters
	private static double pCrossover;	// Probability of crossover. Default = 0.6.
	private static int nCrossover;		// Number of crossover points. Default = 1. 	NOTE: If nCrossover = 0 use UniformCrossover.
	private static double pSwap;		// Probability of swapping each pair of bits.	NOTE: This is for UniformCrossover only.
	private static double pMutation;	// Probability of mutation. Default = 1/n.
	
	// Replacement parameters
	private static int replacementType;	// Replacement method. Default = 1 (Restricted replacement)
	private static int windowSize;		// Size of replacement tournament. Use only with RestrictedReplacement (replacement = 2)	
		
	// Get Selector parameters
	public static int getSelectionMethod(){return selectionMethod;}
	public static int getTourSize(){return tourSize;}
	public static float getPOffspringSize(){return pOffspringSize;}
	// Get Operators parameters
	public static double getPCrossover(){return pCrossover;}
	public static int getNCrossover(){return nCrossover;}
	public static double getPMutation(){return pMutation;}
	// Get Replacement parameters
	public static int getReplacementType(){return replacementType;}
	public static int getWindowSize(){return windowSize;}
	
	
	// NOTE: Execute this initialization PRIOR to any other.
	public static void initializeParameters(String parameterFile){		
		try{
			// Open the file to be read
			FileInputStream fstream = new FileInputStream(parameterFile);
			// Create an object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader buff = new BufferedReader(new InputStreamReader(in));
			int nLine = 0; 						// Line number
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
		// SELECTION METHOD
		if(optionName.equals("selectionMethod")){
			selectionMethod = Integer.parseInt(optionValue);
			if(selectionMethod != 1 && selectionMethod != 2 && selectionMethod != 3)
				exitError("Line " + nLine + " --> INVALID selection method.");
			return; // Option validated!!
		}
		if(optionName.equals("tourSize")){
			tourSize = Integer.parseInt(optionValue);
			if(tourSize < 2)
				exitError("Line " + nLine + " --> Tournament size must be GREATER than '1'.");
			return; // Option validated!!
		}
		if(optionName.equals("offspringSize")){
			pOffspringSize = Float.parseFloat(optionValue);
			if(pOffspringSize  <= 0)
				exitError("Line " + nLine + " --> Offspring set size must be a POSITIVE proportion.");
			return; // Option validated!!
		}
		// OPERATORS
		if(optionName.equals("pCrossover")){
			pCrossover = Double.parseDouble(optionValue);
			if(pCrossover < 0 || pCrossover > 1)
				exitError("Line " + nLine + " --> Probability of crossover must be a number BETWEEN '0' and '1'.");
			return; // Option validated!!
		}
		if(optionName.equals("nCrossover")){
			nCrossover = Integer.parseInt(optionValue);
			if(nCrossover < 0 || nCrossover > Problem.n-1)
				exitError("Line" + nLine + " --> Number of cross points must be an integer BETWEEN '0' and '1'.");
			return; // Option validated!!
		}
		if(optionName.equals("pSwap")){
			pSwap = Double.parseDouble(optionValue);
			if(pSwap < 0 || pSwap > 1)
				exitError("Line " + nLine + " --> Probability of swapping a bit must be a number BETWEEN '0' and '1'.");
			return; // Option validated!!
		}
		if(optionName.equals("pMutation")){
			pMutation = Double.parseDouble(optionValue);
			if(pMutation == 99)
				pMutation = (double)((double)1/(double)Problem.n);
			else if(pMutation < 0 || pMutation > 1)
				exitError("Line " + nLine + " --> Probability of mutation must be a number BETWEEN '0' and '1'.");
			return; // Option validated!!
		}  
		// REPLACEMENT METHOD
		if(optionName.equals("replacementType")){
			replacementType = Integer.parseInt(optionValue);
			if(replacementType != 1 && replacementType != 2 && replacementType != 3)
				exitError("Line " + nLine + " --> INVALID replacement method.");
			return; // Option validated!!
		}
		if(optionName.equals("windowSize")){
			double pWindow = Float.parseFloat(optionValue);
			if(pWindow <= 0)
				exitError("Line " + nLine + " --> Window size must be a POSITIVE proportion.");
			windowSize = (int)(pWindow*Problem.n); 			// Define 'windowSize' as a proportion of the string size.
			return; // Option validated!!
		}
		if(true)
			exitError("Line" + nLine +
					  " --> If you are reading this message something is FUNDAMENTALLY WRONG with 'validateOptionValue(String, String, int)'.\n" + 
					  "You may contact the author at 'unidadeimaginaria@gmail.com'\n" +
					  "Sorry for the inconvenience!");
	}// End of validateOptionValue(...)
	
	
	public static Selection initializeSelection(int N){
		int NS = (int)(pOffspringSize*N);					// NOTE: The Selection Set size is also the Offspring Set size. 
		switch(selectionMethod){
			case 1: return new TourWithReplacement(NS, tourSize);
			case 2: return new TourWithoutReplacement(NS, tourSize);
			case 3: return new Truncation(NS);
			default: exitError("If you are reading this message something is FUNDAMENTALLY WRONG with the validation of the 'selectionMethod' value.\n" + 
							   "You may contact the author at 'unidadeimaginaria@gmail.com'\n" +
							   "Sorry for the inconvenience!");
					 return new Truncation(NS);				//  NOTE: This line is never executed!
		}
	}
	
	public static Crossover initializeCrossover(){
		return (nCrossover == 0)?
			new UniformCrossover(pCrossover, pSwap) :
			new NPointCrossover(pCrossover, nCrossover);
	}
	
	public static Mutation initializeMutation(){
		return new Mutation(pMutation);
	}
	
	public static IReplacement initializeReplacement(int currentN){
		switch(replacementType){
			case 1: return new RestrictedReplacement(windowSize, currentN);
			case 2: return new WorstReplacement();
			case 3: return new FullReplacement();
			default: exitError(" If you are reading this message something is FUNDAMENTALLY WRONG with the validation of the 'replacementType' value.\n" + 
					   "You may contact the author at 'unidadeimaginaria@gmail.com'\n" +
					   "Sorry for the inconvenience!");
					 return new WorstReplacement();			//  NOTE: This line is never executed!
		}
	}
	
	public static String writeParameters(String indent){		
		String str = "    Selection Method:";
		if(selectionMethod == 1)
			str += "\n" + indent + "       Selection = Tournament Selection with replacement" +
				   "\n" + indent + "        tourSize = " + tourSize +
				   "\n" + indent + "   offspringSize = " + pOffspringSize;
		if(selectionMethod == 2)
			str += "\n" + indent + "       Selection = Tournament Selection without replacement" +
				   "\n" + indent + "        tourSize = " + tourSize +
				   "\n" + indent + "   offspringSize = " + pOffspringSize;
		if(selectionMethod == 3)
			str += "\n" + indent + "       Selection = Truncation" +
				   "\n" + indent + "   offspringSize = " + pOffspringSize;
		
		str +=  "\n\n Operators:" +
				"\n" + indent + "pCrossover = " + pCrossover + 
				"\n" + indent + "nCrossover = " + nCrossover + 
				"\n" + indent + "pMutation = " + pMutation +
				"\n\n Replacement Method:" +
				"\n" + indent + "Replacement Type = " + replacementType ;
		if(replacementType == 1)
			str += "\n" + indent + "   windowSize = " + windowSize;
		return str;
	}
	
	// Input error found!! Exit program!
	public static void exitError(String message){
		System.err.println(new Error(message));
		System.exit(1);
	}
	
}// End of class Parameter





