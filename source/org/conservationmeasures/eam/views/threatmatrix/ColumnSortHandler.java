/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
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
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.ViewData;

public abstract class ColumnSortHandler  extends MouseAdapter implements MouseMotionListener
{
	public ColumnSortHandler(ThreatGridPanel threatGirdPanelInUse)
	{
		threatGridPanel = threatGirdPanelInUse;
	}

	public void mouseClicked(MouseEvent e) 
	{
		try
		{
			int clickedColumn = ((JTableHeader)e.getSource()).columnAtPoint(e.getPoint());
			if (clickedColumn >= 0)
			{
				int sortColumn = ((JTableHeader)e.getSource()).getTable().convertColumnIndexToModel(clickedColumn);
				sortBySelectedColumn(sortColumn);
			}
		}
		catch(Exception e1)
		{
			EAM.logException(e1);
		}
	}


	public void mouseDragged(MouseEvent e)
	{
	}

	public void mouseMoved(MouseEvent e)
	{
		JTableHeader header = ((JTableHeader)e.getSource());
		header.setReorderingAllowed(!isMouseInSummaryColumn(header, e.getPoint()));
	}

	private boolean isMouseInSummaryColumn(JTableHeader header, Point p)
	{
		return (header.columnAtPoint(p) == getSummaryColumn());
	}
	
	int getSummaryColumn()
	{
		return threatGridPanel.getThreatMatrixTable().getSummaryColumn();
	}

	// TODO: We should not have to DO the sort here...we should be 
	// able to just save the new sort state, and then the view's 
	// command listener should repaint...but it didn't seem to work 
	// when I tried it. kbs. 2007-05-21
	private void sortBySelectedColumn(int sortColumn) throws Exception
	{
		sort(sortColumn);
		saveState(sortColumn);

		threatGridPanel.revalidate();
		threatGridPanel.repaint();
	}

	
	private Factor[] reverseSort(Factor[] threatList)
	{
		Vector list = new Vector(Arrays.asList(threatList));
		Collections.reverse(list);
		threatList = (Factor[]) list.toArray(new Factor[0]);
		return threatList;
	}
	

	
	void saveSortState(boolean sortDirection, String sortColumnId) throws Exception
	{
		String order = (sortDirection) ? ViewData.SORT_ASCENDING: ViewData.SORT_DESCENDING;
		ViewData viewData = threatGridPanel.getProject().getCurrentViewData();

		executeCommand(new CommandBeginTransaction());
		try
		{
			saveSortDirection(order, viewData);
			saveSortByColumn(sortColumnId, viewData);
		}
		finally
		{
			executeCommand(new CommandEndTransaction());
		}
	}


	private void saveSortByColumn(String sortColumnId, ViewData viewData) throws CommandFailedException
	{
		CommandSetObjectData cmd = new CommandSetObjectData(viewData.getType(),viewData.getId(), 
				ViewData.TAG_CURRENT_SORT_BY, sortColumnId);
		threatGridPanel.getProject().executeCommand(cmd);
	}


	private void saveSortDirection(String order, ViewData viewData) throws CommandFailedException
	{
		CommandSetObjectData cmd = new CommandSetObjectData(viewData.getType(), viewData.getId(), 
				ViewData.TAG_CURRENT_SORT_DIRECTION, order);
		threatGridPanel.getProject().executeCommand(cmd);
	}
	
	
	private void executeCommand(Command cmd) throws CommandFailedException
	{
		threatGridPanel.getProject().executeCommand(cmd);
	}
	

	public void sort(int sortColumn) 
	{
		ThreatMatrixTableModel mainTableModel = (ThreatMatrixTableModel)threatGridPanel.getThreatMatrixTable().getModel();
		
		Comparator comparator = getComparator(sortColumn);

		Factor[] threatList = mainTableModel.getDirectThreats();

		Arrays.sort(threatList, comparator);
		
		if ( getToggle() )  
			threatList = reverseSort(threatList);

		mainTableModel.setThreatRows(threatList);
	}
	
	
	public abstract Comparator getComparator(int sortColumn);
	public abstract void saveState(int sortColumn) throws Exception;
	public abstract boolean getToggle();
	
	protected ThreatGridPanel threatGridPanel;

}
