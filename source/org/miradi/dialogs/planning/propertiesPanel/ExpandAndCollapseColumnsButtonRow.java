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

package org.miradi.dialogs.planning.propertiesPanel;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.dialogs.planning.TableWithExpandableColumnsInterface;
import org.miradi.icons.IconManager;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;

public class ExpandAndCollapseColumnsButtonRow extends JComponent implements AdjustmentListener
{
	public ExpandAndCollapseColumnsButtonRow(TableWithExpandableColumnsInterface tableToSitAbove)
	{
		table = tableToSitAbove;
		add(new PanelTitleLabel(IconManager.getActivityIcon()));
		addMouseListener(new MouseClickHandler());
	}
	
	public void setTableScrollPane(JScrollPane tableScrollPaneToUse)
	{
		tableScrollPane = tableScrollPaneToUse;
		tableScrollPane.getHorizontalScrollBar().addAdjustmentListener(this);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
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
		
		int columnX = getInitialColumnX();
		for(int column = 0; column < table.getColumnCount(); ++column)
		{	
			final int columnWidth = table.getColumnWidth(column);
			
			Rectangle iconHeaderBound = new Rectangle();
			iconHeaderBound.x = columnX;
			iconHeaderBound.y = 0;
			iconHeaderBound.width = columnWidth;
			iconHeaderBound.height = getIconHeight() + ARBITRARY_MARGIN;
			
			iconHeaderBounds.add(iconHeaderBound);
			columnX += columnWidth;
		}
		
		return iconHeaderBounds;
	}
	
	private int getInitialColumnX()
	{
		int columnX = 1;
		if(tableScrollPane != null)
			columnX -= tableScrollPane.getViewport().getViewPosition().x;
		
		return columnX;
	}
	
	private Icon getIcon(int tableColumn)
	{
		if(table.isColumnExpandable(tableColumn))
			return getExpandIcon();
		if(table.isColumnCollapsable(tableColumn))
			return getCollapseIcon();
		return null;
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(0, getIconHeight() + ARBITRARY_MARGIN);
	}

	private int getIconHeight()
	{
		return Math.max(getExpandIcon().getIconHeight(), getCollapseIcon().getIconHeight());
	}
	
	private int getIconWidth()
	{
		return Math.max(getExpandIcon().getIconWidth(), getCollapseIcon().getIconWidth());
	}
	
	public void adjustmentValueChanged(AdjustmentEvent e)
	{
		validate();
		repaint();
	}

	private Icon getExpandIcon()
	{
		return IconManager.getExpandIcon();
	}

	private Icon getCollapseIcon()
	{
		return IconManager.getCollapseIcon();
	}
	
	class MouseClickHandler extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent event)
		{
			super.mouseClicked(event);
			
			try
			{
				int columnClicked = findTableColumn(event.getPoint());
				if (columnClicked >= 0)
					table.respondToExpandOrCollapseColumnEvent(columnClicked);
			}
			catch (Exception e)
			{
				EAM.logException(e);
				EAM.errorDialog(EAM.text("An error occurred while trying to expand/collapse the column."));
			}
		}

		private int findTableColumn(Point point)
		{
			Vector<Rectangle> iconHeaderBounds = getColumnIconHeaderBounds();
			for(int index = 0; index < iconHeaderBounds.size(); ++index)
			{
				Rectangle columnHeaderBounds = new Rectangle(iconHeaderBounds.get(index));
				columnHeaderBounds.width = getIconWidth();
				if (columnHeaderBounds.contains(point))
					return index;
			}
			
			return -1;
		}
	}

	private final static int ARBITRARY_MARGIN = 2;

	private TableWithExpandableColumnsInterface table;
	private JScrollPane tableScrollPane;
}
