/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;


import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;

import org.conservationmeasures.eam.actions.ActionRedo;
import org.conservationmeasures.eam.actions.ActionUndo;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTextArea;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.MenuItemWithoutLocation;
import org.conservationmeasures.eam.utils.MiradiResourceImageIcon;
import org.conservationmeasures.eam.views.umbrella.CopyTextAction;
import org.conservationmeasures.eam.views.umbrella.CutTextAction;
import org.conservationmeasures.eam.views.umbrella.PasteTextAction;

public class ObjectTextInputField extends ObjectDataInputField
{
	public ObjectTextInputField(Project projectToUse, int objectType, BaseId objectId, String tag, PanelTextArea componentToUse)
	{
		super(projectToUse, objectType, objectId, tag);
		field = componentToUse;
		addFocusListener();
		setEditable(true);
		field.getDocument().addDocumentListener(new DocumentEventHandler());
		field.addMouseListener(new MouseHandler());
		
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
	
	class DocumentEventHandler implements DocumentListener
	{
		public void changedUpdate(DocumentEvent arg0)
		{
			setNeedsSave();
		}

		public void insertUpdate(DocumentEvent arg0)
		{
			setNeedsSave();
		}

		public void removeUpdate(DocumentEvent arg0)
		{
			setNeedsSave();
		}
	}

	public class MouseHandler extends MouseAdapter
	{
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
			getRightClickMenu().show(field, e.getX(), e.getY());
		}
		
		public JPopupMenu getRightClickMenu()
		{
			JPopupMenu menu = new JPopupMenu();
			
			JMenuItem menuItemCopy = createMenuItem(new CopyTextAction(field), "icons/copy.gif");
			menuItemCopy.setText(EAM.text("Copy"));
			menu.add(menuItemCopy);
		
			JMenuItem menuItemCut = createMenuItem(new CutTextAction(field), "icons/cut.gif");
			menuItemCut.setText(EAM.text("Cut"));
			menu.add(menuItemCut);
			
			JMenuItem menuItemPaste = createMenuItem(new PasteTextAction(field), "icons/paste.gif");
			menuItemPaste.setText(EAM.text("Paste"));
			menu.add(menuItemPaste);
			
			menu.addSeparator();
			
			Action undoAction = EAM.getMainWindow().getActions().get(ActionUndo.class);
			menu.add(new MenuItemWithoutLocation(undoAction));
			
			Action redoAction = EAM.getMainWindow().getActions().get(ActionRedo.class);
			menu.add(new MenuItemWithoutLocation(redoAction));
			
			return menu;
		}
		
		private JMenuItem createMenuItem(Action action, String iconLocation)
		{
			JMenuItem menuItem = new JMenuItem(action);
			MiradiResourceImageIcon icon = new MiradiResourceImageIcon(iconLocation);
			menuItem.setIcon(icon);
			
			return menuItem;
		}
	}
	
	JTextComponent field;
}
