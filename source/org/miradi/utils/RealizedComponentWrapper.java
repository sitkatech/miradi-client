/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.utils;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.martus.swing.UiScrollPane;

public class RealizedComponentWrapper
{
	public RealizedComponentWrapper(JComponent componentToRealizeToUse)
	{
		componentToRealize = componentToRealizeToUse;

		//TODO: is there a better way to do this?
		frame = new JFrame();
		scrollPane = new UiScrollPane(componentToRealize);
		frame.add(scrollPane);
		frame.pack();
	}
	
	public void cleanup() 
	{
		// NOTE: Free up frame and scroll pane to avoid memory leaks
		frame.remove(scrollPane);
		scrollPane.remove(componentToRealize);
	}
	
	public JComponent getComponent()
	{
		return componentToRealize;
	}
	
	private JFrame frame;
	private UiScrollPane scrollPane;
	private JComponent componentToRealize;
}
