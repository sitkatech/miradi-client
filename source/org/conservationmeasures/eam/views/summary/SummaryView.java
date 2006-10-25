/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import org.conservationmeasures.eam.actions.ActionCreateResource;
import org.conservationmeasures.eam.actions.ActionDeleteResource;
import org.conservationmeasures.eam.actions.ActionModifyResource;
import org.conservationmeasures.eam.actions.ActionTeamAddMember;
import org.conservationmeasures.eam.actions.ActionTeamRemoveMember;
import org.conservationmeasures.eam.actions.ActionViewPossibleTeamMembers;
import org.conservationmeasures.eam.dialogs.PossibleTeamMembersDialog;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.CreateResource;
import org.conservationmeasures.eam.views.umbrella.DeleteResource;
import org.conservationmeasures.eam.views.umbrella.ModifyResource;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.conservationmeasures.eam.views.umbrella.ViewSplitPane;
import org.martus.swing.UiScrollPane;
import org.martus.swing.UiTabbedPane;

public class SummaryView extends UmbrellaView
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

	public void becomeActive() throws Exception
	{
		removeAll();

		bigSplitter = new ViewSplitPane(createSummaryWizardPanel(), createScrollableSummaryPanel(), bigSplitter);

		add(bigSplitter, BorderLayout.CENTER);
	}

	private SummaryWizardPanel createSummaryWizardPanel() throws Exception {
		wizardPanel = new SummaryWizardPanel();
	return wizardPanel;
	}
	
	
	private JComponent createScrollableSummaryPanel()
	{
		CrossOrganizationSummaryPanel crossOrganizationSummaryPanel = new CrossOrganizationSummaryPanel(getMainWindow());
		UiTabbedPane tabbedPanel = new UiTabbedPane();
		tabbedPanel.addTab(EAM.text("General"), new CrossOrganizationSummaryPanel(getMainWindow()));
		UiScrollPane uiScrollPane = new UiScrollPane(crossOrganizationSummaryPanel);
		uiScrollPane.getHorizontalScrollBar().setUnitIncrement(EAM.STANDARD_SCROLL_INCREMENT);
		uiScrollPane.getVerticalScrollBar().setUnitIncrement(EAM.STANDARD_SCROLL_INCREMENT);
		return tabbedPanel;
	}

	public void becomeInactive() throws Exception
	{
		bigSplitter.removeAll();
		wizardPanel = null;
		removeAll();
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
	
	JSplitPane bigSplitter;
	SummaryWizardPanel wizardPanel;
	
	TeamAddMember teamAddMemberDoer;
	TeamRemoveMember teamRemoveMemberDoer;
	CreateResource createResourceDoer;
	ModifyResource modifyResourceDoer;
	DeleteResource deleteResourceDoer;
}
