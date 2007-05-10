package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;

public class FakeCollectionPanel extends ObjectCollectionPanel
{
	public FakeCollectionPanel(Project projectToUse) 
	{
		super(projectToUse, null);
	}
		
	public void commandExecuted(CommandExecutedEvent event)
	{
	}

	public BaseObject getSelectedObject()
	{
		return null;
	}
}