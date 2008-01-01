/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.conservationmeasures.eam.actions.ActionInsertDraftStrategy;
import org.conservationmeasures.eam.actions.ActionInsertFactorLink;
import org.conservationmeasures.eam.actions.ActionInsertGroupBox;
import org.conservationmeasures.eam.actions.ActionInsertStrategy;
import org.conservationmeasures.eam.actions.ActionInsertTarget;
import org.conservationmeasures.eam.actions.ActionInsertTextBox;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.diagram.cells.DiagramGroupBoxCell;
import org.conservationmeasures.eam.diagram.cells.DiagramStrategyCell;
import org.conservationmeasures.eam.diagram.cells.DiagramTargetCell;
import org.conservationmeasures.eam.diagram.cells.DiagramTextBoxCell;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.icons.GoalIcon;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.icons.ObjectiveIcon;
import org.conservationmeasures.eam.icons.ProjectScopeIcon;
import org.conservationmeasures.eam.icons.StressIcon;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.GroupBox;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.IntermediateResult;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.TextBox;
import org.conservationmeasures.eam.objects.ThreatReductionResult;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.DiagramLegendQuestion;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.umbrella.LegendPanel;

import com.jhlabs.awt.GridLayoutPlus;

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
		JPanel jpanel = new JPanel(new GridLayoutPlus(0,3));
		jpanel.setBackground(AppPreferences.CONTROL_PANEL_BACKGROUND);
		
		addIconLineWithCheckBox(jpanel, ConceptualModelDiagram.getObjectType(), SCOPE_BOX, new ProjectScopeIcon());
		
		addButtonLineWithCheckBox(jpanel, Target.getObjectType(), Target.OBJECT_NAME, actions.get(ActionInsertTarget.class));
		createCustomLegendPanelSection(actions, jpanel);
		
		addButtonLineWithCheckBox(jpanel, Strategy.getObjectType(),Strategy.OBJECT_NAME, actions.get(ActionInsertStrategy.class));
		if (mainWindow.getDiagramView().isStategyBrainstormMode())
		{
			addButtonLineWithoutCheckBox(jpanel, Strategy.getObjectType(), Strategy.OBJECT_NAME_DRAFT, actions.get(ActionInsertDraftStrategy.class));
		}

		addButtonLineWithCheckBox(jpanel, FactorLink.getObjectType(), FactorLink.OBJECT_NAME, actions.get(ActionInsertFactorLink.class));
		addTargetLinkLine(jpanel, FactorLink.getObjectType(), FactorLink.OBJECT_NAME_TARGETLINK);
		
		addIconLineWithCheckBox(jpanel, Goal.getObjectType(), Goal.OBJECT_NAME, new GoalIcon());
		addIconLineWithCheckBox(jpanel, Objective.getObjectType(), Objective.OBJECT_NAME, new ObjectiveIcon());
		addIconLineWithCheckBox(jpanel, Indicator.getObjectType(), Indicator.OBJECT_NAME, new IndicatorIcon());
		addIconLineWithCheckBox(jpanel, FactorLink.getObjectType(), FactorLink.OBJECT_NAME_STRESS, new StressIcon());
		addButtonLineWithCheckBox(jpanel, TextBox.getObjectType(), TextBox.OBJECT_NAME, actions.get(ActionInsertTextBox.class));
		addButtonLineWithCheckBox(jpanel, GroupBox.getObjectType(), GroupBox.OBJECT_NAME, actions.get(ActionInsertGroupBox.class));
		
		return jpanel;
	}

	protected void addTargetLinkLine(JPanel jpanel, int objectType, String objectName)
	{
		jpanel.add(new JLabel(""));
		jpanel.add(new PanelTitleLabel(EAM.fieldLabel(objectType, objectName)));
		targetLinkCheckBox = findCheckBox(objectName);
		jpanel.add(targetLinkCheckBox);
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
		ChoiceItem[] choices = new DiagramLegendQuestion("").getChoices();
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