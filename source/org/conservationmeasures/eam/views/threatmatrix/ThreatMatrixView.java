/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetThreatRating;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.RatingCriterion;
import org.conservationmeasures.eam.objects.ValueOption;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.conservationmeasures.eam.views.threatmatrix.wizard.ThreatRatingWizardPanel;
import org.conservationmeasures.eam.views.umbrella.SaveImage;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.conservationmeasures.eam.views.umbrella.ViewSplitPane;
import org.martus.swing.UiLabel;
import org.martus.swing.UiScrollPane;


public class ThreatMatrixView extends UmbrellaView
{
	public ThreatMatrixView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		
		addThreatMatrixiewDoersToMap();
		
		setToolBar(new ThreatMatrixToolBar(getMainWindow().getActions()));
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
		return MatrixTableImageCreator.createImage(grid.getThreatMatrixTable(),grid.getRowHeaderTable());
	}


	public void becomeActive() throws Exception
	{
		super.becomeActive();
		removeAll();

		model = new NonEditableThreatMatrixTableModel(getProject());
		
		bigSplitter = new ViewSplitPane(getMainWindow(), getProject().getCurrentView(),createWizardPanel(), createThreatMatrixPanel());
		
		add(bigSplitter);
			
		selectBundle(null);
		
		grid.establishPriorSortState();
	}

	
	private Container createThreatMatrixPanel() throws Exception
	{
		grid = new ThreatGridPanel(this, model);
		
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
		super.becomeInactive();
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
		
		FactorId threatId = bundle.getThreatId();
		FactorId targetId = bundle.getTargetId();
		BaseId criterionId = criterion.getId();
		BaseId valueId = value.getId();
		setBundleValue(threatId, targetId, criterionId, valueId);
	}

	private void setBundleValue(FactorId threatId, FactorId targetId, BaseId criterionId, BaseId valueId) throws CommandFailedException
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
		updateAfterCommand(event.getCommand());
	}

	public void commandFailed(Command command, CommandFailedException e)
	{
	}
	
	private void updateAfterCommand(Command rawCommand)
	{
		if(rawCommand.getCommandName().equals(CommandCreateObject.COMMAND_NAME))
			updateAfterCreateOrUndo((CommandCreateObject)rawCommand);
		if(rawCommand.getCommandName().equals(CommandDeleteObject.COMMAND_NAME))
			updateAfterDeleteOrUndo((CommandDeleteObject)rawCommand);
		
		try
		{
			snapUiToExecutedCommand(rawCommand);
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
	
	private void updateAfterCreateOrUndo(CommandCreateObject commandDoneOrUndone)
	{
		model.fireTableDataChanged();
	}
	
	private void updateAfterDeleteOrUndo(CommandDeleteObject commandDoneOrUndone)
	{
		model.fireTableDataChanged();
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
	ThreatGridPanel grid;
	ThreatRatingBundlePanel details;
	JPanel gridWithHeadings;
}

