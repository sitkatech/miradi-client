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
package org.miradi.dialogs.planning.propertiesPanel;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTable;

import org.miradi.dialogs.base.EditableObjectTable;
import org.miradi.dialogs.base.EditableObjectTableModel;
import org.miradi.main.MainWindow;

abstract public class AssignmentsComponentTable extends EditableObjectTable
{
	public AssignmentsComponentTable(MainWindow mainWindowToUse, EditableObjectTableModel modelToUse, String uniqueTableIdentifierToUse)
	{
		super(mainWindowToUse, modelToUse, uniqueTableIdentifierToUse);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}
	
	@Override
	public boolean allowUserToSetRowHeight()
	{
		return false;
	}
	
	
	public Dimension getPreferredScrollableViewportSize()
	{
		return getPreferredSize();
	}
	
	@Override
	public boolean shouldSaveColumnSequence()
	{
		return false;
	}
	
	@Override
	public boolean shouldSaveColumnWidth()
	{
		return false;
	}

	public Color getColumnBackGroundColor(int tableColumn)
	{
		return getBackground();
	}
}
