/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import java.awt.Component;

import org.miradi.main.AppPreferences;

public class MiradiWizardScrollPane extends MiradiScrollPane
{
	public MiradiWizardScrollPane(Component view)
	{
		super(view);

		setBackground(AppPreferences.getWizardBackgroundColor());
		getViewport().setBackground(AppPreferences.getWizardBackgroundColor());
	}

}
