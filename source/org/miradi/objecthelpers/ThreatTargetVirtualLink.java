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
package org.miradi.objecthelpers;

import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.objects.ThreatStressRating;
import org.miradi.objects.ValueOption;
import org.miradi.project.Project;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.utils.ThreatStressRatingHelper;
import org.miradi.utils.Utility;

public class ThreatTargetVirtualLink
{
	public ThreatTargetVirtualLink(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public String getCalculatedThreatRatingBundleValue(ORef threatRef, ORef targetRef)
	{
		try
		{
			int calculatedThreatRatingBundleValue = calculateThreatRatingBundleValue(threatRef, targetRef);
			if (calculatedThreatRatingBundleValue == 0)
				return "";
			
			return Integer.toString(calculatedThreatRatingBundleValue);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return EAM.text("Error");
		}
	}
	
	public int calculateThreatRatingBundleValue(ORef threatRef, ORef targetRef) throws Exception
	{
		if(getProject().isStressBaseMode())
			return calculateStressBasedThreatRating(threatRef, targetRef);

		return calculateSimpleThreatRating(threatRef, targetRef);
	}

	private int calculateSimpleThreatRating(ORef threatRef, ORef targetRef) throws Exception
	{
		SimpleThreatRatingFramework framework = getProject().getSimpleThreatRatingFramework();
		ThreatRatingBundle bundle = framework.getBundle(threatRef, targetRef);
		ValueOption valueOption = framework.getBundleValue(bundle);
		return valueOption.getNumericValue();
	}

	private int calculateStressBasedThreatRating(ORef threatRef, ORef targetRef)
	{
		ORefList ratingRefs = getThreatStressRatingRefs(threatRef, targetRef);
		Vector<Integer> ratingBundleValues = new Vector();
		for (int i = 0; i < ratingRefs.size(); ++i)
		{
			ThreatStressRating rating = ThreatStressRating.find(getProject(), ratingRefs.get(i));
			if (rating.isActive())
				ratingBundleValues.add(rating.calculateThreatRating());
		}

		return getProject().getStressBasedThreatFormula().getHighestRatingRule(Utility.convertToIntArray(ratingBundleValues));
	}
	
	public ORef findThreatStressRatingReferringToStress(ORef threatRef, ORef targetRef, ORef stressRef) throws Exception
	{
		ORefList threatStressRatingRefsToUse = getThreatStressRatingRefs(threatRef, targetRef);
		for(int index = 0; index < threatStressRatingRefsToUse.size(); ++index)
		{
			ORef threatStressRatingRef = threatStressRatingRefsToUse.get(index);
			ThreatStressRating threatStressRating = (ThreatStressRating) getProject().findObject(threatStressRatingRef);
			if (stressRef.equals(threatStressRating.getStressRef()))
				return threatStressRatingRef;
		}
		
		throw new Exception("Stress has no matching Threat Stress Rating.  Stress ref = " + stressRef); 
	}
	
	public ORefList getThreatStressRatingRefs(ORef threatRef, ORef targetRef)
	{
		try
		{
			ThreatStressRatingHelper helper = new ThreatStressRatingHelper(getProject());
			return helper.getRelatedThreatStressRatingRefs(threatRef, targetRef);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new ORefList();
		}
	}
		
	public Project getProject()
	{
		return project;
	}
	
	private Project project;
}
