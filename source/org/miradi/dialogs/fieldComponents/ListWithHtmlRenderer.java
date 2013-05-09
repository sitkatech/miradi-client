/* 
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

import java.util.Vector;

import javax.swing.DefaultListCellRenderer;

import org.martus.swing.UiList;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.XmlUtilities2;

public class ListWithHtmlRenderer extends UiList
{
	public ListWithHtmlRenderer(Vector list)
	{
		super(list);
		
		setCellRenderer(new HtmlRenderer());
		setFont(getMainWindow().getUserDataPanelFont());
	}
	
	//TODO should not use static ref here
	public MainWindow getMainWindow()
	{
		return EAM.getMainWindow();
	}
	
	private class HtmlRenderer extends DefaultListCellRenderer
	{
		@Override
		public String getText()
		{
			String text = super.getText();
			text = XmlUtilities2.convertXmlTextToHtml(text);
			
			return text;
		}
	}
}
