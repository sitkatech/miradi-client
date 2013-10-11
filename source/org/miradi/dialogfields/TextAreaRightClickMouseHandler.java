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
package org.miradi.dialogfields;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

import org.miradi.actions.Actions;


public class TextAreaRightClickMouseHandler extends MouseAdapter
{
	public TextAreaRightClickMouseHandler(Actions actionsToUse, JTextComponent textFieldToUse)
	{
		textFieldToUse.addMouseListener(this);
		actions = actionsToUse;
		textField = textFieldToUse;
		invocationLocation = new Point(0, 0);
	}
	
	@Override
	public void mousePressed(MouseEvent e)
	{
		if(e.isPopupTrigger())
			fireRightClick(e);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(e.isPopupTrigger())
			fireRightClick(e);
	}
	
	private void fireRightClick(MouseEvent e)
	{
		setInvocationLocation(e.getPoint());
		getRightClickMenu().show(getTextField(), e.getX(), e.getY());
	}
	
	public JPopupMenu getRightClickMenu()
	{
		JPopupMenu menu = new JPopupMenu();
		TextAreaContextMenuListener listener = createTextAreaContextMenuListener(menu);
		menu.addPopupMenuListener(listener);
		
		return menu;
	}
	
	protected void setInvocationLocation(Point clickLocation)
	{
		invocationLocation = clickLocation;
	}
	
	protected Point getInvocationLocation()
	{
		return invocationLocation;
	}

	protected TextAreaContextMenuListener createTextAreaContextMenuListener(JPopupMenu menu)
	{
		return new TextAreaContextMenuListener(menu, textField, actions);
	}
	
	protected JTextComponent getTextField()
	{
		return textField;
	}
	
	protected Actions getActions()
	{
		return actions;
	}
	
	private JTextComponent textField;
	private Actions actions;
	private Point invocationLocation;
}
