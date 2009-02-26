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
package org.miradi.utils;

import java.util.Vector;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Cause;
import org.miradi.objects.Target;
import org.miradi.objects.ThreatStressRating;
import org.miradi.project.Project;

public class ThreatStressRatingHelper
{
	public ThreatStressRatingHelper(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public Vector<ThreatStressRating> getRelatedThreatStressRatings(Target target, Cause threat)
	{
		Vector<ThreatStressRating> threatStressRatings = new Vector();
		ORefList allThreatStressRatingRefs = getProject().getThreatStressRatingPool().getRefList();
		for (int index = 0; index < allThreatStressRatingRefs.size(); ++index)
		{
		}
		
		return threatStressRatings;
	}

	private Project getProject()
	{
		return project;
	}
	
	private Project project;
}
