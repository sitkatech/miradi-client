/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.main.EAM;

public class NonEditableRowHeaderTableModel extends AbstractTableModel
{
	public NonEditableRowHeaderTableModel(NonEditableThreatMatrixTableModel modelToUse) {
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
			return EAM.text("Summary Threat Rating");
		return model.getThreatNode(row).toString();
	}

	public boolean isCellEditable(int row, int column)
	{
		return false;
	}
	
	public String getColumnName(int columnIndex) 
	{
		return 	EAM.text("THREATS");
	}
	

	NonEditableThreatMatrixTableModel model;
}
