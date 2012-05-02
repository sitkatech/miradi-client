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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.JEditorPane;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;

import org.miradi.main.EAM;

public class TextPaneUndoRedoHandler
{
	public TextPaneUndoRedoHandler(JEditorPane textFieldToUse)
	{
		textField = textFieldToUse;
		undo = new UndoManager();
	}
	
	public void applyUndoRedoActions()
	{
		Document doc = getTextField().getDocument();
		doc.addUndoableEditListener(new UndoEditHandler());
		
		getTextField().getActionMap().put(UNDO_TEXT, new UndoHandler());
		getTextField().getInputMap().put(KeyStroke.getKeyStroke("control Z"), UNDO_TEXT);
		
		getTextField().getActionMap().put(REDO_TEXT,new RedoHandler());
		getTextField().getInputMap().put(KeyStroke.getKeyStroke("control Y"), REDO_TEXT);
	}

	private JEditorPane getTextField()
	{
		return textField;
	}
	
	private class UndoEditHandler implements UndoableEditListener
	{
	    public void undoableEditHappened(UndoableEditEvent evt) 
	    {
	        undo.addEdit(evt.getEdit());
	    }
	}
	
	private class UndoHandler extends AbstractAction implements ActionListener
	{
		public UndoHandler()
		{
			super(UNDO_TEXT);
		}
		
		public void actionPerformed(ActionEvent evt) 
		{
            try 
            {
                if (undo.canUndo()) 
                {
                    undo.undo();
                }
            } 
            catch (CannotRedoException e) 
            {
            	EAM.logException(e);
            	EAM.unexpectedErrorDialog(e);
            }
		}
	}

	private class RedoHandler extends AbstractAction implements ActionListener
	{
		public RedoHandler()
		{
			super(REDO_TEXT);
		}
		
		public void actionPerformed(ActionEvent evt) 
		{
            try 
            {
            	if (undo.canRedo()) 
            	{
            		undo.redo();
            	}
            } 
            catch (CannotRedoException e) 
            {
            	EAM.logException(e);
            	EAM.unexpectedErrorDialog(e);
            }
		}
	}
	
	private static final String UNDO_TEXT = "Undo";
	private static final String REDO_TEXT = "Redo";
	
	private JEditorPane textField;
	private UndoManager undo;
}
