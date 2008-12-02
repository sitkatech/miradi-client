/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.diagram.renderers;

import java.awt.Color;

import javax.swing.text.html.StyleSheet;

import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.dialogs.fieldComponents.HtmlFormViewer;
import org.miradi.main.MainWindow;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;

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
		return getProject().getMetadata().getData(ProjectMetadata.TAG_DIAGRAM_FONT_FAMILY);
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
		return getMainWindow().getProject().getDiagramFontSize();
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
