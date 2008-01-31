/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.accountingcode;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectPoolManagementPanel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.icons.AccountCodeIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.BufferedImageFactory;
import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

public class AccountingCodePoolManagementPanel extends ObjectPoolManagementPanel
{
	public AccountingCodePoolManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, Actions actionsToUse, String overviewText) throws Exception
	{
		super(projectToUse, splitPositionSaverToUse, new AccountingCodePoolTablePanel(projectToUse, actionsToUse),
				new AccountingCodePropertiesPanel(projectToUse, BaseId.INVALID));

		add(new PanelTitleLabel(overviewText), BorderLayout.BEFORE_FIRST_LINE);
	}

	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public Icon getIcon()
	{
		return new AccountCodeIcon();
	}

	public boolean isImageAvailable()
	{
		return true;
	}
	
	public BufferedImage getImage()
	{
		AccountingCodePoolTable table = new AccountingCodePoolTable(new AccountingCodePoolTableModel(getProject()));
		BufferedImage image = BufferedImageFactory.createImageFromTable(table);
		return image;
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Title|Accounting Codes"); 
}
