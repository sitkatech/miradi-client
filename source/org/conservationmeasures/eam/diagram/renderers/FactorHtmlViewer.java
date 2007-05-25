package org.conservationmeasures.eam.diagram.renderers;

import java.awt.Color;

import javax.swing.text.html.StyleSheet;

import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.HtmlFormViewer;

public class FactorHtmlViewer extends HtmlFormViewer
{
	public FactorHtmlViewer()
	{
		super("", null);
	}

	public void customizeStyleSheet(StyleSheet style)
	{
		super.customizeStyleSheet(style);
		for(int i = 0; i < rules.length; ++i)			
			style.addRule(makeSureRuleHasRightPrefix(rules[i]));
		
		addRuleFactorCellBackgroundColor(style);
		addRuleFactorCellFontFamily(style);
		addRuleFactorCellFontSize(style);
	}
	
	public void setFactorCell(EAMGraphCell cell)
	{
			graphCell = cell;
	}
	
	public void addRuleFactorCellFontSize(StyleSheet style)
	{
		String fontSize = getProject().getMetadata().getData(ProjectMetadata.TAG_DIAGRAM_FONT_SIZE);
		if (!fontSize.equals("0"))
			style.addRule(makeSureRuleHasRightPrefix("body {font-size:"+fontSize+"pt;}"));
	}
	
	public void addRuleFactorCellFontFamily(StyleSheet style)
	{
		String fontFamily = getProject().getMetadata().getData(ProjectMetadata.PSEUDO_TAG_DIAGRAM_FONT_FAMILY);
		style.addRule(makeSureRuleHasRightPrefix("body {font-family:"+fontFamily+";}"));
	}

	public void addRuleFactorCellBackgroundColor(StyleSheet style)
	{
		String rule;
		if (graphCell!=null && graphCell.isFactor())
		{
			Color color = ((FactorCell)graphCell).getColor();
			rule = "body {background-color:"+convertColorToHTMLColor(color)+";}";
		}
		else
		//FIXME: ProjectScopeBox should implement getColor()
			if (graphCell!=null && graphCell.isProjectScope())
			{
				rule = "body {background-color:#00FF00;}";
			}
		else
			rule = "body {background-color:white;}";
		
		style.addRule(makeSureRuleHasRightPrefix(rule));
		
	}

	//FIXME: We could get project from cell for a factor cell the underlying factor; and from the project scope box
	private Project getProject()
	{
		return EAM.mainWindow.getProject();
	}

	public static String convertColorToHTMLColor(Color c) {
		return "#" + calcHex(c.getRed()) + calcHex(c.getGreen()) + calcHex(c.getBlue());
	}

	private static String calcHex(int red2)
	{
		String red = "0" + Integer.toHexString(red2);
		return red.substring(red.length() - 2);
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
	
	EAMGraphCell graphCell;

	/*
	 * NOTE! In Java 1.4 the CSS class reverses the meanings of #xxx and .xxx
	 * so if you want to affect a _class_ use #xxx 
	 * and if you want to affect an _id_ use .xxx
	 * GRRRR!
	 */
	final static String[] rules = {
		"body { font-style:italic; font-size:10pt;}",
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
