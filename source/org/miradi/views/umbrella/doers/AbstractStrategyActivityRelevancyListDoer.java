/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.treeRelevancy.StrategyActivityRelevancyManagementPanel;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;

import java.awt.*;

abstract public class AbstractStrategyActivityRelevancyListDoer extends AbstractEditListDoer
{
	@Override
	protected Dimension getDialogPreferredSize()
	{
		return new Dimension(1000, 600);
	}
	
	@Override
	protected String getDialogTitle()
	{
		return EAM.text("Relevant Strategies and Activities");
	}

	@Override
	protected DisposablePanel createEditPanel() throws Exception
	{
		BaseObject selectedObject = getSelectedObject();
		return StrategyActivityRelevancyManagementPanel.createManagementPanel(getMainWindow(), selectedObject);
	}

	@Override
	protected boolean shouldHaveScrollBars()
	{
		return false;
	}
}
