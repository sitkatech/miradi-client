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
package org.miradi.dialogfields;


import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;

import org.miradi.actions.ActionRedo;
import org.miradi.actions.ActionUndo;
import org.miradi.actions.Actions;
import org.miradi.actions.EAMAction;
import org.miradi.dialogs.fieldComponents.PanelTextArea;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.utils.MenuItemWithoutLocation;
import org.miradi.utils.MiradiResourceImageIcon;
import org.miradi.views.umbrella.CopyTextAction;
import org.miradi.views.umbrella.CutTextAction;
import org.miradi.views.umbrella.PasteTextAction;

public class ObjectTextInputField extends ObjectDataInputField
{
	public ObjectTextInputField(Project projectToUse, int objectType, BaseId objectId, String tag, PanelTextArea componentToUse)
	{
		super(projectToUse, objectType, objectId, tag);
		field = componentToUse;
		addFocusListener();
		setEditable(true);
		field.getDocument().addDocumentListener(new DocumentEventHandler());
		field.addMouseListener(new MouseHandler(getActions(), field));
		field.addKeyListener(new KeyHandler());
		
		setDefaultFieldBorder();
	}	

	public JComponent getComponent()
	{
		return field;
	}

	public String getText()
	{
		return field.getText();
	}

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

	public void updateEditableState()
	{
		boolean editable = allowEdits() && isValidObject();
		field.setEditable(editable);
		Color fg = EAM.EDITABLE_FOREGROUND_COLOR;
		Color bg = EAM.EDITABLE_BACKGROUND_COLOR;
		if(!editable)
		{
			fg = EAM.READONLY_FOREGROUND_COLOR;
			bg = EAM.READONLY_BACKGROUND_COLOR;
		}
		field.setForeground(fg);
		field.setBackground(bg);
	}

	public void focusGained(FocusEvent e)
	{
		super.focusGained(e);
		field.setSelectionStart(0);
		field.setSelectionEnd(field.getSize().width);
	}
	
	private Actions getActions()
	{
		return EAM.getMainWindow().getActions();
	}

	private EAMAction getUndoAction()
	{
		return getActions().get(ActionUndo.class);
	}
	
	private EAMAction getRedoAction()
	{
		return getActions().get(ActionRedo.class);
	}

	public class KeyHandler extends KeyAdapter
	{
		public void keyTyped(KeyEvent event)
		{
			try
			{
				char keyChar = event.getKeyChar();
				if(keyChar == ctrl('Z'))
				{
					ObjectDataInputField.saveFocusedFieldPendingEdits();
					getUndoAction().doAction();
				}
				if(keyChar == ctrl('Y'))
				{
					ObjectDataInputField.saveFocusedFieldPendingEdits();
					getRedoAction().doAction();
				}
			}
			catch(CommandFailedException e)
			{
				EAM.errorDialog(EAM.text("An unexpected error prevented that operation"));
			}
		}

		private int ctrl(char letter)
		{
			return Character.toUpperCase(letter) - '@';
		}
		
	}
	
	public class MouseHandler extends MouseAdapter
	{
		public MouseHandler(Actions actionsToUse, JTextComponent textFieldToUse)
		{
			actions = actionsToUse;
		}
		
		public void mousePressed(MouseEvent e)
		{
			if(e.isPopupTrigger())
				fireRightClick(e);
		}

		public void mouseReleased(MouseEvent e)
		{
			if(e.isPopupTrigger())
				fireRightClick(e);
		}
		
		void fireRightClick(MouseEvent e)
		{
			getRightClickMenu().show(getTextField(), e.getX(), e.getY());
		}
		
		public JPopupMenu getRightClickMenu()
		{
			JPopupMenu menu = new JPopupMenu();
			
			Action undoAction = getUndoAction();
			MenuItemWithoutLocation menuItemUndo = new MenuItemWithoutLocation(undoAction);
			menuItemUndo.setAccelerator(KeyStroke.getKeyStroke('Z', KeyEvent.CTRL_DOWN_MASK));
			menu.add(menuItemUndo);
			
			Action redoAction = getRedoAction();
			MenuItemWithoutLocation menuItemRedo = new MenuItemWithoutLocation(redoAction);
			menuItemRedo.setAccelerator(KeyStroke.getKeyStroke('Y', KeyEvent.CTRL_DOWN_MASK));
			menu.add(menuItemRedo);
			
			menu.addSeparator();
			
			JMenuItem menuItemCut = createMenuItem(new CutTextAction(getTextField()), "icons/cut.gif");
			menuItemCut.setText(EAM.text("Cut"));
			menuItemCut.setAccelerator(KeyStroke.getKeyStroke('X', KeyEvent.CTRL_DOWN_MASK));
			menu.add(menuItemCut);
			
			JMenuItem menuItemCopy = createMenuItem(new CopyTextAction(getTextField()), "icons/copy.gif");
			menuItemCopy.setText(EAM.text("Copy"));
			menuItemCopy.setAccelerator(KeyStroke.getKeyStroke('C', KeyEvent.CTRL_DOWN_MASK));
			menu.add(menuItemCopy);
		
			JMenuItem menuItemPaste = createMenuItem(new PasteTextAction(getTextField()), "icons/paste.gif");
			menuItemPaste.setText(EAM.text("Paste"));
			menuItemPaste.setAccelerator(KeyStroke.getKeyStroke('V', KeyEvent.CTRL_DOWN_MASK));
			menu.add(menuItemPaste);
			
			return menu;
		}
		
		private JMenuItem createMenuItem(Action action, String iconLocation)
		{
			JMenuItem menuItem = new JMenuItem(action);
			MiradiResourceImageIcon icon = new MiradiResourceImageIcon(iconLocation);
			menuItem.setIcon(icon);
			
			return menuItem;
		}
		
		private EAMAction getUndoAction()
		{
			return getActions().get(ActionUndo.class);
		}
		
		private EAMAction getRedoAction()
		{
			return getActions().get(ActionRedo.class);
		}
		
		private Actions getActions()
		{
			return actions;
		}
		
		public JTextComponent getTextField()
		{
			return textField;
		}
		
		private JTextComponent textField;
		private Actions actions;
	}
	
	JTextComponent field;
}
