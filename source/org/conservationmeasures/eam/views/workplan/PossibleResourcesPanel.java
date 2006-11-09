/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.actions.ActionResourceListAdd;
import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.dialogs.ResourceListManagementPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class PossibleResourcesPanel extends ResourceListManagementPanel
{
	public PossibleResourcesPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse, getExtraButtonActions(mainWindowToUse), OVERVIEW_TEXT);
	}
	
	private static ObjectsAction[] getExtraButtonActions(MainWindow mainWindowToUse)
	{
		ObjectsAction addResourceAction = mainWindowToUse.getActions().getObjectsAction(ActionResourceListAdd.class);
		return new ObjectsAction[] {addResourceAction};
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
