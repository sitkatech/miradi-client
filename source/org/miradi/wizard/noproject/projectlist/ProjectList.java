/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.noproject.projectlist;

import java.io.File;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.martus.swing.HyperlinkHandler;
import org.martus.util.MultiCalendar;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.utils.HyperlinkLabel;
import org.miradi.views.umbrella.CreateProjectDialog;
import org.miradi.wizard.noproject.NoProjectWizardStep;

import com.jhlabs.awt.GridLayoutPlus;

public class ProjectList extends JPanel
{
	public ProjectList(HyperlinkHandler handlerToUse)
	{
		handler = handlerToUse;

		int COL_GUTTER = 5;
		int ROW_GUTTER = 0;
		GridLayoutPlus layout = new GridLayoutPlus(0, 2, COL_GUTTER, ROW_GUTTER);
		setLayout(layout);
		
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		setBackground(AppPreferences.getWizardBackgroundColor());
		refresh();
	}
	
	public void refresh()
	{
		removeAll();
		add(new TableHeadingText("Project Filename"));
		add(new TableHeadingText("Last Modified"));
		
		File[] projectDirectories = getProjectDirectories();
		for(int i = 0; i < projectDirectories.length; ++i)
		{
			File projectFile = projectDirectories[i];
			String name = projectFile.getName();
			MultiCalendar date = new MultiCalendar(new Date(projectFile.lastModified()));
			String isoDate = date.toIsoDateString();
			add(new HyperlinkLabel(name, NoProjectWizardStep.OPEN_PREFIX+name, handler));
			add(new HtmlLabel("<font size='%100'>" + isoDate + "</font>"));
		}
		
		// NOTE: invalidate() is not strong enough to blank the bottom row after delete
		repaint();
	}

	public File[] getProjectDirectories()
	{
		File home = EAM.getHomeDirectory();
		home.mkdirs();
		return home.listFiles(new CreateProjectDialog.DirectoryFilter());

	}
	
	class HtmlLabel extends PanelTitleLabel
	{
		public HtmlLabel(String text)
		{
			super("<html><span style='background-color: " + AppPreferences.getWizardBackgroundColorForCss() + ";'" + text + "</span></html>");
		}
	}
	
	class TableHeadingText extends HtmlLabel
	{
		public TableHeadingText(String text)
		{
			super("<strong><font size='+1'>" + text + "</font></strong>");
		}
		
	}

	HyperlinkHandler handler;
}
