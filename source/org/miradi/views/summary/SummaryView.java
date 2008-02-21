/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.summary;


import java.util.HashSet;

import javax.swing.JToolBar;

import org.martus.util.MultiCalendar;
import org.miradi.actions.ActionCreateOrganization;
import org.miradi.actions.ActionDeleteOrganization;
import org.miradi.actions.ActionDeleteTeamMember;
import org.miradi.actions.ActionTeamCreateMember;
import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.ModelessDialogWithClose;
import org.miradi.dialogs.organization.OrganizationManagementPanel;
import org.miradi.dialogs.resource.PossibleTeamMembersPanel;
import org.miradi.dialogs.summary.TeamManagementPanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.FosProjectData;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.RareProjectData;
import org.miradi.objects.TncProjectData;
import org.miradi.objects.WcpaProjectData;
import org.miradi.objects.WcsProjectData;
import org.miradi.objects.WwfProjectData;
import org.miradi.project.Project;
import org.miradi.views.TabbedView;
import org.miradi.views.summary.doers.CreateOranizationDoer;
import org.miradi.views.summary.doers.DeleteOranizationDoer;
import org.miradi.views.summary.doers.TeamCreateMemberDoer;
import org.miradi.views.umbrella.DeleteResource;

public class SummaryView extends TabbedView
{
	public SummaryView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		tabPanels = new HashSet<DisposablePanel>();
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
		
		teamManagementPanel = new TeamManagementPanel(getProject(), getMainWindow(), getMainWindow().getActions());
		organizationManagementPanel = new OrganizationManagementPanel(getProject(), getMainWindow(), getMainWindow().getActions());

		ORef[] allRelatedRefs = new ORef[] {
			metadata.getRef(),
			getProject().getSingletonObjectRef(TncProjectData.getObjectType()),
			getProject().getSingletonObjectRef(WwfProjectData.getObjectType()),
			getProject().getSingletonObjectRef(WcsProjectData.getObjectType()),
			getProject().getSingletonObjectRef(RareProjectData.getObjectType()),
			getProject().getSingletonObjectRef(FosProjectData.getObjectType()),
			getProject().getSingletonObjectRef(WcpaProjectData.getObjectType()),
		};
		
		addSummaryTab(new SummaryProjectPanel(getProject(), metadata.getRef()));
		addNonScrollingTab(teamManagementPanel);
		addNonScrollingTab(organizationManagementPanel);
		addSummaryTab(new SummaryScopeTabPanel(getProject(), allRelatedRefs));
		addSummaryTab(new SummaryLocationPanel(getProject(), metadata.getRef()));
		addSummaryTab(new SummaryPlanningPanel(getMainWindow(), metadata.getRef()));
		addMemberOrgTab("TNCPanel.html", new TNCSummaryPanel(getProject(), metadata));
		addMemberOrgTab("WWFPanel.html", new WWFSummaryPanel(getProject(), metadata));
		addMemberOrgTab("WCSPanel.html", new WCSSummaryPanel(getProject()));
		addMemberOrgTab("RAREPanel.html", new RARESummaryPanel(getProject()));
		addMemberOrgTab("FOSPanel.html", new FOSSummaryPanel(getProject()));
		addSummaryTab(new SummaryOtherOrgPanel(getProject(), metadata.getRef()));
	}
	
	private void addSummaryTab(AbstractObjectDataInputPanel tabPanel)
	{
		tabPanels.add(tabPanel);
		addScrollingTab(tabPanel);
	}
	
	private void addMemberOrgTab(String htmlResourceName, AbstractObjectDataInputPanel dataPanel) throws Exception
	{
		MemberOrgTabPanel tabPanel = new MemberOrgTabPanel(getMainWindow(), htmlResourceName, dataPanel);
		tabPanels.add(tabPanel);
		addScrollingTab(tabPanel);
	}
	
	public void deleteTabs() throws Exception
	{
		teamManagementPanel.dispose();
		organizationManagementPanel.dispose();
		
		for(DisposablePanel panel : tabPanels)
		{
			panel.dispose();
		}
		tabPanels.clear();
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
	
	private HashSet<DisposablePanel> tabPanels;

	private TeamManagementPanel teamManagementPanel;
	private OrganizationManagementPanel organizationManagementPanel;
}
