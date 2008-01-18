/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.main.menu;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.EAMenuItem;

public abstract class MiradiMenu extends JMenu
{
	public MiradiMenu(String text, Actions actionsToUse)
	{
		super(text);
		actions = actionsToUse;
	}
	
	void addMenuItem(Class actionClass, int mnemonic)
	{
		EAMenuItem menuItemNewProject = new EAMenuItem(actions.get(actionClass), mnemonic);
		add(menuItemNewProject);
	}
	
	void addDisabledMenuItem(String label)
	{
		JMenuItem item = new JMenuItem(label);
		item.setEnabled(false);
		add(item);
	}
	
	Actions actions;
}
