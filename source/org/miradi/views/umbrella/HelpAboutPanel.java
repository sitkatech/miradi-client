/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import javax.swing.JComponent;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.main.MiradiLogoPanel;
import org.miradi.utils.HtmlViewPanel;

public class HelpAboutPanel extends HtmlViewPanel
{
	public HelpAboutPanel(MainWindow mainWindowToUse, String htmlBodyText)
	{
		super(mainWindowToUse, EAM.text("Title|About Miradi"), htmlBodyText, 1000);
	}
	
	@Override
	protected JComponent createTopPanel()
	{
		return new MiradiLogoPanel();
	}
}
