/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;


import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionCreateOrganization;
import org.conservationmeasures.eam.actions.ActionDeleteOrganization;
import org.conservationmeasures.eam.actions.ActionDeleteTeamMember;
import org.conservationmeasures.eam.actions.ActionTeamCreateMember;
import org.conservationmeasures.eam.dialogs.base.ModelessDialogWithClose;
import org.conservationmeasures.eam.dialogs.organization.OrganizationManagementPanel;
import org.conservationmeasures.eam.dialogs.resource.PossibleTeamMembersPanel;
import org.conservationmeasures.eam.dialogs.summary.TeamManagementPanel;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.views.summary.doers.CreateOranizationDoer;
import org.conservationmeasures.eam.views.summary.doers.DeleteOranizationDoer;
import org.conservationmeasures.eam.views.summary.doers.TeamCreateMemberDoer;
import org.conservationmeasures.eam.views.umbrella.DeleteResource;
import org.martus.util.MultiCalendar;

public class SummaryView extends TabbedView
{
	public SummaryView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
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

	public void becomeActive() throws Exception
	{
		super.becomeActive();
		
		teamManagementPanel.updateSplitterLocation();
		organizationManagementPanel.updateSplitterLocation();
	}
	
	public JToolBar createToolBar()
	{
		return new SummaryToolBar(getMainWindow().getActions());
	}
	
	public void createTabs() throws Exception
	{
		ProjectMetadata metadata = getProject().getMetadata();
		tncSummaryPanel = new TNCSummaryPanel(getProject(), metadata);
		wwfSummaryPanel = new WWFSummaryPanel(getProject(), metadata);
		wcssSummaryPanel =new WCSSummaryPanel(getProject()); 
		rareSummaryPanel = new RARESummaryPanel(getProject());
		fosSummaryPanel = new FOSSummaryPanel(getProject());
		
		summaryProjectPanel = new SummaryProjectPanel(getProject(), metadata.getRef());
		summaryScopePanel = new SummaryScopePanel(getProject(), metadata.getRef());
		summaryLocationPanel = new SummaryLocationPanel(getProject(), metadata.getRef());
		summaryPlanningPanel = new SummaryPlanningPanel(getMainWindow(), metadata.getRef());
		summaryOtherOrgPanel = new SummaryOtherOrgPanel(getProject(), metadata.getRef());
				
		teamManagementPanel = new TeamManagementPanel(getProject(), getMainWindow(), getMainWindow().getActions());
		organizationManagementPanel = new OrganizationManagementPanel(getProject(), getMainWindow(), getMainWindow().getActions());

		addScrollingTab(summaryProjectPanel);
		addNonScrollingTab(teamManagementPanel);
		addNonScrollingTab(organizationManagementPanel);
		addScrollingTab(summaryScopePanel);
		addScrollingTab(summaryLocationPanel);
		addScrollingTab(summaryPlanningPanel);
		addScrollingTab(tncSummaryPanel);
		addScrollingTab(wwfSummaryPanel);
		addScrollingTab(wcssSummaryPanel);
		addScrollingTab(rareSummaryPanel);
		addScrollingTab(fosSummaryPanel);
		addScrollingTab(summaryOtherOrgPanel);
	}
	
	public void deleteTabs() throws Exception
	{
		summaryProjectPanel.dispose();
		summaryScopePanel.dispose();
		summaryLocationPanel.dispose();
		summaryPlanningPanel.dispose();
		
		tncSummaryPanel.dispose();
		wwfSummaryPanel.dispose();
		wcssSummaryPanel.dispose();
		rareSummaryPanel.dispose();
		fosSummaryPanel.dispose();
		summaryOtherOrgPanel.dispose();
		teamManagementPanel.dispose();
		organizationManagementPanel.dispose();
	}
	
	public void showTeamAddMembersDialog() throws Exception
	{
		PossibleTeamMembersPanel panel = new PossibleTeamMembersPanel(getMainWindow());
		ModelessDialogWithClose dlg = new ModelessDialogWithClose(getMainWindow(), panel, panel.getPanelDescription());
		showFloatingPropertiesDialog(dlg);
		panel.updateSplitterLocation();
	}
	
	private void addSummaryDoersToMap()
	{
		addDoerToMap(ActionTeamCreateMember.class, new TeamCreateMemberDoer());
		addDoerToMap(ActionDeleteTeamMember.class, new DeleteResource());
		
		addDoerToMap(ActionCreateOrganization.class, new CreateOranizationDoer());
		addDoerToMap(ActionDeleteOrganization.class, new DeleteOranizationDoer());
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		if (event.isSetDataCommandWithThisTypeAndTag(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_WORKPLAN_START_DATE) ||
			event.isSetDataCommandWithThisTypeAndTag(ProjectMetadata.getObjectType(), ProjectMetadata.TAG_START_DATE))
			handleWorkPlanStartDate();
	}
	
	private void handleWorkPlanStartDate()
	{
		MultiCalendar workPlanStartDate = getProject().getMetadata().getWorkPlanStartDate();
		MultiCalendar projectStartDate = getProject().getMetadata().getProjectStartDate();
		if (workPlanStartDate == null || projectStartDate == null)
			return;
		
		if (workPlanStartDate.before(projectStartDate))
			EAM.errorDialog(EAM.text("Work plan start date is before project start date"));	
	}

	private TNCSummaryPanel tncSummaryPanel;

	private WWFSummaryPanel wwfSummaryPanel;
	private WCSSummaryPanel wcssSummaryPanel; 
	private RARESummaryPanel rareSummaryPanel;
	private FOSSummaryPanel fosSummaryPanel;
	
	private SummaryProjectPanel summaryProjectPanel;
	private SummaryScopePanel summaryScopePanel;
	private SummaryLocationPanel summaryLocationPanel;
	private SummaryPlanningPanel summaryPlanningPanel;
	private SummaryOtherOrgPanel summaryOtherOrgPanel;
	private TeamManagementPanel teamManagementPanel;
	private OrganizationManagementPanel organizationManagementPanel;
}
