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

import org.miradi.dialogs.planning.propertiesPanel.WorkUnitsTableModel;
import org.miradi.main.EAM;

abstract public class AbstractExpandColumnAction extends AbstractAction
{
	public AbstractExpandColumnAction(WorkUnitsTableModel workUnitsTableModelToUse)
	{
		super(EAM.text("Expand Selected Column"));
		
		workUnitsTableModel = workUnitsTableModelToUse;
	}
	
	public void actionPerformed(ActionEvent event)
	{
		try
		{
			workUnitsTableModel.respondToExpandOrCollapseColumnEvent(getSelectedColumn());
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}

	abstract protected int getSelectedColumn();

	private WorkUnitsTableModel workUnitsTableModel;
}