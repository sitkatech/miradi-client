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
import java.util.Comparator;
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
	public ColumnSortListener(ThreatGridPanel threatGirdPanelInUse)
	{
		threatGirdPanel = threatGirdPanelInUse;
		 mainTableModel = (NonEditableThreatMatrixTableModel)threatGirdPanelInUse.getThreatMatrixTable().getModel();
	}

	public void mouseClicked(MouseEvent e) 
	{
		int clickedColumn = ((JTableHeader)e.getSource()).columnAtPoint(e.getPoint());
		if (clickedColumn >= 0)
		{
			int sortColumn = ((JTableHeader)e.getSource()).getTable().convertColumnIndexToModel(clickedColumn);
			sortBySelectedColumn(sortColumn);
		}
	}


	private void sortBySelectedColumn(int sortColumn)
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
	

	public  void sort(String currentSortBy, String currentSortDirection) 
	{
		ConceptualModelNode[] threatList = mainTableModel.getDirectThreats();
		
		Comparator comparator = getComparator(currentSortBy);
		
		Arrays.sort(threatList, comparator);
		
		if (currentSortDirection.equals(ViewData.SORT_ASCENDING)) 
			threatList = reverseSort(threatList);
		
		mainTableModel.setThreatRows(threatList);
	}

	
	public void sort(int sortColumn) 
	{
		ConceptualModelNode[] threatList = mainTableModel.getDirectThreats();

		Comparator comparator = getComparator(sortColumn);

		Arrays.sort(threatList, comparator);
		
		if ( toggle() )  
			threatList = reverseSort(threatList);

		mainTableModel.setThreatRows(threatList);
		
		saveState(sortColumn);
	}
	
	
	public abstract Comparator getComparator(String currentSortBy);
	public abstract Comparator getComparator(int sortColumn);
	public abstract void saveState(int sortColumn);
	public abstract boolean toggle();
	
	private ThreatGridPanel threatGirdPanel;
	protected NonEditableThreatMatrixTableModel mainTableModel;

}
