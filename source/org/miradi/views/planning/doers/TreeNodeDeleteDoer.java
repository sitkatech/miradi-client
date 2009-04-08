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

import java.util.Arrays;
import java.util.Vector;

import org.miradi.commands.CommandDeleteObject;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.Task;
import org.miradi.views.diagram.DeleteAnnotationDoer;
import org.miradi.views.umbrella.DeleteActivity;
import org.miradi.views.umbrella.doers.AbstractDeleteDoer;

public class TreeNodeDeleteDoer extends AbstractDeleteDoer
{
	public boolean isAvailable()
	{
		BaseObject selected = getSingleSelectedObject();
		if(selected == null)
			return false;
		
		return canDelete(selected);
	}

	private boolean canDelete(BaseObject selected)
	{
		if (Indicator.is(selected))
			return true;
		
		if (Objective.is(selected))
			return true;
		
		if (Measurement.is(selected))
			return true;
		
		if (Assignment.is(selected))
			return true;
		
		return Task.is(selected.getType());
	}

	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		BaseObject selected = getSingleSelectedObject();
		
		//TODO this might be a redundant test since isAvailable is testing same thing.  why is it here
		if(!canDelete(selected))
			return;
		
		try
		{
			if (Task.is(selected))
				deleteTask(selected);
			
			if (Indicator.is(selected))
				deleteAnnotation(selected, Factor.TAG_INDICATOR_IDS);
			
			if (Objective.is(selected))
				deleteAnnotation(selected, Factor.TAG_OBJECTIVE_IDS);
			
			if (Measurement.is(selected))
				deleteAnnotation(selected, Indicator.TAG_MEASUREMENT_REFS);
			
			if (Assignment.is(selected))
				deleteAnnotation(selected, Task.TAG_ASSIGNMENT_IDS);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	private void deleteAnnotation(BaseObject selected, String annotationListTag) throws Exception
	{
		Vector commands = new Vector();
		ORefList ownerRefs = selected.findObjectsThatReferToUs();
		for (int refIndex = 0; refIndex < ownerRefs.size(); ++refIndex)
		{
			ORef ownerRef = ownerRefs.get(refIndex);
			BaseObject owner = getProject().findObject(ownerRef);
			commands.add(DeleteAnnotationDoer.buildCommandToRemoveAnnotationFromObject(owner, annotationListTag, selected.getRef()));
		}
		
		commands.addAll(Arrays.asList(selected.createCommandsToClear()));
		commands.add(new CommandDeleteObject(selected.getRef()));
		getProject().executeCommandsAsTransaction(commands);
	}

	private void deleteTask(BaseObject selected) throws CommandFailedException
	{		
		Task selectedTaskToDelete = (Task) selected;
		if (shouldDeleteFromParentOnly(selectedTaskToDelete))
			DeleteActivity.deleteTaskWithUserConfirmation(getProject(), getSelectionHierarchy(), selectedTaskToDelete);
		else
			DeleteActivity.deleteTaskWithUserConfirmation(getProject(), selectedTaskToDelete.findObjectsThatReferToUs(), selectedTaskToDelete);
	}
	
	private boolean shouldDeleteFromParentOnly(Task selectedTaskToDelete)
	{
		ORefList referrers = selectedTaskToDelete.findObjectsThatReferToUs();
		ORefList selectionHierarchy = getSelectionHierarchy();
		
		return selectionHierarchy.containsAnyOf(referrers);
	}
}
