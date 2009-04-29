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
package org.miradi.dialogs.planning.propertiesPanel;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.miradi.actions.ActionAssignResource;
import org.miradi.actions.ActionRemoveAssignment;
import org.miradi.main.EAM;
import org.miradi.main.EAMenuItem;
import org.miradi.utils.AbstractTableRightClickHandler;

class PlanningRightClickHandler extends AbstractTableRightClickHandler
{
	public PlanningRightClickHandler(WorkUnitsTable workUnitsTableToUse)
	{
		super(workUnitsTableToUse.getMainWindow(), workUnitsTableToUse);
		
		workUnitsTable = workUnitsTableToUse;
		expandAction = new ExpandAction();
		collapseAction = new CollapseAction();
	}
	
	@Override
	protected void populateMenu(JPopupMenu popupMenu)
	{
		if (!this.workUnitsTable.isDayColumnSelected())
			addColpseExpandColumnMenuItems(popupMenu);
		
		popupMenu.addSeparator();
		popupMenu.add(new EAMenuItem(getActions().get(ActionAssignResource.class)));
		popupMenu.add(new EAMenuItem(getActions().get(ActionRemoveAssignment.class)));
	}

	private void addColpseExpandColumnMenuItems(JPopupMenu popupMenu)
	{
		if (this.workUnitsTable.isSelectedDateUnitColumnExpanded())
			popupMenu.add(new JMenuItem(collapseAction));
		else
			popupMenu.add(new JMenuItem(expandAction));
	}
	
	private WorkUnitsTable getWorkUnitsTable()
	{
		return workUnitsTable;
	}
	
	class ExpandAction extends AbstractAction
	{
		public ExpandAction()
		{
			super(EAM.text("Expand Selected Column"));
		}
		
		public void actionPerformed(ActionEvent event)
		{
			try
			{
				getWorkUnitsTable().respondToExpandOrCollapseColumnEvent();
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}	
	}
	
	class CollapseAction extends AbstractAction
	{
		public CollapseAction()
		{
			super(EAM.text("Collapse Selected Column"));
		}
		
		public void actionPerformed(ActionEvent event)
		{
			try
			{
				getWorkUnitsTable().respondToExpandOrCollapseColumnEvent();
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}	
	}
	
	private Action expandAction;
	private Action collapseAction;
	private WorkUnitsTable workUnitsTable;
}