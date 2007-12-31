/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.main.EAM;

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
