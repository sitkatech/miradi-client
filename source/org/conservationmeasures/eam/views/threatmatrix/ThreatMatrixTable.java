/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class ThreatMatrixTable extends JTable
{
	public ThreatMatrixTable(TableModel model)
	{
		super(model);
	}

	public int getSelectedModelColumn() {
		int tableColumn = super.getSelectedColumn();
		int newColumn = getColumnModel().getColumn(tableColumn).getModelIndex();
		return newColumn;
	}

}
