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

package org.miradi.dialogfields;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Desire;
import org.miradi.project.Project;
import org.miradi.utils.CommandVector;

public class StrategyDesireOverrideListField extends AbstractRelevancyOverrideListField
{
	public StrategyDesireOverrideListField(Project projectToUse, ORef strategyRef, int objectTypeToUpdate)
	{
		super(projectToUse, strategyRef, objectTypeToUpdate);
	}

	@Override
	protected CommandVector getCommandsToEnsureProperRelevancy(ORefList selectedObjectRefs, ORef objectRef) throws Exception
	{
		Desire desire = Desire.findDesire(getProject(), objectRef);
		if (selectedObjectRefs.contains(objectRef))
			return desire.createCommandsToEnsureStrategyOrActivityIsRelevant(getORef());

		return desire.createCommandsToEnsureStrategyOrActivityIsIrrelevant(getORef());
	}

	@Override
	protected ORefList findAllRelevantObjects(int objectTypeToUpdate) throws Exception
	{
		return Desire.findAllRelevantDesires(getProject(), getORef(), objectTypeToUpdate);
	}
}
