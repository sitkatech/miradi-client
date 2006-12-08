/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.icons.AccountCodeIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiLabel;

public class AccountingCodePoolManagementPanel extends ObjectPoolManagementPanel
{
	public AccountingCodePoolManagementPanel(Project projectToUse, Actions actionsToUse, String overviewText) throws Exception
	{
		super(new AccountingCodePoolTablePanel(projectToUse, actionsToUse),
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
