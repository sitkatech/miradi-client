/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.noproject.wizard;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.umbrella.CreateProjectDialog;
import org.martus.swing.HyperlinkHandler;
import org.martus.swing.UiLabel;
import org.martus.util.MultiCalendar;

import com.jhlabs.awt.BasicGridLayout;

public class ProjectList extends JPanel
{
	public ProjectList(HyperlinkHandler handlerToUse)
	{
		super(new BasicGridLayout(0, 2));
		handler = handlerToUse;
		
		setBackground(Color.WHITE);
		refresh();
	}
	
	public void refresh()
	{
		removeAll();
		add(new UiLabel("Project Filename"));
		add(new UiLabel("Last Modified"));
		
		File[] projectDirectories = getProjectDirectories();
		for(int i = 0; i < projectDirectories.length; ++i)
		{
			File projectFile = projectDirectories[i];
			String name = projectFile.getName();
			MultiCalendar date = new MultiCalendar(new Date(projectFile.lastModified()));
			String isoDate = date.toIsoDateString();
			add(new Hyperlink(NoProjectWizardPanel.OPEN_PREFIX+name, name, handler));
			add(new TableHeadingText(isoDate));
		}
		
		// NOTE: invalidate() is not strong enough to blank the bottom row after delete
		repaint();
	}

	public File[] getProjectDirectories()
	{
		File home = EAM.getHomeDirectory();
		return home.listFiles(new CreateProjectDialog.DirectoryFilter());

	}
	
	class TableHeadingText extends UiLabel
	{
		public TableHeadingText(String text)
		{
			super(text);
			setFont(getFont().deriveFont(Font.BOLD));
		}
		
	}
	
	static class Hyperlink extends UiLabel implements MouseListener
	{
		public Hyperlink(String urlToReturn, String textToShow, HyperlinkHandler handlerToUse)
		{
			super("<html><u>" + textToShow + "</u></html>");
			url = urlToReturn;
			text = textToShow;
			handler = handlerToUse;
			
			setBackground(Color.WHITE);
			setForeground(Color.BLUE);
			setOpaque(true);
			addMouseListener(this);
		}

		public void mouseClicked(MouseEvent e)
		{
			if(e.getButton() == MouseEvent.BUTTON1)
				handler.linkClicked(url);
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
			JPopupMenu menu = handler.getRightClickMenu(text);
			menu.show(this, e.getX(), e.getY());
		}
		
		String url;
		String text;
		HyperlinkHandler handler;
	}
	
	HyperlinkHandler handler;
}
