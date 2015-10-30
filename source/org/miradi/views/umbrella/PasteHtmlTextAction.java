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
package org.miradi.views.umbrella;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;

import javax.swing.AbstractAction;
import javax.swing.JEditorPane;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTMLDocument;

import net.atlanticbb.tantlinger.ui.text.CompoundUndoManager;

import org.miradi.main.EAM;
import org.miradi.utils.AbstractHtmlPane;
import org.miradi.utils.AbstractHtmlPane.HtmlEditorKitWithNonSharedStyleSheet;
import org.miradi.utils.HtmlUtilities;

public class PasteHtmlTextAction extends AbstractAction
{
	public PasteHtmlTextAction(JTextComponent fieldToUse)
	{
		textField = fieldToUse;
	}

	public void actionPerformed(ActionEvent e)
	{
		try
		{
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			Transferable contents = clipboard.getContents(null);
			if(contents == null)
				return;                

			final DataFlavor[] transferDataFlavors = contents.getTransferDataFlavors();
			DataFlavor dataFlavor = selectBestDataFlavor(transferDataFlavors);
			if (dataFlavor == null)
				return;

			String clipboardValue = read(dataFlavor.getReaderForText(contents));
			if(clipboardValue == null)
				return;

			CompoundUndoManager.beginCompoundEdit(getEditorField().getDocument());
			pasteText(dataFlavor, clipboardValue);
			CompoundUndoManager.endCompoundEdit(getEditorField().getDocument());
		}
		catch(Exception exception)
		{
			EAM.alertUserOfNonFatalException(exception);
		}
	}

    private void pasteText(DataFlavor dataFlavor, String clipboardValue) throws Exception
    {
        if (isMimeType(dataFlavor, HTML_MIME_TYPE))
        {
            replaceNormalizedHtmlAtCaretPosition(clipboardValue);
        }
        else
        {
            String newLineToBr = HtmlUtilities.convertPlainTextToHtmlText(clipboardValue);
            replaceTextAtCaretPosition(newLineToBr);
        }
    }

	private void replaceNormalizedHtmlAtCaretPosition(String html) throws Exception
    {
		html = AbstractHtmlPane.getNormalizedAndSanitizedHtmlText(html);
		//NOTE: We are right before release 4.0 and instead of splitting HtmlUtilitiesRelatedToShef
		//into xml and html, we chose a safe fix: undo the apostrophe encoding done by the normalizer.
		html = convertXmlToHtml(html);
		
		replaceTextAtCaretPosition(html);
    }
	
	private String convertXmlToHtml(String value)
	{
		return value.replaceAll("&apos;", "'");
	}

	private void replaceTextAtCaretPosition(String textToInsert) throws Exception
	{
		removeSelectedText();
        JEditorPane editor = (JEditorPane) getEditorField();
        HTMLDocument document = (HTMLDocument)editor.getDocument();
        final int insertAtCaretPosition = editor.getCaretPosition();

        HtmlEditorKitWithNonSharedStyleSheet kit = (HtmlEditorKitWithNonSharedStyleSheet) editor.getEditorKit();
        kit.read(new StringReader(textToInsert), document, insertAtCaretPosition);
   	}
	
	private void removeSelectedText() throws Exception
	{
		Caret caret = getEditorField().getCaret();
		int start = Math.min(caret.getDot(), caret.getMark());
        int end = Math.max(caret.getDot(), caret.getMark());
        int length = end - start;
        
        HTMLDocument document = (HTMLDocument)getEditorField().getDocument();
		document.remove(start, length);
	}

	private DataFlavor selectBestDataFlavor(final DataFlavor[] transferDataFlavors)
	{
		DataFlavor dataFlavor = findDataFlavor(transferDataFlavors, HTML_MIME_TYPE);
		if (dataFlavor != null)
			return dataFlavor;

		dataFlavor = findDataFlavor(transferDataFlavors, PLAIN_TEXT_MIME_TYPE);
		if (dataFlavor != null)
			return dataFlavor;

		return DataFlavor.selectBestTextFlavor(transferDataFlavors);
	}

	private DataFlavor findDataFlavor(final DataFlavor[] transferDataFlavors, String mimeType)
	{
		for (DataFlavor dataFlavor : transferDataFlavors)
		{		
			if (isMimeType(dataFlavor, mimeType))
				return dataFlavor;
		}
		return null;
	}

	private boolean isMimeType(DataFlavor dataFlavor, String mimeType)
	{
		return dataFlavor.getMimeType().startsWith(mimeType);
	} 

	private String read(Reader inputReader) throws Exception
	{
		BufferedReader bufferedReader = new BufferedReader(inputReader);
		StringBuffer stringBuffer = new StringBuffer();

		try
		{
			int characterRead;
			while((characterRead = bufferedReader.read()) != -1)
			{
				stringBuffer.append((char)characterRead);
			}
		}
		finally
		{
			bufferedReader.close();
		}

		return stringBuffer.toString();
	}
	
	private JTextComponent getEditorField()
	{
		return textField;
	}
	
	private JTextComponent textField;
	private static final String HTML_MIME_TYPE = "text/html";
	private static final String PLAIN_TEXT_MIME_TYPE = "text/plain";
}
