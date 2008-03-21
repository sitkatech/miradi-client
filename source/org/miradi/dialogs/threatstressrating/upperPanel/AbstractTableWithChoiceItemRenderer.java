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
package org.miradi.dialogs.threatstressrating.upperPanel;

import javax.swing.table.TableModel;

import org.miradi.dialogs.tablerenderers.ChoiceItemTableCellRendererWithGrayCells;
import org.miradi.dialogs.tablerenderers.DefaultFontProvider;
import org.miradi.utils.TableWithRowHeightSaver;

abstract public class AbstractTableWithChoiceItemRenderer extends TableWithRowHeightSaver
{
	public AbstractTableWithChoiceItemRenderer(TableModel model)
	{
		super(model);
		
		setColumnRenderers();
	}

	private void setColumnRenderers()
	{
		MainThreatTableModel model = (MainThreatTableModel) getModel();
		ChoiceItemTableCellRendererWithGrayCells renderer = new ChoiceItemTableCellRendererWithGrayCells(model, new DefaultFontProvider());
		for (int i = 0; i < getColumnCount(); ++i)
		{
			getColumnModel().getColumn(i).setCellRenderer(renderer);
		}
	}
}
