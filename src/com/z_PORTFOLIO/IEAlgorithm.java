package com.z_PORTFOLIO;

//DESIGN PATTERN STRATEGY
abstract class IEAlgorithm{
	protected String paramFile;
	protected String EAName;
	
	public String getEAName(){return this.EAName;}
	
	public abstract IEASolver newIEASolver(int currentN);

	public abstract String toString();
}





