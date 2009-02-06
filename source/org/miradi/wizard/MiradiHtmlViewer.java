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
import org.miradi.dialogs.fieldComponents.HtmlFormViewer;
import org.miradi.main.AppPreferences;
import org.miradi.main.MainWindow;

public class MiradiHtmlViewer extends HtmlFormViewer
{
	public MiradiHtmlViewer(MainWindow mainWindow, HyperlinkHandler hyperLinkHandler)
	{
		super(mainWindow, "", hyperLinkHandler);
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
		"code {}",
		"h3 { margin-top: 2px; margin-bottom: 0; }",
		"p  {  }",
		"  .sidebar {background-color: " + AppPreferences.getWizardSidebarBackgroundColorForCss() + ";}",
		"  .viewname { font-size: 125%; font-weight: bold }",
		"  .appname { font-size: 200%; font-weight: bold }",
		"  .apptagline { font-size: 110%; font-weight: bold }",
		"  .processsection { font-size: 120%; }",
		"  .taskheading { font-size: 110%; font-weight: bold; font-style: italics; }",
		"  .nextsteps { font-weight: 700; font-size: 100%;}",
		"  .navigation { background-color: " + AppPreferences.getWizardSidebarBackgroundColorForCss() + "; border-width: 1; border-color: black; font-size: 100%; }",
		"  .hintheading { font-style: italics; font-weight: bold; font-size: 100%;}",
		"  .task {font-weight: bold; font-size: 100%;}",
		"  .toolbarbutton {color: #A06020; font-weight: bold; font-size: 100%;}",
		"  .hint {font-style: italics; font-size: 100%;}",
		"  .definition {font-style: italics; font-size: 100%;}",
		"  .intro {font-size: 110%;}",
		"  .introdefinition {font-style: italics; font-size: 110%;}",
		"  .indented {margin: 10pt;}",
		"  .WizardText {" +
			"background-color: " + AppPreferences.getWizardBackgroundColorForCss() +
			";}",
		"  .LogoHeader {" + 
			"background-color: " + AppPreferences.getWizardTitleBackgroundColorForCss() + ";}",
		"  .processsteptitle {" +
			"background-color: " + AppPreferences.getWizardTitleBackgroundColorForCss() + ";" +
			"color: " + AppPreferences.WIZARD_TITLE_FOREGROUND_FOR_CSS + ";" +
			"font-weight: bold;}",
		"  .wizardscreentitle {" +
			"background-color: " + AppPreferences.getWizardTitleBackgroundColorForCss() + ";" +
			"font-size: 110%; font-weight: bold;}",
		"  .OrgTopPanel {" + 
			"background-color: " + AppPreferences.getDataPanelBackgroundColorForCss() + ";}",
		"  .DataPanel {" + 
			"background-color: " + AppPreferences.getDataPanelBackgroundColorForCss() + ";}",
	};
}
