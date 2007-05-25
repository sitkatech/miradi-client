/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.diagram.renderers;

import java.awt.Color;

import javax.swing.text.html.StyleSheet;

import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.ProjectScopeBox;
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
		if (graphCell==null)
			return;
		
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
		if (fontSize.equals("0"))
			style.addRule(makeSureRuleHasRightPrefix("body {font-size:"+getFont().getSize()+"pt;}"));			
		else
			style.addRule(makeSureRuleHasRightPrefix("body {font-size:"+fontSize+"pt;}"));		
	}
	
	public void addRuleFactorCellFontFamily(StyleSheet style)
	{
		String fontFamily = getProject().getMetadata().getData(ProjectMetadata.PSEUDO_TAG_DIAGRAM_FONT_FAMILY);
		style.addRule(makeSureRuleHasRightPrefix("body {font-family:"+fontFamily+";}"));
	}

	public void addRuleFactorCellBackgroundColor(StyleSheet style)
	{
		if (graphCell.isFactor())
		{
			Color color = ((FactorCell)graphCell).getColor();
			style.addRule(makeSureRuleHasRightPrefix("body {background-color:"+convertColorToHTMLColor(color)+";}"));
		}

		if (graphCell.isProjectScope())
		{
			Color color = ((ProjectScopeBox)graphCell).getColor();
			style.addRule(makeSureRuleHasRightPrefix("body {background-color:"+convertColorToHTMLColor(color)+";}"));
		}
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

	final static String[] rules = {
		"body {text-align:center;}",
	};

}
