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

import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.ThreatStressRating;
import org.miradi.project.Project;

//FIXME this class under development along with its test
public class ThreatStressRatingEnsurer implements CommandExecutedListener 
{
	public ThreatStressRatingEnsurer(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public void dispose()
	{
		getProject().removeCommandExecutedListener(this);
	}
	
	public void createOrDeleteThreatStressRatingsAsNeeded()
	{
		ORefList allTargetRefs = getProject().getTargetPool().getRefList();
		for (int index = 0; index < allTargetRefs.size(); ++index)
		{
			Target target = Target.find(getProject(), allTargetRefs.get(index));
			ORefList stressRefs = target.getStressRefs();
			findReferringThreatStressRatingRefs(stressRefs);
		}
	}

	private void findReferringThreatStressRatingRefs(ORefList stressRefs)
	{
		ORefSet referringThreatStressRatingRefs = new ORefSet();
		for (int index = 0; index < stressRefs.size(); ++index)
		{
			Stress stress = Stress.find(getProject(), stressRefs.get(index));
			ORefList referrerRefs = stress.findObjectsThatReferToUs(ThreatStressRating.getObjectType());
			referringThreatStressRatingRefs.addAllRefs(referrerRefs);		
		}
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		createOrDeleteThreatStressRatingsAsNeeded();
	}
	
	public Project getProject()
	{
		return project;
	}
	
	private Project project;
}
