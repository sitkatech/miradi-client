/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.dialogs.tablerenderers.DefaultTableCellRendererWithPreferredHeightFactory;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;

public class AbstractAssignmentDetailsMainTable extends AbstractAssignmentDetailsTable
{
	public AbstractAssignmentDetailsMainTable(MainWindow mainWindowToUse, AbstractAssignmentSummaryTableModel modelToUse, String uniqueIdentifier) throws Exception
	{
		super(mainWindowToUse, modelToUse, uniqueIdentifier);
		
		setBackground(getColumnBackGroundColor(0, 0));
		rebuildColumnEditorsAndRenderers();
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		TableCellRenderer cellRenderer = super.getCellRenderer(row, column);

		if (getModel().isCellEditable(row, column))
			return cellRenderer;

		DefaultTableCellRendererWithPreferredHeightFactory totalsRenderer = (DefaultTableCellRendererWithPreferredHeightFactory) cellRenderer;
		totalsRenderer.setCellBackgroundColor(EAM.READONLY_BACKGROUND_COLOR);

		return cellRenderer;
	}

	@Override
	public Color getColumnBackGroundColor(int row, int column)
	{
		return AppPreferences.RESOURCE_TABLE_BACKGROUND;
	}

	@Override
	public void rebuildColumnEditorsAndRenderers() throws Exception
	{
	}
	
	protected AbstractAssignmentSummaryTableModel getAbstractSummaryTableModel()
	{
		return (AbstractAssignmentSummaryTableModel) getModel();
	}
	
	@Override
	public boolean shouldSaveColumnSequence()
	{
		return false;
	}

	@Override
	public String getToolTipText(MouseEvent event)
	{
		Point at = new Point(event.getX(), event.getY());
		int row = rowAtPoint(at);
		int column = columnAtPoint(at);
		Object object = getAbstractSummaryTableModel().getValueAt(row, column);
		if(object == null)
			return null;
		
		return "<html><b>" + object.toString() + "</b><br>";
	}
}
