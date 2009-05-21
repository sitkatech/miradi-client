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

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.IconManager;
import org.miradi.main.EAM;

public class ExpandAndCollapseColumnsButtonRow extends JComponent implements AdjustmentListener
{
	public ExpandAndCollapseColumnsButtonRow(AssignmentDateUnitsTable tableToSitAbove)
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
		int columnX = getInitialColumnX();
		for(int column = 0; column < table.getColumnCount(); ++column)
		{
			Icon icon = getIcon(column);
			if (icon != null)
				icon.paintIcon(this, g, columnX, ARBITRARY_MARGIN / 2);
			
			columnX += table.getColumnModel().getColumn(column).getWidth();
		}
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
				int columnClicked = findModelColumn(event.getPoint());
				if (columnClicked >= 0)
					table.getWorkUnitsTableModel().respondToExpandOrCollapseColumnEvent(columnClicked);
			}
			catch (Exception e)
			{
				EAM.logException(e);
				EAM.errorDialog(EAM.text("An error occurred while trying to expand/collapse the column."));
			}
		}

		private int findModelColumn(Point point)
		{
			int columnX = getInitialColumnX();
			
			Rectangle iconBounds = new Rectangle();
			iconBounds.y = ARBITRARY_MARGIN / 2;
			iconBounds.x = columnX;
			iconBounds.height = getIconHeight();
			iconBounds.width = getIconWidth();
			
			for(int column = 0; column < table.getColumnCount(); ++column)
			{	
				if (iconBounds.contains(point))
					return table.convertColumnIndexToModel(column);
				
				columnX += table.getColumnModel().getColumn(column).getWidth();
				iconBounds.x = columnX;
			}
			
			return -1;
		}
	}

	private final static int ARBITRARY_MARGIN = 2;

	private AssignmentDateUnitsTable table;
	private JScrollPane tableScrollPane;
}
