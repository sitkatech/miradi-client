/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.EventObject;
import java.util.HashMap;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.utils.HtmlFormViewer;


public class WizardStep extends SkeletonWizardStep implements MouseListener
{
	public WizardStep(WizardPanel wizardToUse)
	{
		super(wizardToUse);

		htmlViewer = new WizardHtmlViewer(this);
		JScrollPane scrollPane = new JScrollPane(htmlViewer);
		add(scrollPane);
		htmlViewer.addMouseListener(this);
	}

	public void refresh() throws Exception
	{
		String htmlText = getText();
		htmlViewer.setText(htmlText);
		invalidate();
		validate();
	}
	
	public void linkClicked(String linkDescription)
	{
		if (linkDescription.startsWith("Definition:"))
		{
			Definition def = DefinitionCommonTerms.getDefintion(linkDescription);
			EAM.okDialog(def.term, new String[] {def.definition});
		}
	}
	
	public void valueChanged(String name, String value)
	{
		nameToValueMap.put(name, value);
	}
	
	public String getValue(String name)
	{
		if (nameToValueMap.containsKey(name))
			return (String)nameToValueMap.get(name);
		return "";
	}



	public JPopupMenu getRightClickMenu(Actions actions)
	{
		JPopupMenu menu = new JPopupMenu();
		
		JMenuItem menuItemCopy = new JMenuItem(new EditorActionCopy(getWizard().getMainWindow(),htmlViewer));
		menu.add(menuItemCopy);
		
		JMenuItem menuItemCut = new JMenuItem(actions.get(ActionCut.class));
		menuItemCut.setEnabled(false);
		menu.add(menuItemCut);
		
		JMenuItem menuItemPaste = new JMenuItem(actions.get(ActionPaste.class));
		menuItemPaste.setEnabled(false);
		menu.add(menuItemPaste);
		
		JMenuItem menuItemDelete = new JMenuItem(actions.get(ActionDelete.class));
		menuItemDelete.setEnabled(false);
		menu.add(menuItemDelete);
		
		return menu;
	}
	
	public void mouseClicked(MouseEvent e)
	{
	}

	public void mouseEntered(MouseEvent e)
	{
	}

	public void mouseExited(MouseEvent e)
	{
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
		getRightClickMenu(getWizard().getMainWindow().getActions()).show(this, e.getX(), e.getY());
	}
	
	class EditorActionCopy extends ActionCopy
	{
		public EditorActionCopy(MainWindow mainWindow, HtmlFormViewer htmlViewerToUse)
		{
			super(mainWindow);
			htmlViewer = htmlViewerToUse;
		}
		
		public void doAction(EventObject event) throws CommandFailedException
		{
			htmlViewer.copy();
		}
		
		private HtmlFormViewer htmlViewer;
	}
	
	HashMap nameToValueMap = new HashMap();
	private HtmlFormViewer htmlViewer;


}
