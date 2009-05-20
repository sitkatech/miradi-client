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
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.IconManager;

public class ExpandAndCollapseColumnsButtonRow extends JComponent implements AdjustmentListener
{
	public ExpandAndCollapseColumnsButtonRow(AssignmentDateUnitsTable tableToSitAbove)
	{
		table = tableToSitAbove;
		add(new PanelTitleLabel(IconManager.getActivityIcon()));
	}
	
	public void setTableScrollPane(JScrollPane tableScrollPaneToUse)
	{
		tableScrollPane = tableScrollPaneToUse;
		tableScrollPane.getHorizontalScrollBar().addAdjustmentListener(this);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		int columnX = 1;
		if(tableScrollPane != null)
			columnX -= tableScrollPane.getViewport().getViewPosition().x;
		for(int column = 0; column < table.getColumnCount(); ++column)
		{
			Icon icon = getIcon(column);
			if (icon != null)
				icon.paintIcon(this, g, columnX, ARBITRARY_MARGIN / 2);
			columnX += table.getColumnModel().getColumn(column).getWidth();
		}
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
		int height = Math.max(getExpandIcon().getIconHeight(), getCollapseIcon().getIconHeight());
		return new Dimension(0, height + ARBITRARY_MARGIN);
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

	private final static int ARBITRARY_MARGIN = 2;

	private AssignmentDateUnitsTable table;
	private JScrollPane tableScrollPane;
}
