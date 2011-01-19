/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.project.threatrating;

import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ThreatTargetVirtualLinkHelper;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ThreatRatingQuestion;
import org.miradi.utils.Utility;

public class StressBasedThreatRatingFramework extends ThreatRatingFramework
{
	public StressBasedThreatRatingFramework(Project projectToUse)
	{
		super(projectToUse);
		
		stressBasedThreatFormula = new StressBasedThreatFormula();
		threatRatingQuestion = new ThreatRatingQuestion();
	}
	
	public StressBasedThreatFormula getStressBasedThreatFormula()
	{
		return stressBasedThreatFormula; 
	}
	
	@Override
	protected ThreatFormula getThreatFormula()
	{
		return getStressBasedThreatFormula();
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
	
	public int getTargetMajorityRating() throws Exception
	{
		Factor[] targets = getProject().getTargetPool().getSortedTargets();
		Vector<Integer> highestTargetRatingValues = new Vector<Integer>();
		for (int i = 0; i < targets.length; ++i)
		{
			Factor target = targets[i];
			int[] bundleValues = calculateSummaryRatingValues(target);
			int summaryRatingValue = getStressBasedThreatFormula().getHighestRating357Not2Prime(bundleValues);
			if (summaryRatingValue > 0)
				highestTargetRatingValues.add(summaryRatingValue);
		}
		
		return getStressBasedThreatFormula().getMajority(Utility.convertToIntArray(highestTargetRatingValues));
	}
	
	public int getRollupRatingOfThreats() throws Exception
	{ 
		Factor[] factors = getProject().getCausePool().getDirectThreats();
		int[] summaryValues = new int[factors.length];
		for (int i = 0; i < factors.length; ++i)
		{
			summaryValues[i] = get2PrimeSummaryRatingValue(factors[i]);
		}
		
		return getStressBasedThreatFormula().getSummaryOfBundlesWithTwoPrimeRule(summaryValues);
	}
	
	@Override
	public ChoiceItem getThreatThreatRatingValue(ORef threatRef) throws Exception
	{
		Cause threat = Cause.find(getProject(), threatRef);
		int summaryRatingOfThreat = get2PrimeSummaryRatingValue(threat);
		return  threatRatingQuestion.findChoiceByCode(Integer.toString(summaryRatingOfThreat));
	}
	
	@Override
	protected ORefSet getUpstreamThreatRefs(Target target)
	{
		return new ThreatTargetVirtualLinkHelper(getProject()).getUpstreamThreatRefsViaTSR(target);
	}
	
	@Override
	protected ORefSet getDownstreamTargetRefs(Cause threat)
	{
		return new ThreatTargetVirtualLinkHelper(getProject()).getDownstreamTargetsVisTSR(threat);
	}

	private StressBasedThreatFormula stressBasedThreatFormula;
	private ThreatRatingQuestion threatRatingQuestion;
}
