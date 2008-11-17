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

package org.miradi.dialogs.base;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;

import org.martus.swing.Utilities;

public class DialogWithButtonBar extends MiradiDialog
{
	public DialogWithButtonBar(JFrame parent, String heading)
	{
		super(parent, heading);

		Container contents = getContentPane();
		contents.setLayout(new BorderLayout());
	}
	
	public void setButtons(Vector<Component> buttons)
	{
		Container contents = getContentPane();
		contents.add(createButtonBar(buttons), BorderLayout.AFTER_LAST_LINE);
		pack();
		Utilities.fitInScreen(this);
	}

	private Box createButtonBar(Vector<Component> boxComponents)
	{
		Box buttonBar = Box.createHorizontalBox();
		buttonBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		Utilities.addComponentsRespectingOrientation(buttonBar, boxComponents.toArray(new Component[0]));

		int boxComponentCount = boxComponents.size();
		if(boxComponentCount > 0)
			getRootPane().setDefaultButton((JButton)boxComponents.get(boxComponentCount - 1));
		
		return buttonBar;
	}
	
}
