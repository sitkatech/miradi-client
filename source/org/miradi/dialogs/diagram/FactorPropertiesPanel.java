/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.diagram;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.martus.swing.UiLabel;
import org.miradi.diagram.DiagramComponent;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.activity.ActivityListManagementPanel;
import org.miradi.dialogs.base.ModelessDialogPanel;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTabbedPane;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.dialogs.goal.GoalListManagementPanel;
import org.miradi.dialogs.indicator.SimpleViabilityPanel;
import org.miradi.dialogs.objective.ObjectiveListManagementPanel;
import org.miradi.dialogs.objective.ObjectiveListTablePanel;
import org.miradi.dialogs.stress.StressListManagementPanel;
import org.miradi.dialogs.subTarget.SubTargetManagementPanel;
import org.miradi.dialogs.viability.FactorPropertiesViabilityTreeManagementPanel;
import org.miradi.dialogs.viability.TargetPropertiesKeaViabilityTreeManagementPanel;
import org.miradi.dialogs.viability.TargetViabilityTreeManagementPanel;
import org.miradi.icons.ContributingFactorIcon;
import org.miradi.icons.DirectThreatIcon;
import org.miradi.icons.GroupBoxIcon;
import org.miradi.icons.IntermediateResultIcon;
import org.miradi.icons.StrategyIcon;
import org.miradi.icons.TargetIcon;
import org.miradi.icons.TextBoxIcon;
import org.miradi.icons.ThreatReductionResultIcon;
import org.miradi.ids.FactorId;
import org.miradi.main.AppPreferences;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Factor;
import org.miradi.objects.GroupBox;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.TextBox;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.project.Project;
import org.miradi.questions.DirectThreatQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.ViabilityModeQuestion;

public class FactorPropertiesPanel extends ModelessDialogPanel implements CommandExecutedListener
{
	public FactorPropertiesPanel(MainWindow parent,DiagramComponent diagramToUse)
	{
		mainWindow = parent;
		diagram = diagramToUse;
		setBackground(AppPreferences.getDarkPanelBackgroundColor());
		setBorder(BorderFactory.createEmptyBorder(0,3,3,3));

		getProject().addCommandExecutedListener(this);
	}
	
	//TODO: can put a loop of disposable panels and move the code to DIsposablePanel passing in list
	public void dispose()
	{
		disposeTabs();
		getProject().removeCommandExecutedListener(this);
		super.dispose();
	}

	private void disposeTabs()
	{
		if(detailsTab != null)
		{
			detailsTab.dispose();
			detailsTab = null;
		}
		if(indicatorsTab != null)
		{
			indicatorsTab.dispose();
			indicatorsTab = null;
		}
		if(goalsTab != null)
		{
			goalsTab.dispose();
			goalsTab = null;
		}
		if(objectivesTab != null)
		{
			objectivesTab.dispose();
			objectivesTab = null;
		}
		if(activitiesTab != null)
		{
			activitiesTab.dispose();
			activitiesTab = null;
		}
		if(viabilityTab != null)
		{
			viabilityTab.dispose();
			viabilityTab = null;
		}
		if (simpleViabilityTab != null)
		{
			simpleViabilityTab.dispose();
			simpleViabilityTab = null;
		}
		if (stressTab != null)
		{
			stressTab.dispose();
			stressTab = null;
		}
		if (subTargetTab != null)
		{
			subTargetTab.dispose();
			subTargetTab = null;
		}
		if(grid != null)
		{
			grid.dispose();
			grid = null;
		}
	}
	
	private Component createTabbedPane(DiagramFactor diagramFactor) throws Exception
	{
		tabs = new PanelTabbedPane();
		tabs.setFocusable(false);
		detailsTab = new FactorSummaryScrollablePanel(mainWindow, diagramFactor);
		
		tabs.addTab(detailsTab.getPanelDescription(), detailsTab.getIcon(), detailsTab);
		Factor factor = (Factor) getProject().findObject(diagramFactor.getWrappedORef());

		boolean isKeaViabilityMode = (factor.isTarget() && factor.getData(Target.TAG_VIABILITY_MODE).equals(ViabilityModeQuestion.TNC_STYLE_CODE));
		
		if(factor.canHaveGoal())
		{
			goalsTab = new GoalListManagementPanel(getProject(), mainWindow, getCurrentDiagramFactor().getWrappedORef(), mainWindow.getActions());
			tabs.addTab(goalsTab.getPanelDescription(), goalsTab.getIcon(), goalsTab );
		}
		
		if(factor.canHaveObjectives())
		{
			ObjectiveListTablePanel objectListPanel = new ObjectiveListTablePanel(getProject(), mainWindow.getActions(), getCurrentDiagramFactor().getWrappedORef());
			objectivesTab = new ObjectiveListManagementPanel(getProject(), mainWindow, getCurrentDiagramFactor().getWrappedORef(), mainWindow.getActions(), objectListPanel);
			tabs.addTab(objectivesTab.getPanelDescription(), objectivesTab.getIcon(),  objectivesTab);
		}
		
		if(factor.isStrategy())
		{
			activitiesTab = new ActivityListManagementPanel(getProject(), mainWindow, getCurrentDiagramFactor().getWrappedORef(), mainWindow.getActions());
			tabs.addTab(activitiesTab.getPanelDescription(), activitiesTab.getIcon() , activitiesTab);
		}

		if (factor.canHaveIndicators() && !isKeaViabilityMode && !factor.isTarget())
		{
			indicatorsTab = new FactorPropertiesViabilityTreeManagementPanel(mainWindow, getCurrentDiagramFactor().getWrappedORef(), mainWindow.getActions());
			tabs.addTab(indicatorsTab.getPanelDescription(), indicatorsTab.getIcon(), indicatorsTab );
		}
		
		if ( factor.isTarget() && !isKeaViabilityMode)
		{
			simpleViabilityTab = new SimpleViabilityPanel(mainWindow, getCurrentDiagramFactor().getWrappedORef());
			tabs.addTab(simpleViabilityTab.getPanelDescription(), simpleViabilityTab.getIcon(), simpleViabilityTab);
		}
		
		if(isKeaViabilityMode)
		{
			viabilityTab = new TargetPropertiesKeaViabilityTreeManagementPanel(getProject(), mainWindow, getCurrentDiagramFactor().getWrappedId(), mainWindow.getActions());
			tabs.addTab(viabilityTab.getPanelDescription(), viabilityTab.getIcon(), viabilityTab);
		}

		if (factor.isTarget() && getProject().getMetadata().isStressBasedThreatRatingMode())
		{
			stressTab = new StressListManagementPanel(getProject(), mainWindow, getCurrentDiagramFactor().getWrappedORef(), mainWindow.getActions());
			tabs.addTab(stressTab.getPanelDescription(), stressTab.getIcon(), stressTab);
		}
			
		if (factor.isTarget())
		{
			subTargetTab = new SubTargetManagementPanel(getProject(), mainWindow, getCurrentDiagramFactor().getWrappedORef(), mainWindow.getActions());
			tabs.addTab(subTargetTab.getPanelDescription(), subTargetTab.getIcon(), subTargetTab);
		}
		
		return tabs;
	}
	

	public void selectTab(int tabIdentifier)
	{
		switch(tabIdentifier)
		{
			case TAB_OBJECTIVES:
				tabs.setSelectedComponent(objectivesTab);
				break;
			case TAB_INDICATORS:
			{
				if (indicatorsTab!= null)
					tabs.setSelectedComponent(indicatorsTab);
				else
					tabs.setSelectedComponent(simpleViabilityTab);
				break;
			}
			case TAB_GOALS:
				tabs.setSelectedComponent(goalsTab);
				break;
			case TAB_VIABILITY:
				tabs.setSelectedComponent(viabilityTab);
				break;
			case TAB_STRESS:
				tabs.setSelectedComponent(stressTab);
			case TAB_SUB_TARGET:
				tabs.setSelectedComponent(subTargetTab);
			case TAB_SIMPLE_VIABILITY:
				tabs.setSelectedComponent(simpleViabilityTab);
			default:
				tabs.setSelectedComponent(detailsTab);
				break;
		}
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
		
		if (subTargetTab != null)
			subTargetTab.updateSplitterLocation();
		
		if (simpleViabilityTab != null)
			simpleViabilityTab.updateSplitterLocation();
	}
	
	public void setCurrentDiagramFactor(DiagramComponent diagram, DiagramFactor diagramFactor)
	{
		currentDiagramFactor = diagramFactor;

		rebuildPanel();
	}

	private void rebuildPanel()
	{
		disposeTabs();
		removeAll();
		try
		{
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
			PanelTitleLabel modeLabel = new PanelTitleLabel(EAM.text("Viability Analysis Mode"));
			ObjectDataInputField modeField = grid.createChoiceField(ObjectType.TARGET, Target.TAG_VIABILITY_MODE, new ViabilityModeQuestion());
			grid.addFieldWithCustomLabel(modeField, modeLabel);
		}
		
		if (factor.isThreatReductionResult())
		{
			grid.addField(grid.createReadOnlyChoiceField(ObjectType.THREAT_REDUCTION_RESULT, ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF, new DirectThreatQuestion(mainWindow.getProject())));
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
		ObjectDataInputField field =  grid.createReadOnlyChoiceField(ObjectType.TARGET, Target.PSEUDO_TAG_TARGET_VIABILITY, new StatusQuestion());
		return field;
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

	public BaseObject getObject()
	{
		return currentDiagramFactor;
	}

	public String getPanelDescription()
	{
		return EAM.text("Factor Properties");
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		//TODO: refactor entire tab add remove mechisisim
		if (event.isSetDataCommandWithThisTypeAndTag(ObjectType.TARGET, Target.TAG_VIABILITY_MODE))
		{
			rebuildPanel();
			
			SwingUtilities.getWindowAncestor(detailsTab).pack();
			if(viabilityTab != null)
				viabilityTab.updateSplitterLocation();
			
			if (stressTab != null)
				stressTab.updateSplitterLocation();
			
			subTargetTab.updateSplitterLocation();
			goalsTab.updateSplitterLocation();
			
			if(indicatorsTab != null)
				indicatorsTab.updateSplitterLocation();
			
			if (simpleViabilityTab != null)
				simpleViabilityTab.updateSplitterLocation();
			
			validate();
		}
	}

	public static final int TAB_DETAILS = 0;
	public static final int TAB_INDICATORS = 1;
	public static final int TAB_OBJECTIVES = 2;
	public static final int TAB_GOALS = 3;
	public static final int TAB_VIABILITY = 4;
	public static final int TAB_STRESS = 5;
	public static final int TAB_SUB_TARGET = 6;
	public static final int TAB_SIMPLE_VIABILITY = 7;

	protected JTabbedPane tabs;
	private FactorSummaryScrollablePanel detailsTab;
	private ObjectiveListManagementPanel objectivesTab;
	private TargetViabilityTreeManagementPanel indicatorsTab;
	private GoalListManagementPanel goalsTab;
	private TargetViabilityTreeManagementPanel viabilityTab;
	private SimpleViabilityPanel simpleViabilityTab;
	private StressListManagementPanel stressTab;
	private ActivityListManagementPanel activitiesTab;
	private SubTargetManagementPanel subTargetTab;
	private MainWindow mainWindow;
	private DiagramComponent diagram;
	private DiagramFactor currentDiagramFactor;
	private FactorInputPanel grid;
}
