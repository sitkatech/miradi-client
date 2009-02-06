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
package org.miradi.wizard;


import javax.swing.text.html.StyleSheet;

import org.martus.swing.HyperlinkHandler;
import org.miradi.main.AppPreferences;
import org.miradi.main.MainWindow;

public class WizardHtmlViewer extends MiradiHtmlViewer
{
	public WizardHtmlViewer(MainWindow mainWindow, HyperlinkHandler hyperLinkHandler)
	{
		super(mainWindow, hyperLinkHandler);
		setBackground(AppPreferences.getWizardBackgroundColor());
	}

	public void customizeStyleSheet(StyleSheet style)
	{
		super.customizeStyleSheet(style);
		for(int i = 0; i < rules.length; ++i)			
			style.addRule(makeSureRuleHasRightPrefix(rules[i]));
	}

	/*
	 * NOTE! In Java 1.4 the CSS class reverses the meanings of #xxx and .xxx
	 * so if you want to affect a _class_ use #xxx 
	 * and if you want to affect an _id_ use .xxx
	 * GRRRR!
	 */
	final static String[] rules = {
		"body {margin: 10; background-color: " + AppPreferences.getWizardBackgroundColorForCss() + ";}",
	};
}
