/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import org.miradi.objects.BaseObject;
import org.miradi.objects.StrategyActivityRelevancyInterface;

public class CreateRelevancyActivityDoer extends CreateActivityNodeDoer
{
	@Override
	protected void doWork(ORefList selectionHierarchy, ORef newTaskRef) throws Exception
	{
		ORef parentObjectRef = findParentObjectRef(selectionHierarchy);
		if (parentObjectRef.isInvalid())
			return;

		StrategyActivityRelevancyInterface parentObject = (StrategyActivityRelevancyInterface) getProject().findObject(parentObjectRef);
		ORefList strategyAndActivityRefs = parentObject.getRelevantStrategyAndActivityRefs();
		strategyAndActivityRefs.add(newTaskRef);
		RelevancyOverrideSet relevancySet = parentObject.getCalculatedRelevantStrategyActivityOverrides(strategyAndActivityRefs);
		
		CommandSetObjectData addNewActivityToRelevancyList = new CommandSetObjectData(parentObjectRef, parentObject.getRelevantStrategyActivitySetTag(), relevancySet.toString());
		getProject().executeCommand(addNewActivityToRelevancyList);
	}
	
	private ORef findParentObjectRef(ORefList selectionHierarchy)
	{
		for (int index = 0; index < selectionHierarchy.size(); ++index)
		{
			ORef ref = selectionHierarchy.get(index);
			BaseObject parentObject = getProject().findObject(ref);

			if (parentObject instanceof StrategyActivityRelevancyInterface)
				return ref;
		}
		
		return ORef.INVALID;
	}
}
