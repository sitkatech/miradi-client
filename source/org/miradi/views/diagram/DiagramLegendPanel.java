/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.views.diagram;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.miradi.actions.ActionInsertDraftStrategy;
import org.miradi.actions.ActionInsertFactorLink;
import org.miradi.actions.ActionInsertGroupBox;
import org.miradi.actions.ActionInsertStrategy;
import org.miradi.actions.ActionInsertTarget;
import org.miradi.actions.ActionInsertTextBox;
import org.miradi.actions.Actions;
import org.miradi.diagram.cells.DiagramGroupBoxCell;
import org.miradi.diagram.cells.DiagramStrategyCell;
import org.miradi.diagram.cells.DiagramTargetCell;
import org.miradi.diagram.cells.DiagramTextBoxCell;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.FactorLinkIcon;
import org.miradi.icons.GoalIcon;
import org.miradi.icons.IndicatorIcon;
import org.miradi.icons.ObjectiveIcon;
import org.miradi.icons.ProjectScopeIcon;
import org.miradi.icons.StressIcon;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Goal;
import org.miradi.objects.GroupBox;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Objective;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.TextBox;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.objects.ViewData;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.DiagramLegendQuestion;
import org.miradi.utils.CodeList;
import org.miradi.views.umbrella.LegendPanel;

abstract public class DiagramLegendPanel extends LegendPanel
{
	abstract protected void createCustomLegendPanelSection(Actions actions, JPanel jpanel);
	
	public DiagramLegendPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse.getProject());
		mainWindow = mainWindowToUse;
		
		createLegendCheckBoxes();
		addAllComponents();
		updateLegendPanel(getLegendSettings(ViewData.TAG_DIAGRAM_HIDDEN_TYPES));
	}
	
	private void addAllComponents()
	{
		setBorder(new EmptyBorder(5,5,5,5));
		
		add(createLegendButtonPanel(mainWindow.getActions()));
		updateCheckBoxs();
		setMinimumSize(new Dimension(0,0));
	}
	
	
	private void createLegendCheckBoxes()
	{
		createCheckBox(SCOPE_BOX);
		createCheckBox( Target.OBJECT_NAME);
		
		createCheckBox(Cause.OBJECT_NAME_THREAT);
		createCheckBox(Cause.OBJECT_NAME_CONTRIBUTING_FACTOR);
		createCheckBox(ThreatReductionResult.OBJECT_NAME);
		createCheckBox(IntermediateResult.OBJECT_NAME);
		createCheckBox(Strategy.OBJECT_NAME);
		createCheckBox(Strategy.OBJECT_NAME_DRAFT);
		createCheckBox(TextBox.OBJECT_NAME);
		createCheckBox(GroupBox.OBJECT_NAME);
		
		createCheckBox(FactorLink.OBJECT_NAME);
		createCheckBox(FactorLink.OBJECT_NAME_TARGETLINK);
		createCheckBox(FactorLink.OBJECT_NAME_STRESS);
		
		createCheckBox(Goal.OBJECT_NAME);
		createCheckBox(Objective.OBJECT_NAME);
		createCheckBox(Indicator.OBJECT_NAME);

	}
	
	protected JPanel createLegendButtonPanel(Actions actions)
	{
		TwoColumnPanel jpanel = new TwoColumnPanel();
		jpanel.disableFill();
		jpanel.setBackground(AppPreferences.getControlPanelBackgroundColor());
		
		addIconLineWithCheckBox(jpanel, ConceptualModelDiagram.getObjectType(), SCOPE_BOX, new ProjectScopeIcon());
		
		addButtonLineWithCheckBox(jpanel, Target.getObjectType(), Target.OBJECT_NAME, actions.get(ActionInsertTarget.class));
		createCustomLegendPanelSection(actions, jpanel);
		
		addButtonLineWithCheckBox(jpanel, Strategy.getObjectType(),Strategy.OBJECT_NAME, actions.get(ActionInsertStrategy.class));
		if (mainWindow.getDiagramView().isStategyBrainstormMode())
		{
			addButtonLineWithCheckBox(jpanel, Strategy.getObjectType(), Strategy.OBJECT_NAME_DRAFT, actions.get(ActionInsertDraftStrategy.class));
		}

		addButtonLineWithCheckBox(jpanel, FactorLink.getObjectType(), FactorLink.OBJECT_NAME, actions.get(ActionInsertFactorLink.class));
		addTargetLinkLine(jpanel, FactorLink.getObjectType(), FactorLink.OBJECT_NAME_TARGETLINK);
		
		addIconLineWithCheckBox(jpanel, Goal.getObjectType(), Goal.OBJECT_NAME, new GoalIcon());
		addIconLineWithCheckBox(jpanel, Objective.getObjectType(), Objective.OBJECT_NAME, new ObjectiveIcon());
		addIconLineWithCheckBox(jpanel, Indicator.getObjectType(), Indicator.OBJECT_NAME, new IndicatorIcon());
		
		if (getProject().getMetadata().isStressBasedThreatRatingMode())
			addIconLineWithCheckBox(jpanel, FactorLink.getObjectType(), FactorLink.OBJECT_NAME_STRESS, new StressIcon());
		
		addButtonLineWithCheckBox(jpanel, TextBox.getObjectType(), TextBox.OBJECT_NAME, actions.get(ActionInsertTextBox.class));
		addButtonLineWithCheckBox(jpanel, GroupBox.getObjectType(), GroupBox.OBJECT_NAME, actions.get(ActionInsertGroupBox.class));
		
		return jpanel;
	}

	protected void addTargetLinkLine(JPanel jpanel, int objectType, String objectName)
	{
		targetLinkCheckBox = findCheckBox(objectName);
		jpanel.add(targetLinkCheckBox);
		jpanel.add(new PanelTitleLabel(EAM.fieldLabel(objectType, objectName), new FactorLinkIcon()));
	}
	
	private void updateCheckBoxs()
	{
		Object[] keys = checkBoxes.keySet().toArray();
		for (int i=0; i<keys.length; ++i)
		{
			updateCheckBox(getLayerManager(), ((JCheckBox)checkBoxes.get(keys[i])).getClientProperty(LAYER).toString());
		}
	}
	
	public void actionPerformed(ActionEvent event)
	{
		JCheckBox checkBox = (JCheckBox)event.getSource();
		String property = (String) checkBox.getClientProperty(LAYER);
		LayerManager manager = getLayerManager();
		setLegendVisibilityOfFacactorCheckBoxes(manager, property);
		updateVisiblity();
		saveSettingsToProject(ViewData.TAG_DIAGRAM_HIDDEN_TYPES);
	}

	protected void setLegendVisibilityOfFacactorCheckBoxes(LayerManager manager, String property)
	{
		JCheckBox checkBox = findCheckBox(property);
		
		if (property.equals(Strategy.OBJECT_NAME))
			manager.setVisibility(DiagramStrategyCell.class, checkBox.isSelected());
		else if (property.equals(Target.OBJECT_NAME))
			manager.setVisibility(DiagramTargetCell.class, checkBox.isSelected());
		else if (property.equals(FactorLink.OBJECT_NAME))
		{
			manager.setFactorLinksVisible(checkBox.isSelected());
			targetLinkCheckBox.setEnabled(checkBox.isSelected());
		}
		else if (property.equals(FactorLink.OBJECT_NAME_TARGETLINK))
			manager.setTargetLinksVisible(checkBox.isSelected());
		else if (property.equals(Goal.OBJECT_NAME))
			manager.setGoalsVisible(checkBox.isSelected());
		else if (property.equals(Objective.OBJECT_NAME))
			manager.setObjectivesVisible(checkBox.isSelected());
		else if (property.equals(Indicator.OBJECT_NAME))
			manager.setIndicatorsVisible(checkBox.isSelected());
		else if (property.equals(SCOPE_BOX))
			manager.setScopeBoxVisible(checkBox.isSelected()); 
		else if (property.equals(TextBox.OBJECT_NAME))
			manager.setVisibility(DiagramTextBoxCell.class, checkBox.isSelected());
		else if (property.equals(GroupBox.OBJECT_NAME))
			manager.setVisibility(DiagramGroupBoxCell.class, checkBox.isSelected());
		else if (property.equals(FactorLink.OBJECT_NAME_STRESS))
			manager.setStressesVisible(checkBox.isSelected());
	}
	
	public void resetCheckBoxes()
	{
		removeAll();
		addAllComponents();
		updateLegendPanel(getLegendSettings(ViewData.TAG_DIAGRAM_HIDDEN_TYPES));
		validate();
	}
	
	public void updateCheckBox(LayerManager manager, String property)
	{
		JCheckBox checkBox = findCheckBox(property);

		if (property.equals(SCOPE_BOX))
			checkBox.setSelected(manager.isScopeBoxVisible());
	
		else if (property.equals(Target.OBJECT_NAME))
			checkBox.setSelected(manager.isTypeVisible(DiagramTargetCell.class));
		
		else if (property.equals(Strategy.OBJECT_NAME))
			checkBox.setSelected(manager.isTypeVisible(DiagramStrategyCell.class));
		
		else if (property.equals(FactorLink.OBJECT_NAME))
			checkBox.setSelected(manager.areFactorLinksVisible());
		
		else if (property.equals(FactorLink.OBJECT_NAME_TARGETLINK))
			checkBox.setSelected(manager.areTargetLinksVisible());
		
		else if (property.equals(Goal.OBJECT_NAME))
			checkBox.setSelected(manager.areGoalsVisible());
		
		else if (property.equals(Objective.OBJECT_NAME))
			checkBox.setSelected(manager.areObjectivesVisible());
		
		else if (property.equals(Indicator.OBJECT_NAME))
			checkBox.setSelected(manager.areIndicatorsVisible());
		
		else if (property.equals(TextBox.OBJECT_NAME))
			checkBox.setSelected(manager.areTextBoxesVisible());
		
		else if (property.equals(GroupBox.OBJECT_NAME))
			checkBox.setSelected(manager.areGroupBoxesVisible());
		
		else if (property.equals(FactorLink.OBJECT_NAME_STRESS))
			checkBox.setSelected(manager.areStressesVisible());
	}
	
	public CodeList getLegendSettings()
	{
		CodeList hiddenTypes = new CodeList();
		ChoiceItem[] choices = new DiagramLegendQuestion().getChoices();
		for (int i=0; i<choices.length; ++i)
		{
			if (!isSelected(choices[i].getCode()))
				hiddenTypes.add(choices[i].getCode());
		}
		return hiddenTypes;
	}
	
	public void updateLegendPanel(CodeList hiddenTypes)
	{
		Object[] keys = checkBoxes.keySet().toArray();
		for (int i=0; i<keys.length; ++i)
		{
			findCheckBox(keys[i]).setSelected(true);
			setLegendVisibilityOfFacactorCheckBoxes(getLayerManager(), keys[i].toString());
		}
		
		for (int i=0; i<hiddenTypes.size(); ++i)
		{
			findCheckBox(hiddenTypes.get(i)).setSelected(false);
			setLegendVisibilityOfFacactorCheckBoxes(getLayerManager(), hiddenTypes.get(i));
		}

		updateVisiblity();
	}

	private void updateVisiblity()
	{
		mainWindow.preventActionUpdates();
		try
		{
			mainWindow.getDiagramView().updateVisibilityOfFactorsAndClearSelectionModel();
			mainWindow.updateStatusBar();
		}
		finally
		{
			mainWindow.allowActionUpdates();
			mainWindow.updateActionsAndStatusBar();
		}
	}
		
	private LayerManager getLayerManager()
	{
		return mainWindow.getProject().getLayerManager();
	}
	
	public static final String SCOPE_BOX = "ScopeBox";

	MainWindow mainWindow;
	JCheckBox targetLinkCheckBox;
}