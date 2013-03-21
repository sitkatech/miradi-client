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

import javax.swing.AbstractAction;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.html.HTMLDocument;

import net.atlanticbb.tantlinger.ui.text.CompoundUndoManager;

import org.miradi.main.EAM;
import org.miradi.utils.AbstractHtmlPane;

public class PasteTextAction extends AbstractAction
{
	public PasteTextAction(JTextComponent fieldToUse)
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
			insertHtmlAfterElementFoundtAtCaretPosition(clipboardValue);
			CompoundUndoManager.endCompoundEdit(getEditorField().getDocument());
		}
		catch(Exception exception)
		{
			EAM.alertUserOfNonFatalException(exception);
		}
	}

	private void insertHtmlAfterElementFoundtAtCaretPosition(String html) throws Exception
	{
		HTMLDocument document = (HTMLDocument)getEditorField().getDocument();
		html = AbstractHtmlPane.getNormalizedAndSanitizedHtmlText(html);
		final int caretPostion = getEditorField().getCaretPosition();
		Element elementAtCaretPosition = document.getCharacterElement(caretPostion);
		document.insertAfterEnd(elementAtCaretPosition, html);
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
