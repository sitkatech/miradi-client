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

import net.atlanticbb.tantlinger.ui.text.CompoundUndoManager;
import org.miradi.main.EAM;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextPaneUndoRedoHandler
{
	public TextPaneUndoRedoHandler(JEditorPane textFieldToUse)
	{
		textField = textFieldToUse;
		undo = new UndoManager();
	}
	
	public void applyUndoRedoActions()
	{
		Document document = getTextField().getDocument();
		CompoundUndoManager compoundUndoManager = new CompoundUndoManager(document, undo);
		document.addUndoableEditListener(compoundUndoManager);

		getTextField().getActionMap().put(UNDO_TEXT, new UndoHandler());
		getTextField().getInputMap().put(KeyStroke.getKeyStroke("control Z"), UNDO_TEXT);

		getTextField().getActionMap().put(REDO_TEXT,new RedoHandler());
		getTextField().getInputMap().put(KeyStroke.getKeyStroke("control Y"), REDO_TEXT);
	}

	private JEditorPane getTextField()
	{
		return textField;
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
            	EAM.alertUserOfNonFatalException(e);
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
            	EAM.alertUserOfNonFatalException(e);
            }
		}
	}
	
	private static final String UNDO_TEXT = "Undo";
	private static final String REDO_TEXT = "Redo";
	
	private JEditorPane textField;
	private UndoManager undo;
}
