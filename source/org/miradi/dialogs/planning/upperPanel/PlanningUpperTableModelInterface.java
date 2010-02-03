/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.dialogs.base.ChoiceItemTableModel;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.utils.ColumnTagProvider;
import org.miradi.utils.OptionalDouble;

public interface PlanningUpperTableModelInterface extends ChoiceItemTableModel, ColumnTagProvider
{
	public Color getCellBackgroundColor(int column);
	public boolean isCurrencyColumn(int column);
	public boolean isChoiceItemColumn(int column);
	public boolean isProgressColumn(int column);
	public boolean isWorkUnitColumn(int column);
	public String getColumnGroupCode(int modelColumn);
	public String getTagForCell(int objectType, int modelColumn);
	public boolean isColumnExpandable(int modelColumn);
	public boolean isColumnCollapsable(int modelColumn);
	public DateUnit getDateUnit(int modelColumn);
	public void respondToExpandOrCollapseColumnEvent(int modelColumnIndex) throws Exception;
	public boolean isFullTimeEmployeeFractionAvailable(int row, int modelColumn);
	public void updateFullTimeEmployeeDaysPerYearFraction(int row, int modelColumn, double percent);
	public OptionalDouble getCellFraction(int row, int modelColumn);
}
