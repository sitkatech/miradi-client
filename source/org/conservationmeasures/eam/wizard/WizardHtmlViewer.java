/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard;

import javax.swing.text.html.StyleSheet;

import org.conservationmeasures.eam.dialogs.fieldComponents.HtmlFormViewer;
import org.martus.swing.HyperlinkHandler;

public class WizardHtmlViewer extends HtmlFormViewer
{
	public WizardHtmlViewer(HyperlinkHandler hyperLinkHandler)
	{
		super("", hyperLinkHandler);
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
		"body {margin: 10}",
		"code {}",
		"  .viewname { font-size: 125%; font-weight: bold }",
		"  .appname { font-size: 200%; font-weight: bold }",
		"  .apptagline { font-size: 110%; font-weight: bold }",
		"  .processsection { font-size: 120%; }",
		"  .taskheading { font-size: 110%; font-weight: bold; font-style: italics; }",
		"  .nextsteps { font-weight: 700; }",
		"  .navigation { background-color: #eeeeee; border-width: 1; border-color: black; font-size: 100%; }",
		"  .hintheading { font-style: italics; font-weight: bold; }",
		"  .task {font-weight: bold; }",
		"  .toolbarbutton {color: #A06020; font-weight: bold; font-size: 100%;}",
		"  .hint {font-style: italics; }",
		"  .definition {font-style: italics; }",
	};
}
