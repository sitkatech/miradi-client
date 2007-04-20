/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.ActionInsertFactorLink;
import org.conservationmeasures.eam.actions.ActionInsertIntermediateResult;
import org.conservationmeasures.eam.actions.ActionInsertStrategy;
import org.conservationmeasures.eam.actions.ActionInsertTarget;
import org.conservationmeasures.eam.actions.ActionInsertThreatReductionResult;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.diagram.cells.DiagramStrategyCell;
import org.conservationmeasures.eam.diagram.cells.DiagramTargetCell;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.icons.ObjectiveIcon;
import org.conservationmeasures.eam.icons.ProjectScopeIcon;
import org.conservationmeasures.eam.icons.StressIcon;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.IntermediateResult;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.ThreatReductionResult;

import com.jhlabs.awt.GridLayoutPlus;

public class ResultsChainDiagramLegendPanel extends DiagramLegendPanel
{
	public ResultsChainDiagramLegendPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}
	
	protected JPanel createLegendButtonPanel(Actions actions)
	{
		JPanel jpanel = new JPanel(new GridLayoutPlus(0,3));
		
		addIconLineWithCheckBox(jpanel, SCOPE_BOX_TEXT, new ProjectScopeIcon());	
		addButtonLineWithCheckBox(jpanel, Target.OBJECT_NAME, actions.get(ActionInsertTarget.class));
		addButtonLineWithCheckBox(jpanel, ThreatReductionResult.OBJECT_NAME, actions.get(ActionInsertThreatReductionResult.class));
		addButtonLineWithCheckBox(jpanel, IntermediateResult.OBJECT_NAME, actions.get(ActionInsertIntermediateResult.class));
		addButtonLineWithCheckBox(jpanel, Strategy.OBJECT_NAME, actions.get(ActionInsertStrategy.class));
		addButtonLineWithCheckBox(jpanel, FactorLink.OBJECT_NAME, actions.get(ActionInsertFactorLink.class));
		addTargetLinkLine(jpanel, TARGET_LINKS_TEXT);
		
		addIconLineWithCheckBox(jpanel, Goal.OBJECT_NAME, new GoalIcon());
		addIconLineWithCheckBox(jpanel, Objective.OBJECT_NAME, new ObjectiveIcon());
		addIconLineWithCheckBox(jpanel, Indicator.OBJECT_NAME, new IndicatorIcon());
		addIconLineWithoutCheckBox(jpanel, "Stress", new StressIcon());
		
		return jpanel;
	}
	
	public void actionPerformed(ActionEvent event)
	{
		JCheckBox checkBox = (JCheckBox)event.getSource();

		String property = (String) checkBox.getClientProperty(LAYER);

		LayerManager manager = mainWindow.getProject().getLayerManager();

		
		if (property.equals(Strategy.OBJECT_NAME))
			manager.setVisibility(DiagramStrategyCell.class, checkBox.isSelected());
		else if (property.equals(Target.OBJECT_NAME))
			manager.setVisibility(DiagramTargetCell.class, checkBox.isSelected());
		else if (property.equals(IntermediateResult.OBJECT_NAME))
			manager.setIntermediateResultVisible(checkBox.isSelected());
		else if (property.equals(ThreatReductionResult.OBJECT_NAME))
			manager.setThreatReductionResultVisible(checkBox.isSelected());
		else if (property.equals(Target.OBJECT_NAME))
			manager.setVisibility(DiagramTargetCell.class, checkBox.isSelected());
		else if (property.equals(FactorLink.OBJECT_NAME))
		{
			manager.setFactorLinksVisible(checkBox.isSelected());
			targetLinkCheckBox.setEnabled(checkBox.isSelected());
		}
		else if (property.equals(TARGET_LINKS_TEXT))
			manager.setTargetLinksVisible(checkBox.isSelected());
		else if (property.equals(Goal.OBJECT_NAME))
			manager.setGoalsVisible(checkBox.isSelected());
		else if (property.equals(Objective.OBJECT_NAME))
			manager.setObjectivesVisible(checkBox.isSelected());
		else if (property.equals(Indicator.OBJECT_NAME))
			manager.setIndicatorsVisible(checkBox.isSelected());
		else if (property.equals(SCOPE_BOX_TEXT))
			manager.setScopeBoxVisible(checkBox.isSelected());

		mainWindow.getDiagramView().updateVisibilityOfFactors();
		mainWindow.updateStatusBar();
			
	}
}
