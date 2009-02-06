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

import java.awt.Dimension;

import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.treeRelevancy.StrategyActivityRelevancyTreeTablePanel;
import org.miradi.main.EAM;
import org.miradi.objects.Desire;

abstract public class AbstractStrategyActivityRelevancyListDoer extends AbstractEditListDoer
{
	@Override
	protected Dimension getDialogPreferredSize()
	{
		return new Dimension(600, 600);
	}
	
	@Override
	protected String getDialogTitle()
	{
		return EAM.text("Relevant Strategies and Activities");
	}

	@Override
	protected DisposablePanel createEditPanel() throws Exception
	{
		Desire desire = (Desire) getSelectedObject();
		return StrategyActivityRelevancyTreeTablePanel.createStrategyActivityRelevancyTreeTablePanel(getMainWindow(), desire);
	}
}
