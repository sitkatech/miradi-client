/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogfields.ObjectReadonlyChoiceField;
import org.conservationmeasures.eam.icons.ContributingFactorIcon;
import org.conservationmeasures.eam.icons.DirectThreatIcon;
import org.conservationmeasures.eam.icons.StrategyIcon;
import org.conservationmeasures.eam.icons.TargetIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.TargetStatusQuestion;
import org.conservationmeasures.eam.questions.ViabilityModeQuestion;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.martus.swing.UiLabel;
import org.martus.swing.UiTextField;

public class FactorPropertiesPanel extends DisposablePanel implements CommandExecutedListener
{
	public FactorPropertiesPanel(MainWindow parent,DiagramComponent diagramToUse)
	{
		mainWindow = parent;
		diagram = diagramToUse;
		getProject().addCommandExecutedListener(this);
	}
	
	//TODO: can put a loop of disposable panels and move the code to DIsposablePanel passing in list
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
		if(grid != null)
			grid.dispose();
		getProject().removeCommandExecutedListener(this);
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

	public void setCurrentDiagramFactor(DiagramComponent diagram, FactorCell diagramFactor)
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

	public FactorCell getCurrentDiagramFactor()
	{
		return currentDiagramFactor;
	}

	public FactorId getCurrentFactorId()
	{
		return getCurrentDiagramFactor().getWrappedId();
	}

	private Component createLabelBar(FactorCell diagramFactor)
	{
		grid = new FactorInputPanel(getProject(), diagramFactor.getUnderlyingObject().getId());
		
		grid.addField(grid.createStringField(Factor.TAG_LABEL, MAX_LABEL_LENGTH));
		if(diagramFactor.isDirectThreat())
			grid.addLine(new UiLabel(Factor.OBJECT_NAME_THREAT, new DirectThreatIcon(), UiLabel.LEADING), new JPanel());
		else if (diagramFactor.isContributingFactor())
			grid.addLine(new UiLabel(Factor.OBJECT_NAME_CONTRIBUTING_FACTOR, new ContributingFactorIcon(), UiLabel.LEADING), new JPanel());
		else if (diagramFactor.isStrategy()) 
			grid.addLine(new UiLabel(Strategy.OBJECT_NAME, new StrategyIcon(), UiLabel.LEADING), new JPanel());
		else if (diagramFactor.isTarget())
		{
			grid.addLine(new UiLabel(Target.OBJECT_NAME, new TargetIcon(), UiLabel.LEADING), new JPanel());
			grid.addField(getTargetRating(diagramFactor.getUnderlyingObject()));
		}
		
		grid.add(new UiLabel());
		return grid;
	}
	
	
	private ObjectDataInputField getTargetRating(Factor factor)
	{
		ObjectDataInputField field =  new ObjectReadonlyChoiceField(getProject(), ObjectType.FACTOR, factor.getId(), new TargetStatusQuestion(Target.TAG_TARGET_STATUS));
		field.setText(factor.getData(Target.TAG_TARGET_STATUS));
		return field;
	}
	
	private Component createTabbedPane(FactorCell diagramFactor) throws Exception
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
		public FactorDetailsTab(Project projectToUse, FactorCell diagramFactor) throws Exception
		{
			realPanel = new FactorDetailsPanel(projectToUse, diagramFactor);
			add(new FastScrollPane(realPanel));
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



	class FactorInputPanel extends ObjectDataInputPanel
	{

		public FactorInputPanel(Project projectToUse, BaseId idToUse)
		{
			super(projectToUse, ObjectType.FACTOR, idToUse);
		}

		public String getPanelDescription()
		{
			return "";
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
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		if (event.isSetDataCommandWithThisTypeAndTag(ObjectType.FACTOR, Target.TAG_VIABILITY_MODE))
		{
			CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
			String value = cmd.getDataValue();
			if (value.equals(ViabilityModeQuestion.TNC_STYLE_CODE))
				tabs.addTab(viabilityTab.getPanelDescription(), viabilityTab.getIcon(), viabilityTab);
			else
				tabs.remove(tabs.indexOfComponent(viabilityTab));
		}
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
	FactorCell currentDiagramFactor;
	UiTextField textField;
	boolean ignoreObjectiveChanges;
	FactorInputPanel grid;


}
