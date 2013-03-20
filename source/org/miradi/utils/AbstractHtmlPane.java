/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JEditorPane;
import javax.swing.KeyStroke;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLWriter;
import javax.swing.text.html.StyleSheet;

import net.atlanticbb.tantlinger.ui.text.CompoundUndoManager;
import net.atlanticbb.tantlinger.ui.text.HTMLUtils;
import net.atlanticbb.tantlinger.ui.text.WysiwygHTMLEditorKit;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLTextEditAction;

import org.miradi.dialogfields.DocumentEventHandler;
import org.miradi.dialogfields.ObjectScrollingMultilineInputField;
import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objectdata.AbstractUserTextDataWithHtmlFormatting;

abstract public class AbstractHtmlPane extends MiradiTextPane
{
	public AbstractHtmlPane(MainWindow mainWindow)
	{
		this(mainWindow, AbstractObjectDataInputPanel.DEFAULT_TEXT_COLUM_COUNT, ObjectScrollingMultilineInputField.INITIAL_MULTI_LINE_TEXT_AREA_ROW_COUNT);
	}

	public AbstractHtmlPane(MainWindow mainWindow, int fixedApproximateColumnCount, int initialApproximateRowCount)
	{
		super(mainWindow, fixedApproximateColumnCount, initialApproximateRowCount);

		handler = new HyperlinkHandler();
		final HTMLEditorKitWithCustomLinkController htmlEditorKit = new HTMLEditorKitWithCustomLinkController();
		setEditorKitForContentType(htmlEditorKit.getContentType(), htmlEditorKit);
		setContentType(htmlEditorKit.getContentType()); 
		initializeEditorComponent();
		addHyperlinkListener(new HyperlinkOpenHandler());
		updateStyleSheet();
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
			HTMLEditorKit htmlEditorKit = (HTMLEditorKit) getEditorKit();
			Document document = getDocument();
			final String jEditorPaneizeHtml = HTMLUtils.jEditorPaneizeHTML(html);
			StringReader stringReader = new StringReader(jEditorPaneizeHtml);
			htmlEditorKit.read(stringReader, document, location);
		} 
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
	}

	public void setSaverListener(DocumentEventHandler saverListenerToUse)
	{
		saverListener = saverListenerToUse;
	}

	private DocumentEventHandler getSaverListener()
	{
		return saverListener;
	}

	@Override
	public void setText(String text)
	{
		clearDocumentToAvoidFormattingLeak();
		updateStyleSheet();
		String topText = HtmlUtilities.removeAllExcept(text, AbstractUserTextDataWithHtmlFormatting.getAllowedHtmlTags());
		// NOTE: Shef does not encode/decode apostrophes as we need for proper XML
		topText = XmlUtilities2.convertXmlTextToHtmlWithoutSurroundingHtmlTags(topText);
		insertHtml(topText, 0);            
		CompoundUndoManager.discardAllEdits(getDocument());
	}

	private void clearDocumentToAvoidFormattingLeak()
	{
		//NOTE: per java instructions found here:
		//http://docs.oracle.com/javase/1.5.0/docs/api/javax/swing/JEditorPane.html#setText%28java.lang.String%29
		//we are creating a new Document to avoid any cross document leaks.
		HTMLEditorKit htmlEditorKit = (HTMLEditorKit) getEditorKit();
		final Document replacementDocument = htmlEditorKit.createDefaultDocument();

		if (getSaverListener() != null)
			getSaverListener().stopSaverListener();

		setDocument(replacementDocument);
		if (getSaverListener() != null)
			getSaverListener().startSaverListener();
	}

	@Override
	public String getText()
	{
		String text = super.getText();

		String normalizedAndSanitizedHtmlText = stripEntireHeadAndStyle(text);
		normalizedAndSanitizedHtmlText = XmlUtilities2.getWithUnescapedNumericEntities(normalizedAndSanitizedHtmlText);
		normalizedAndSanitizedHtmlText = getNormalizedAndSanitizedHtmlText(normalizedAndSanitizedHtmlText);

		return normalizedAndSanitizedHtmlText;
	}

	public static String getNormalizedAndSanitizedHtmlText(final String text)
	{
		return HtmlUtilitiesRelatedToShef.getNormalizedAndSanitizedHtmlText(text, AbstractUserTextDataWithHtmlFormatting.getAllowedHtmlTags());
	}

	private void updateStyleSheet()
	{
		HTMLEditorKit htmlKit = (HTMLEditorKit)getEditorKit();
		StyleSheet style = htmlKit.getStyleSheet();
		customizeStyleSheet(style);
		htmlKit.setStyleSheet(style);
	}

	public void handleOpenLink(Point mousePosition)
	{
		handler.activateHyperlink(this, mousePosition);
	}

	public static String stripEntireHeadAndStyle(String htmlText)
	{
		return HtmlUtilities.removeStartToEndTagAndItsContent(htmlText, new String[]{"style", "head"});
	}

	abstract protected void customizeStyleSheet(StyleSheet style);

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
	
	public class HTMLEditorKitWithCustomLinkController extends HTMLEditorKitWithCtrlVFixed 
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
		public void write(Writer out, Document doc, int pos, int len) throws IOException, BadLocationException
		{
			HTMLWriter w = new HtmlWriterWithoutIndenting(out, (HTMLDocument)doc, pos, len);
			w.write();
		}
	}
	
	public class HTMLEditorKitWithCtrlVFixed extends HtmlEditorKitWithNonSharedStyleSheet
	{
		@Override
		public void install(JEditorPane editorPane)
		{
			super.install(editorPane);
			
			removeCtrlVHandlerThatDoesTheWrongThing();
			if(editorToActionsMap.containsKey(editorPane))
	            return;

			removeShefPasteAction(editorPane);
			ActionMap actionMap = editorPane.getActionMap();
			HashMap<String, Action> actions = new HashMap<String, Action>();
			Action delegate = actionMap.get(DefaultEditorKit.pasteAction);
	        HTMLTextEditAction pasteSanitizedAction = new PasteSanitizedTextAction();
	        pasteSanitizedAction.putContextValue(HTMLTextEditAction.EDITOR, editorPane);
	        actions.put(DefaultEditorKit.pasteAction, delegate);
	        actionMap.put(DefaultEditorKit.pasteAction, pasteSanitizedAction);
	        editorToActionsMap.put(editorPane, actions);
		}
		
		private void removeShefPasteAction(JEditorPane editorPane)
		{
			ActionMap actionMap = editorPane.getActionMap();
			if(!editorToActionsMap.containsKey(editorPane))
	            return;
			
			HashMap actions = editorToActionsMap.get(editorPane);
			Action currentAction = actionMap.get(DefaultEditorKit.pasteAction);
			if(currentAction instanceof net.atlanticbb.tantlinger.ui.text.actions.PasteAction)
				actionMap.put(DefaultEditorKit.pasteAction, (Action)actions.get(DefaultEditorKit.pasteAction));

			editorToActionsMap.remove(editorPane);
		}

		@Override
		public void deinstall(JEditorPane editorPane)
		{
	        ActionMap actionMap = editorPane.getActionMap();
	        HashMap actions = editorToActionsMap.get(editorPane);
	        Action currentAction = actionMap.get(DefaultEditorKit.pasteAction);
	        if(currentAction instanceof PasteSanitizedTextAction)
	            actionMap.put(DefaultEditorKit.pasteAction, (Action)actions.get(DefaultEditorKit.pasteAction));
	        
	        editorToActionsMap.remove(editorPane);
			
			super.deinstall(editorPane);
		}

		// NOTE: We are not sure why, but Shef installs a Ctrl-V binding 
		// that maps to its PasteAction class, which throws away any 
		// formatting on the clipboard and pastes plain text. Our default 
		// paste handler calls editor.paste which does the right thing
		private void removeCtrlVHandlerThatDoesTheWrongThing()
		{
			InputMap inputMap = getInputMap();
			if(inputMap == null)
				return;

			ActionMap actionMap = getActionMap();
			if(actionMap == null)
				return;

			KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK);
			Object binding = inputMap.get(ks);
			if(binding == null)
				return;

			Action action = actionMap.get(binding);
			if(action == null)
				return;

			inputMap.put(ks, null);
			actionMap.put(binding, null);
		}
		
		private HashMap<JEditorPane, HashMap<String, Action>> editorToActionsMap = new HashMap<JEditorPane, HashMap<String, Action>>();
	}
	
	public class HtmlEditorKitWithNonSharedStyleSheet extends WysiwygHTMLEditorKit
	{	
		@Override
		public StyleSheet getStyleSheet()
		{
			if (styleSheet == null)
			{
				styleSheet = new StyleSheet();
				styleSheet.addStyleSheet(super.getStyleSheet());
			}

			return styleSheet;
		}

		@Override
		public void setStyleSheet(StyleSheet styleSheetToUse)
		{
			styleSheet = styleSheetToUse;
		}

		private StyleSheet styleSheet;
	}

	private class HtmlWriterThatFixesIllegalNesting extends HTMLWriter
	{
		public HtmlWriterThatFixesIllegalNesting(Writer out, HTMLDocument doc, int pos, int len)
		{
			super(out, doc, pos, len);
		}
	}

	private class HtmlWriterWithoutIndenting extends HtmlWriterThatFixesIllegalNesting
	{
		public HtmlWriterWithoutIndenting(Writer out, HTMLDocument doc, int pos, int len)
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
	private DocumentEventHandler saverListener;

}
