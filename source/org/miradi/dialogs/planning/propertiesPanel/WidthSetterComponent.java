/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

public class WidthSetterComponent extends JComponent
{
	public WidthSetterComponent(JComponent componentToControl, int defaultWidth)
	{
		controlled = componentToControl;
		controlledWidth = defaultWidth;
		
		setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));

		MouseEventHandler mouseHandler = new MouseEventHandler();
		addMouseListener(mouseHandler);
		addMouseMotionListener(mouseHandler);
	}
	
	public int getControlledWidth()
	{
		return controlledWidth;
	}

	@Override
	public Dimension getSize()
	{
		Dimension size = new Dimension(controlled.getSize());
		size.width = DEFAULT_SETTER_WIDTH;
		return size;
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		Dimension size = new Dimension(controlled.getPreferredSize());
		size.width = DEFAULT_SETTER_WIDTH;
		return size;
	}
	
	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
	
		g2.setColor(getForeground());
		g2.drawLine(0, 0, getWidth() - RIGHT_INDENT, 0);
		g2.drawLine(0, getHeight() - 1, getWidth() - RIGHT_INDENT, getHeight() - 1);
		g2.drawLine(getWidth() - RIGHT_INDENT, 0, getWidth() - RIGHT_INDENT, getHeight() - 1);
	}
	
	protected void updateWidth(int width)
	{
		controlledWidth = width;
		if(getParent() != null)
		{
			getParent().doLayout();
			getParent().invalidate();
			getParent().validate();
			getParent().repaint();
		}
	}

	class MouseEventHandler extends MouseAdapter implements MouseMotionListener
	{
		public void mouseDragged(MouseEvent e)
		{
			int width = controlled.getSize().width + e.getX();
			width = Math.max(width, MINIMUM_WIDTH);
			updateWidth(width);
		}

		public void mouseMoved(MouseEvent e)
		{
		}
	}
	
	private static final int MINIMUM_WIDTH = 5;
	private static final int DEFAULT_SETTER_WIDTH = 10;
	private static final int RIGHT_INDENT = 4;
	
	private JComponent controlled;
	private int controlledWidth;
}
