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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JEditorPane;
import javax.swing.JPopupMenu;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;

import net.atlanticbb.tantlinger.ui.text.CompoundUndoManager;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLEditorActionFactory;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLInlineAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLTextEditAction;

import org.bushe.swing.action.ActionList;
import org.miradi.actions.Actions;
import org.miradi.dialogfields.HtmlEditorContextMenuListener;
import org.miradi.dialogfields.TextAreaRightClickMouseHandler;
import org.miradi.main.EAM;

public class HtmlEditorRightClickMouseHandler extends TextAreaRightClickMouseHandler
{
	public HtmlEditorRightClickMouseHandler(Actions actionsToUse, JTextComponent editPaneToUse)
	{
		super(actionsToUse, editPaneToUse);
		
		createEditorActions();

		getTextField().addCaretListener(new CaretHandler());
		getTextField().addFocusListener(new FocusHandler());
	}
	
	private void createEditorActions()
	{        
		actionList = new ActionList("style");
		actionList.add(new HTMLInlineAction(HTMLInlineAction.BOLD));
		actionList.add(new HTMLInlineAction(HTMLInlineAction.ITALIC));
		actionList.add(new HTMLInlineAction(HTMLInlineAction.UNDERLINE));
		actionList.add(new HTMLInlineAction(HTMLInlineAction.STRIKE));
		actionList.add(null);
		actionList.addAll(HTMLEditorActionFactory.createListElementActionList());
		actionList.add(null);
		actionList.add(new HyperlinkOpenInBrowserAction());
	}
	
	@Override
	protected HtmlEditorContextMenuListener createTextAreaContextMenuListener(JPopupMenu menu)
	{
		return new HtmlEditorContextMenuListener(menu, getTextField(), getActions(), actionList);
	}
	
	private void updateState()
	{
		actionList.putContextValueForAll(HTMLTextEditAction.EDITOR, getTextField());
		actionList.updateEnabledForAll();
	}
	
	private class CaretHandler implements CaretListener
	{
		public void caretUpdate(CaretEvent e)
		{            
			updateState();
		}        
	}

	private class FocusHandler implements FocusListener
	{
		public void focusGained(FocusEvent e)
		{
			JEditorPane ed = (JEditorPane)e.getComponent();
			CompoundUndoManager.updateUndo(ed.getDocument());

			updateState();
		}

		public void focusLost(FocusEvent e)
		{
		}
	}
	
	private class HyperlinkOpenInBrowserAction extends HTMLTextEditAction
	{
		public HyperlinkOpenInBrowserAction()
		{
			super(EAM.text("Open hyperlink"));
		}

		@Override
		protected void wysiwygEditPerformed(ActionEvent e, JEditorPane editor)
		{
			final int caretPosition = editor.getCaretPosition();
			final String hyperlink = StringUtilities.getToken(editor.getText(), caretPosition);
			EAM.getMainWindow().mainLinkFunction(hyperlink);
		}

		@Override
		protected void sourceEditPerformed(ActionEvent e, JEditorPane editor)
		{
		}
	}
	
	private ActionList actionList;
}
