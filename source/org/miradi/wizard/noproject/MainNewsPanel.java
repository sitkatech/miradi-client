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
package org.miradi.wizard.noproject;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.martus.swing.HyperlinkHandler;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.layout.OneColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.MiradiMultiCalendar;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.utils.Translation;
import org.miradi.wizard.MiradiHtmlViewer;

public class MainNewsPanel extends DisposablePanel
{
	public MainNewsPanel(MainWindow mainWindowToUse, HyperlinkHandler hyperLinkHandlerToUse) throws Exception
	{
		super();
		
		mainWindow = mainWindowToUse;
		hyperLinkHandler = hyperLinkHandlerToUse;
		cardLayout = new CardLayout();
		setLayout(cardLayout);

		OldNewsPanel oldNewsPanel = new OldNewsPanel();
		addPanelAsCard(oldNewsPanel, OLD_NEWS_PANEL_DESCRIPTION);
		
		NewsPanel newNewsPanel = new NewsPanel(mainWindow, hyperLinkHandler, this);
		addPanelAsCard(newNewsPanel, NEW_NEWS_PANEL_DESCRIPTION);
		
		cardLayout.show(this, OLD_NEWS_PANEL_DESCRIPTION);			
	}

	private void addPanelAsCard(JComponent newsPanel, String panelDescription)
	{
		MiradiScrollPane oldNewsScrollPane = new MiradiScrollPane(newsPanel);
		oldNewsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(oldNewsScrollPane, NEW_NEWS_PANEL_DESCRIPTION);
	}
	
	
	public void updateNewNewsText(String newsHtml)
	{
		String oldNews = mainWindow.getAppPreferences().getNewsText();
		if (newsHtml.equals(oldNews))
		{
			cardLayout.show(this, OLD_NEWS_PANEL_DESCRIPTION);
		}
		else
		{
			cardLayout.show(this, NEW_NEWS_PANEL_DESCRIPTION);
			getAppPreferences().setNewsText(newsHtml);
			MiradiMultiCalendar calendar = new MiradiMultiCalendar();
			getAppPreferences().setNewsDate(calendar.toIsoDateString());
		}
	}

	private void showNewNewsPanel()
	{
		cardLayout.show(this, NEW_NEWS_PANEL_DESCRIPTION);
		validate();
		repaint();
	}	
		
	private AppPreferences getAppPreferences()
	{
		return getMainWindow().getAppPreferences();
	}

	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	class ViewNewsButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			showNewNewsPanel();
		}
	}
	
	class OldNewsPanel extends OneColumnPanel
	{
		public OldNewsPanel() throws Exception
		{
			super();
			setBorder(BorderFactory.createEmptyBorder(EMPTY_BORDER_THICKNESS, EMPTY_BORDER_THICKNESS, EMPTY_BORDER_THICKNESS, EMPTY_BORDER_THICKNESS));
			
			setBackground(AppPreferences.getSideBarBackgroundColor());
			addComponents();
		}	
		
		private void addComponents() throws Exception
		{
			String newsDate = getMainWindow().getAppPreferences().getNewsDate();
			String html = Translation.getHtmlContent(NO_NEWS_MESSAGE_HTML_FILE_NAME);
			
			html = html.replace(DATE_TOKEN, newsDate);
			MiradiHtmlViewer htmlViewer = new MiradiHtmlViewer(getMainWindow(), hyperLinkHandler);
			htmlViewer.setText(HTML_NAVIGATION_TAG + html);
			add(htmlViewer);
			viewNewsButton = new PanelButton(EAM.text("View News"));
			viewNewsButton.addActionListener(new ViewNewsButtonHandler());
			add(viewNewsButton);
		}
		
		private PanelButton viewNewsButton;
	}

	private HyperlinkHandler hyperLinkHandler;
	private MainWindow mainWindow;
	private CardLayout cardLayout;
	private static final String NEW_NEWS_PANEL_DESCRIPTION = "NewNewsPanelDescription";
	private static final String OLD_NEWS_PANEL_DESCRIPTION = "OldNewsPanelDescription";
	private static final String NO_NEWS_MESSAGE_HTML_FILE_NAME = "OldNewsMessage.html";
	private static final String DATE_TOKEN = "$DATE$";
	private static final String HTML_NAVIGATION_TAG = "<div class='navigation'>";
	private static final int EMPTY_BORDER_THICKNESS = 10;
}
