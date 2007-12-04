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
		Factor[] threats = getProject().getCausePool().getDirectThreats();
		int[] threatSummaryValues = new int[threats.length];
		for(int i = 0; i < threats.length; ++i)
		{
			threatSummaryValues[i] = getThreatSumaryRatingValue(threats[i]);
		}
		
		return getFormula().getHighestRatingRule(threatSummaryValues);
	}
	
	public int getThreatSumaryRatingValue(Factor threat) throws Exception
	{
		return getFormula().getHighestRatingRule(calculateThreatSummaryRatingValues(threat));
	}
	
	public int[] calculateThreatSummaryRatingValues(Factor threat) throws Exception
	{
		ORefList factorLinkReferrers = threat.findObjectsThatReferToUs(FactorLink.getObjectType());
		Vector<Integer> calculatedThreatSummaryRatingValues = new Vector();
		for (int i = 0; i < factorLinkReferrers.size(); ++i)
		{
			FactorLink factorLink = FactorLink.find(getProject(), factorLinkReferrers.get(i));
			if (factorLink.isThreatTargetLink())
				calculatedThreatSummaryRatingValues.add(factorLink.calculateThreatRatingBundleValue());
		}
		
		return Utility.convertToIntArray(calculatedThreatSummaryRatingValues);
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
