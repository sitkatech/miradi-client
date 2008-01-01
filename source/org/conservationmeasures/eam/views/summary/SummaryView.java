/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;


import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionCreateResource;
import org.conservationmeasures.eam.actions.ActionDeleteResource;
import org.conservationmeasures.eam.actions.ActionModifyResource;
import org.conservationmeasures.eam.actions.ActionTeamCreateMember;
import org.conservationmeasures.eam.actions.ActionTeamRemoveMember;
import org.conservationmeasures.eam.actions.ActionViewPossibleTeamMembers;
import org.conservationmeasures.eam.dialogs.base.ModelessDialogWithClose;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.dialogs.resource.PossibleTeamMembersPanel;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.views.summary.doers.TeamCreateMemberDoer;
import org.conservationmeasures.eam.views.summary.doers.TeamRemoveMember;
import org.conservationmeasures.eam.views.summary.doers.ViewPossibleTeamMembers;
import org.conservationmeasures.eam.views.umbrella.CreateResource;
import org.conservationmeasures.eam.views.umbrella.DeleteResource;
import org.conservationmeasures.eam.views.umbrella.ModifyResource;

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

	public JToolBar createToolBar()
	{
		return new SummaryToolBar(getMainWindow().getActions());
	}
	
	public void createTabs() throws Exception
	{
		ProjectMetadata metadata = getProject().getMetadata();
		crossOrganizationSummaryPanel = new CrossOrganizationSummaryPanel(getMainWindow(), metadata);
		tncSummaryPanel = new TNCSummaryPanel(getProject(), metadata);

		wwfSummaryPanel = new WWFSummaryPanel(getProject(), metadata);
		wcssSummaryPanel =new WCSSummaryPanel(getProject(), metadata); 
		rareSummaryPanel = new RARESummaryPanel(getProject(), metadata);
		fosSummaryPanel = new FOSSummaryPanel(getProject(), metadata);
		
		
		addPanelAsTab(crossOrganizationSummaryPanel);
		addPanelAsTab(tncSummaryPanel);
		
		addPanelAsTab(wwfSummaryPanel);
		addPanelAsTab(wcssSummaryPanel);
		addPanelAsTab(rareSummaryPanel);
		addPanelAsTab(fosSummaryPanel);
	}
	
	void addPanelAsTab(ObjectDataInputPanel panel)
	{
		addTab(panel.getPanelDescription(), new FastScrollPane(panel));
	}

	public void deleteTabs() throws Exception
	{
		crossOrganizationSummaryPanel.dispose();
		tncSummaryPanel.dispose();
		wwfSummaryPanel.dispose();
		wcssSummaryPanel.dispose();
		rareSummaryPanel.dispose();
		fosSummaryPanel.dispose();
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
		addDoerToMap(ActionViewPossibleTeamMembers.class, new ViewPossibleTeamMembers());
		addDoerToMap(ActionTeamCreateMember.class, new TeamCreateMemberDoer());
		addDoerToMap(ActionTeamRemoveMember.class, new TeamRemoveMember());
		addDoerToMap(ActionCreateResource.class, new CreateResource());
		addDoerToMap(ActionModifyResource.class, new ModifyResource());
		addDoerToMap(ActionDeleteResource.class, new DeleteResource());
	}
	
	CrossOrganizationSummaryPanel crossOrganizationSummaryPanel;
	TNCSummaryPanel tncSummaryPanel;

	WWFSummaryPanel wwfSummaryPanel;
	WCSSummaryPanel wcssSummaryPanel; 
	RARESummaryPanel rareSummaryPanel;
	FOSSummaryPanel fosSummaryPanel;
}
