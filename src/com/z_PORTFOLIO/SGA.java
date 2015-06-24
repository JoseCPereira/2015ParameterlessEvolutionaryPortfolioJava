package com.z_PORTFOLIO;

import com.a_SGA.SGASolver;


class SGA extends IEAlgorithm{
	
	public SGA(String parFile){
		this.paramFile = parFile;
		this.EAName    = "SGA";
	}
		
	public IEASolver newIEASolver(int currentN){return new SGASolver(paramFile, currentN);}		// Design Pattern Strategy. 
	
	public String toString(){
		String indent = "     ";
		return " SGA:\n" + com.a_SGA.Parameter.writeParameters(indent);
	}
}
