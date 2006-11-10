/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.ThreatRatingBundle;


class CellSelectionListener implements ListSelectionListener
{
	public CellSelectionListener(ThreatMatrixTable threatTableInUse, ThreatGirdPanel threatGirdPanelInUse) {
		threatTable = threatTableInUse;
		threatGirdPanel = threatGirdPanelInUse;
	}

	public void valueChanged(ListSelectionEvent e)
	{
		if (threatTable.getSelectedRow() >= 0) 
		{
			int row = threatTable.getSelectedRow();
			int column = threatTable.convertColumnIndexToModel(threatTable.getSelectedColumn());

			if(((NonEditableThreatMatrixTableModel) threatTable.getModel())
					.isBundleTableCellABundle(row, column))
				notifyComponents(row, column);
			else
				notifyComponentsClearSelection();

			unselectToForceFutureNotifications(row, threatTable.getSelectedColumn());
		}
	}

	
	private void unselectToForceFutureNotifications(int row, int column)
	{
		threatTable.changeSelection(row, column, true,false);
	}
	
	private void notifyComponentsClearSelection()
	{
		try
		{
			threatGirdPanel.getThreatMatrixView().selectBundle(null);
		}
		catch(Exception ex)
		{
			EAM.logException(ex);
		}
	}

	private void notifyComponents(int row, int column)
	{
		try
		{
			NonEditableThreatMatrixTableModel model = (NonEditableThreatMatrixTableModel)threatTable.getModel();
			ThreatRatingBundle threatRatingBundle = model.getBundle(row, column);
			threatGirdPanel.getThreatMatrixView().selectBundle(threatRatingBundle);
		}
		// TODO: must add errDialog call....need to see how to call when on the swing event thread
		catch(Exception ex)
		{
			EAM.logException(ex);
		}
	}
	
	
	
	ThreatMatrixTable threatTable;
	ThreatGirdPanel threatGirdPanel;
}

