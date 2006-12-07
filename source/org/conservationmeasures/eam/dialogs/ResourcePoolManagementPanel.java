/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.icons.ProjectResourceIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiLabel;

public class ResourcePoolManagementPanel extends ObjectPoolManagementPanel
{
	public ResourcePoolManagementPanel(Project projectToUse, Actions actionsToUse, String overviewText) throws Exception
	{
		super(new ResourcePoolTablePanel(projectToUse, actionsToUse),
				new ResourcePropertiesPanel(projectToUse, BaseId.INVALID));

		add(new UiLabel(overviewText), BorderLayout.BEFORE_FIRST_LINE);
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Resources");
	}
	
	public Icon getIcon()
	{
		return new ProjectResourceIcon();
	}
}