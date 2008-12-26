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
package org.miradi.dialogs.fieldComponents;

import javax.swing.table.TableModel;

import org.miradi.dialogs.tablerenderers.BasicTableCellRendererFactory;
import org.miradi.main.MainWindow;
import org.miradi.utils.TableRowHeightManager;
import org.miradi.utils.TableWithHelperMethods;

public class PanelTable extends TableWithHelperMethods
{
	public PanelTable(MainWindow mainWindowToUse, TableModel model)
	{
		super(model);
		mainWindow = mainWindowToUse;
		setFontData();
	}

	private void setFontData()
	{
		setFont(getMainWindow().getUserDataPanelFont());
		getTableHeader().setFont(getMainWindow().getUserDataPanelFont());
		setRowHeight(getFontMetrics(getFont()).getHeight() + VERTICAL_FONT_CUSHION);
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}

	private static final int INTERCELL_LINE_SIZE = 3;
	private static final int VERTICAL_FONT_CUSHION = INTERCELL_LINE_SIZE + 
			TableRowHeightManager.ROW_RESIZE_MARGIN + 2*BasicTableCellRendererFactory.CELL_MARGIN;
	
	private MainWindow mainWindow;
}
