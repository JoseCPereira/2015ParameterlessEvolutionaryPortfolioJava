package com.z_PORTFOLIO;

import com.b_UMDA.UMDASolver;


class UMDA extends IEAlgorithm{
	
	public UMDA(String parFile){
		this.paramFile = parFile;
		this.EAName	   = "UMDA";
	}
	
	public IEASolver newIEASolver(int currentN){return new UMDASolver(paramFile, currentN);} // Design Pattern Strategy.
	
	public String toString(){
		String indent = "     ";
		return " UMDA:\n" + com.b_UMDA.Parameter.writeParameters(indent);
	}
}


