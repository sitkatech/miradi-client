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
import javax.swing.table.TableColumn;

import org.miradi.main.MainWindow;

public class TargetSummaryRowTable extends AbstractTableWithChoiceItemRenderer
{
	public TargetSummaryRowTable(MainWindow mainWindowToUse, TargetSummaryRowTableModel model, TargetThreatLinkTable tableThatControlsColumns) throws Exception
	{
		super(mainWindowToUse, model, UNIQUE_IDENTIFIER);
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTableHeader().setPreferredSize(new Dimension(0, 0));
		setCellSelectionEnabled(false);
		
		setColumnController(model, tableThatControlsColumns);
	}

	private void setColumnController(TargetSummaryRowTableModel model, TargetThreatLinkTable tableThatControlsColumns) throws Exception
	{
		columnController = tableThatControlsColumns;
		
		reloadColumnSequences();
		reloadColumnWidths();
		
		model.fireTableDataChanged();
	}
	
	@Override
	public int convertColumnIndexToModel(int viewColumnIndex)
	{
		if (isValidColumnController())
			return columnController.convertColumnIndexToModel(viewColumnIndex);
		
		return viewColumnIndex;
	}

	@Override
	public int convertColumnIndexToView(int modelColumnIndex)
	{	
		if (isValidColumnController())
			return columnController.convertColumnIndexToView(modelColumnIndex);
		
		return modelColumnIndex;
	}
	
	private boolean isValidColumnController()
	{
		return columnController != null;
	}
		
	public boolean shouldSaveColumnSequence()
	{
		return false;
	}
		
	@Override
	public void reloadColumnWidths()
	{
		for (int tableColumn = 0; tableColumn < getColumnCount(); ++tableColumn)
		{	
			TableColumn thisColumn = getColumnModel().getColumn(tableColumn);
			TableColumn thatColumn = columnController.getColumnModel().getColumn(tableColumn);
			thisColumn.setWidth(thatColumn.getWidth());
			thisColumn.setPreferredWidth(thatColumn.getPreferredWidth());
		}
	}

	private TargetThreatLinkTable columnController;
	
	public static final String UNIQUE_IDENTIFIER = "TargetSummaryRowTable";
}
