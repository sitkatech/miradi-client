/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.util.Vector;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.FactorLinkSet;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.utils.Utility;

public class SimpleModeThreatRatingFramework
{
	public SimpleModeThreatRatingFramework(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public int getOverallProjectRating()
	{
		try
		{
			int rollup = getProjectRatingRollup();
			//FIXME add majority rating too
			//ValueOption majority = getProjectMajorityRating();
			//if(majority.getNumericValue() > rollup)
			//	return majority;
			
			return rollup;
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return 0;
		}	
	}
	
	public int getProjectRatingRollup() throws Exception
	{
		FactorLinkSet allThreatLinks = getProject().getFactorLinkPool().getDirectThreatTargetLinks();
		int[] factorLinkRatingValues = getFactorLinkRatingValues(allThreatLinks);

		return getSummaryOfRatingValues(factorLinkRatingValues);
	}

	private int[] getFactorLinkRatingValues(FactorLinkSet allThreatLinks) throws Exception
	{
		Vector<Integer> factorLinkRatingValues = new Vector<Integer>();
		for(FactorLink factorLink : allThreatLinks)
		{
			factorLinkRatingValues.add(factorLink.calculateThreatRatingBundleValue());
		}
		return Utility.convertToIntArray(factorLinkRatingValues);
	}
	
	protected int getSummaryOfRatingValues(int[] ratingValues)
	{
		SimpleThreatFormula formula = new SimpleThreatFormula();
		return formula.getSummaryOfBundlesWithTwoPrimeRule(ratingValues);
	}
	
	public Project getProject()
	{
		return project;
	}
	
	private Project project;
}
