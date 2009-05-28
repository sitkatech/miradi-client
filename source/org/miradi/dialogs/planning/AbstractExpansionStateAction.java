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
package org.miradi.dialogs.planning;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.miradi.dialogs.planning.propertiesPanel.AssignmentDateUnitsTableModel;
import org.miradi.main.EAM;

abstract public class AbstractExpansionStateAction extends AbstractAction
{
	public AbstractExpansionStateAction(String actionLabel, Icon icon)
	{
		super(actionLabel, icon);
	}
	
	public void actionPerformed(ActionEvent event)
	{
		try
		{
			getModelForSelectedColumn().respondToExpandOrCollapseColumnEvent(getSelectedColumn());
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
	
	abstract protected int getSelectedColumn();
	
	abstract protected AssignmentDateUnitsTableModel getModelForSelectedColumn();
}