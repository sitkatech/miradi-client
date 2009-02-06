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
package org.miradi.dialogs.groupboxLink;

import java.awt.BorderLayout;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.dialogs.base.ObjectListTablePanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.FactorLink;

public class GroupBoxLinkListTablePanel extends ObjectListTablePanel
{
	public GroupBoxLinkListTablePanel(MainWindow mainWindowToUse, GroupBoxLinkTableModel model)
	{
		super(mainWindowToUse, new GroupBoxLinkListTable(mainWindowToUse, model));
		
		groupBoxLinkpropertiesPanel = new GroupBoxLinkPropertiesPanel(mainWindowToUse.getProject());
		add(groupBoxLinkpropertiesPanel,BorderLayout.BEFORE_FIRST_LINE);
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		
		groupBoxLinkpropertiesPanel.dispose();
	}
		
	@Override
	public void setPropertiesPanel(AbstractObjectDataInputPanel panel)
	{
		super.setPropertiesPanel(panel);
		
		ORef diagramLinkRef = panel.getRefForType(DiagramLink.getObjectType());
		groupBoxLinkpropertiesPanel.setObjectRef(diagramLinkRef);
	}
	
	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		if (event.isSetDataCommandWithThisTypeAndTag(FactorLink.getObjectType(), FactorLink.BIDIRECTIONAL_LINK))
			updateSiblings((CommandSetObjectData) event.getCommand());
	}

	private void updateSiblings(CommandSetObjectData command)
	{
	}

	private GroupBoxLinkPropertiesPanel groupBoxLinkpropertiesPanel;
}
