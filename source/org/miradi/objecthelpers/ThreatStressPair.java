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

import org.miradi.objects.Cause;
import org.miradi.objects.Stress;
import org.miradi.objects.ThreatStressRating;
import org.miradi.project.Project;

public class ThreatStressPair 
{		
	public ThreatStressPair(Project projectToUse, ThreatStressRating threatStressRating)
	{
		this(projectToUse, threatStressRating.getThreatRef(), threatStressRating.getStressRef());
	}

	public ThreatStressPair(Project projectToUse, ORef threatRefToUse, ORef stressRefToUse)
	{
		project = projectToUse;
		threatRefToUse.ensureType(Cause.getObjectType());
		stressRefToUse.ensureType(Stress.getObjectType());
		
		threatRef = threatRefToUse;
		stressRef = stressRefToUse;
	}
	
	public static ORef findMatchingThreatStressRating(Project projectToUse, ORef threatRefToUse, ORef stressRefToUse)
	{
		ORefList tsrReferrerRefsToStress = getTsrReferrerRefsToStress(projectToUse, stressRefToUse);
		ORefList tsrReferrerRefsToThreat = getTsrReferrerRefsToThreat(projectToUse, threatRefToUse);
		ORefList overLappingRefs = tsrReferrerRefsToStress.getOverlappingRefs(tsrReferrerRefsToThreat);
		
		return overLappingRefs.getRefForType(ThreatStressRating.getObjectType());		
	}
	
	public ORef findMatchingThreatStressRating()
	{
		return findMatchingThreatStressRating(getProject(), getThreatRef(), getStressRef());
	}

	private static ORefList getTsrReferrerRefsToStress(Project projectToUse, ORef stressRefToUse)
	{
		Stress stress = Stress.find(projectToUse, stressRefToUse);
		return stress.findObjectsThatReferToUs(ThreatStressRating.getObjectType());
	}
	
	private static ORefList getTsrReferrerRefsToThreat(Project projectToUse, ORef threatRefToUse)
	{
		Cause threat = Cause.find(projectToUse, threatRefToUse);
		return threat.findObjectsThatReferToUs(ThreatStressRating.getObjectType());
	}
	
	public ORef getThreatRef()
	{
		return threatRef;
	}
	
	public ORef getStressRef()
	{
		return stressRef;
	}
	
	@Override
	public boolean equals(Object rawOther)
	{
		if (!(rawOther instanceof ThreatStressPair))
			return false;
		
		ThreatStressPair other = (ThreatStressPair) rawOther;
		if (!getThreatRef().equals(other.getThreatRef()))
			return false;
		
		if (!getStressRef().equals(other.getStressRef()))
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode()
	{
		return getThreatRef().hashCode() + getStressRef().hashCode();
	}
	
	private Project getProject()
	{
		return project;
	}
			
	private ORef threatRef;
	private ORef stressRef;
	private Project project;
}