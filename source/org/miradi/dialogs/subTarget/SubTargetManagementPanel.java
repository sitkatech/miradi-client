/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
