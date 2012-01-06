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
import org.miradi.dialogfields.TextAreaHtmlContextMenuListener;
import org.miradi.dialogfields.TextAreaRightClickMouseHandler;

public class HtmlTextEditingHandler extends TextAreaRightClickMouseHandler
{
	public HtmlTextEditingHandler(Actions actionsToUse, JTextComponent editPaneToUse)
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
	}
	
	@Override
	protected TextAreaHtmlContextMenuListener createTextAreaContextMenuListener(JPopupMenu menu)
	{
		return new TextAreaHtmlContextMenuListener(menu, getTextField(), getActions(), actionList);
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
	
	private ActionList actionList;
}
