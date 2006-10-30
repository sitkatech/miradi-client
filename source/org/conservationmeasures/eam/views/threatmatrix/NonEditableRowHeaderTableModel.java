/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import javax.swing.table.DefaultTableModel;

public class NonEditableRowHeaderTableModel extends DefaultTableModel
{
	public NonEditableRowHeaderTableModel(int row, int column) {
		super(row,column);
	}

	public boolean isCellEditable(int row, int column)
	{
		return false;
	}
}
