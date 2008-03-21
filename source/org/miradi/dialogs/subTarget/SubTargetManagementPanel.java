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
package org.miradi.dialogs.subTarget;

import javax.swing.Icon;

import org.miradi.actions.Actions;
import org.miradi.actions.jump.ActionJumpDiagramWizardReviewAndModifyTargetsStep;
import org.miradi.dialogs.base.ObjectListManagementPanel;
import org.miradi.icons.SubTargetIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.utils.SplitterPositionSaverAndGetter;

public class SubTargetManagementPanel extends ObjectListManagementPanel
{
	public SubTargetManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, ORef nodeRef, Actions actions) throws Exception
	{
		super(splitPositionSaverToUse, new SubTargetListTablePanel(projectToUse, actions, nodeRef), new SubTargetPropertiesPanel(projectToUse));
	}

	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public Icon getIcon()
	{
		return new SubTargetIcon();
	}
	
	@Override
	public Class getJumpActionClass()
	{
		// TODO Auto-generated method stub
		return ActionJumpDiagramWizardReviewAndModifyTargetsStep.class;
	}

	
	private static String PANEL_DESCRIPTION = EAM.text("Tab|Nested Targets"); 
}
