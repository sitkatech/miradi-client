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

import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;

public class TableRowHeightSaver implements MouseListener, MouseMotionListener
{
	public TableRowHeightSaver()
	{
	}
	
	public void manage(JTable tableToManage, String uniqueTableIdentifierToUse)
	{
		table = tableToManage;
		table.addMouseListener(this);
		table.addMouseMotionListener(this);

		uniqueTableIdentifier = uniqueTableIdentifierToUse;
		restoreRowHeight();
	}
	
	private void restoreRowHeight()
	{
		int rowHeight = getPreferences().getTaggedInt(getKey());
		if(rowHeight > 0)
		{
			table.setRowHeight(rowHeight);
			EAM.logVerbose("restoreRowHeight " + getKey() + ": " + table.getRowHeight());
		}
	}
	
	public void saveRowHeight()
	{
		EAM.logVerbose("saveRowHeight " + getKey() + ": " + table.getRowHeight());
		getPreferences().setTaggedInt(getKey(), table.getRowHeight());
	}

	private AppPreferences getPreferences()
	{
		return EAM.getMainWindow().getAppPreferences();
	}
	
	private String getKey()
	{
		return "RowHeight." + uniqueTableIdentifier;
	}
	
	public void mouseClicked(MouseEvent e)
	{
		if(resizeInProgress)
			e.consume();
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
		
		int y = event.getY();
		int row = table.rowAtPoint(point);

		int height = table.getRowHeight();
		int rowStartY = row * height;
		int withinRowY = y - rowStartY;
		int border = ROW_RESIZE_MARGIN;
		
		boolean inBorderChangeArea = (withinRowY >= height - border);
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
		table.getTopLevelAncestor().repaint();
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
	
    public final static int ROW_RESIZE_MARGIN = 2;

    private JTable table;
	private String uniqueTableIdentifier;
	
	private boolean resizeInProgress;
	private int dragStartedY;
	private int rowBeingResized;
	private int originalRowHeight;
	private int sizeDeltaY;
	private Cursor oldCursor;
}

