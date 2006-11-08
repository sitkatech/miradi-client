/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.table.JTableHeader;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ViewData;

public abstract class ColumnHeaderListener  extends MouseAdapter
{
	public ColumnHeaderListener(ThreatGirdPanel threatGirdPanelInUse)
	{
		threatGirdPanel = threatGirdPanelInUse;
	}

	public void mouseClicked(MouseEvent e) 
	{
		clearSelectionCellBorder();
		notifyComponentsClearSelection();
		
		int clickedColumn = ((JTableHeader)e.getSource()).columnAtPoint(e.getPoint());
		int sortColumn = ((JTableHeader)e.getSource()).getColumnModel().getColumn(clickedColumn).getModelIndex();
		
		sort(sortColumn);

		threatGirdPanel.revalidate();
		threatGirdPanel.repaint();
	}

//TODO: It is not understood why the selection listner is not called every time.  So it can not be used to set selection cell border
	private void clearSelectionCellBorder()
	{
		threatGirdPanel.getThreatMatrixTable().setFocusable(false);
		threatGirdPanel.getThreatMatrixTable().setFocusable(true);
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


	void saveSortState(boolean sortDirection, ConceptualModelNode sortColumnNode)
	{
		String value = sortColumnNode.getId().toString();
		saveSortState(sortDirection, value);
	}

	
	public static void establishPriorSortState(ThreatGirdPanel threatGirdPanel)
	{
		try
		{
			String currentSortBy = threatGirdPanel.getProject().getViewData(threatGirdPanel.getProject().getCurrentView())
					.getData(ViewData.TAG_CURRENT_SORT_BY);

			boolean hastPriorSortBy = currentSortBy.length()!=0;
			if (hastPriorSortBy)
				restorePriorSortState(threatGirdPanel, currentSortBy);
		}
		catch(Exception e)
		{
			EAM.logError("Unable to retrieve sort state:" + e);
		}
	}


	private static void restorePriorSortState(ThreatGirdPanel threatGirdPanel, String currentSortBy) throws Exception
	{
		String currentSortDirection= threatGirdPanel.getProject().getViewData(threatGirdPanel.getProject().getCurrentView())
				.getData(ViewData.TAG_CURRENT_SORT_DIRECTION);

		if (currentSortBy.equals(ViewData.SORT_TARGETS))
			TargetRowHeaderListener.sort(threatGirdPanel, currentSortBy,
					currentSortDirection);
		else
			ThreatColumnHeaderListener.sort(threatGirdPanel, currentSortBy,
					currentSortDirection);
	}
	
	
	void saveSortState(boolean sortDirection, String sortColumnId)
	{
		try
		{
			String order = (sortDirection) ? ViewData.SORT_ASCENDING: ViewData.SORT_DESCENDING;

			ViewData viewData = threatGirdPanel.getProject().getCurrentViewData();

			executeCommand(new CommandBeginTransaction());
			
			CommandSetObjectData cmd = new CommandSetObjectData(viewData.getType(), viewData.getId(), 
					ViewData.TAG_CURRENT_SORT_DIRECTION, order);
			threatGirdPanel.getProject().executeCommand(cmd);

			cmd = new CommandSetObjectData(viewData.getType(),viewData.getId(), 
					ViewData.TAG_CURRENT_SORT_BY, sortColumnId);
			threatGirdPanel.getProject().executeCommand(cmd);

			executeCommand(new CommandEndTransaction());
		}
		catch(Exception e)
		{
			EAM.logError("Unable to save sort state:" + e);
		}
	}
	
	
	private void executeCommand(Command cmd) throws CommandFailedException
	{
		threatGirdPanel.getProject().executeCommand(cmd);
	}
	
	public abstract void sort(int sortColumnToUse);
	
	ThreatGirdPanel threatGirdPanel;
	NonEditableThreatMatrixTableModel model;

}
