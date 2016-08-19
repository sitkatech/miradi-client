/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.utils;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;

public class EditableHtmlPane extends AbstractHtmlPane
{
	public EditableHtmlPane(MainWindow mainWindow, int fixedApproximateColumnCount, int initialApproximateRowCount)
	{
		super(mainWindow, fixedApproximateColumnCount, initialApproximateRowCount);
	}

	public EditableHtmlPane(MainWindow mainWindow)
	{
		super(mainWindow);
	}

	@Override
	public void setEditable(boolean b)
	{
		super.setEditable(b);
		updateStyleSheet();
	}

	@Override
	protected void customizeStyleSheet(StyleSheet style)
	{
		customizeStyleSheet(style, EAM.EDITABLE_FOREGROUND_COLOR);
	}

	@Override
	protected void updateStyleSheet()
	{
		Color fg = isEditable() ? EAM.EDITABLE_FOREGROUND_COLOR : EAM.READONLY_FOREGROUND_COLOR;
		updateStyleSheet(fg);
	}

	private void updateStyleSheet(Color fg)
	{
		EditorKit editorKit = getEditorKit();
		if (editorKit instanceof HTMLEditorKit)
		{
			HTMLEditorKit htmlKit = (HTMLEditorKit)getEditorKit();
			StyleSheet style = htmlKit.getStyleSheet();
			customizeStyleSheet(style, fg);
			htmlKit.setStyleSheet(style);
		}
	}

	private void customizeStyleSheet(StyleSheet style, Color fg)
	{
		final int fontSize = getMainWindow().getWizardFontSize();
		HtmlUtilities.addRuleFontSize(style, getFont().getSize(), fontSize);
		HtmlUtilities.addRuleFontFamily(style, getMainWindow().getDataPanelFontFamily());
		HtmlUtilities.addFontColor(style, fg);
	}
}
