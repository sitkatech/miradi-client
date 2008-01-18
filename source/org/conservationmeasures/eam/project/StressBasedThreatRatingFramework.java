/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import java.util.Vector;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.ThreatRatingQuestion;
import org.conservationmeasures.eam.utils.Utility;

public class StressBasedThreatRatingFramework extends ThreatRatingFramework
{
	public StressBasedThreatRatingFramework(Project projectToUse)
	{
		super(projectToUse);
		
		stressBasedThreatFormula = new StressBasedThreatFormula();
		threatRatingQuestion = new ThreatRatingQuestion(FactorLink.PSEUDO_TAG_THREAT_RATING_BUNDLE_VALUE);
	}
	
	public StressBasedThreatFormula getStressBasedThreatFormula()
	{
		return stressBasedThreatFormula; 
	}
	
	public int getOverallProjectRating()
	{
		try
		{
			int rollup = getRollupRatingOfThreats();
			int majority = getTargetMajorityRating();
			return Math.max(rollup, majority);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return 0;
		}	
	}
	
	private int getTargetMajorityRating() throws Exception
	{
		Factor[] targets = getProject().getTargetPool().getTargets();
		int[] highestTargetRatingValues = new int[targets.length];
		for (int i = 0; i < targets.length; ++i)
		{
			highestTargetRatingValues[i] = getHighestFactorSummaryRatingValue(targets[i]);
		}
		
		return getStressBasedThreatFormula().getMajority(highestTargetRatingValues);
	}
	
	private int getRollupRatingOfThreats() throws Exception
	{ 
		Factor[] factors = getProject().getCausePool().getDirectThreats();
		int[] summaryValues = new int[factors.length];
		for (int i = 0; i < factors.length; ++i)
		{
			summaryValues[i] = get2PrimeSummaryRatingValue(factors[i]);
		}
		
		return getStressBasedThreatFormula().getHighestRatingRule(summaryValues);
	}
	
	public ChoiceItem getThreatThreatRatingValue(ORef threatRef) throws Exception
	{
		Cause threat = Cause.find(getProject(), threatRef);
		int highestSummaryRating = getHighestFactorSummaryRatingValue(threat);
		return  threatRatingQuestion.findChoiceByCode(Integer.toString(highestSummaryRating));
	}
	
	public int get2PrimeSummaryRatingValue(Factor factor) throws Exception
	{
		return getStressBasedThreatFormula().getSummaryOfBundlesWithTwoPrimeRule(calculateSummaryRatingValues(factor));
	}
	
	public int getHighestFactorSummaryRatingValue(Factor factor) throws Exception
	{
		return getStressBasedThreatFormula().getHighestRatingRule(calculateSummaryRatingValues(factor));
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
	
	private StressBasedThreatFormula stressBasedThreatFormula;
	private ThreatRatingQuestion threatRatingQuestion;
}
