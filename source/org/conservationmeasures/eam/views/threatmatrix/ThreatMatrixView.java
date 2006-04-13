/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetThreatRating;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;
import org.conservationmeasures.eam.objects.ThreatRatingValueOption;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.conservationmeasures.eam.views.interview.ThreatMatrixToolBar;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiLabel;
import org.martus.swing.UiScrollPane;


public class ThreatMatrixView extends UmbrellaView implements CommandExecutedListener
{
	public ThreatMatrixView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new ThreatMatrixToolBar(getMainWindow().getActions()));
		setLayout(new BorderLayout());
		
		bigSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		bigSplitter.setResizeWeight(.5);
		add(bigSplitter, BorderLayout.CENTER);

		getProject().addCommandExecutedListener(this);
	}

	public String cardName()
	{
		return getViewName();
	}

	static public String getViewName()
	{
		return "ThreatMatrix";
	}
	
	public void becomeCurrentView() throws Exception
	{
		model = new ThreatMatrixTableModel(getProject());

		grid = new ThreatGridPanel(this, model);
		wizard = new ThreatRatingWizardPanel(this);
		details = new ThreatRatingBundlePanel(getProject(), new OkListener(), new CancelListener());
		String targetLabelText = "<html><h2>TARGETS</h2></html>";
		UiLabel targetLabel = new UiLabel(EAM.text(targetLabelText));
		targetLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		JPanel gridWithHeadings = new JPanel(new BorderLayout());
		gridWithHeadings.add(targetLabel, BorderLayout.BEFORE_FIRST_LINE);
		gridWithHeadings.add(grid, BorderLayout.CENTER);
		
		Container bottomHalf = new JPanel(new BorderLayout());
		bottomHalf.add(new UiScrollPane(gridWithHeadings), BorderLayout.CENTER);
		bottomHalf.add(new UiScrollPane(details), BorderLayout.AFTER_LINE_ENDS);
		
		int dividerAt = bigSplitter.getDividerLocation();
		bigSplitter.setTopComponent(wizard);
		bigSplitter.setBottomComponent(bottomHalf);
		bigSplitter.setDividerLocation(dividerAt);
		
		selectBundle(null);
	}
	
	public ThreatMatrixTableModel getModel()
	{
		return model;
	}
	
	public void selectBundle(ThreatRatingBundle bundle) throws Exception
	{
		wizard.selectBundle(bundle);
		details.selectBundle(bundle);
		grid.selectBundle(bundle);
		invalidate();
		validate();
	}
	
	public void setBundleValue(ThreatRatingCriterion criterion, ThreatRatingValueOption value) throws Exception
	{
		ThreatRatingBundle bundle = grid.getSelectedBundle();
		int threatId = bundle.getThreatId();
		int targetId = bundle.getTargetId();
		int criterionId = criterion.getId();
		int valueId = value.getId();
		setBundleValue(threatId, targetId, criterionId, valueId);
	}

	private void setBundleValue(int threatId, int targetId, int criterionId, int valueId) throws CommandFailedException
	{
		CommandSetThreatRating cmd = new CommandSetThreatRating(threatId, targetId, criterionId, valueId);
		getProject().executeCommand(cmd);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			selectBundle(grid.getSelectedBundle());
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	public void commandFailed(Command command, CommandFailedException e)
	{
	}

	abstract class ButtonListener implements ActionListener
	{
		abstract void takeAction(ThreatRatingBundle originalBundle, ThreatRatingBundle workingBundle) throws Exception;

		public void actionPerformed(ActionEvent event)
		{
			try
			{
				ThreatRatingBundle workingBundle = details.getBundle();
				ThreatRatingBundle originalBundle = model.getBundle(workingBundle.getThreatId(), workingBundle.getTargetId());
				takeAction(originalBundle, workingBundle);
			}
			catch (Exception e)
			{
				EAM.logException(e);
			}
		}
	}
	
	class OkListener extends ButtonListener
	{
		void takeAction(ThreatRatingBundle originalBundle, ThreatRatingBundle workingBundle) throws Exception
		{
			Project project = getProject();
			ThreatRatingFramework framework = project.getThreatRatingFramework();
			ThreatRatingCriterion[] criteria = framework.getCriteria();
			project.executeCommand(new CommandBeginTransaction());
			for(int i = 0; i < criteria.length; ++i)
			{
				int criterionId = criteria[i].getId();
				int valueId = workingBundle.getValueId(criterionId);
				if(valueId != originalBundle.getValueId(criterionId))
				{
					setBundleValue(workingBundle.getThreatId(), workingBundle.getTargetId(), criterionId, valueId);
				}
			}
			project.executeCommand(new CommandEndTransaction());
		}
	}
	
	class CancelListener extends ButtonListener
	{
		void takeAction(ThreatRatingBundle originalBundle, ThreatRatingBundle workingBundle) throws Exception
		{
			details.selectBundle(originalBundle);
		}
		
	}
	
	JSplitPane bigSplitter;
	ThreatMatrixTableModel model;
	ThreatRatingWizardPanel wizard;
	ThreatGridPanel grid;
	ThreatRatingBundlePanel details;
}

