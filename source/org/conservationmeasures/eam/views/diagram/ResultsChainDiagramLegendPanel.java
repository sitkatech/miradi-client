/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.icons.ObjectiveIcon;
import org.conservationmeasures.eam.icons.ProjectScopeIcon;
import org.conservationmeasures.eam.icons.StressIcon;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;

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
//FIXME add the right buttons to this legent panel		
//		addButtonLineWithCheckBox(jpanel, Target.OBJECT_NAME, actions.get(ActionInsertTarget.class));
//		addButtonLineWithCheckBox(jpanel, Factor.OBJECT_NAME_THREAT, actions.get(ActionInsertDirectThreat.class));
//		addButtonLineWithCheckBox(jpanel, Factor.OBJECT_NAME_CONTRIBUTING_FACTOR, actions.get(ActionInsertContributingFactor.class));
//		addButtonLineWithCheckBox(jpanel, Strategy.OBJECT_NAME, actions.get(ActionInsertStrategy.class));
//		addButtonLineWithCheckBox(jpanel, FactorLink.OBJECT_NAME, actions.get(ActionInsertFactorLink.class));
		addTargetLinkLine(jpanel, TARGET_LINKS_TEXT);
		
		addIconLineWithCheckBox(jpanel, Goal.OBJECT_NAME, new GoalIcon());
		addIconLineWithCheckBox(jpanel, Objective.OBJECT_NAME, new ObjectiveIcon());
		addIconLineWithCheckBox(jpanel, Indicator.OBJECT_NAME, new IndicatorIcon());
		addIconLineWithoutCheckBox(jpanel, "Stress", new StressIcon());
		
		return jpanel;
	}
}
