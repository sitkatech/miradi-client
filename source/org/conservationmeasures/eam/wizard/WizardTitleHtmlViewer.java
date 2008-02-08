/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.wizard;

import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.MainWindow;

public class WizardTitleHtmlViewer extends MiradiHtmlViewer
{
	public WizardTitleHtmlViewer(MainWindow mainWindow)
	{
		super(mainWindow, null);
		setBackground(AppPreferences.getWizardTitleBackground());
	}

}
