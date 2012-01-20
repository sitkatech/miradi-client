/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.utils;

import java.io.StringReader;

import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import net.atlanticbb.tantlinger.ui.text.CompoundUndoManager;
import net.atlanticbb.tantlinger.ui.text.HTMLUtils;
import net.atlanticbb.tantlinger.ui.text.WysiwygHTMLEditorKit;

import org.miradi.dialogfields.ObjectScrollingMultilineInputField;
import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class EditableHtmlPane extends MiradiTextPane
{
	public EditableHtmlPane(MainWindow mainWindow)
	{
		this(mainWindow, AbstractObjectDataInputPanel.DEFAULT_TEXT_COLUM_COUNT, ObjectScrollingMultilineInputField.INITIAL_MULTI_LINE_TEXT_AREA_ROW_COUNT);
	}
	
	public EditableHtmlPane(MainWindow mainWindow, int fixedApproximateColumnCount, int initialApproximateRowCount)
	{
		super(mainWindow, fixedApproximateColumnCount, initialApproximateRowCount);
		
		setEditorKitForContentType("text/html", new WysiwygHTMLEditorKit());
		setContentType("text/html"); 
		insertHtml("", 0);        
	}

	private void insertHtml(String html, int location) 
	{       
		try 
		{
			HTMLEditorKit htmlEditorKit = (HTMLEditorKit) getEditorKit();
			Document document = getDocument();
			final String jEditorPaneizeHtml = HTMLUtils.jEditorPaneizeHTML(html);
			StringReader stringReader = new StringReader(jEditorPaneizeHtml);
			htmlEditorKit.read(stringReader, document, location);
		} 
		catch (Exception e)
		{
			EAM.unexpectedErrorDialog(e);
			EAM.logException(e);
		}
	}

	@Override
	public void setText(String text)
	{
		updateStyleSheet();
		String topText = HtmlUtilities.removeAllExcept(text, getTagsToKeep());
		super.setText("");
		insertHtml(topText, 0);            
		CompoundUndoManager.discardAllEdits(getDocument());
	}

	@Override
	public String getText()
	{
		String text = super.getText();
		
		return HtmlUtilities.prepareForSaving(text, getTagsToKeep());
	}
	
	private String[] getTagsToKeep()
	{
		return new String[] {"br", "b", "i", "ul", "ol", "li", "u", "strike", };
	}
	
	private void updateStyleSheet()
	{
		HTMLEditorKit htmlKit = (HTMLEditorKit)getEditorKit();
		StyleSheet style = htmlKit.getStyleSheet();
		customizeStyleSheet(style);
		htmlKit.setStyleSheet(style);
	}

	private void customizeStyleSheet(StyleSheet style)
	{
		final int fontSize = getMainWindow().getWizardFontSize();
		HtmlUtilities.addRuleFontSize(style, getFont().getSize(), fontSize);
	}
}
