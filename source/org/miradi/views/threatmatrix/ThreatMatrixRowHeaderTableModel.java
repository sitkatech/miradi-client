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
package org.miradi.views.threatmatrix;

import javax.swing.table.AbstractTableModel;

import org.miradi.main.EAM;

public class ThreatMatrixRowHeaderTableModel extends AbstractTableModel
{
	public ThreatMatrixRowHeaderTableModel(ThreatMatrixTableModel modelToUse) 
	{
		model = modelToUse;
	}

	public int getColumnCount()
	{
		return 1;
	}

	public int getRowCount()
	{
		return model.getRowCount();
	}
	
	public Object getValueAt(int row, int column)
	{
		if (row==model.getRowCount()-1) 
			return EAM.text("Summary Target Rating");
		return model.getDirectThreats()[row];
	}

	public boolean isCellEditable(int row, int column)
	{
		return false;
	}
	
	public String getColumnName(int columnIndex) 
	{
		return 	EAM.text("THREATS");
	}
	

	ThreatMatrixTableModel model;
}
