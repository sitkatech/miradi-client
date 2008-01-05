/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

abstract public class UiTableWithAlternatingRows extends TableWithColumnWidthSaver
{
	public UiTableWithAlternatingRows()
	{
		super();
		initialize();
	}

	public UiTableWithAlternatingRows(TableModel model)
	{
		super(model);
		initialize();
	}
	
	public UiTableWithAlternatingRows(Object[][] data, String[] columnNames)
	{
		this(new DefaultTableModel(data, columnNames));
	}

	private void initialize()
	{
		backgrounds = new Color[] { Color.WHITE, new Color(0xf0, 0xf0, 0xf0), };
		setDefaultRenderer(Object.class, new Renderer());
		
		MouseHandler mouseHandler = new MouseHandler();
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);

	}
	
	class Renderer extends DefaultTableCellRenderer
	{

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (!isSelected)
			{
				component.setBackground(backgrounds[row%2]);
				component.setForeground(Color.BLACK);
			}
			
			if(component instanceof JLabel)
				((JLabel)component).setVerticalAlignment(JLabel.TOP);
				
			int height = component.getPreferredSize().height;
			if (height > table.getRowHeight(row))
				table.setRowHeight (row, height);
			
			return component;
		}
		
	}

	class MouseHandler implements MouseListener, MouseMotionListener
	{
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
			setRowHeight(rowBeingResized, getNewRowHeight());
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
			if(columnAtPoint(point) != 0)
				return false;
			
			int y = event.getY();
			int row = rowAtPoint(point);

			int rowStartY = row * getRowHeight();
			int withinRowY = y - rowStartY;
			int percentWithinRow = withinRowY * 100 / getRowHeight();

			boolean inBorderChangeArea = (percentWithinRow > 70);
			return inBorderChangeArea;
		}
		
		void beginResizing(MouseEvent event)
		{
			dragStartedY = event.getY();
			originalRowHeight = getRowHeight();
			rowBeingResized = rowAtPoint(event.getPoint());
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
			setRowHeight(newHeight);
			Point point = new Point(0, rowBeingResized * newHeight);
			Rectangle resized = new Rectangle(point, new Dimension(1, newHeight));
			scrollRectToVisible(resized);
			getSelectionModel().setSelectionInterval(rowBeingResized, rowBeingResized);
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
			
			oldCursor = getCursor();
			setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
		}
		
		private void restoreDefaultCursor()
		{
			setCursor(oldCursor);
			oldCursor = null;
		}
		
		private boolean resizeInProgress;
		private int dragStartedY;
		private int rowBeingResized;
		private int originalRowHeight;
		private int sizeDeltaY;
		private Cursor oldCursor;
	}
	
	Color[] backgrounds;
}
