/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import org.conservationmeasures.eam.actions.ActionSaveImage;
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
import org.conservationmeasures.eam.utils.BufferedImageFactory;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardPanel;
import org.conservationmeasures.eam.views.umbrella.SaveImage;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.conservationmeasures.eam.views.umbrella.ViewSplitPane;
import org.martus.swing.UiLabel;
import org.martus.swing.UiScrollPane;


public class ThreatMatrixView extends UmbrellaView implements CommandExecutedListener
{
	public ThreatMatrixView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		
		addThreatMatrixiewDoersToMap();
		
		setToolBar(new ThreatMatrixToolBar(getMainWindow().getActions()));
		
		getProject().addCommandExecutedListener(this);
	}

	
	private void addThreatMatrixiewDoersToMap()
	{
		addDoerToMap(ActionSaveImage.class, new SaveImage());
	}
	
	public String cardName()
	{
		return getViewName();
	}

	static public String getViewName()
	{
		return Project.THREAT_MATRIX_VIEW_NAME;
	}
	
	public BufferedImage getImage()
	{
		return BufferedImageFactory.getImage(gridWithHeadings,0);
	}
	
	public void becomeActive() throws Exception
	{
		removeAll();

		model = new NonEditableThreatMatrixTableModel(getProject());
		
		bigSplitter =new ViewSplitPane(createWizardPanel(), createThreatMatrixPanel(), bigSplitter);
		
		add(bigSplitter);
			
		selectBundle(null);
	}


	private Container createThreatMatrixPanel() throws Exception
	{
		grid = new MyThreatGirdPanel(this, model,getProject());
		
		JComponent heading = createHeading();
		gridWithHeadings = new JPanel(new BorderLayout());
		gridWithHeadings.add(heading, BorderLayout.BEFORE_FIRST_LINE);
		gridWithHeadings.add(grid, BorderLayout.CENTER);
		details = new ThreatRatingBundlePanel(this);
		
		Container bottomHalf = new JPanel(new BorderLayout());
		bottomHalf.add(new UiScrollPane(details), BorderLayout.AFTER_LINE_ENDS);
		bottomHalf.add(gridWithHeadings, BorderLayout.CENTER); 

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

	public NonEditableThreatMatrixTableModel getModel()
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
	NonEditableThreatMatrixTableModel model;
	ThreatRatingWizardPanel wizardPanel;
	MyThreatGirdPanel grid;
	ThreatRatingBundlePanel details;
	JPanel gridWithHeadings;
}

