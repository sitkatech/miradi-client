/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.accountingcode;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectPoolManagementPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.AccountCodeIcon;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.utils.BufferedImageFactory;
import org.miradi.utils.ExportableTableInterface;
import org.miradi.utils.SplitterPositionSaverAndGetter;

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
		AccountingCodePoolTable table = createTable();
		BufferedImage image = BufferedImageFactory.createImageFromTable(table);
		return image;
	}

	@Override
	public boolean isExportableTableAvailable()
	{
		return true;
	}
	
	public ExportableTableInterface getExportableTable() throws Exception
	{
		return createTable();
	}
	
	private AccountingCodePoolTable createTable()
	{
		return new AccountingCodePoolTable(new AccountingCodePoolTableModel(getProject()));
	}
	
	@Override
	public boolean isPrintable()
	{
		return true;
	}
	
	@Override
	public JComponent getPrintableComponent() throws Exception
	{
		return createTable();
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Title|Accounting Codes"); 
}
