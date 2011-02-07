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

package org.miradi.dialogs.fieldComponents;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;

import org.miradi.main.EAM;

public class ClickablePanelTitleLabel extends PanelTitleLabel
{
	public ClickablePanelTitleLabel(Icon icon, String diaglogHtmlFileNameToUse)
	{
		super(icon);
		
		diaglogHtmlFileName = diaglogHtmlFileNameToUse;
		addMouseListener(new MouseClickedHandler());
	}
	
	private class MouseClickedHandler extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent mouseEvent)
		{
			super.mouseClicked(mouseEvent);
			
			try
			{
				EAM.showHtmlInfoMessageOkDialog(diaglogHtmlFileName);
			}
			catch (Exception e)
			{
				EAM.logException(e);
				EAM.unexpectedErrorDialog(e);
			}
		}
	}
	
	private String diaglogHtmlFileName;
}
