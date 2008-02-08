/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard;


import javax.swing.text.html.StyleSheet;

import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.MainWindow;
import org.martus.swing.HyperlinkHandler;

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
		"body {background-color: " + AppPreferences.getWizardBackgroundColorForCss() + ";}",
	};
}
