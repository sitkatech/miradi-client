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

import javax.swing.table.TableModel;

import org.miradi.utils.ColumnTagProvider;

public class PlanningTreeMultiTableModel extends MultiTableModel implements ColumnTagProvider
{
	@Override
	public void addModel(TableModel modelToAdd)
	{
		throw new RuntimeException(getClass().getName() + ".addModel: Model must be a ColumnTagProvider");
	}
	
	public void addModel(PlanningUpperTableModelInterface modelToAdd)
	{
		// NOTE: We are calling a DIFFERENT variant in super!
		// This is NOT a do-nothing method!!
		super.addModel(modelToAdd);
	}
	
	public String getColumnTag(int column)
	{
		return getCastedModel(column).getColumnTag(findColumnWithinSubTable(column));
	}
	
	public Color getCellBackgroundColor(int row, int column)
	{
		return getCastedModel(column).getCellBackgroundColor(findColumnWithinSubTable(column));
	}
	
	public boolean isCurrencyColumn(int column)
	{
		return getCastedModel(column).isCurrencyColumn(findColumnWithinSubTable(column));
	}

	private PlanningUpperTableModelInterface getCastedModel(int column)
	{
		return (PlanningUpperTableModelInterface) findTable(column);
	}

}
