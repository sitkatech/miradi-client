/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.wizard;

import javax.swing.text.html.StyleSheet;

import org.conservationmeasures.eam.dialogs.fieldComponents.HtmlFormViewer;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.MainWindow;
import org.martus.swing.HyperlinkHandler;

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
		"  .viewname { font-size: 125%; font-weight: bold }",
		"  .appname { font-size: 200%; font-weight: bold }",
		"  .apptagline { font-size: 110%; font-weight: bold }",
		"  .processsection { font-size: 120%; }",
		"  .taskheading { font-size: 110%; font-weight: bold; font-style: italics; }",
		"  .nextsteps { font-weight: 700; font-size: 100%;}",
		"  .navigation { background-color: #eeeeee; border-width: 1; border-color: black; font-size: 100%; }",
		"  .hintheading { font-style: italics; font-weight: bold; font-size: 100%;}",
		"  .task {font-weight: bold; font-size: 100%;}",
		"  .toolbarbutton {color: #A06020; font-weight: bold; font-size: 100%;}",
		"  .hint {font-style: italics; font-size: 100%;}",
		"  .definition {font-style: italics; font-size: 100%;}",
		"  .intro {font-size: 120%;}",
		"  .introdefinition {font-style: italics; font-size: 120%;}",
		"  .processsteptitle {" +
			"background-color: " + AppPreferences.WIZARD_TITLE_BACKGROUND_FOR_CSS + ";" +
			"color: " + AppPreferences.WIZARD_TITLE_FOREGROUND_FOR_CSS + ";" +
			"font-weight: bold;}",
		"  .wizardscreentitle {" +
			"background-color: " + AppPreferences.WIZARD_TITLE_BACKGROUND_FOR_CSS + ";" +
			"font-size: 110%; font-weight: bold;}",
	};
}
