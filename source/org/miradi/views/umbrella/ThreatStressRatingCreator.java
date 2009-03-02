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
package org.miradi.views.umbrella;

import org.miradi.commands.CommandCreateObject;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objecthelpers.CreateThreatStressRatingParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Cause;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.ThreatStressRating;
import org.miradi.project.Project;

public class ThreatStressRatingCreator
{
	public ThreatStressRatingCreator(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public void createAndAddThreatStressRatingsFromTarget(ORef FactorLinkRef, ORef targetRef) throws Exception
	{
		FactorLink factorLink = FactorLink.find(getProject(), FactorLinkRef);
		ORef threatRef = factorLink.getUpstreamThreatRef();
		createAndAddThreatStressRating(targetRef, threatRef);
	}

	public void createAndAddThreatStressRating(ORef targetRef, ORef threatRef)	throws CommandFailedException
	{
		Target target = (Target) project.findObject(targetRef);
		ORefList stressRefs = target.getStressRefs();
		for (int i = 0; i < stressRefs.size(); ++i)
		{			
			ORef stressRef = stressRefs.get(i);
			createThreatStressRating(stressRef, threatRef);
		}
	}

	public ORef createThreatStressRating(ORef stressRef, ORef threatRef) throws CommandFailedException
	{
		stressRef.ensureType(Stress.getObjectType());
		threatRef.ensureType(Cause.getObjectType());
		
		CreateThreatStressRatingParameter extraInfo = new CreateThreatStressRatingParameter(stressRef, threatRef);
		CommandCreateObject createThreatStressRating = new CommandCreateObject(ThreatStressRating.getObjectType(), extraInfo);
		project.executeCommand(createThreatStressRating);
		
		return createThreatStressRating.getObjectRef();
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project;
}
