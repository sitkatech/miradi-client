/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.dialogs.planning;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.table.JTableHeader;

import org.miradi.icons.IconManager;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;

public class TableHeaderWithExpandCollapseIcons extends JTableHeader
{
	public TableHeaderWithExpandCollapseIcons(TableWithExpandableColumnsInterface tableToUse)
	{
		super(tableToUse.getColumnModel());
		tableWithExpandableColumnsInterface = tableToUse;
		addMouseListener(new HeaderMouseHandler());
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		Dimension size = super.getPreferredSize();
		if(preferredHeight == 0)
			return size;
		
		return new Dimension(size.width, preferredHeight);
	}
	
	@Override
	public void paint(Graphics g)
	{
		if(preferredHeight == 0)
			preferredHeight = getPreferredSize().height;
		
		for(int column = 0; column < getColumnModel().getColumnCount(); ++column)
			getColumnModel().getColumn(column).setHeaderValue("");

		super.paint(g);
		g.setColor(AppPreferences.getControlPanelBackgroundColor());
		Vector<Rectangle> iconHeaderBoundsVector = getColumnHeaderBounds();
		for(int index = 0; index < iconHeaderBoundsVector.size(); ++index)
		{
			Rectangle columnHeaderBounds = iconHeaderBoundsVector.get(index);
			drawColumnHeader(g, columnHeaderBounds, getIcon(index), table.getColumnName(index));
		}
	}

	private void drawColumnHeader(Graphics g, Rectangle columnHeaderBounds, Icon icon, String text)
	{
		int textX = columnHeaderBounds.x + ARBITRARY_MARGIN;
		int textY = columnHeaderBounds.y + columnHeaderBounds.height - ARBITRARY_MARGIN;

		Shape oldClip = g.getClip();
		try
		{
			g.clipRect(columnHeaderBounds.x, columnHeaderBounds.y, columnHeaderBounds.width, columnHeaderBounds.height);
			if (icon != null)
			{
				icon.paintIcon(this, g, columnHeaderBounds.x, ARBITRARY_MARGIN / 2);
				textX += icon.getIconWidth();
			}
	
			g.setColor(getForeground());
			g.setFont(getFont());
			g.drawString(text, textX, textY);
		}
		finally
		{
			g.setClip(oldClip);
		}
	}

	private Vector<Rectangle> getColumnHeaderBounds()
	{
		Vector<Rectangle> columnHeaderBoundsVector = new Vector();
		if(tableWithExpandableColumnsInterface == null)
			return columnHeaderBoundsVector;
		
		int columnX = 0;
		for(int column = 0; column < tableWithExpandableColumnsInterface.getColumnCount(); ++column)
		{	
			final int columnWidth = tableWithExpandableColumnsInterface.getColumnWidth(column);
			
			Rectangle columnHeaderBounds = new Rectangle();
			columnHeaderBounds.x = columnX;
			columnHeaderBounds.y = 0;
			columnHeaderBounds.width = columnWidth;
			columnHeaderBounds.height = getHeight();
			
			columnHeaderBoundsVector.add(columnHeaderBounds);
			columnX += columnWidth;
		}
		
		return columnHeaderBoundsVector;
	}
	
	private Icon getIcon(int tableColumn)
	{
		if(tableWithExpandableColumnsInterface.isColumnExpandable(tableColumn))
			return getExpandIcon();
		if(tableWithExpandableColumnsInterface.isColumnCollapsable(tableColumn))
			return getCollapseIcon();
		return null;
	}

	private Icon getExpandIcon()
	{
		return IconManager.getExpandIcon();
	}

	private Icon getCollapseIcon()
	{
		return IconManager.getCollapseIcon();
	}

	class HeaderMouseHandler extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			super.mouseClicked(e);
			
			Point point = e.getPoint();
			int tableColumnIndex = getColumnModel().getColumnIndexAtX(point.x);
			Icon icon = getIcon(tableColumnIndex);
			if(icon == null)
				return;

			Rectangle columnHeaderRectangle = getHeaderRect(tableColumnIndex);
			Rectangle iconRectangle = createIconRectangle(columnHeaderRectangle, icon);
			if(!iconRectangle.contains(point))
				return;

			handleClick(tableColumnIndex);
		}
		
		private Rectangle createIconRectangle(Rectangle columnHeaderRectangle, Icon icon)
		{
			Dimension iconSize = new Dimension(icon.getIconWidth(), icon.getIconHeight());
			return new Rectangle(columnHeaderRectangle.getLocation(), iconSize);
		}

		private void handleClick(int tableColumnIndex)
		{
			try
			{
				tableWithExpandableColumnsInterface.respondToExpandOrCollapseColumnEvent(tableColumnIndex);
			}
			catch(Exception e)
			{
				EAM.logException(e);
				EAM.unexpectedErrorDialog(e);
			}
		}
	}
	
	protected static final int ARBITRARY_MARGIN = 2;

	private TableWithExpandableColumnsInterface tableWithExpandableColumnsInterface;
	private int preferredHeight;
}
