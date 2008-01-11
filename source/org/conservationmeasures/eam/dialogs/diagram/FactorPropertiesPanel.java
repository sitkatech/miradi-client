/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.diagram;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.activity.ActivityListManagementPanel;
import org.conservationmeasures.eam.dialogs.base.DisposablePanel;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTabbedPane;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.dialogs.goal.GoalListManagementPanel;
import org.conservationmeasures.eam.dialogs.objective.ObjectiveListManagementPanel;
import org.conservationmeasures.eam.dialogs.objective.ObjectiveListTablePanel;
import org.conservationmeasures.eam.dialogs.stress.StressListManagementPanel;
import org.conservationmeasures.eam.dialogs.viability.TargetViabilityTreeManagementPanel;
import org.conservationmeasures.eam.icons.ContributingFactorIcon;
import org.conservationmeasures.eam.icons.DirectThreatIcon;
import org.conservationmeasures.eam.icons.GroupBoxIcon;
import org.conservationmeasures.eam.icons.IntermediateResultIcon;
import org.conservationmeasures.eam.icons.StrategyIcon;
import org.conservationmeasures.eam.icons.TargetIcon;
import org.conservationmeasures.eam.icons.TextBoxIcon;
import org.conservationmeasures.eam.icons.ThreatReductionResultIcon;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.GroupBox;
import org.conservationmeasures.eam.objects.IntermediateResult;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.TextBox;
import org.conservationmeasures.eam.objects.ThreatReductionResult;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.DirectThreatQuestion;
import org.conservationmeasures.eam.questions.StatusQuestion;
import org.conservationmeasures.eam.questions.ViabilityModeQuestion;
import org.martus.swing.UiLabel;

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
		if (stressTab != null)
			stressTab.dispose();
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
			case TAB_STRESS:
				tabs.setSelectedComponent(stressTab);
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
		
		grid.addFieldWithCustomLabel(grid.createExpandableField(Factor.TAG_LABEL), createFactorTypeLabel(factor));
		
		if (factor.isTarget())
		{
			grid.addField(createTargetStatusField(factor));
			grid.addField(grid.createChoiceField(ObjectType.TARGET, new ViabilityModeQuestion(Target.TAG_VIABILITY_MODE)));
		}
		
		if (factor.isThreatReductionResult())
		{
			grid.addField(grid.createReadOnlyChoiceField(ObjectType.THREAT_REDUCTION_RESULT, new DirectThreatQuestion(mainWindow.getProject(), ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF)));
		}
		
		grid.setObjectRef(factorORef);
		return grid;
	}
	
	private UiLabel createFactorTypeLabel(Factor factor)
	{
		if(factor.isDirectThreat())
			return new PanelTitleLabel(EAM.fieldLabel(Cause.getObjectType(), Cause.OBJECT_NAME_THREAT), new DirectThreatIcon(), UiLabel.LEADING);
		
		if (factor.isContributingFactor())
			return new PanelTitleLabel(EAM.fieldLabel(Cause.getObjectType(), Cause.OBJECT_NAME_CONTRIBUTING_FACTOR), new ContributingFactorIcon(), UiLabel.LEADING);
		
		if (factor.isStrategy())
			return new PanelTitleLabel(EAM.fieldLabel(Strategy.getObjectType(), Strategy.OBJECT_NAME), new StrategyIcon(), UiLabel.LEADING);
		
		if (factor.isTarget())
			return new PanelTitleLabel(EAM.fieldLabel(Target.getObjectType(), Target.OBJECT_NAME), new TargetIcon(), UiLabel.LEADING);
		
		if (factor.isIntermediateResult())
			return new PanelTitleLabel(EAM.fieldLabel(IntermediateResult.getObjectType(), IntermediateResult.OBJECT_NAME), new IntermediateResultIcon(), UiLabel.LEADING);

		if (factor.isThreatReductionResult())
			return new PanelTitleLabel(EAM.fieldLabel(ThreatReductionResult.getObjectType(), ThreatReductionResult.OBJECT_NAME), new ThreatReductionResultIcon(), UiLabel.LEADING);
		
		if (factor.isTextBox())
			return new PanelTitleLabel(EAM.fieldLabel(TextBox.getObjectType(), TextBox.OBJECT_NAME), new TextBoxIcon(), UiLabel.LEADING);
		
		if (factor.isGroupBox())
			return new PanelTitleLabel(EAM.fieldLabel(GroupBox.getObjectType(), GroupBox.OBJECT_NAME), new GroupBoxIcon(), UiLabel.LEADING);
		
		throw new RuntimeException("Unknown factor type");
	}
	
	private ObjectDataInputField createTargetStatusField(Factor factor)
	{
		ObjectDataInputField field =  grid.createReadOnlyChoiceField(ObjectType.TARGET,new StatusQuestion(Target.PSEUDO_TAG_TARGET_VIABILITY));
		return field;
	}
	
	private Component createTabbedPane(DiagramFactor diagramFactor) throws Exception
	{
		tabs = new PanelTabbedPane();
		tabs.setFocusable(false);
		detailsTab = new FactorSummaryScrollablePanel(getProject(), diagramFactor);
		
		tabs.addTab(detailsTab.getPanelDescription(), detailsTab.getIcon(), detailsTab);
		Factor factor = (Factor) getProject().findObject(diagramFactor.getWrappedORef());

		if (factor.canHaveIndicators())
		{
			indicatorsTab = new TargetViabilityTreeManagementPanel(mainWindow, getCurrentDiagramFactor().getWrappedORef(), mainWindow.getActions());
			tabs.addTab(indicatorsTab.getPanelDescription(), indicatorsTab.getIcon(), indicatorsTab );
		}
		
		if(factor.canHaveObjectives())
		{
			ObjectiveListTablePanel objectListPanel = new ObjectiveListTablePanel(getProject(), mainWindow.getActions(), getCurrentDiagramFactor().getWrappedORef());
			objectivesTab = new ObjectiveListManagementPanel(getProject(), mainWindow, getCurrentDiagramFactor().getWrappedORef(), mainWindow.getActions(), objectListPanel);
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
			if (getProject().getMetadata().isStressBasedThreatRatingMode())
			{
				stressTab = new StressListManagementPanel(getProject(), mainWindow, getCurrentDiagramFactor().getWrappedORef(), mainWindow.getActions());
				tabs.addTab(stressTab.getPanelDescription(), stressTab.getIcon(), stressTab);
			}
		}
		
		return tabs;
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
		
		if (stressTab != null)
			stressTab.updateSplitterLocation();
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		//TODO: refactor entire tab add remove mechisisim
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
			stressTab.updateSplitterLocation();
			goalsTab.updateSplitterLocation();
			indicatorsTab.updateSplitterLocation();
			validate();
		}
	}

	private void handleViabilityTabOFF()
	{
		tabs.remove(tabs.indexOfComponent(viabilityTab));
		tabs.addTab(indicatorsTab.getPanelDescription(), indicatorsTab.getIcon(), indicatorsTab);
	}

	private void handleViabilityTabON()
	{
		tabs.addTab(viabilityTab.getPanelDescription(), viabilityTab.getIcon(), viabilityTab);
		tabs.remove(tabs.indexOfComponent(indicatorsTab));
	}

	static final int MAX_LABEL_LENGTH = 40;
	public static final int TAB_DETAILS = 0;
	public static final int TAB_INDICATORS = 1;
	public static final int TAB_OBJECTIVES = 2;
	public static final int TAB_GOALS = 3;
	public static final int TAB_VIABILITY = 4;
	public static final int TAB_STRESS = 5;

	protected JTabbedPane tabs;
	private FactorSummaryScrollablePanel detailsTab;
	private ObjectiveListManagementPanel objectivesTab;
	private TargetViabilityTreeManagementPanel indicatorsTab;
	private GoalListManagementPanel goalsTab;
	private TargetViabilityTreeManagementPanel viabilityTab;
	private StressListManagementPanel stressTab;
	private ActivityListManagementPanel activitiesTab;
	private MainWindow mainWindow;
	private DiagramComponent diagram;
	private DiagramFactor currentDiagramFactor;
	private FactorInputPanel grid;
}
