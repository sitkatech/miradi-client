/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.planning.doers;

import java.util.Vector;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.views.diagram.doers.ShareActivityDoer;

public class TreeNodeShareActivityDoer extends ShareActivityDoer
{
	@Override
	public boolean isAvailable()
	{
		if (getSingleSelected(Strategy.getObjectType()) == null)
			return false;
			
		return hasSharableAcitities();
	}
	
	private boolean hasSharableAcitities()
	{
		ORef strategyRef = getParentRefOfShareableObjects();
		if (!Strategy.is(strategyRef))
			return false;

		Strategy strategy = Strategy.find(getProject(), strategyRef);
		Vector<Task> activities = strategy.getActivities();
		Vector<Task> allActivities = getProject().getTaskPool().getAllActivities();
		allActivities.removeAll(activities);
		
		return allActivities.size() > 0;
	}

}
