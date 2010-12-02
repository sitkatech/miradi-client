/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.event.PopupMenuEvent;
import javax.swing.text.JTextComponent;

import org.miradi.actions.ActionRedo;
import org.miradi.actions.ActionUndo;
import org.miradi.actions.Actions;
import org.miradi.actions.MiradiAction;
import org.miradi.main.EAM;
import org.miradi.utils.MenuItemWithoutLocation;
import org.miradi.utils.MiradiResourceImageIcon;
import org.miradi.views.umbrella.CopyTextAction;
import org.miradi.views.umbrella.CutTextAction;
import org.miradi.views.umbrella.PasteTextAction;

import com.inet.jortho.AddWordAction;
import com.inet.jortho.MiradiCheckerListener;

/**
 * NOTE: This class is required because of the odd way the jortho 
 * library works. You can't just get a list of suggestions. 
 * Instead, they have a menu listener which detects when the 
 * menu is about to be displayed, and adds the suggestions and 
 * Add Word item to that menu.
 * 
 * We want it formatted a little differently, and we need our 
 * normal context menu items (e.g. cut and undo), so we need 
 * this class.
 */
class TextAreaContextMenuListener extends MiradiCheckerListener
{
	public TextAreaContextMenuListener(JPopupMenu menuToUse, JTextComponent textFieldToUse, Actions actionsToUse)
	{
		super(menuToUse, null);
		menu = menuToUse;
		textField = textFieldToUse;
		actions = actionsToUse;
	}

	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent ev)
	{
		super.popupMenuWillBecomeVisible(ev);

		Component[] menuItems = menu.getComponents();
		boldifySuggestedWords(menuItems);
		extractMoreSuggestionsSubmenu(menuItems);
		
		if(menuItems.length > 0)
			menu.addSeparator();

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
		
	}

	private void extractMoreSuggestionsSubmenu(Component[] menuItems)
	{
		JMenu suggestions = new JMenu(EAM.text("More Suggestions"));
		final int MAX_SUGGESTIONS_AT_TOP_LEVEL = 5;
		boolean hasMoreSuggestions = false;
		for(int i = MAX_SUGGESTIONS_AT_TOP_LEVEL; i < menuItems.length - 1; ++i)
		{
			Component suggestionToMove = menuItems[i];
			menu.remove(suggestionToMove);
			suggestions.add(suggestionToMove);
			hasMoreSuggestions = true;
		}
		if(hasMoreSuggestions)
			menu.insert(suggestions, menu.getComponentCount() - 1);
	}

	private void boldifySuggestedWords(Component[] menuItems)
	{
		int menuItemCountExcludingAddWord = menuItems.length - 1;
		for(int i = 0; i < menuItemCountExcludingAddWord; ++i)
		{
			Font oldFont = menuItems[i].getFont();
			Font newFont = oldFont.deriveFont(Font.BOLD);
			menuItems[i].setFont(newFont);
		}
	}
	
	@Override
	protected void addMenuItemAddToDictionary(JTextComponent jText, String word, boolean addSeparator) 
	{
		String addWordLabel = EAM.substitute(EAM.text("Add '%s' to User Dictionary"), word);
		AddWordAction addWordAction = new AddWordAction(textField, word, addWordLabel);
		menu.add(addWordAction);
	}

	private JMenuItem createMenuItem(Action action, String iconLocation)
	{
		JMenuItem menuItem = new JMenuItem(action);
		MiradiResourceImageIcon icon = new MiradiResourceImageIcon(iconLocation);
		menuItem.setIcon(icon);
		
		return menuItem;
	}
	
	private MiradiAction getUndoAction()
	{
		return getActions().get(ActionUndo.class);
	}
	
	private MiradiAction getRedoAction()
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
	
	private JPopupMenu menu;
	private JTextComponent textField;
	private Actions actions;
}