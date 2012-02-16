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

import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLWriter;
import javax.swing.text.html.StyleSheet;

import net.atlanticbb.tantlinger.ui.text.CompoundUndoManager;
import net.atlanticbb.tantlinger.ui.text.HTMLUtils;
import net.atlanticbb.tantlinger.ui.text.WysiwygHTMLEditorKit;

import org.miradi.dialogfields.ObjectScrollingMultilineInputField;
import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objectdata.AbstractUserTextDataWithHtmlFormatting;

public class EditableHtmlPane extends MiradiTextPane
{
	public EditableHtmlPane(MainWindow mainWindow)
	{
		this(mainWindow, AbstractObjectDataInputPanel.DEFAULT_TEXT_COLUM_COUNT, ObjectScrollingMultilineInputField.INITIAL_MULTI_LINE_TEXT_AREA_ROW_COUNT);
	}
	
	public EditableHtmlPane(MainWindow mainWindow, int fixedApproximateColumnCount, int initialApproximateRowCount)
	{
		super(mainWindow, fixedApproximateColumnCount, initialApproximateRowCount);
		
		handler = new HyperlinkHandler();
		final HTMLEditorKitWithCustomLinkController htmlEditorKit = new HTMLEditorKitWithCustomLinkController();
		setEditorKitForContentType(htmlEditorKit.getContentType(), htmlEditorKit);
		setContentType(htmlEditorKit.getContentType()); 
		initializeEditorComponent();
		addHyperlinkListener(new HyperlinkOpenHandler());
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
			html = html.replaceAll(StringUtilities.EMPTY_SPACE, XmlUtilities2.NON_BREAKING_SPACE_NAME);
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
		String topText = HtmlUtilities.removeAllExcept(text, AbstractUserTextDataWithHtmlFormatting.getAllowedHtmlTags());
		// NOTE: Shef does not encode/decode apostrophes as we need for proper XML
		topText = XmlUtilities2.getXmlDecodedApostrophes(topText);
		super.setText("");
		insertHtml(topText, 0);            
		CompoundUndoManager.discardAllEdits(getDocument());
	}

	@Override
	public String getText()
	{
		String text = super.getText();
		
		return getNormalizedAndSanitizedHtmlText(text);
	}
	
	public static String getNormalizedAndSanitizedHtmlText(final String text)
	{
		return HtmlUtilities.getNormalizedAndSanitizedHtmlText(text, AbstractUserTextDataWithHtmlFormatting.getAllowedHtmlTags());
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
	
	public void handleOpenLink(Point mousePosition)
	{
		handler.activateHyperlink(this, mousePosition);
	}
	
	 private class HyperlinkOpenHandler implements HyperlinkListener 
	 {
		 public void hyperlinkUpdate(HyperlinkEvent e) 
		 {
			 if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) 
			 {
				 final URL url = e.getURL();
				 if (url != null)
				 {
					 getMainWindow().mainLinkFunction(url.toString());
				 }
			 }
		 }
	 }
	 
	 public class HTMLEditorKitWithCustomLinkController extends WysiwygHTMLEditorKit 
	 {
		 @Override
		 public void install(JEditorPane editorPane) 
		 {
			 MouseListener[] oldMouseListeners = editorPane.getMouseListeners();
	         MouseMotionListener[] oldMouseMotionListeners = editorPane.getMouseMotionListeners();
			 
	         super.install(editorPane);
	         removeMouseAndMotionListeners(editorPane);
			 restoreMouseAndMotionListeners(editorPane, oldMouseListeners,	oldMouseMotionListeners);

			 editorPane.addMouseListener(handler);
			 editorPane.addMouseMotionListener(handler);
		 }

		 private void removeMouseAndMotionListeners(JEditorPane editorPane)
		 {
			 for (MouseListener l: editorPane.getMouseListeners()) 
			 {
				 editorPane.removeMouseListener(l);
			 }

			 for (MouseMotionListener l: editorPane.getMouseMotionListeners()) 
			 {
				 editorPane.removeMouseMotionListener(l);
			 }
		 }

		 private void restoreMouseAndMotionListeners(JEditorPane editorPane, MouseListener[] oldMouseListeners, MouseMotionListener[] oldMouseMotionListeners)
		 {
			 for (MouseListener l: oldMouseListeners) 
			 {
				 editorPane.addMouseListener(l);
			 }

			 for (MouseMotionListener l: oldMouseMotionListeners) 
			 {
				 editorPane.addMouseMotionListener(l);
			 }
		 }
		 
		 @Override
		public void write(Writer out, Document doc, int pos, int len)
				throws IOException, BadLocationException
		{
		    HTMLWriter w = new HTMLWriterWithoutIndenting(out, (HTMLDocument)doc, pos, len);
		    w.write();
		}
	 }
	 
	 class HTMLWriterWithoutIndenting extends HTMLWriter
	 {
		public HTMLWriterWithoutIndenting(Writer out, HTMLDocument doc, int pos, int len)
		{
			super(out, doc, pos, len);
		}
		
		@Override
		protected void indent() throws IOException
		{
			// never indent
		}
		
		@Override
		protected void writeLineSeparator() throws IOException
		{
			// never write newlines
		}
		
		@Override
		protected void setCanWrapLines(boolean newValue)
		{
			// never wrap
			super.setCanWrapLines(false);
		}
	 }
	 
	 private HyperlinkHandler handler;
}
