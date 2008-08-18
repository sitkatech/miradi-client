/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import java.awt.Dimension;

import org.martus.swing.Utilities;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.dialogs.treeRelevancy.StrategyActivityRelevancyTreeTablePanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Objective;
import org.miradi.views.ObjectsDoer;

public class EditStrategyActivityRelevacyListDoer extends ObjectsDoer
{
	@Override
	public boolean isAvailable()
	{
		return (getSelectedObjective() != null);
	}

	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		try
		{
			Objective objective = getSelectedObjective();
			StrategyActivityRelevancyTreeTablePanel panel = StrategyActivityRelevancyTreeTablePanel.createStrategyActivityRelevancyTreeTablePanel(getMainWindow(), objective);
			ModalDialogWithClose dialog = new ModalDialogWithClose(getMainWindow(), panel, EAM.text("Relevant Strategies and Activities"));
			dialog.setPreferredSize(new Dimension(600, 600));
			Utilities.centerDlg(dialog);
			dialog.setVisible(true);
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	private Objective getSelectedObjective()
	{
		BaseObject singleSelected = getSingleSelected(Objective.getObjectType());
		if (singleSelected == null)
			return null;
			
	
		return (Objective) singleSelected;
	}
}
