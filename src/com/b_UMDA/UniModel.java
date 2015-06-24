package com.b_UMDA;

import com.z_PORTFOLIO.Individual;
import com.z_PORTFOLIO.PORTFOLIO;
import com.z_PORTFOLIO.Problem;
import com.z_PORTFOLIO.SelectedSet;

class UniModel{
	public int offspringSize;
	
	public UniModel(int offSize){offspringSize = offSize;}	
	
	public Individual[] sampleNewIndividuals(SelectedSet selectedSet){
		Individual[] newIndividuals = new Individual[offspringSize];
		int[] frequencies = selectedSet.getUniFrequencies();
		int NS = selectedSet.getN();
		for(int i = 0; i < offspringSize; i++){
			newIndividuals[i] = new Individual();
			for(int j = 0; j < Problem.n; j++){
				double probJ = ((double)(frequencies[j]))/NS;
				if (PORTFOLIO.random.nextDouble() < probJ)
					newIndividuals[i].setAllele(j, '1');
				else
					newIndividuals[i].setAllele(j, '0');
			}
		}
		return newIndividuals;
	}
	
}