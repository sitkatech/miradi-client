/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.actions.ActionResourceListAdd;
import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.dialogs.ResourcePoolManagementPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class PossibleResourcesPanel extends ResourcePoolManagementPanel
{
	public PossibleResourcesPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse.getProject(), mainWindowToUse, mainWindowToUse.getActions(), OVERVIEW_TEXT);
		ObjectsAction addResourceAction = mainWindowToUse.getActions().getObjectsAction(ActionResourceListAdd.class);
		addTablePanelButton(addResourceAction);
	}
	
	final static String OVERVIEW_TEXT = 
		EAM.text("<html>" +
				"<p>" +
				"This table lists all the Resources that have been created within this project. " +
				"</p>" +
				"<p>" +
				"You can select existing resources and add them to the list of resources, " +
				"or you can create new resources." +
				"</p>" +
				"</html");




}
