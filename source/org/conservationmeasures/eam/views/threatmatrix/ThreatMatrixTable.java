/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import javax.swing.JTable;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.TableModel;

public class ThreatMatrixTable extends JTable
{
	public ThreatMatrixTable(TableModel model)
	{
		super(model);
	}

	public void columnMoved(TableColumnModelEvent event)
	{
		if(event.getToIndex() == getSummaryColumn())
			moveColumn(event.getToIndex(), event.getFromIndex());
		else
			super.columnMoved(event);
	}
	
	public int getSummaryColumn()
	{
		return getColumnCount() - 1;
	}

}
