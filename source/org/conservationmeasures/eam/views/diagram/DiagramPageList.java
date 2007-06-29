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
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;

abstract public class DiagramPageList extends JList implements CommandExecutedListener
{
	public DiagramPageList(Project projectToUse, int objectTypeToUse)
	{
		super();
		project = projectToUse;
		objectType = objectTypeToUse;
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setBorder(BorderFactory.createEtchedBorder());
		fillList();
		project.addCommandExecutedListener(this);
	}
	
	public void dispose()
	{
		project.removeCommandExecutedListener(this);
	}
	
	private void fillList()
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
		int objectTypeFromCommand = createCommand.getObjectType();
		if (objectType != objectTypeFromCommand)
			return;
	
		fillList();
	}
	
	abstract public boolean isResultsChainPageList();
	
	abstract public boolean isConceptualModelPageList();
	
	Project project;
	int objectType;
}