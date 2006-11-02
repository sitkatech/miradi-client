/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;


import org.conservationmeasures.eam.actions.ActionCreateResource;
import org.conservationmeasures.eam.actions.ActionDeleteResource;
import org.conservationmeasures.eam.actions.ActionModifyResource;
import org.conservationmeasures.eam.actions.ActionTeamAddMember;
import org.conservationmeasures.eam.actions.ActionTeamRemoveMember;
import org.conservationmeasures.eam.actions.ActionViewPossibleTeamMembers;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputPanel;
import org.conservationmeasures.eam.dialogs.ModelessDialogWithClose;
import org.conservationmeasures.eam.dialogs.NewResourceManagementPanel;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.views.summary.wizard.SummaryWizardPanel;
import org.conservationmeasures.eam.views.umbrella.CreateResource;
import org.conservationmeasures.eam.views.umbrella.DeleteResource;
import org.conservationmeasures.eam.views.umbrella.ModifyResource;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.martus.swing.UiScrollPane;

public class SummaryView extends TabbedView
{
	public SummaryView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new SummaryToolBar(mainWindowToUse.getActions()));
		
		addSummaryDoersToMap();
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.SUMMARY_VIEW_NAME;
	}

	public WizardPanel createWizardPanel() throws Exception 
	{
		wizardPanel = new SummaryWizardPanel(getMainWindow().getActions());
		return wizardPanel;
	}
		
	public void createTabs() throws Exception
	{
		ProjectMetadata metadata = getProject().getMetadata();
		CrossOrganizationSummaryPanel crossOrganizationSummaryPanel = new CrossOrganizationSummaryPanel(getMainWindow(), metadata);
		TNCSummaryPanel tncSummaryPanel = new TNCSummaryPanel(getProject(), metadata);

		WWFSummaryPanel wwfSummaryPanel = new WWFSummaryPanel(getProject(), metadata);
		WCSSummaryPanel wcssSummaryPanel =new WCSSummaryPanel(getProject(), metadata); 
		RARESummaryPanel rareSummaryPanel = new RARESummaryPanel(getProject(), metadata);
		FOSSummaryPanel fosSummaryPanel = new FOSSummaryPanel(getProject(), metadata);
		
		
		addPanelAsTab(crossOrganizationSummaryPanel);
		addPanelAsTab(tncSummaryPanel);
		
		addPanelAsTab(wwfSummaryPanel);
		addPanelAsTab(wcssSummaryPanel);
		addPanelAsTab(rareSummaryPanel);
		addPanelAsTab(fosSummaryPanel);
	}
	
	void addPanelAsTab(ObjectDataInputPanel panel)
	{
		addTab(panel.getPanelDescription(), new UiScrollPane(panel));
	}

	public void deleteTabs() throws Exception
	{
	}

	public void showTeamAddMembersDialog() throws Exception
	{
		NewResourceManagementPanel panel = new NewResourceManagementPanel(getMainWindow());
		ModelessDialogWithClose dlg = new ModelessDialogWithClose(getMainWindow(), panel, panel.getPanelDescription());
		showFloatingPropertiesDialog(dlg);
	}
	
	private void addSummaryDoersToMap()
	{
		teamAddMemberDoer = new TeamAddMember();
		teamRemoveMemberDoer = new TeamRemoveMember();
		createResourceDoer = new CreateResource();
		modifyResourceDoer = new ModifyResource();
		deleteResourceDoer = new DeleteResource();
		
		addDoerToMap(ActionViewPossibleTeamMembers.class, new ViewPossibleTeamMembers());
		addDoerToMap(ActionTeamAddMember.class, teamAddMemberDoer);
		addDoerToMap(ActionTeamRemoveMember.class, teamRemoveMemberDoer);
		addDoerToMap(ActionCreateResource.class, createResourceDoer);
		addDoerToMap(ActionModifyResource.class, modifyResourceDoer);
		addDoerToMap(ActionDeleteResource.class, deleteResourceDoer);
	}
	
	public void jump(Class stepMarker) throws Exception
	{
		wizardPanel.jump(stepMarker);
	}

	SummaryWizardPanel wizardPanel;
	
	TeamAddMember teamAddMemberDoer;
	TeamRemoveMember teamRemoveMemberDoer;
	CreateResource createResourceDoer;
	ModifyResource modifyResourceDoer;
	DeleteResource deleteResourceDoer;
}
