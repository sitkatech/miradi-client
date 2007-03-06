/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.icons.ContributingFactorIcon;
import org.conservationmeasures.eam.icons.DirectThreatIcon;
import org.conservationmeasures.eam.icons.StrategyIcon;
import org.conservationmeasures.eam.icons.TargetIcon;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.FactorCommandHelper;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.DialogGridPanel;
import org.conservationmeasures.eam.utils.UiTextFieldWithLengthLimit;
import org.martus.swing.UiLabel;
import org.martus.swing.UiTextField;

public class FactorPropertiesPanel extends DisposablePanel
{
	public FactorPropertiesPanel(MainWindow parent,DiagramComponent diagramToUse)
	{
		mainWindow = parent;
		diagram = diagramToUse;
	}
	
	public void dispose()
	{
		detailsTab.dispose();
		if(indicatorsTab != null)
			indicatorsTab.dispose();
		if(goalsTab != null)
			goalsTab.dispose();
		if(objectivesTab != null)
			objectivesTab.dispose();
		if(activitiesTab != null)
			activitiesTab.dispose();
		if(viabilityTab != null)
			viabilityTab.dispose();
		super.dispose();
	}
	
	public void selectTab(int tabIdentifier)
	{
		switch(tabIdentifier)
		{
			case TAB_OBJECTIVES:
				tabs.setSelectedComponent(objectivesTab);
				break;
			case TAB_INDICATORS:
				tabs.setSelectedComponent(indicatorsTab);
				break;
			case TAB_GOALS:
				tabs.setSelectedComponent(goalsTab);
				break;
			case TAB_VIABILITY:
				tabs.setSelectedComponent(viabilityTab);
				break;
			default:
				tabs.setSelectedComponent(detailsTab);
				break;
		}
	}

	public void setCurrentDiagramFactor(DiagramComponent diagram, DiagramFactor diagramFactor)
	{
		this.setLayout(new BorderLayout());
		this.removeAll();
		try
		{
			currentDiagramFactor = diagramFactor;
			this.add(createLabelBar(currentDiagramFactor),
					BorderLayout.BEFORE_FIRST_LINE);
			this.add(createTabbedPane(currentDiagramFactor), BorderLayout.CENTER);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error reading factor information:" + e.getMessage());
		}
	}

	public DiagramFactor getCurrentDiagramFactor()
	{
		return currentDiagramFactor;
	}

	public FactorId getCurrentFactorId()
	{
		return getCurrentDiagramFactor().getWrappedId();
	}

	private Component createLabelBar(DiagramFactor diagramFactor)
	{
		createTextField(diagramFactor.getLabel(), MAX_LABEL_LENGTH);

		DialogGridPanel grid = new DialogGridPanel();

		grid.add(new UiLabel(EAM.fieldLabel(ObjectType.FACTOR, "Label")));
		grid.add(textField);

		grid.add(new UiLabel(EAM.fieldLabel(ObjectType.FACTOR, "Type")));
		
		if(diagramFactor.isDirectThreat())
			grid.add(new UiLabel(Factor.OBJECT_NAME_THREAT, new DirectThreatIcon(), UiLabel.LEADING));
		else if (diagramFactor.isContributingFactor())
			grid.add(new UiLabel(Factor.OBJECT_NAME_CONTRIBUTING_FACTOR, new ContributingFactorIcon(), UiLabel.LEADING));
		else if (diagramFactor.isStrategy()) 
			grid.add(new UiLabel(Strategy.OBJECT_NAME, new StrategyIcon(), UiLabel.LEADING));
		else if (diagramFactor.isTarget())
			grid.add(new UiLabel(Target.OBJECT_NAME, new TargetIcon(), UiLabel.LEADING));

		grid.add(new UiLabel());
		return grid;
	}
	
	private Component createTabbedPane(DiagramFactor diagramFactor) throws Exception
	{
		tabs = new JTabbedPane();
		tabs.setFocusable(false);
		detailsTab = new FactorDetailsTab(getProject(), diagramFactor);
		
		tabs.addTab(detailsTab.getPanelDescription(), detailsTab.getIcon(), detailsTab);

		indicatorsTab = new IndicatorListManagementPanel(getProject(), mainWindow, getCurrentDiagramFactor().getWrappedId(), mainWindow.getActions());
		tabs.addTab(indicatorsTab.getPanelDescription(), indicatorsTab.getIcon(), indicatorsTab );
		
		if(diagramFactor.canHaveObjectives())
		{
			objectivesTab = new ObjectiveListManagementPanel(getProject(), mainWindow, getCurrentDiagramFactor().getWrappedId(), mainWindow.getActions());
			tabs.addTab(objectivesTab.getPanelDescription(), objectivesTab.getIcon(),  objectivesTab);
		}
		
		if(diagramFactor.canHaveGoal())
		{
			goalsTab = new GoalListManagementPanel(getProject(), mainWindow, getCurrentDiagramFactor().getWrappedId(), mainWindow.getActions());
			tabs.addTab(goalsTab.getPanelDescription(), goalsTab.getIcon(), goalsTab );
		}
		
		if(diagramFactor.isStrategy())
		{
			activitiesTab = new ActivityListManagementPanel(getProject(), mainWindow, getCurrentFactorId(), mainWindow.getActions());
			tabs.addTab(activitiesTab.getPanelDescription(), activitiesTab.getIcon() , activitiesTab);
		}
		
		if (diagramFactor.isTarget())
		{
			viabilityTab = new TargetViabilityTreeManagementPanel(getProject(), mainWindow, getCurrentDiagramFactor().getWrappedId(), mainWindow.getActions());
			tabs.addTab(viabilityTab.getPanelDescription(), viabilityTab.getIcon(), viabilityTab );
		}
		
		return tabs;
	}
	
	class FactorDetailsTab extends ModelessDialogPanel
	{
		public FactorDetailsTab(Project projectToUse, DiagramFactor diagramFactor) throws Exception
		{
			realPanel = new FactorDetailsPanel(projectToUse, diagramFactor);
			add(new JScrollPane(realPanel));
		}

		public void dispose()
		{
			realPanel.dispose();
			super.dispose();
		}

		public EAMObject getObject()
		{
			return realPanel.getObject();
		}

		public String getPanelDescription()
		{
			return realPanel.getPanelDescription();
		}
		
		public Icon getIcon()
		{
			return realPanel.getIcon();
		}
		
		FactorDetailsPanel realPanel;
	}


	private Component createTextField(String initialText, int maxLength)
	{
		textField = new UiTextFieldWithLengthLimit(maxLength);
		textField.addFocusListener(new LabelFocusHandler());
		textField.requestFocus(true);

		textField.setText(initialText);
		textField.selectAll();

		JPanel component = new JPanel(new BorderLayout());
		component.add(textField, BorderLayout.LINE_START);
		return component;
	}

	class LabelFocusHandler implements FocusListener
	{
		public void focusGained(FocusEvent event)
		{
		}

		public void focusLost(FocusEvent event)
		{
			String newText = getText();
			if(newText.equals(getCurrentDiagramFactor().getLabel()))
				return;
			try
			{
				CommandSetObjectData cmd = FactorCommandHelper
						.createSetLabelCommand(getCurrentFactorId(), newText);
				getProject().executeCommand(cmd);
			}
			catch(CommandFailedException e)
			{
				EAM.logException(e);
				EAM.errorDialog("That action failed due to an unknown error");
			}
		}
	}



	private Project getProject()
	{
		return getDiagram().getProject();
	}

	private DiagramComponent getDiagram()
	{
		return diagram;
	}

	private String getText()
	{
		return textField.getText();
	}

	public void setAllTabSplitterLocationsToMiddle()
	{
		if (indicatorsTab != null)
			indicatorsTab.updateSplitterLocationToMiddle();
		
		if (objectivesTab != null)
			objectivesTab.updateSplitterLocationToMiddle();
		
		if (goalsTab != null)
			goalsTab.updateSplitterLocationToMiddle();
		
		if (activitiesTab != null)
			activitiesTab.updateSplitterLocationToMiddle();
		
		if (viabilityTab != null)
			viabilityTab.updateSplitterLocationToMiddle();
	}

	static final int MAX_LABEL_LENGTH = 40;
	public static final int TAB_DETAILS = 0;
	public static final int TAB_INDICATORS = 1;
	public static final int TAB_OBJECTIVES = 2;
	public static final int TAB_GOALS = 3;
	public static final int TAB_VIABILITY = 4;

	JTabbedPane tabs;
	FactorDetailsTab detailsTab;
	ObjectiveListManagementPanel objectivesTab;
	IndicatorListManagementPanel indicatorsTab;
	GoalListManagementPanel goalsTab;
	TargetViabilityTreeManagementPanel viabilityTab;
	ActivityListManagementPanel activitiesTab;
	MainWindow mainWindow;
	DiagramComponent diagram;
	DiagramFactor currentDiagramFactor;
	UiTextField textField;
	boolean ignoreObjectiveChanges;
	

}
