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
package org.miradi.dialogs.threatrating.upperPanel;

import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.miradi.main.MainWindow;

public class OverallProjectSummaryCellTable extends AbstractTableWithChoiceItemRenderer
{
	public OverallProjectSummaryCellTable(MainWindow mainWindowToUse, TableModel model)
	{
		super(mainWindowToUse, model, UNIQUE_IDENTIFIER);
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTableHeader().setPreferredSize(new Dimension(0, 0));
		setCellSelectionEnabled(false);
	}
	
	public static final String UNIQUE_IDENTIFIER = "OverallProjectSummaryCellTable";
}
