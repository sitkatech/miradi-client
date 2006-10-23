/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetThreatRating;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.RatingCriterion;
import org.conservationmeasures.eam.objects.ValueOption;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardPanel;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiLabel;
import org.martus.swing.UiScrollPane;


public class ThreatMatrixView extends UmbrellaView implements CommandExecutedListener
{
	public ThreatMatrixView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new ThreatMatrixToolBar(getMainWindow().getActions()));
		getProject().addCommandExecutedListener(this);
	}

	public String cardName()
	{
		return getViewName();
	}

	static public String getViewName()
	{
		return Project.THREAT_MATRIX_VIEW_NAME;
	}
	
	public void becomeActive() throws Exception
	{
		removeAll();

		model = new ThreatMatrixTableModel(getProject());
		
		bigSplitter =new ViewSplitPane(createWizardPanel(), createThreatMatrixPanel(), bigSplitter);
		
		add(bigSplitter);
			
		selectBundle(null);
	}


	private Container createThreatMatrixPanel() throws Exception
	{
		grid = new ThreatGridPanel(this, model);
		
		JComponent heading = createHeading();
		JPanel gridWithHeadings = new JPanel(new BorderLayout());
		gridWithHeadings.add(heading, BorderLayout.BEFORE_FIRST_LINE);
		gridWithHeadings.add(grid, BorderLayout.CENTER);
		
		details = new ThreatRatingBundlePanel(this);
		
		Container bottomHalf = new JPanel(new BorderLayout());
		bottomHalf.add(new UiScrollPane(gridWithHeadings), BorderLayout.CENTER);
		bottomHalf.add(new UiScrollPane(details), BorderLayout.AFTER_LINE_ENDS);
		return bottomHalf;
	}

	private JComponent createHeading()
	{
		String targetLabelText = "<html><h2>TARGETS</h2></html>";
		UiLabel targetLabel = new UiLabel(EAM.text(targetLabelText));
		targetLabel.setHorizontalAlignment(SwingConstants.CENTER);
		return targetLabel;
	}
	
	private ThreatRatingWizardPanel createWizardPanel() throws Exception
	{
		wizardPanel = new ThreatRatingWizardPanel(this);
		return wizardPanel;
	}
	
	public void becomeInactive() throws Exception
	{
		// TODO: Should clear ALL view data
		grid = null;
	}

	public ThreatMatrixTableModel getModel()
	{
		return model;
	}
	
	public ThreatRatingFramework getThreatRatingFramework()
	{
		return getProject().getThreatRatingFramework();
	}
	
	public void selectBundle(ThreatRatingBundle bundle) throws Exception
	{
		wizardPanel.selectBundle(bundle);
		details.selectBundle(bundle);
		grid.selectBundle(bundle);
		invalidate();
		validate();
	}
	
	public void setBundleValue(RatingCriterion criterion, ValueOption value) throws Exception
	{
		ThreatRatingBundle bundle = grid.getSelectedBundle();
		if(bundle == null)
			return;
		
		ModelNodeId threatId = bundle.getThreatId();
		ModelNodeId targetId = bundle.getTargetId();
		BaseId criterionId = criterion.getId();
		BaseId valueId = value.getId();
		setBundleValue(threatId, targetId, criterionId, valueId);
	}

	private void setBundleValue(ModelNodeId threatId, ModelNodeId targetId, BaseId criterionId, BaseId valueId) throws CommandFailedException
	{
		try
		{
			ThreatRatingBundle bundle = getThreatRatingFramework().getBundle(threatId, targetId);
			if(bundle.getValueId(criterionId).equals(valueId))
				return;
			CommandSetThreatRating cmd = new CommandSetThreatRating(threatId, targetId, criterionId, valueId);
			getProject().executeCommand(cmd);

		}
		catch(Exception e)
		{
			EAM.logException(e);
		}		
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			snapUiToExecutedCommand(event.getCommand());
			if(grid != null)
			{
				selectBundle(grid.getSelectedBundle());
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	public void commandUndone(CommandExecutedEvent event)
	{
		try
		{
			snapUiToExecutedCommand(event.getCommand());
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	public void commandFailed(Command command, CommandFailedException e)
	{
	}
	
	private void snapUiToExecutedCommand(Command executedCommand) throws Exception
	{
		if(executedCommand.getCommandName().equals(CommandSetThreatRating.COMMAND_NAME))
		{
			CommandSetThreatRating cmd = (CommandSetThreatRating)executedCommand;
			ThreatRatingBundle bundle = model.getBundle(cmd.getThreatId(), cmd.getTargetId());
			selectBundle(bundle);
		}
	}

	public void jump(Class stepMarker) throws Exception
	{
		wizardPanel.jump(stepMarker);
	}

	JSplitPane bigSplitter;
	ThreatMatrixTableModel model;
	ThreatRatingWizardPanel wizardPanel;
	ThreatGridPanel grid;
	ThreatRatingBundlePanel details;
}

