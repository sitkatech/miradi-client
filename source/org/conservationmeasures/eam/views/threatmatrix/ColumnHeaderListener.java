/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
	public ColumnHeaderListener(MyThreatGirdPanel threatGirdPanelInUse)
	{
		threatGirdPanel = threatGirdPanelInUse;
	}

	public void mouseClicked(MouseEvent e) 
	{
		clearSelectionCellBorder();
		notifyComponentsClearSelection();
		
		int clickedColumn = threatGirdPanel.globalTthreatTable.columnAtPoint(e.getPoint());
		int sortColumn = threatGirdPanel.globalTthreatTable.getColumnModel().getColumn(clickedColumn).getModelIndex();
		
		sort(sortColumn);

		threatGirdPanel.revalidate();
		threatGirdPanel.repaint();
	}

//TODO: It is not understood why the selection listner is not called every time.  So it can not be used to set selection cell border
	private void clearSelectionCellBorder()
	{
		threatGirdPanel.globalTthreatTable.setFocusable(false);
		threatGirdPanel.globalTthreatTable.setFocusable(true);
	}

	private void notifyComponentsClearSelection()
	{
		try
		{
			threatGirdPanel.view.selectBundle(null);
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

	
	public static void establishPriorSortState(MyThreatGirdPanel threatGirdPanel)
	{
		try
		{
			String currentSortBy = threatGirdPanel.project.getViewData(threatGirdPanel.project.getCurrentView())
					.getData(ViewData.TAG_CURRENT_SORT_BY);

			String currentSortDirection= threatGirdPanel.project.getViewData(threatGirdPanel.project.getCurrentView())
					.getData(ViewData.TAG_CURRENT_SORT_DIRECTION);

			if (currentSortBy.equals(ViewData.SORT_TARGETS))
				TargetRowHeaderListener.sort(threatGirdPanel, currentSortBy,
						currentSortDirection);
			else
				ThreatColumnHeaderListener.sort(threatGirdPanel, currentSortBy,
						currentSortDirection);
		}
		catch(Exception e)
		{
			EAM.logError("Unable to retrieve sort state:" + e);
		}
	}
	
	
	void saveSortState(boolean sortDirection, String sortColumnId)
	{
		try
		{
			String order = (sortDirection) ? ViewData.SORT_ASCENDING: ViewData.SORT_DESCENDING;

			ViewData viewData = threatGirdPanel.project.getCurrentViewData();

			executeCommand(new CommandBeginTransaction());
			
			CommandSetObjectData cmd = new CommandSetObjectData(viewData.getType(), viewData.getId(), 
					ViewData.TAG_CURRENT_SORT_DIRECTION, order);
			threatGirdPanel.project.executeCommand(cmd);

			cmd = new CommandSetObjectData(viewData.getType(),viewData.getId(), 
					ViewData.TAG_CURRENT_SORT_BY, sortColumnId);
			threatGirdPanel.project.executeCommand(cmd);

			executeCommand(new CommandEndTransaction());
		}
		catch(Exception e)
		{
			EAM.logError("Unable to save sort state:" + e);
		}
	}
	
	
	private void executeCommand(Command cmd) throws CommandFailedException
	{
		threatGirdPanel.project.executeCommand(cmd);
	}
	
	public abstract void sort(int sortColumnToUse);
	
	MyThreatGirdPanel threatGirdPanel;
	NonEditableThreatMatrixTableModel model;

}
