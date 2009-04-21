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
package org.miradi.views.umbrella.doers;

import java.util.EventObject;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.miradi.actions.Actions;
import org.miradi.actions.EAMAction;
import org.miradi.actions.MainWindowAction;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.views.Doer;
import org.miradi.views.ObjectsDoer;

abstract public class AbstractPopDownMenuDoer extends ObjectsDoer
{
	@Override
	public boolean isAvailable()
	{
		if(!super.isAvailable())
			return false;
		
		if(getAvailableActions().size() == 0)
			return false;
		
		return true;
	}
	
	@Override
	public void doIt() throws CommandFailedException
	{
		EAM.logWarning("AbstractMenuDoer called without an event");
	}

	@Override
	public void doIt(EventObject event) throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		JComponent component = (JComponent)event.getSource();
		int x = component.getX();
		int y = component.getY()+component.getHeight();

		JPopupMenu menu = new JPopupMenu();
		populateMenu(menu);
		menu.show(component.getParent(), x, y);
	}
	
	private void populateMenu(JPopupMenu menuToFill)
	{
		menuToFill.removeAll();
		Vector<EAMAction> actions = getAvailableActions();
		for(EAMAction action : actions)
		{
			if(action == null)
				menuToFill.addSeparator();
			else
				menuToFill.add(new JMenuItem(action));
		}
	}
	
	private Vector<EAMAction> getAvailableActions()
	{
		Actions actions = getMainWindow().getActions();
		Vector<EAMAction> available = new Vector<EAMAction>();
		Class[] allPossible = getAllPossibleActionClasses();
		for(Class actionClass : allPossible)
		{
			if(actionClass == null)
			{
				available.add(null);
				continue;
			}

			MainWindowAction action = (MainWindowAction) actions.get(actionClass);
			Doer doer = action.getDoer();
			if(doer.isAvailable())
				available.add(action);
		}
		
		stripExtraSeparators(available);
		return available;
	}

	private void stripExtraSeparators(Vector<EAMAction> actions)
	{
		for(int i = 0; i < actions.size()-1; ++i)
		{
			if(actions.get(i) != null)
				continue;
			
			while(i+1 < actions.size() && actions.get(i+1) == null)
				actions.remove(i+1);
		}

		while(actions.size() > 0 && actions.get(0) == null)
			actions.remove(0);
		
		while(actions.size() > 0 && actions.get(actions.size()-1) == null)
			actions.remove(actions.size()-1);
	}

	abstract protected Class[] getAllPossibleActionClasses();
}
