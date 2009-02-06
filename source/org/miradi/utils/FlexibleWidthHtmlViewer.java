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

package org.miradi.utils;

import java.awt.Dimension;

import javax.swing.JComponent;

import org.martus.swing.HyperlinkHandler;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.wizard.MiradiHtmlViewer;

public class FlexibleWidthHtmlViewer extends MiradiHtmlViewer
{
	public FlexibleWidthHtmlViewer(MainWindow mainWindow, String htmlText)
	{
		this(mainWindow, null, htmlText);
	}
	
	public FlexibleWidthHtmlViewer(MainWindow mainWindow, HyperlinkHandler hyperLinkHandler, String htmlText)
	{
		this(mainWindow, hyperLinkHandler);
		setText(htmlText);
	}
	
	public FlexibleWidthHtmlViewer(MainWindow mainWindow, HyperlinkHandler hyperLinkHandler)
	{
		super(mainWindow, hyperLinkHandler);
	}
	
	@Override
	public Dimension getPreferredSize()
	{
		if(getParent() == null)
			return super.getPreferredSize();
		int width = getParent().getWidth();
		
		setMaximumSize(new Dimension(width, Integer.MAX_VALUE));
		int height = super.getPreferredSize().height;
		setMaximumSize(null);
		return new Dimension(width, height);
	}
	
	public static JComponent createFromResourceFile(MainWindow mainWindow, String htmlFile)
	{
		try
		{
			String html = Translation.getHtmlContent(htmlFile);
			return new FlexibleWidthHtmlViewer(mainWindow, html);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new FlexibleWidthHtmlViewer(mainWindow, "");
		}
	}	
}