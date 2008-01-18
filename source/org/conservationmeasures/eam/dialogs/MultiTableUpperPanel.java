/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.dialogs.base.ObjectCollectionPanel;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

public class MultiTableUpperPanel extends ObjectCollectionPanel implements ListSelectionListener
{
	public MultiTableUpperPanel(Project projectToUse, ObjectPicker pickerToUse)
	{
		super(projectToUse, pickerToUse);
		pickerToUse.addSelectionChangeListener(this);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
	}

	public BaseObject getSelectedObject()
	{
		return null;
	}

	public void valueChanged(ListSelectionEvent event)
	{
	}
}
