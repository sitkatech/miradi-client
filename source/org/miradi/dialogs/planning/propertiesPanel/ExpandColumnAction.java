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

import javax.swing.JTable;

import org.miradi.dialogs.planning.AbstractExpandColumnAction;

public class ExpandColumnAction extends AbstractExpandColumnAction
{
	public ExpandColumnAction(JTable tableToUse, AssignmentDateUnitsTableModel workUnitsTableModelToUse)
	{
		table = tableToUse;
		workUnitsTableModel = workUnitsTableModelToUse;
	}
	
	@Override
	protected int getSelectedColumn()
	{
		int tableColumn = table.getSelectedColumn();
		return table.convertColumnIndexToModel(tableColumn);
	}
	
	@Override
	protected AssignmentDateUnitsTableModel getModelForSelectedColumn()
	{
		return workUnitsTableModel;
	}
	
	private JTable table;
	private AssignmentDateUnitsTableModel workUnitsTableModel;
}