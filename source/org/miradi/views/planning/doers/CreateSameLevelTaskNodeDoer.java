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

package org.miradi.views.planning.doers;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Task;

public class CreateSameLevelTaskNodeDoer extends AbstractCreateTaskNodeDoer
{
	@Override
	protected ORef getParentRef()
	{
		ORefList selectionHierarchy = getSelectionHierarchy();
		if (selectionHierarchy.isEmpty())
			return ORef.INVALID;

		ORef selectedRef = selectionHierarchy.getFirstElement();
		if (selectedRef.isInvalid())
			return ORef.INVALID;
		
		if (!Task.is(selectedRef))
			return ORef.INVALID;
		
		Task task = Task.find(getProject(), selectedRef);
		if (!task.isTask())
			return ORef.INVALID;
		
		selectionHierarchy.removeFirstElement();
		ORef parentRefOfSelection = selectionHierarchy.getFirstElement();
		if (Task.is(parentRefOfSelection))
			return parentRefOfSelection;
		
		return ORef.INVALID;
	}
}
