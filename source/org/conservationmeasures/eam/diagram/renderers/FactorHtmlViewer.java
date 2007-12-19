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
import org.conservationmeasures.eam.dialogs.fieldComponents.HtmlFormViewer;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;

public class FactorHtmlViewer extends HtmlFormViewer
{
	public FactorHtmlViewer(MainWindow mainWindow)
	{
		super(mainWindow,"", null);
	}

	public void customizeStyleSheet(StyleSheet style)
	{
		if (graphCell==null)
			return;
		
		super.customizeStyleSheet(style);
		for(int i = 0; i < rules.length; ++i)			
			style.addRule(makeSureRuleHasRightPrefix(rules[i]));
		
		addRuleBackgroundColor(style);
	}
	
	public void setFactorCell(EAMGraphCell cell)
	{
			graphCell = cell;
	}
	
	public void addRuleBackgroundColor(StyleSheet style)
	{
		Color color = graphCell.getColor();
		style.addRule(makeSureRuleHasRightPrefix("body {background-color:"+convertColorToHTMLColor(color)+";}"));
	}

	public String getFontFamily()
	{
		return getProject().getMetadata().getData(ProjectMetadata.PSEUDO_TAG_DIAGRAM_FONT_FAMILY);
	}

	public int getFontSize()
	{
		int systemFontSize = getSystemFontSize();
		float diagramFactorFontSize = getDiagramFactorFontSize();
		if (diagramFactorFontSize == 0)
			return systemFontSize;

		if (systemFontSize == 0)
			systemFontSize = 11;

		int scaledFontSize = (int)(systemFontSize * diagramFactorFontSize);
		return scaledFontSize;
	}


	private int getSystemFontSize()
	{
		String sizeAsString = getProject().getMetadata().getData(ProjectMetadata.TAG_DIAGRAM_FONT_SIZE);
		if(sizeAsString.length() == 0)
			return 0;
		
		return new Integer(sizeAsString).intValue();
	}
	
	private float getDiagramFactorFontSize()
	{
		if (graphCell == null)
			return 0.0f;
		
		if (!graphCell.isFactor())
			return 0.0f;
		
		FactorCell factorCell = (FactorCell) graphCell;
		String fontSizeAsString = factorCell.getDiagramFactor().getFontSize();
		if (fontSizeAsString.length() == 0)
			return 0.0f;
		
		return Float.parseFloat(fontSizeAsString);
	}

	private Project getProject()
	{
		return getMainWindow().getProject();
	}

	public static String convertColorToHTMLColor(Color c) {
		return "#" + calcHex(c.getRed()) + calcHex(c.getGreen()) + calcHex(c.getBlue());
	}

	private static String calcHex(int red2)
	{
		String red = "0" + Integer.toHexString(red2);
		return red.substring(red.length() - 2);
	}
	
	EAMGraphCell graphCell;

	final static String[] rules = {
		"body {text-align:center;}",
	};

}
