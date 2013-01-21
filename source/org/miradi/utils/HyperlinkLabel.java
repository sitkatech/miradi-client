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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;

import org.martus.swing.HyperlinkHandler;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;

public class HyperlinkLabel extends PanelTitleLabel implements MouseListener
{
	
	public HyperlinkLabel(String linkText, String popUpTextToUse)
	{
		this(linkText);
		popUpText = popUpTextToUse;
	}
	
	public HyperlinkLabel(String linkText, String urlToReturn, HyperlinkHandler handlerToUse)
	{
		this(linkText);
		url = urlToReturn;
		handler = handlerToUse;
	}
	
	private HyperlinkLabel(String linkText)
	{
		super("<html>" +
				"<span style='" +
				"color: blue;" +
				"font-size: 120%; " +
				"text-decoration: underline;" +
				"'>" + 
				linkText + 
				"</span>" +
				"</html>");
		text = linkText;
		setBackground(NORMAL_BACKGROUND);
		setOpaque(true);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		addMouseListener(this);
	}

	public void mouseClicked(MouseEvent e)
	{
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			if (handler == null)
			{
				String[] array = {popUpText};
				EAM.okDialog(EAM.text("Title|Information"), array);
			}
			else
				handler.linkClicked(url);
		}
	}

	public void mouseEntered(MouseEvent e)
	{
		setBackground(HOVER_BACKGROUND);
		invalidate();
	}

	public void mouseExited(MouseEvent e)
	{
		setBackground(NORMAL_BACKGROUND);
		invalidate();
	}

	public void mousePressed(MouseEvent e)
	{
		if(e.isPopupTrigger())
			fireRightClick(e);
	}

	public void mouseReleased(MouseEvent e)
	{
		if(e.isPopupTrigger())
			fireRightClick(e);
	}
	
	void fireRightClick(MouseEvent e)
	{
		if (handler != null)
		{
			JPopupMenu menu = handler.getRightClickMenu(text);
			menu.show(this, e.getX(), e.getY());
		}
	}
	
	Color NORMAL_BACKGROUND = AppPreferences.getWizardBackgroundColor();
	Color HOVER_BACKGROUND = Color.LIGHT_GRAY;
	String url;
	String text;
	String popUpText;
	HyperlinkHandler handler;
}
