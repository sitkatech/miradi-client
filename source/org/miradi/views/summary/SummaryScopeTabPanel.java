/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.summary;

import javax.swing.Icon;

import org.miradi.actions.jump.ActionJumpSummaryWizardDefineProjecScope;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.ProjectScopeIcon;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;

public class SummaryScopeTabPanel extends ObjectDataInputPanel
{
	public SummaryScopeTabPanel(Project projectToUse, ORef[] orefsToUse)
	{
		super(projectToUse, orefsToUse);
		setLayout(new OneColumnGridLayout());
		
		ScopeAndVisionPanel scopeVision = new ScopeAndVisionPanel(getProject(), getRefForType(ProjectMetadata.getObjectType()));
		addSubPanelWithTitledBorder(scopeVision);

		BiodiversityPanel biodiversity = new BiodiversityPanel(getProject(), getRefForType(ProjectMetadata.getObjectType()));
		addSubPanelWithTitledBorder(biodiversity);
		
		HumanStakeholderPanel humans = new HumanStakeholderPanel(getProject(), getRefForType(ProjectMetadata.getObjectType()));
		addSubPanelWithTitledBorder(humans);
		
		ProtectedAreaPanel protectedAreas = new ProtectedAreaPanel(getProject(), getSelectedRefs().toArray());
		addSubPanelWithTitledBorder(protectedAreas);

		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Scope");
	}
	
	@Override
	public Icon getIcon()
	{
		return new ProjectScopeIcon();
	}
	
	@Override
	public Class getJumpActionClass()
	{
		return ActionJumpSummaryWizardDefineProjecScope.class;
	}
	

}
