/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

import javax.swing.JComponent;

import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.utils.SummaryViewTabChangeLinkHandler;

public class ReadonlyStaticClickableLinkField extends ObjectDataField
{
	public ReadonlyStaticClickableLinkField(MainWindow mainWindow, ORef refToUse, String htmlLink, String tabIdentifier) throws Exception
	{
		super(mainWindow.getProject(), refToUse);
		
		staticHtmlForm = new StaticHtmlForm(mainWindow, htmlLink, new SummaryViewTabChangeLinkHandler(mainWindow, tabIdentifier));
	}

	@Override
	public String getTag()
	{
		return "";
	}

	@Override
	public void updateFromObject()
	{
	}

	@Override
	public JComponent getComponent()
	{
		return staticHtmlForm;
	}

	@Override
	public void saveIfNeeded()
	{
	}

	private StaticHtmlForm staticHtmlForm;
}

