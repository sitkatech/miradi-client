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
import org.conservationmeasures.eam.dialogs.PossibleTeamMembersDialog;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.views.umbrella.CreateResource;
import org.conservationmeasures.eam.views.umbrella.DeleteResource;
import org.conservationmeasures.eam.views.umbrella.ModifyResource;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

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
		return new SummaryWizardPanel();
	}
		
	public void createTabs() throws Exception
	{
		CrossOrganizationSummaryPanel crossOrganizationSummaryPanel = new CrossOrganizationSummaryPanel(getMainWindow());
		addTab(crossOrganizationSummaryPanel.getPanelDescriptionText(), crossOrganizationSummaryPanel);
		TNCSummaryPanel tncSummaryPanel = new TNCSummaryPanel(getMainWindow());
		addTab(tncSummaryPanel.getPanelDescriptionText(), tncSummaryPanel);
	}

	public void deleteTabs() throws Exception
	{
	}

	public void showTeamAddMembersDialog()
	{
		PossibleTeamMembersDialog dlg = new PossibleTeamMembersDialog(getMainWindow());
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
	
	
	TeamAddMember teamAddMemberDoer;
	TeamRemoveMember teamRemoveMemberDoer;
	CreateResource createResourceDoer;
	ModifyResource modifyResourceDoer;
	DeleteResource deleteResourceDoer;
}
