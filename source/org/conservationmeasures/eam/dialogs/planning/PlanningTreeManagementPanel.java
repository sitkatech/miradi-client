/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.ObjectListManagementPanel;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreeManagementPanel extends ObjectListManagementPanel implements CommandExecutedListener
{
	public PlanningTreeManagementPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse, PlanningTreeTablePanel.createPlanningTreeTablePanel(mainWindowToUse), getPropertiesPanel(mainWindowToUse));
		project = mainWindowToUse.getProject();
		project.addCommandExecutedListener(this);
	}

	public void dispose()
	{
		project.removeCommandExecutedListener(this);
		super.dispose();
	}

	public static PlanningTreePropertiesPanel getPropertiesPanel(MainWindow mainWindowToUse) throws Exception
	{
		return new PlanningTreePropertiesPanel(mainWindowToUse.getProject(), ORef.INVALID);
	}
	
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		if(!event.isSetDataCommand())
			return;
		
		CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
		if(cmd.getObjectType() != ViewData.getObjectType())
			return;
		if(!cmd.getFieldTag().equals(ViewData.TAG_PLANNING_HIDDEN_TYPES))
			return;
		
		getPlanningModel().rebuildEntireTree();
	}
	
	PlanningTreeTablePanel getPlanningTreePanel()
	{
		return (PlanningTreeTablePanel)getListComponent();
	}
	
	PlanningTreeModel getPlanningModel()
	{
		return (PlanningTreeModel)getPlanningTreePanel().getModel();
	}

	private static String PANEL_DESCRIPTION = EAM.text("Tab|Planning");
	
	Project project;

}
