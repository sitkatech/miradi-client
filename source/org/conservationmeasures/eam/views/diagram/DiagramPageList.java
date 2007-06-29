/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;

abstract public class DiagramPageList extends JList implements CommandExecutedListener
{
	public DiagramPageList(Project projectToUse, int objectType)
	{
		super();
		project = projectToUse;
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setBorder(BorderFactory.createEtchedBorder());
		fillList(objectType);
		//FIXME nima dispose so that listener is removed
		project.addCommandExecutedListener(this);
	}
	
	private void fillList(int objectType)
	{
		EAMObjectPool pool = project.getPool(objectType);
		ORefList refList = pool.getORefList();
		BaseObject[] diagramObjects = project.getObjectManager().findObjects(refList);
		setListData(diagramObjects);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		
		Command rawCommand = event.getCommand();
		if (! rawCommand.getCommandName().equals(CommandCreateObject.COMMAND_NAME))
			return;
		
		CommandCreateObject createCommand = (CommandCreateObject) rawCommand;
		int objectType = createCommand.getObjectType();
		if (!isDiagramObjectType(objectType))
			return;
	
		fillList(objectType);
	}

	private boolean isDiagramObjectType(int objectType)
	{
		if (objectType == ObjectType.CONCEPTUAL_MODEL_DIAGRAM)
			return true;
		
		if (objectType == ObjectType.RESULTS_CHAIN_DIAGRAM)
			return true;
		
		return false;
	}
	
	Project project;
}