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
import net.atlanticbb.tantlinger.ui.text.actions.HTMLBlockAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLInlineAction;
import net.atlanticbb.tantlinger.ui.text.actions.HTMLLinkAction;
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
		actionList.add(createAction(HTMLInlineAction.BOLD, EAM.text("Bold")));
		actionList.add(createAction(HTMLInlineAction.ITALIC, EAM.text("Italic")));
		actionList.add(createAction(HTMLInlineAction.UNDERLINE, EAM.text("Underline")));
		actionList.add(createAction(HTMLInlineAction.STRIKE, EAM.text("Strike")));
		actionList.add(null);
        actionList.add(createBlockAction(HTMLBlockAction.UL, EAM.text("Unordered List")));
        actionList.add(createBlockAction(HTMLBlockAction.OL, EAM.text("Ordered List")));
		actionList.add(null);
		
		final HTMLLinkAction insertHyperlinkAction = new HTMLLinkAction();
		insertHyperlinkAction.setActionName(EAM.text("Insert Hyperlink"));
		actionList.add(insertHyperlinkAction);
		actionList.add(new HyperlinkOpenInBrowserAction());
	}

	private HTMLBlockAction createBlockAction(final int ol, String name)
	{
		final HTMLBlockAction action = new HTMLBlockAction(ol);
		action.setActionName(name);
		
		return action;
	}

	private HTMLInlineAction createAction(final int bold, String name)
	{
		final HTMLInlineAction action = new HTMLInlineAction(bold);
		action.setActionName(name);
		
		return action;
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
			EditableHtmlPane pane = (EditableHtmlPane) editor;
			pane.handleOpenLink(getInvocationLocation());
		}

		@Override
		protected void sourceEditPerformed(ActionEvent e, JEditorPane editor)
		{
		}
	}

	private ActionList actionList;
}
