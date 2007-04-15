/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.viability.TargetViabilityTreeManagementPanel;
import org.conservationmeasures.eam.icons.ContributingFactorIcon;
import org.conservationmeasures.eam.icons.DirectThreatIcon;
import org.conservationmeasures.eam.icons.StrategyIcon;
import org.conservationmeasures.eam.icons.TargetIcon;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.StatusQuestion;
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

	public void setCurrentDiagramFactor(DiagramComponent diagram, DiagramFactor diagramFactor)
	{
		setLayout(new BorderLayout());
		removeAll();
		try
		{
			currentDiagramFactor = diagramFactor;
			add(createLabelBar(currentDiagramFactor),	BorderLayout.BEFORE_FIRST_LINE);
			add(createTabbedPane(currentDiagramFactor), BorderLayout.CENTER);
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
		ORef factorORef = diagramFactor.getWrappedORef();
		Factor factor = (Factor) getProject().findObject(factorORef);
		grid = new FactorInputPanel(getProject(), factorORef);
		
		grid.addFieldWithCustomLabel(grid.createStringField(Factor.TAG_LABEL), createFactorTypeLabel(factor));
		
		if (factor.isTarget())
			grid.addField(createTargetStatusField(factor));
		
		grid.setObjectRef(factorORef);
		return grid;
	}
	
	private UiLabel createFactorTypeLabel(Factor factor)
	{
		if(factor.isDirectThreat())
			return new UiLabel(Factor.OBJECT_NAME_THREAT, new DirectThreatIcon(), UiLabel.LEADING);
		if (factor.isContributingFactor())
			return new UiLabel(Factor.OBJECT_NAME_CONTRIBUTING_FACTOR, new ContributingFactorIcon(), UiLabel.LEADING);
		if (factor.isStrategy()) 
			return new UiLabel(Strategy.OBJECT_NAME, new StrategyIcon(), UiLabel.LEADING);
		if (factor.isTarget())
			return new UiLabel(Target.OBJECT_NAME, new TargetIcon(), UiLabel.LEADING);
		
		throw new RuntimeException("Unknown factor type");
	}
	
	private ObjectDataInputField createTargetStatusField(Factor factor)
	{
		ObjectDataInputField field =  grid.createReadOnlyChoiceField(ObjectType.TARGET,new StatusQuestion(Target.PSEUDO_TAG_TARGET_VIABILITY));
		return field;
	}
	
	private Component createTabbedPane(DiagramFactor diagramFactor) throws Exception
	{
		tabs = new JTabbedPane();
		tabs.setFocusable(false);
		detailsTab = new FactorDetailsTab(getProject(), diagramFactor);
		
		tabs.addTab(detailsTab.getPanelDescription(), detailsTab.getIcon(), detailsTab);

		indicatorsTab = new IndicatorListManagementPanel(getProject(), mainWindow, getCurrentDiagramFactor().getWrappedORef(), mainWindow.getActions());
		tabs.addTab(indicatorsTab.getPanelDescription(), indicatorsTab.getIcon(), indicatorsTab );
		
		Factor factor = (Factor) getProject().findObject(diagramFactor.getWrappedORef());
		
		if(factor.canHaveObjectives())
		{
			objectivesTab = new ObjectiveListManagementPanel(getProject(), mainWindow, getCurrentDiagramFactor().getWrappedORef(), mainWindow.getActions());
			tabs.addTab(objectivesTab.getPanelDescription(), objectivesTab.getIcon(),  objectivesTab);
		}
		
		if(factor.canHaveGoal())
		{
			goalsTab = new GoalListManagementPanel(getProject(), mainWindow, getCurrentDiagramFactor().getWrappedORef(), mainWindow.getActions());
			tabs.addTab(goalsTab.getPanelDescription(), goalsTab.getIcon(), goalsTab );
		}
		
		if(factor.isStrategy())
		{
			activitiesTab = new ActivityListManagementPanel(getProject(), mainWindow, getCurrentDiagramFactor().getWrappedORef(), mainWindow.getActions());
			tabs.addTab(activitiesTab.getPanelDescription(), activitiesTab.getIcon() , activitiesTab);
		}
		
		if (factor.isTarget())
		{
			viabilityTab = new TargetViabilityTreeManagementPanel(getProject(), mainWindow, getCurrentDiagramFactor().getWrappedId(), mainWindow.getActions());
			if (factor.getData(Target.TAG_VIABILITY_MODE).equals(ViabilityModeQuestion.TNC_STYLE_CODE))
			{
				handleViabilityTabON();
			}
		}
		
		return tabs;
	}
	
	class FactorDetailsTab extends ModelessDialogPanel
	{
		public FactorDetailsTab(Project projectToUse, DiagramFactor diagramFactor) throws Exception
		{
			realPanel = new FactorDetailsPanel(projectToUse, diagramFactor);
			add(new FastScrollPane(realPanel));
		}

		public void dispose()
		{
			realPanel.dispose();
			super.dispose();
		}

		public BaseObject getObject()
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

		public FactorInputPanel(Project projectToUse, ORef oRefToUse)
		{
			super(projectToUse, oRefToUse);
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


	public void updateAllSplitterLocations()
	{
		if (indicatorsTab != null)
			indicatorsTab.updateSplitterLocation();
		
		if (objectivesTab != null)
			objectivesTab.updateSplitterLocation();
		
		if (goalsTab != null)
			goalsTab.updateSplitterLocation();
		
		if (activitiesTab != null)
			activitiesTab.updateSplitterLocation();
		
		if (viabilityTab != null)
			viabilityTab.updateSplitterLocation();
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		//FIXME: refactor entire tab add remove mechisisim (Richard, with Kevin)
		if (event.isSetDataCommandWithThisTypeAndTag(ObjectType.TARGET, Target.TAG_VIABILITY_MODE))
		{
			CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
			String value = cmd.getDataValue();
			if (value.equals(ViabilityModeQuestion.TNC_STYLE_CODE))
			{
				handleViabilityTabON();
			}
			else
			{
				handleViabilityTabOFF();
			}
			
			SwingUtilities.getWindowAncestor(detailsTab).pack();
			viabilityTab.updateSplitterLocation();
			goalsTab.updateSplitterLocation();
			indicatorsTab.updateSplitterLocation();
			validate();
		}
	}

	private void handleViabilityTabOFF()
	{
		tabs.remove(tabs.indexOfComponent(viabilityTab));
		tabs.addTab(indicatorsTab.getPanelDescription(), indicatorsTab.getIcon(), indicatorsTab);
		tabs.addTab(goalsTab.getPanelDescription(), goalsTab.getIcon(), goalsTab );
	}

	private void handleViabilityTabON()
	{
		tabs.addTab(viabilityTab.getPanelDescription(), viabilityTab.getIcon(), viabilityTab);
		tabs.remove(tabs.indexOfComponent(goalsTab));
		tabs.remove(tabs.indexOfComponent(indicatorsTab));
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
	FactorInputPanel grid;


}
