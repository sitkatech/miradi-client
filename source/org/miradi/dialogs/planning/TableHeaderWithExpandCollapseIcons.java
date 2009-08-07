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

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
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
		table = tableToUse;
		addMouseListener(new HeaderMouseHandler());
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		g.setColor(AppPreferences.getControlPanelBackgroundColor());
		Vector<Rectangle> iconHeaderBounds = getColumnIconHeaderBounds();
		for(int index = 0; index < iconHeaderBounds.size(); ++index)
		{
			Icon icon = getIcon(index);
			if (icon != null)
				icon.paintIcon(this, g, iconHeaderBounds.get(index).x, ARBITRARY_MARGIN / 2);
		}
	}

	private Vector<Rectangle> getColumnIconHeaderBounds()
	{
		Vector<Rectangle> iconHeaderBounds = new Vector();
		if(table == null)
			return iconHeaderBounds;
		
		int columnX = 0;
		for(int column = 0; column < table.getColumnCount(); ++column)
		{	
			final int columnWidth = table.getColumnWidth(column);
			
			Rectangle iconHeaderBound = new Rectangle();
			iconHeaderBound.x = columnX;
			iconHeaderBound.y = 0;
			iconHeaderBound.width = getIconWidth();
			iconHeaderBound.height = getIconHeight() + ARBITRARY_MARGIN;
			
			iconHeaderBounds.add(iconHeaderBound);
			columnX += columnWidth;
		}
		
		return iconHeaderBounds;
	}
	
	private Icon getIcon(int tableColumn)
	{
		if(table.isColumnExpandable(tableColumn))
			return getExpandIcon();
		if(table.isColumnCollapsable(tableColumn))
			return getCollapseIcon();
		return null;
	}

	private int getIconWidth()
	{
		return Math.max(getExpandIcon().getIconWidth(), getCollapseIcon().getIconWidth());
	}
	
	private Icon getExpandIcon()
	{
		return IconManager.getExpandIcon();
	}

	private Icon getCollapseIcon()
	{
		return IconManager.getCollapseIcon();
	}

	private int getIconHeight()
	{
		return Math.max(getExpandIcon().getIconHeight(), getCollapseIcon().getIconHeight());
	}

	class HeaderMouseHandler extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			super.mouseClicked(e);
			
			Point point = e.getPoint();
			Vector<Rectangle> iconHeaderBounds = getColumnIconHeaderBounds();
			for(int index = 0; index < iconHeaderBounds.size(); ++index)
			{
				Rectangle iconRectangle = iconHeaderBounds.get(index);
				if(iconRectangle.contains(point))
				{
					handleClick(index);
					return;
				}
			}
		}
		
		private void handleClick(int tableColumnIndex)
		{
			Icon icon = getIcon(tableColumnIndex);
			if (icon == null)
				return;
			
			try
			{
				table.respondToExpandOrCollapseColumnEvent(tableColumnIndex);
			}
			catch(Exception e)
			{
				EAM.logException(e);
				EAM.errorDialog(EAM.text("An unexpected error has occurred"));
			}
		}
	}
	
	protected static final int ARBITRARY_MARGIN = 2;

	private TableWithExpandableColumnsInterface table;
}