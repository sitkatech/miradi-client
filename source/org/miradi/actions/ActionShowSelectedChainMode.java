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
package org.miradi.actions;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionShowSelectedChainMode extends ViewAction
{
	public ActionShowSelectedChainMode(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), getIconName());
	}

	private static String getIconName()
	{
		return "icons/chain.gif";
	}
	
	private static String getLabel()
	{
		return EAM.text("Action|Brainstorm Mode");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Isolate the selected factor chain in Brainstorm Mode");
	}

}
