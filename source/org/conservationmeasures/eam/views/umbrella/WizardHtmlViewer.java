/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import javax.swing.text.html.StyleSheet;

import org.conservationmeasures.eam.main.EAM;
import org.martus.swing.HtmlViewer;
import org.martus.swing.HyperlinkHandler;

public class WizardHtmlViewer extends HtmlViewer
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
	
	private String makeSureRuleHasRightPrefix(String rule)
	{
		if (cssDotPrefixWorksCorrectly())
			return rule;

		return replaceDotWithPoundSign(rule);
	}
	
	private String replaceDotWithPoundSign(String rule)
	{
		if (rule.trim().startsWith("."))
			return rule.trim().replaceFirst(".", "#");

		return rule;
	}

	private boolean cssDotPrefixWorksCorrectly()
	{
		String javaVersion = EAM.getJavaVersion();
		if (javaVersion.startsWith("1.4"))
			return false;
		return true;
	}
	
	/*
	 * NOTE! In Java 1.4 the CSS class reverses the meanings of #xxx and .xxx
	 * so if you want to affect a _class_ use #xxx 
	 * and if you want to affect an _id_ use .xxx
	 * GRRRR!
	 */
	final static String[] rules = {
		"body {font-family: sans-serif, arial; }",
		"code {font-family: sans-serif; }",
		"  .viewname { font-size: 125%;  }",
		"  .processsection { font-size: 120%; }",
		"  .taskheading { font-size: 110%; font-weight: bold; font-style: italics; }",
		"  .nextsteps { font-weight: 700; }",
		"  .navigation { background-color: #eeeeee; " +
			"border-width: 1; border-color: black; font-size: small; }",
		"  .hintheading { font-style: italics; font-weight: bold; }",
		"  .task {font-weight: bold; color: #0000ff; }",
		"  .toolbarbutton {color: #0000ff; }",
		"  .hint {font-style: italics; }",
	};
}
