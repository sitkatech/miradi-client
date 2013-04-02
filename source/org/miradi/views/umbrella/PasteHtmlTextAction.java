/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTMLDocument;

import net.atlanticbb.tantlinger.ui.text.CompoundUndoManager;

import org.miradi.main.EAM;
import org.miradi.utils.AbstractHtmlPane;
import org.miradi.utils.AbstractHtmlPane.HtmlEditorKitWithNonSharedStyleSheet;

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
			String clipboardValue = getClipboardContent();
			if(clipboardValue == null)
				return;

			CompoundUndoManager.beginCompoundEdit(getEditorField().getDocument());
			replaceNormalizedHtmlAtCaretPosition(clipboardValue);
			CompoundUndoManager.endCompoundEdit(getEditorField().getDocument());
		}
		catch(Exception exception)
		{
			EAM.alertUserOfNonFatalException(exception);
		}
	}

	private void replaceNormalizedHtmlAtCaretPosition(String html) throws Exception
    {
		removeSelectedText();
        JEditorPane editor = (JEditorPane) getEditorField();
        HTMLDocument document = (HTMLDocument)editor.getDocument();
        html = AbstractHtmlPane.getNormalizedAndSanitizedHtmlText(html);
        final int insertAtCaretPostion = editor.getCaretPosition();
        Element elementAtCaretPosition = document.getCharacterElement(insertAtCaretPostion);
        int elementStartsAt = elementAtCaretPosition.getStartOffset();

        HtmlEditorKitWithNonSharedStyleSheet kit = (HtmlEditorKitWithNonSharedStyleSheet) editor.getEditorKit();
        kit.read(new StringReader(html), document, insertAtCaretPostion);
        int endingCaretPosition = editor.getCaretPosition();
        
        // NOTE: Reload text to merge the pasted implied-p into the surrounding implied-p
        // to avoid showing newlines in the editor panel
        String result = editor.getText();
        editor.setText(result);
        
        // NOTE: Reset the cursor position, but account for the newline(s) that were removed
        if(elementStartsAt == insertAtCaretPostion)
            endingCaretPosition -= 1;
        else 
            endingCaretPosition -= 2;
        
        if(endingCaretPosition >= document.getLength() - 1)
            endingCaretPosition = document.getLength() - 2;
        
        editor.setCaretPosition(endingCaretPosition);
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

	private String getClipboardContent() throws Exception
	{
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(null);
		if(contents == null)
			return null;                

		final DataFlavor[] transferDataFlavors = contents.getTransferDataFlavors();
		DataFlavor dataFlavor = selectBestDataFlavor(transferDataFlavors);        

		return read(dataFlavor.getReaderForText(contents));
	}
	
	private DataFlavor selectBestDataFlavor(final DataFlavor[] transferDataFlavors)
	{
		DataFlavor dataFlavor = findDataFlavor(transferDataFlavors, "text/html");
		if (dataFlavor != null)
			return dataFlavor;

		dataFlavor = findDataFlavor(transferDataFlavors, "text/plain");
		if (dataFlavor != null)
			return dataFlavor;

		return DataFlavor.selectBestTextFlavor(transferDataFlavors);		
	}

	private DataFlavor findDataFlavor(final DataFlavor[] transferDataFlavors, String mimeType)
	{
		for (DataFlavor dataFlavor : transferDataFlavors)
		{		
			if (dataFlavor.getMimeType().startsWith(mimeType))
				return dataFlavor;
		}
		return null;
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
}
