/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.commands.CommandSetObjectData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objects.Desire;

public class CreateRelevancyActivityDoer extends CreateActivityNodeDoer
{
	@Override
	protected void doWork(ORefList selectionHiearchy, ORef newTaskRef) throws Exception
	{
		ORef desireRef = findDesireRef(selectionHiearchy);
		if (desireRef.isInvalid())
			return;
		
		Desire desire = Desire.findDesire(getProject(), desireRef);
		ORefList strategyAndActivityRefs = desire.getRelevantStrategyAndActivityRefs();
		strategyAndActivityRefs.add(newTaskRef);
		RelevancyOverrideSet relevancySet = desire.getCalculatedRelevantStrategyActivityOverrides(strategyAndActivityRefs);
		
		CommandSetObjectData addNewActivityToRelevancyList = new CommandSetObjectData(desireRef, Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, relevancySet.toString());
		getProject().executeCommand(addNewActivityToRelevancyList);
	}
	
	private ORef findDesireRef(ORefList selectionHierarchy)
	{
		for (int index = 0; index < selectionHierarchy.size(); ++index)
		{
			ORef ref = selectionHierarchy.get(index);
			if (Desire.isDesire(ref))
				return ref;
		}
		
		return ORef.INVALID;
	}
}
