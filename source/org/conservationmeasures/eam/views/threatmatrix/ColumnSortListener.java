/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import javax.swing.table.JTableHeader;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ViewData;

public abstract class ColumnSortListener  extends MouseAdapter
{
	public ColumnSortListener(ThreatGirdPanel threatGirdPanelInUse)
	{
		threatGirdPanel = threatGirdPanelInUse;
	}

	public void mouseClicked(MouseEvent e) 
	{
		int clickedColumn = ((JTableHeader)e.getSource()).columnAtPoint(e.getPoint());
		if (clickedColumn >= 0)
		{
			int sortColumn = ((JTableHeader)e.getSource()).getColumnModel().getColumn(clickedColumn).getModelIndex();
			sortBySelectedColumn(e, sortColumn);
		}
	}


	private void sortBySelectedColumn(MouseEvent e, int sortColumn)
	{
		sort(sortColumn);
		threatGirdPanel.revalidate();
		threatGirdPanel.repaint();
	}

	
	public ConceptualModelNode[] reverseSort(ConceptualModelNode[] threatList)
	{
		Vector list = new Vector(Arrays.asList(threatList));
		Collections.reverse(list);
		threatList = (ConceptualModelNode[]) list.toArray(new ConceptualModelNode[0]);
		return threatList;
	}
	
	
	void saveSortState(boolean sortDirection, ConceptualModelNode sortColumnNode)
	{
		String value = sortColumnNode.getId().toString();
		saveSortState(sortDirection, value);
	}

	
	void saveSortState(boolean sortDirection, String sortColumnId)
	{
		try
		{
			String order = (sortDirection) ? ViewData.SORT_ASCENDING: ViewData.SORT_DESCENDING;

			ViewData viewData = threatGirdPanel.getProject().getCurrentViewData();

			executeCommand(new CommandBeginTransaction());
			
			saveSortDirection(order, viewData);

			saveSortByColumn(sortColumnId, viewData);

			executeCommand(new CommandEndTransaction());
		}
		catch(Exception e)
		{
			EAM.logError("Unable to save sort state:" + e);
		}
	}


	private void saveSortByColumn(String sortColumnId, ViewData viewData) throws CommandFailedException
	{
		CommandSetObjectData cmd = new CommandSetObjectData(viewData.getType(),viewData.getId(), 
				ViewData.TAG_CURRENT_SORT_BY, sortColumnId);
		threatGirdPanel.getProject().executeCommand(cmd);
	}


	private void saveSortDirection(String order, ViewData viewData) throws CommandFailedException
	{
		CommandSetObjectData cmd = new CommandSetObjectData(viewData.getType(), viewData.getId(), 
				ViewData.TAG_CURRENT_SORT_DIRECTION, order);
		threatGirdPanel.getProject().executeCommand(cmd);
	}
	
	
	private void executeCommand(Command cmd) throws CommandFailedException
	{
		threatGirdPanel.getProject().executeCommand(cmd);
	}
	

	public abstract void sort(int sortColumnToUse);
	public abstract void sort(String currentSortBy, String currentSortDirection);
	
	protected ThreatGirdPanel threatGirdPanel;
	protected NonEditableThreatMatrixTableModel model;

}
