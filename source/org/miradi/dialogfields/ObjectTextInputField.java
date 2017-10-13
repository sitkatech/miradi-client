/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

import javax.swing.JComponent;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.miradi.actions.Actions;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

import com.inet.jortho.SpellChecker;

abstract public class ObjectTextInputField extends ObjectDataInputField
{
	public ObjectTextInputField(MainWindow mainWindowToUse, ORef refToUse, String tag, JTextComponent componentToUse) throws Exception
	{
		this(mainWindowToUse, refToUse, tag, componentToUse, componentToUse.getDocument());
	}
	
	public ObjectTextInputField(MainWindow mainWindowToUse, ORef refToUse, String tag, JTextComponent componentToUse, Document document) throws Exception
	{
		super(mainWindowToUse.getProject(), refToUse, tag);
		
		field = componentToUse;
		field.setDocument(document);
		addFocusListener();
		setEditable(true);
		final DocumentEventHandler saveListenerToUse = createDocumentEventHandler();
		addSaverListener(saveListenerToUse);
		setSaveListener(saveListenerToUse);
		createRightClickMouseHandler();
		setDefaultFieldBorder();
	}

	public void addSaverListener(final DocumentEventHandler saveListenerToUse)
	{
		getTextField().getDocument().addDocumentListener(saveListenerToUse);
	}
	
	public void removeSaveListener(final DocumentEventHandler saveListener)
	{
		getTextField().getDocument().removeDocumentListener(saveListener);
	}

	protected void setSaveListener(DocumentEventHandler saveListenerToUse)  throws Exception
	{
	}
	
	protected void createRightClickMouseHandler()
	{
		new TextAreaRightClickMouseHandler(getActions(), field);
	}	

	@Override
	public JComponent getComponent()
	{
		return field;
	}

	@Override
	public String getText()
	{
		return field.getText();
	}

	@Override
	public void setText(String newValue)
	{
		setTextWithoutScrollingToMakeFieldVisible(newValue);
		clearNeedsSave();
	}
	
	private void setTextWithoutScrollingToMakeFieldVisible(String newValue)
	{
		DefaultCaret caret = (DefaultCaret)field.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		field.setText(newValue);
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}

	@Override
	public void updateEditableState()
	{
		super.updateEditableState();

		if(EAM.getMainWindow().isSpellCheckerActive())
			enableSpellChecker();
		else
			disableSpellChecker();
	}
	
	@Override
	protected boolean shouldBeEditable()
	{
		return allowEdits() && isValidObject();
	}

	private void disableSpellChecker()
	{
		SpellChecker.unregister(getTextField());
	}

	private void enableSpellChecker()
	{
		disableSpellChecker();
		SpellChecker.register(getTextField(), false, false, true);
	}

	protected Actions getActions()
	{
		return EAM.getMainWindow().getActions();
	}
	
	protected JTextComponent getTextField()
	{
		return field;
	}

	protected void selectAll()
	{
		field.setSelectionStart(0);
		field.setSelectionEnd(field.getSize().width);
	}
	
	@Override
	public void focusGained(FocusEvent e)
	{
		super.focusGained(e);
		UndoRedoKeyHandler.enableUndoAndRedo(EAM.getMainWindow(), field);
	}
	
	@Override
	public void focusLost(FocusEvent e)
	{
		isTemporaryFocusLoss = e.isTemporary();
		super.focusLost(e);
	}
	
	protected boolean wasFocusLostTemporarily()
	{
		return isTemporaryFocusLoss;
	}
	
	private DocumentEventHandler createDocumentEventHandler()
	{
		return new DocumentEventHandler(this);
	}

	private boolean isTemporaryFocusLoss;
	private JTextComponent field;
}
