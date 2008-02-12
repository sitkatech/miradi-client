/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.threatmatrix;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.main.EAM;
import org.miradi.project.ThreatRatingBundle;


class CellSelectionListener implements ListSelectionListener
{
	public CellSelectionListener(ThreatMatrixTable threatTableInUse, ThreatGridPanel threatGirdPanelInUse) {
		threatTable = threatTableInUse;
		threatGirdPanel = threatGirdPanelInUse;
	}

	public void valueChanged(ListSelectionEvent e)
	{
		int row = threatTable.getSelectedRow();
		if (row < 0)
			return;
		
		int column = threatTable.convertColumnIndexToModel(threatTable.getSelectedColumn());
		if(column < 0)
			return;

		try
		{
			ThreatRatingBundle bundle = getBundleIfAny(row, column);
			threatGirdPanel.getThreatMatrixView().selectBundle(bundle);
		}
		catch(Exception ex)
		{
			// TODO: must add errDialog call....need to see how to call when on the swing event thread
			EAM.logException(ex);
		}

		unselectToForceFutureNotifications(row, threatTable.getSelectedColumn());
	}

	private ThreatRatingBundle getBundleIfAny(int row, int column) throws Exception
	{
		ThreatMatrixTableModel model = (ThreatMatrixTableModel) threatTable.getModel();
		if(model.isSumaryColumn(column) || model.isSumaryRow(row))
			return null;
				
		return model.getBundle(row, column);
	}

	private void unselectToForceFutureNotifications(int row, int column)
	{
		threatTable.changeSelection(row, column, true,false);
	}
	
	ThreatMatrixTable threatTable;
	ThreatGridPanel threatGirdPanel;
}

