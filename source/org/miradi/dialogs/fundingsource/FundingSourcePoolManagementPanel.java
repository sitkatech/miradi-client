/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.fundingsource;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.miradi.dialogs.base.ObjectPoolManagementPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.FundingSourceIcon;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.AbstractTableExporter;
import org.miradi.utils.BufferedImageFactory;
import org.miradi.utils.ObjectTableModelExporter;

public class FundingSourcePoolManagementPanel extends ObjectPoolManagementPanel
{
	public FundingSourcePoolManagementPanel(MainWindow mainWindowToUse, String overviewText) throws Exception
	{
		super(mainWindowToUse, new FundingSourcePoolTablePanel(mainWindowToUse),
				new FundingSourcePropertiesPanel(mainWindowToUse.getProject(), BaseId.INVALID));

		add(new PanelTitleLabel(overviewText), BorderLayout.BEFORE_FIRST_LINE);
	}

	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public Icon getIcon()
	{
		return new FundingSourceIcon();
	}
	
	@Override
	public boolean isImageAvailable()
	{
		return true;
	}
	
	@Override
	public BufferedImage getImage()
	{
		return BufferedImageFactory.createImageFromTable(createTable());
	}

	private FundingSourcePoolTable createTable()
	{
		return new FundingSourcePoolTable(getMainWindow(), createTableModel());
	}

	private FundingSourcePoolTableModel createTableModel()
	{
		return new FundingSourcePoolTableModel(getProject());
	}
	
	@Override
	public boolean isExportableTableAvailable()
	{
		return true;
	}
	
	@Override
	public AbstractTableExporter getTableExporter() throws Exception
	{
		return new ObjectTableModelExporter(createTableModel());
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
	
	private static String PANEL_DESCRIPTION = EAM.text("Title|Funding Sources"); 
}
