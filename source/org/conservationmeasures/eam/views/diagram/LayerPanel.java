/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.conservationmeasures.eam.diagram.cells.DiagramStrategy;
import org.conservationmeasures.eam.diagram.cells.DiagramTarget;
import org.conservationmeasures.eam.dialogs.DisposablePanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiCheckBox;
import org.martus.swing.UiVBox;

public class LayerPanel extends DisposablePanel implements ActionListener
{
	public LayerPanel(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
		
		strategyCheckBox = new UiCheckBox(EAM.text("Label|Show Strategies"));
		strategyCheckBox.addActionListener(this);
		contributingFactorCheckBox = new UiCheckBox(EAM.text("Label|Show Contributing Factors"));
		contributingFactorCheckBox.addActionListener(this);
		threatCheckBox = new UiCheckBox(EAM.text("Label|Show Direct Threats"));
		threatCheckBox.addActionListener(this);
		targetCheckBox = new UiCheckBox(EAM.text("Label|Show Targets"));
		targetCheckBox.addActionListener(this);
		factorLinkCheckBox = new UiCheckBox(EAM.text("Label|Show Links"));
		factorLinkCheckBox.addActionListener(this);
		goalsCheckBox = new UiCheckBox(EAM.text("Label|Show Goals"));
		goalsCheckBox.addActionListener(this);
		objectivesCheckBox = new UiCheckBox(EAM.text("Label|Show Objectives"));
		objectivesCheckBox.addActionListener(this);
		
		indicatorCheckBox = new UiCheckBox(EAM.text("Label|Show Indicators"));
		indicatorCheckBox.addActionListener(this);
		
		setControlsFromLayerManager();
		
		UiVBox bigBox = new UiVBox();
		bigBox.add(createLayerOptions());
		
		add(bigBox);
	}

	private Component createLayerOptions()
	{
		UiVBox options = new UiVBox();
		options.add(strategyCheckBox);
		options.add(contributingFactorCheckBox);
		options.add(threatCheckBox);
		options.add(targetCheckBox);
		options.add(factorLinkCheckBox);
		options.add(goalsCheckBox);
		options.add(objectivesCheckBox);
		options.add(indicatorCheckBox);
		return options;
	}
	
	public void actionPerformed(ActionEvent event)
	{
		applyChanges();
	}
	
	public boolean getResult()
	{
		return result;
	}
	
	private void setControlsFromLayerManager()
	{
		strategyCheckBox.setSelected(getLayerManager().isTypeVisible(DiagramStrategy.class));
		contributingFactorCheckBox.setSelected(getLayerManager().areContributingFactorsVisible());
		threatCheckBox.setSelected(getLayerManager().areDirectThreatsVisible());
		targetCheckBox.setSelected(getLayerManager().isTypeVisible(DiagramTarget.class));
		factorLinkCheckBox.setSelected(getLayerManager().areFactorLinksVisible());
		goalsCheckBox.setSelected(getLayerManager().areGoalsVisible());
		objectivesCheckBox.setSelected(getLayerManager().areObjectivesVisible());
		indicatorCheckBox.setSelected(getLayerManager().areIndicatorsVisible());
	}
	
	private void updateLayerManagerFromControls()
	{
		getLayerManager().setVisibility(DiagramStrategy.class, strategyCheckBox.isSelected());
		getLayerManager().setContributingFactorsVisible(contributingFactorCheckBox.isSelected());
		getLayerManager().setDirectThreatsVisible(threatCheckBox.isSelected());
		getLayerManager().setVisibility(DiagramTarget.class, targetCheckBox.isSelected());
		getLayerManager().setFactorLinksVisible(factorLinkCheckBox.isSelected());
		getLayerManager().setGoalsVisible(goalsCheckBox.isSelected());
		getLayerManager().setObjectivesVisible(objectivesCheckBox.isSelected());
		getLayerManager().setIndicatorsVisible(indicatorCheckBox.isSelected());
	}
	
	public void applyChanges()
	{
		updateLayerManagerFromControls();
		getProject().updateVisibilityOfFactors();
		mainWindow.updateStatusBar();
	}

	private LayerManager getLayerManager()
	{
		LayerManager manager = getProject().getLayerManager();
		return manager;
	}

	private Project getProject()
	{
		return mainWindow.getProject();
	}
	
	MainWindow mainWindow;
	boolean result;

	UiCheckBox strategyCheckBox;
	UiCheckBox contributingFactorCheckBox;
	UiCheckBox threatCheckBox;
	UiCheckBox targetCheckBox;
	UiCheckBox factorLinkCheckBox;
	UiCheckBox goalsCheckBox;
	UiCheckBox objectivesCheckBox;
	UiCheckBox indicatorCheckBox;
}
