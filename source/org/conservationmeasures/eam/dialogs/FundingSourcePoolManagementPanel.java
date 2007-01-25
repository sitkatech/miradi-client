/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.icons.FundingSourceIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.SplitterPositionSaver;
import org.martus.swing.UiLabel;

public class FundingSourcePoolManagementPanel extends ObjectPoolManagementPanel
{
	public FundingSourcePoolManagementPanel(Project projectToUse, SplitterPositionSaver splitPositionSaverToUse, Actions actionsToUse, String overviewText) throws Exception
	{
		super(splitPositionSaverToUse, new FundingSourcePoolTablePanel(projectToUse, actionsToUse),
				new FundingSourcePropertiesPanel(projectToUse, BaseId.INVALID));

		add(new UiLabel(overviewText), BorderLayout.BEFORE_FIRST_LINE);
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Funding Sources");
	}
	
	public Icon getIcon()
	{
		return new FundingSourceIcon();
	}
}
