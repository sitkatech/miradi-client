/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableModel;

public class ThreatMatrixRowHeaderTable extends JTable
{
	final JTable table = this;
	
	public ThreatMatrixRowHeaderTable(TableModel rowHeaderData)
	{
		super(rowHeaderData);
	}
	
	public void columnMarginChanged(ChangeEvent e)    
	{
        super.columnMarginChanged(e);
		resetWidthToAllowResizeOfRowHeader();
    }

	private void resetWidthToAllowResizeOfRowHeader()
	{
		Dimension dimension = table.getPreferredScrollableViewportSize();
		dimension.width = table.getPreferredSize().width;
		table.setPreferredScrollableViewportSize(dimension);
	}
}
