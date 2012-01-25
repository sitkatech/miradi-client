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
		
		final WysiwygHTMLEditorKit htmlEditorKit = new WysiwygHTMLEditorKit();
		setEditorKitForContentType(htmlEditorKit.getContentType(), htmlEditorKit);
		setContentType(htmlEditorKit.getContentType()); 
		initializeEditorComponent();
	}

	protected void initializeEditorComponent()
	{
		insertHtml("<div></div>", 0);
	}

	private void insertHtml(String html, int location) 
	{       
		try 
		{
			// NOTE: The Java HTML parser compresses all whitespace to a single space
			// (http://java.sun.com/products/jfc/tsc/articles/bookmarks/)
			html = html.replaceAll(StringUtilities.EMPTY_SPACE, getEscapedWhiteSpace());
			html = html.replaceAll(StringUtilities.EMPTY_SPACE, "&#160;");
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

	public static String getEscapedWhiteSpace()
	{
		return "&nbsp;";
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
		
		return prepareForSaving(text, getTagsToKeep());
	}
	
	public static String prepareForSaving(final String text, String[] htmlTagsToKeep)
	{
		String trimmedText = "";
		final String[] lines = text.split(HtmlUtilities.getNewlineRegex());
		for (int index = 0; index < lines.length; ++index)
		{
			//NOTE: Shef editor never splits text between lines, so we can safely ignore the text\ntext case
			String line = lines[index];
			String leadingSpacesRemoved = line.replaceAll("^[ \t]+", "");
			trimmedText += leadingSpacesRemoved;
		}
		
		// NOTE: The Java HTML parser compresses all whitespace to a single space
		// (http://java.sun.com/products/jfc/tsc/articles/bookmarks/)
		trimmedText = trimmedText.replaceAll(getEscapedWhiteSpace(), StringUtilities.EMPTY_SPACE);
		trimmedText = trimmedText.replaceAll("&#160;", StringUtilities.EMPTY_SPACE);
		trimmedText = HtmlUtilities.removeNonHtmlNewLines(trimmedText);
		trimmedText = HtmlUtilities.appendNewlineToEndDivTags(trimmedText);
		trimmedText = HtmlUtilities.removeAllExcept(trimmedText, htmlTagsToKeep);
		trimmedText = trimmedText.trim();
		trimmedText = HtmlUtilities.replaceNonHtmlNewlines(trimmedText);
		//NOTE: Third party library  uses <br> instead of <br/>.  If we don't replace <br> then 
		//save method thinks there was a change and attempts to save.
		trimmedText = HtmlUtilities.replaceStartBrTagsWithEmptyBrTags(trimmedText);
		HtmlUtilities.ensureNoCloseBrTags(trimmedText);
		
		return trimmedText;
	}
	
	public static String[] getTagsToKeep()
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
		HtmlUtilities.addRuleFontFamily(style, getMainWindow().getDataPanelFontFamily());
	}
}
