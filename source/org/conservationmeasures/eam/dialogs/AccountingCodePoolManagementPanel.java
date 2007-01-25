/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.icons.AccountCodeIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.SplitterPositionSaver;
import org.martus.swing.UiLabel;

public class AccountingCodePoolManagementPanel extends ObjectPoolManagementPanel
{
	public AccountingCodePoolManagementPanel(Project projectToUse, SplitterPositionSaver splitPositionSaverToUse, Actions actionsToUse, String overviewText) throws Exception
	{
		super(splitPositionSaverToUse, new AccountingCodePoolTablePanel(projectToUse, actionsToUse),
				new AccountingCodePropertiesPanel(projectToUse, BaseId.INVALID));

		add(new UiLabel(overviewText), BorderLayout.BEFORE_FIRST_LINE);
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Accounting Codes");
	}
	
	public Icon getIcon()
	{
		return new AccountCodeIcon();
	}
}
