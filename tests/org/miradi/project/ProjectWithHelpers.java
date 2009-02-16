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
package org.miradi.project;

import java.io.File;
import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.database.ProjectServer;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.PersistentDiagramModel;
import org.miradi.ids.BaseId;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.ConceptualModelDiagramPool;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramObject;
import org.miradi.views.diagram.DiagramModelUpdater;

public class ProjectWithHelpers extends Project implements CommandExecutedListener
{
	public ProjectWithHelpers(ProjectServer server) throws Exception
	{
		super(server);

		addCommandExecutedListener(this);
		diagramModel = new PersistentDiagramModel(this);
		commandStack = new Vector();
	}
	
	public DiagramObject getTestingDiagramObject()
	{
		return getDiagramModel().getDiagramObject();
	}
	
	public DiagramModel getDiagramModel()
	{
		return diagramModel;
	}

	protected void finishOpening() throws Exception
	{
		super.finishOpening();
		loadDiagram();
	}

	public void close() throws Exception
	{
		super.close();
		
		diagramModel = null;
	}

	protected void setDefaultDiagramPage(int objectType)
	{
		//TODO overrode to be empty, so that tests dont fail
	}

	public void closeAndReopen() throws Exception
	{
		File topDirectory = getDatabase().getTopDirectory();
		getTestDatabase().closeAndDontDelete();
		createOrOpen(topDirectory);
	}

	protected void fireCommandExecuted(Command command)
	{
		super.fireCommandExecuted(command);
		if(commandStack != null)
			commandStack.add(command);
	}

	protected Command getLastCommand()
	{
		return (Command)commandStack.remove(commandStack.size()-1);
	}

	public ProjectServerForTesting getTestDatabase()
	{
		return (ProjectServerForTesting)getDatabase();
	}

	public void loadDiagram() throws Exception
	{
		ConceptualModelDiagramPool diagramContentsPool = (ConceptualModelDiagramPool) getPool(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
		ORefList oRefs = diagramContentsPool.getORefList();
		ConceptualModelDiagram diagramContentsObject = getDiagramContentsObject(oRefs);
		getDiagramModel().fillFrom(diagramContentsObject);
	}
	
	ConceptualModelDiagram getDiagramContentsObject(ORefList oRefs) throws Exception
	{
		if (oRefs.size() == 0)
		{
			BaseId id = createObjectAndReturnId(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
			return (ConceptualModelDiagram) findObject(new ORef(ObjectType.CONCEPTUAL_MODEL_DIAGRAM, id));
		}
		if (oRefs.size() > 1)
		{
			EAM.logVerbose("Found more than one diagram contents inside pool");
		}

		ORef oRef = oRefs.get(0);
		return (ConceptualModelDiagram) findObject(oRef);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		if (! event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return;
	
		try
		{
			CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
			DiagramModelUpdater modelUpdater = new DiagramModelUpdater(this, getDiagramModel());
			modelUpdater.commandSetObjectDataWasExecuted(setCommand);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private PersistentDiagramModel diagramModel;
	private Vector commandStack;
}
