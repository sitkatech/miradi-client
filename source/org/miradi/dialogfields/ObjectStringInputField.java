/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogfields;

import java.awt.event.FocusEvent;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.martus.swing.UiTextArea;
import org.miradi.dialogs.fieldComponents.PanelTextArea;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.utils.StringUtilities;
import org.miradi.utils.XmlUtilities2;

public class ObjectStringInputField extends ObjectTextInputField
{
	public ObjectStringInputField(MainWindow mainWindowToUse, ORef refToUse, String tagToUse, int columnsToUse) throws Exception
	{
		this(mainWindowToUse, refToUse, tagToUse, new PanelTextArea(0, columnsToUse));		
	}
	
	public ObjectStringInputField(MainWindow mainWindowToUse, ORef refToUse, String tagToUse, int columnsToUse, Document document) throws Exception
	{
		this(mainWindowToUse, refToUse, tagToUse, new PanelTextArea(0, columnsToUse), document);
	}
	
	private ObjectStringInputField(MainWindow mainWindowToUse, ORef refToUse, String tagToUse, PanelTextArea componentToUse) throws Exception
	{
		this(mainWindowToUse, refToUse, tagToUse, componentToUse, componentToUse.getDocument());
	}

	private ObjectStringInputField(MainWindow mainWindowToUse, ORef refToUse, String tagToUse, PanelTextArea componentToUse, Document document) throws Exception
	{
		super(mainWindowToUse, refToUse, tagToUse, componentToUse, document);
		
		UndoableEditHandler handler = new UndoableEditHandler();
		((JTextComponent)getComponent()).getDocument().addUndoableEditListener(handler);
		((UiTextArea)getComponent()).setWrapStyleWord(true);
		((UiTextArea)getComponent()).setLineWrap(true);		
	}

	@Override
	public void setText(String newValue)
	{
		//FIXME - medium - the line below is not doing anything, since its not setting newValue
		newValue.replaceAll(StringUtilities.NEW_LINE, StringUtilities.EMPTY_SPACE);
		newValue = XmlUtilities2.getXmlDecoded(newValue);
		
		super.setText(newValue);
	}
	
	@Override
	public String getText()
	{
		String text = super.getText();
		text = XmlUtilities2.getXmlEncoded(text);
		
		return text;
	}

	@Override
	public void focusGained(FocusEvent e)
	{
		super.focusGained(e);
		
		if (!wasFocusLostTemporarily())
			selectAll();
	}

	//FIXME medium - this handler is here to remove new lines entered by the user.
	// this mechanism should be replaced with a Document that does not allow new lines.
	private class UndoableEditHandler implements UndoableEditListener
	{
		public void undoableEditHappened(UndoableEditEvent e)
		{
			Document document = (Document)e.getSource();
			try
			{
				if (document.getLength()==0)
					return;
			
				String text = document.getText(0, document.getLength());
				int index = text.indexOf('\n');
				if (index>=0)
				{
					e.getEdit().undo();
				}
			}
			catch(BadLocationException e1)
			{
				EAM.logException(e1);
			}
		}
	}
}

