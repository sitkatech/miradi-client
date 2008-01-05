/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JTable;

class TableRowHeightSaver implements MouseListener, MouseMotionListener
{
	public TableRowHeightSaver()
	{
	}
	
	public void manage(JTable tableToManage)
	{
		table = tableToManage;
		table.addMouseListener(this);
		table.addMouseMotionListener(this);
	}
	
	public void mouseClicked(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
	}

	public void mousePressed(MouseEvent e)
	{
		if(!inRowResizeArea(e))
			return;
		
		beginResizing(e);
		e.consume();
	}

	public void mouseReleased(MouseEvent e)
	{
		endResizing();
	}

	public void mouseDragged(MouseEvent e)
	{
		if(!resizeInProgress)
			return;
		
		sizeDeltaY = e.getY() - dragStartedY;
		table.setRowHeight(rowBeingResized, getNewRowHeight());
		e.consume();
	}

	public void mouseMoved(MouseEvent event)
	{
		if(resizeInProgress)
			return;
		
		if(inRowResizeArea(event))
			setResizeCursor();
		else
			restoreDefaultCursor();
	}

	private boolean inRowResizeArea(MouseEvent event)
	{
		Point point = event.getPoint();
		if(table.columnAtPoint(point) != 0)
			return false;
		
		int y = event.getY();
		int row = table.rowAtPoint(point);

		int height = table.getRowHeight();
		int rowStartY = row * height;
		int withinRowY = y - rowStartY;
		int percentWithinRow = withinRowY * 100 / height;

		boolean inBorderChangeArea = (percentWithinRow > 70);
		return inBorderChangeArea;
	}
	
	void beginResizing(MouseEvent event)
	{
		dragStartedY = event.getY();
		originalRowHeight = table.getRowHeight();
		rowBeingResized = table.rowAtPoint(event.getPoint());
		resizeInProgress = true;
		setResizeCursor();
	}

	void endResizing()
	{
		if(!resizeInProgress)
			return;
		
		resizeInProgress = false;
		restoreDefaultCursor();
		int newHeight = getNewRowHeight();
		table.setRowHeight(newHeight);
		Point point = new Point(0, rowBeingResized * newHeight);
		Rectangle resized = new Rectangle(point, new Dimension(1, newHeight));
		table.scrollRectToVisible(resized);
		table.getSelectionModel().setSelectionInterval(rowBeingResized, rowBeingResized);
	}

	private int getNewRowHeight()
	{
		int newRowHeight = originalRowHeight + sizeDeltaY;
		newRowHeight = Math.max(newRowHeight, 10);
		return newRowHeight;
	}

	private void setResizeCursor()
	{
		if(oldCursor != null)
			return;
		
		oldCursor = table.getCursor();
		table.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
	}
	
	private void restoreDefaultCursor()
	{
		table.setCursor(oldCursor);
		oldCursor = null;
	}
	
	private JTable table;
	private boolean resizeInProgress;
	private int dragStartedY;
	private int rowBeingResized;
	private int originalRowHeight;
	private int sizeDeltaY;
	private Cursor oldCursor;
}

