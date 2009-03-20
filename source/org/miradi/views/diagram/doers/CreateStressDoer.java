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
package org.miradi.views.diagram.doers;

import org.miradi.commands.CommandCreateObject;
import org.miradi.objecthelpers.CreateThreatStressRatingParameter;
import org.miradi.objecthelpers.FactorLinkSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.ThreatStressRating;
import org.miradi.project.Project;
import org.miradi.views.diagram.CreateAnnotationDoer;

public class CreateStressDoer extends CreateAnnotationDoer
{
	public static void createThreatStressRatingsForAttachedLinks(Project project, ORef newlyCreatedStressRef, Factor selectedFactor) throws Exception
	{
		if(!selectedFactor.isTarget())
			return;

		if (newlyCreatedStressRef.getObjectType() != Stress.getObjectType())
			return;
		
		Target target = (Target) selectedFactor;
		FactorLinkSet directThreatTargetLinks = target.getThreatTargetFactorLinks();
		for(FactorLink factorLink : directThreatTargetLinks)
		{
			ORef threatRef = factorLink.getUpstreamThreatRef();
			CreateThreatStressRatingParameter extraInfo = new CreateThreatStressRatingParameter(newlyCreatedStressRef, threatRef);
			CommandCreateObject createThreatStressRating = new CommandCreateObject(ThreatStressRating.getObjectType(), extraInfo);
			project.executeCommand(createThreatStressRating);			
		}
	}

	public int getAnnotationType()
	{
		return Stress.getObjectType();
	}
	public String getAnnotationListTag()
	{
		return Target.TAG_STRESS_REFS;
	}
}
