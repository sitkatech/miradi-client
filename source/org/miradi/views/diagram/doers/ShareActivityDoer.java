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

import java.util.Vector;

import org.miradi.dialogs.activity.ShareableActivityPoolTablePanel;
import org.miradi.dialogs.base.ObjectPoolTablePanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.views.planning.doers.AbstractShareDoer;

public class ShareActivityDoer extends AbstractShareDoer
{	
	public boolean isAvailable()
	{
		if (getSingleSelected(Strategy.getObjectType()) == null)
			return false;
		
		return hasSharableActivities();
	}
	
	private boolean hasSharableActivities()
	{
		ORef strategyRef = getParentRefOfShareableObjects();
		if (!Strategy.is(strategyRef))
			return false;

		Strategy strategy = Strategy.find(getProject(), strategyRef);
		Vector<Task> activities = strategy.getActivities();
		Vector<Task> activitiesNotAlreadyInStrategy = getProject().getTaskPool().getAllActivities();
		activitiesNotAlreadyInStrategy.removeAll(activities);
		
		return activitiesNotAlreadyInStrategy.size() > 0;
	}

	protected String getParentTaskIdsTag()
	{
		return Strategy.TAG_ACTIVITY_IDS;
	}

	protected String getShareDialogTitle()
	{
		return EAM.text("Share Activity");
	}

	protected ObjectPoolTablePanel createShareableObjectPoolTablePanel(ORef parentOfSharedObjectRefs)
	{
		return new ShareableActivityPoolTablePanel(getMainWindow(), parentOfSharedObjectRefs);
	}

	protected int getParentType()
	{
		return Strategy.getObjectType();
	}
}
