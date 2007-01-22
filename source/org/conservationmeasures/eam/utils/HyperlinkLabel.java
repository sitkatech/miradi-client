/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;

import org.conservationmeasures.eam.main.EAM;
import org.martus.swing.HyperlinkHandler;
import org.martus.swing.UiLabel;

public class HyperlinkLabel extends UiLabel implements MouseListener
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
		super("<html><u>" + linkText + "</u></html>");
		text = linkText;
		setBackground(Color.WHITE);
		setForeground(Color.BLUE);
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
				EAM.okDialog(EAM.text("Hyperlink Label"), array);
			}
			else
				handler.linkClicked(url);
		}
	}

	public void mouseEntered(MouseEvent e)
	{
		setBackground(Color.LIGHT_GRAY);
		invalidate();
	}

	public void mouseExited(MouseEvent e)
	{
		setBackground(Color.WHITE);
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
	
	String url;
	String text;
	String popUpText;
	HyperlinkHandler handler;
}
