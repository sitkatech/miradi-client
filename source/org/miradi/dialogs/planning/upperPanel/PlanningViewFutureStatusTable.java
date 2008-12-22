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
package org.miradi.dialogs.planning.upperPanel;

import java.awt.Color;

import javax.swing.table.TableCellRenderer;

import org.miradi.diagram.renderers.FactorRenderer;
import org.miradi.dialogs.tablerenderers.FontForObjectTypeProvider;
import org.miradi.dialogs.tablerenderers.ObjectTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.SingleLineObjectTableCellRendererFactory;
import org.miradi.main.MainWindow;
import org.miradi.utils.TableWithTreeTableNodes;

public class PlanningViewFutureStatusTable extends TableWithTreeTableNodes
{
	public PlanningViewFutureStatusTable(MainWindow mainWindowToUse, PlanningViewFutureStatusTableModel model, FontForObjectTypeProvider fontProvider)
	{
		super(mainWindowToUse, model, UNIQUE_IDENTIFIER);
		renderer = new SingleLineObjectTableCellRendererFactory(model, fontProvider);
	}
	
	public TableCellRenderer getCellRenderer(int row, int tableColumn)
	{
		renderer.setCellBackgroundColor(getColumnBackGroundColor(tableColumn));
		return renderer;
	}

	public Color getColumnBackGroundColor(int tableColumn)
	{
		return FactorRenderer.ANNOTATIONS_COLOR;
	}
	
	public static final String UNIQUE_IDENTIFIER = "PlanningViewFutureStatusTable";

	private ObjectTableCellRendererFactory renderer;
}
