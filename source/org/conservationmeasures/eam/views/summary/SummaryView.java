/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.conservationmeasures.eam.actions.ActionModifyResource;
import org.conservationmeasures.eam.actions.ActionTeamAddMember;
import org.conservationmeasures.eam.actions.ActionTeamRemoveMember;
import org.conservationmeasures.eam.actions.ActionViewPossibleTeamMembers;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.PossibleTeamMembersDialog;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.ModifyResource;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiScrollPane;

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
		int dividerAt = -1;
		if(bigSplitter != null)
			dividerAt = bigSplitter.getDividerLocation();
		
		removeAll();
		
		summaryPanel = new SummaryPanel(getMainWindow());
		UiScrollPane uiScrollPane = new UiScrollPane(summaryPanel);
		uiScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		uiScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		uiScrollPane.getHorizontalScrollBar().setUnitIncrement(getProject().getGridSize());
		uiScrollPane.getVerticalScrollBar().setUnitIncrement(getProject().getGridSize());

		// NOTE: For reasons I don't understand, if we construct the splitter 
		// in the constructor, it always ignores the setDividerLocation and ends up
		// at zero.
		bigSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		bigSplitter.setResizeWeight(.5);
		bigSplitter.setTopComponent(new SummaryWizardPanel());
		bigSplitter.setBottomComponent(uiScrollPane);
		
		if(dividerAt > 0)
			bigSplitter.setDividerLocation(dividerAt);
		
		add(bigSplitter, BorderLayout.CENTER);
	}

	public void becomeInactive() throws Exception
	{
		bigSplitter.removeAll();
		wizardPanel = null;
		summaryPanel = null;
		removeAll();
	}
	
	public void showTeamAddMembersDialog()
	{
		PossibleTeamMembersDialog dlg = new PossibleTeamMembersDialog(getMainWindow());
		showFloatingPropertiesDialog(dlg);
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		updateTeamList(event);
	}

	public void commandUndone(CommandExecutedEvent event)
	{
		super.commandUndone(event);
		updateTeamList(event);
	}
	
	private void updateTeamList(CommandExecutedEvent event)
	{
		if(!event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return;
		
		CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
		if(cmd.getObjectType() != ObjectType.PROJECT_METADATA)
			return;
		
		summaryPanel.rebuild();
	}

	private void addSummaryDoersToMap()
	{
		teamAddMemberDoer = new TeamAddMember();
		teamRemoveMemberDoer = new TeamRemoveMember();
		modifyResourceDoer = new ModifyResource();
		
		addDoerToMap(ActionViewPossibleTeamMembers.class, new ViewPossibleTeamMembers());
		addDoerToMap(ActionTeamAddMember.class, teamAddMemberDoer);
		addDoerToMap(ActionTeamRemoveMember.class, teamRemoveMemberDoer);
		addDoerToMap(ActionModifyResource.class, modifyResourceDoer);
	}
	
	JSplitPane bigSplitter;
	SummaryWizardPanel wizardPanel;
	SummaryPanel summaryPanel;
	
	TeamAddMember teamAddMemberDoer;
	TeamRemoveMember teamRemoveMemberDoer;
	ModifyResource modifyResourceDoer;
}
