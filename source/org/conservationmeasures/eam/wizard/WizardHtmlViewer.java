/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard;

import javax.swing.text.html.StyleSheet;

import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.utils.HtmlFormViewer;
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
		
		addRuleFontFamily(style);
		addRuleFontSize(style);
	}
	
	public void addRuleFontSize(StyleSheet style)
	{
		String fontSize = getMainWindow().getTaggedString(AppPreferences.TAG_WIZARD_FONT_SIZE);
		if (fontSize.equals("0"))
			style.addRule(makeSureRuleHasRightPrefix("body {font-size:"+getFont().getSize()+"pt;}"));			
		else
			style.addRule(makeSureRuleHasRightPrefix("body {font-size:"+fontSize+"pt;}"));		
		
		System.out.println("addRuleFontSize:" + fontSize );
	}
	
	public void addRuleFontFamily(StyleSheet style)
	{
		String fontFamily = getMainWindow().getTaggedString(AppPreferences.TAG_WIZARD_FONT_FAMILY);
		style.addRule(makeSureRuleHasRightPrefix("body {font-family:"+fontFamily+";}"));
		System.out.println("addRuleFontSize:" +fontFamily);
	}
	

	//FIXME: should not use static ref here
	private MainWindow getMainWindow()
	{
		return EAM.mainWindow;
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
		"body {font-family: sans-serif, arial; margin: 10}",
		"code {font-family: sans-serif; }",
		"  .viewname { font-size: 125%; font-weight: bold }",
		"  .appname { font-size: 200%; font-weight: bold }",
		"  .apptagline { font-size: 110%; font-weight: bold }",
		"  .processsection { font-size: 120%; }",
		"  .taskheading { font-size: 110%; font-weight: bold; font-style: italics; }",
		"  .nextsteps { font-weight: 700; }",
		"  .navigation { background-color: #eeeeee; " +
			"border-width: 1; border-color: black; font-size: small; }",
		"  .hintheading { font-style: italics; font-weight: bold; }",
		"  .task {font-weight: bold; }",
		"  .toolbarbutton {color: #A06020; font-weight: bold;}",
		"  .hint {font-style: italics; }",
		"  .definition {font-style: italics; }",
	};
}
