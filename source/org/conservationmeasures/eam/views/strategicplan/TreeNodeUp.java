/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.IdList;
import org.conservationmeasures.eam.objects.Task;

public class TreeNodeUp extends StratPlanDoer
{
	public boolean isAvailable()
	{
		Task selected = getSelectedTask();
		if(selected == null)
			return false;
		ConceptualModelIntervention parent = getPanel().getParentIntervention(selected);
		IdList siblings = parent.getActivityIds();
		if(siblings.find(selected.getId())== 0)
			return false;
		
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		Task selected = getSelectedTask();
		if(selected == null)
			return;
		ConceptualModelIntervention parent = getPanel().getParentIntervention(selected);
		IdList siblings = new IdList(parent.getActivityIds());
		IdList newSiblings = new IdList(siblings);
		BaseId id = selected.getId();
		int wasAt = siblings.find(id);
		newSiblings.removeId(id);
		newSiblings.insertAt(id, wasAt - 1);
		CommandSetObjectData cmd = new CommandSetObjectData(parent.getType(), parent.getId(), ConceptualModelIntervention.TAG_ACTIVITY_IDS, newSiblings.toString());
		getProject().executeCommand(cmd);
		getPanel().selectObject(selected);
	}

}
