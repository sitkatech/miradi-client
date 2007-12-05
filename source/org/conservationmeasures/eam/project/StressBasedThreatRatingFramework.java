/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.util.Vector;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.utils.Utility;

public class StressBasedThreatRatingFramework
{
	public StressBasedThreatRatingFramework(Project projectToUse)
	{
		project = projectToUse;
		formula = new SimpleThreatFormula();
	}
	
	public int getOverallProjectRating()
	{
		try
		{
			int rollup = getFactorRollupRating();
			int majority = getTargetRollupRating();
			return Math.max(rollup, majority);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return 0;
		}	
	}
	
	private int getTargetRollupRating() throws Exception
	{
		Factor[] targets = getProject().getTargetPool().getTargets();
		int[] highestTargetRatingValues = new int[targets.length];
		for (int i = 0; i < targets.length; ++i)
		{
			highestTargetRatingValues[i] = getFactorSumaryRatingValue(targets[i]);
		}
		
		return getFormula().getMajority(highestTargetRatingValues);
	}
	
	private int getFactorRollupRating() throws Exception
	{ 
		Factor[] factors = getProject().getCausePool().getDirectThreats();
		int[] summaryValues = new int[factors.length];
		for (int i = 0; i < factors.length; ++i)
		{
			summaryValues[i] = get2PrimeSummaryRatingValue(factors[i]);
		}
		
		return getFormula().getHighestRatingRule(summaryValues);
	}
		
	public int get2PrimeSummaryRatingValue(Factor factor) throws Exception
	{
		return getFormula().getSummaryOfBundlesWithTwoPrimeRule(calculateSummaryRatingValues(factor));
	}
	
	//TODO refactor rename to getHighestFactorSummaryRatingValue
	public int getFactorSumaryRatingValue(Factor factor) throws Exception
	{
		return getFormula().getHighestRatingRule(calculateSummaryRatingValues(factor));
	}
	
	public int[] calculateSummaryRatingValues(Factor factor) throws Exception
	{
		ORefList factorLinkReferrers = factor.findObjectsThatReferToUs(FactorLink.getObjectType());
		Vector<Integer> calculatedSummaryRatingValues = new Vector();
		for (int i = 0; i < factorLinkReferrers.size(); ++i)
		{
			FactorLink factorLink = FactorLink.find(getProject(), factorLinkReferrers.get(i));
			if (factorLink.isThreatTargetLink())
				calculatedSummaryRatingValues.add(factorLink.calculateThreatRatingBundleValue());
		}
		
		return Utility.convertToIntArray(calculatedSummaryRatingValues);
	}
	
	public Project getProject()
	{
		return project;
	}
	
	public SimpleThreatFormula getFormula()
	{
		return formula;
	}
	
	private Project project;
	private SimpleThreatFormula formula;
}
