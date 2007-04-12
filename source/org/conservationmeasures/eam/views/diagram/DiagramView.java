/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

import org.conservationmeasures.eam.actions.ActionConfigureLayers;
import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCreateActivity;
import org.conservationmeasures.eam.actions.ActionCreateBendPoint;
import org.conservationmeasures.eam.actions.ActionCreateGoal;
import org.conservationmeasures.eam.actions.ActionCreateIndicator;
import org.conservationmeasures.eam.actions.ActionCreateKeyEcologicalAttribute;
import org.conservationmeasures.eam.actions.ActionCreateKeyEcologicalAttributeIndicator;
import org.conservationmeasures.eam.actions.ActionCreateObjective;
import org.conservationmeasures.eam.actions.ActionCreateResultsChain;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionDeleteActivity;
import org.conservationmeasures.eam.actions.ActionDeleteBendPoint;
import org.conservationmeasures.eam.actions.ActionDeleteGoal;
import org.conservationmeasures.eam.actions.ActionDeleteIndicator;
import org.conservationmeasures.eam.actions.ActionDeleteKeyEcologicalAttribute;
import org.conservationmeasures.eam.actions.ActionDeleteKeyEcologicalAttributeIndicator;
import org.conservationmeasures.eam.actions.ActionDeleteObjective;
import org.conservationmeasures.eam.actions.ActionDeleteResultsChain;
import org.conservationmeasures.eam.actions.ActionInsertContributingFactor;
import org.conservationmeasures.eam.actions.ActionInsertDirectThreat;
import org.conservationmeasures.eam.actions.ActionInsertDraftStrategy;
import org.conservationmeasures.eam.actions.ActionInsertFactorLink;
import org.conservationmeasures.eam.actions.ActionInsertStrategy;
import org.conservationmeasures.eam.actions.ActionInsertTarget;
import org.conservationmeasures.eam.actions.ActionNudgeDown;
import org.conservationmeasures.eam.actions.ActionNudgeLeft;
import org.conservationmeasures.eam.actions.ActionNudgeRight;
import org.conservationmeasures.eam.actions.ActionNudgeUp;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionPasteWithoutLinks;
import org.conservationmeasures.eam.actions.ActionPrint;
import org.conservationmeasures.eam.actions.ActionProperties;
import org.conservationmeasures.eam.actions.ActionSaveImage;
import org.conservationmeasures.eam.actions.ActionSelectAll;
import org.conservationmeasures.eam.actions.ActionSelectChain;
import org.conservationmeasures.eam.actions.ActionShowFullModelMode;
import org.conservationmeasures.eam.actions.ActionShowResultsChain;
import org.conservationmeasures.eam.actions.ActionShowSelectedChainMode;
import org.conservationmeasures.eam.actions.ActionZoomIn;
import org.conservationmeasures.eam.actions.ActionZoomOut;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.EAMGraphSelectionModel;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.dialogs.FactorPropertiesPanel;
import org.conservationmeasures.eam.dialogs.ModelessDialogWithClose;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.ConceptualModelDiagramPool;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.views.umbrella.DeleteActivity;
import org.conservationmeasures.eam.views.umbrella.SaveImageDoer;
import org.conservationmeasures.eam.wizard.WizardPanel;
import org.martus.swing.Utilities;


public class DiagramView extends TabbedView implements CommandExecutedListener
{
	public DiagramView(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse);
		mode = ViewData.MODE_DEFAULT;
		
		addDiagramViewDoersToMap();
		
		wizardPanel = new WizardPanel(mainWindowToUse, this);
	}

	private void updateToolBar()
	{
		getMainWindow().updateToolBar();
	}
	
	public DiagramComponent getDiagramComponent()
	{
		return diagram;
	}

	public String cardName()
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.DIAGRAM_VIEW_NAME;
	}
	
	public JComponent createToolBar()
	{
		return new DiagramToolBar(getActions(), mode);
	}

	public BufferedImage getImage()
	{
		return diagram.getImage();
	}
	
	public JComponent getPrintableComponent()
	{
		return diagram.getPrintableComponent();
	}
	
	public BaseObject getSelectedObject()
	{
		FactorCell node = diagram.getSelectedFactor();
		if(node == null)
			return null;
		return node.getUnderlyingObject();
	}

	public Properties getPropertiesDoer()
	{
		return propertiesDoer;
	}

	private void addDiagramViewDoersToMap()
	{
		propertiesDoer = new Properties(diagram);

		addDoerToMap(ActionInsertTarget.class, new InsertTargetDoer());
		addDoerToMap(ActionInsertContributingFactor.class, new InsertContributingFactorDoer());
		addDoerToMap(ActionInsertDirectThreat.class, new InsertDirectThreatDoer());
		addDoerToMap(ActionInsertStrategy.class, new InsertStrategyDoer());
		addDoerToMap(ActionInsertDraftStrategy.class, new InsertDraftStrategyDoer());
		addDoerToMap(ActionInsertFactorLink.class, new InsertFactorLinkDoer());
		addDoerToMap(ActionCreateBendPoint.class, new CreateBendPointDoer());
		addDoerToMap(ActionDeleteBendPoint.class, new DeleteBendPointDoer());
		addDoerToMap(ActionCopy.class, new Copy());
		addDoerToMap(ActionSelectAll.class, new SelectAllDoer());
		addDoerToMap(ActionCut.class, new Cut());
		addDoerToMap(ActionDelete.class, new Delete());
		addDoerToMap(ActionPaste.class, new Paste());
		addDoerToMap(ActionPasteWithoutLinks.class, new PasteWithoutLinks());
		addDoerToMap(ActionSelectChain.class, new SelectChain());
		addDoerToMap(ActionProperties.class, propertiesDoer);
		addDoerToMap(ActionPrint.class, new Print());
		addDoerToMap(ActionSaveImage.class, new SaveImageDoer());
		addDoerToMap(ActionConfigureLayers.class, new ConfigureLayers());
		addDoerToMap(ActionShowSelectedChainMode.class, new ShowSelectedChainModeDoer());
		addDoerToMap(ActionShowFullModelMode.class, new ShowFullModelModeDoer());
		addDoerToMap(ActionZoomIn.class, new ZoomIn());
		addDoerToMap(ActionZoomOut.class, new ZoomOut());
		addDoerToMap(ActionNudgeUp.class, new NudgeDoer(KeyEvent.VK_UP)); 
		addDoerToMap(ActionNudgeDown.class, new NudgeDoer(KeyEvent.VK_DOWN));
		addDoerToMap(ActionNudgeLeft.class, new NudgeDoer(KeyEvent.VK_LEFT));
		addDoerToMap(ActionNudgeRight.class, new NudgeDoer(KeyEvent.VK_RIGHT));
		
		addDoerToMap(ActionCreateActivity.class, new CreateActivityDoer());
		addDoerToMap(ActionDeleteActivity.class, new DeleteActivity());

		addDoerToMap(ActionCreateObjective.class, new CreateObjective());
		addDoerToMap(ActionDeleteObjective.class, new DeleteObjective());
		
		addDoerToMap(ActionCreateIndicator.class, new CreateIndicator());
		addDoerToMap(ActionDeleteIndicator.class, new DeleteIndicator());
		
		addDoerToMap(ActionCreateGoal.class, new CreateGoal());
		addDoerToMap(ActionDeleteGoal.class, new DeleteGoal());
		
		addDoerToMap(ActionCreateKeyEcologicalAttribute.class, new CreateKeyEcologicalAttributeDoer());
		addDoerToMap(ActionDeleteKeyEcologicalAttribute.class, new DeleteKeyEcologicalAttributeDoer());
		addDoerToMap(ActionCreateKeyEcologicalAttributeIndicator.class, new CreateKeyEcologicalAttributeIndicatorDoer());
		addDoerToMap(ActionDeleteKeyEcologicalAttributeIndicator.class, new DeleteKeyEcologicalAttributeIndicatorDoer());
		addDoerToMap(ActionCreateResultsChain.class, new CreateResultsChainDoer());
		addDoerToMap(ActionShowResultsChain.class, new ShowResultsChainDoer());
		addDoerToMap(ActionDeleteResultsChain.class, new DeleteResultsChainDoer());
	}
	
	public void createTabs() throws Exception
	{
		DiagramModel diagramModel = new DiagramModel(getProject());
		getProject().setDiagramModel(diagramModel);
		ConceptualModelDiagram conceptualModelDiagram = getDiagramObject();
		diagramModel.fillFrom(conceptualModelDiagram);
		diagramModel.updateProjectScopeBox();
		
		diagram = new DiagramComponent(getMainWindow());
		diagram.setModel(diagramModel);
		diagram.setGraphLayoutCache(diagramModel.getGraphLayoutCache());
		getProject().setSelectionModel(diagram.getEAMGraphSelectionModel());
		ConceptualModelDiagramSplitPane splitPane = new ConceptualModelDiagramSplitPane(getMainWindow(), diagram);

		//TODO fix tab name
		addTab("Main Diagram", splitPane);
		addResultsChainTabs();
	}

	private ConceptualModelDiagram getDiagramObject() throws Exception
	{
		ConceptualModelDiagramPool diagramContentsPool = (ConceptualModelDiagramPool) getProject().getPool(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
		ORefList oRefs = diagramContentsPool.getORefList();
		return getDiagramContentsObject(oRefs);
	}
	
	private ConceptualModelDiagram getDiagramContentsObject(ORefList oRefs) throws Exception
	{
		if (oRefs.size() == 0)
		{
			BaseId id = getProject().createObject(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
			return (ConceptualModelDiagram) getProject().findObject(new ORef(ObjectType.CONCEPTUAL_MODEL_DIAGRAM, id));
		}
		if (oRefs.size() > 1)
		{
			EAM.logVerbose("Found more than one diagram contents inside pool");
		}

		ORef oRef = oRefs.get(0);
		return (ConceptualModelDiagram) getProject().findObject(oRef);
	}

	private void addResultsChainTabs() throws Exception
	{
		IdList resultsChains = getProject().getResultsChainDiagramPool().getIdList();
		for (int i = 0; i < resultsChains.size(); i++)
		{
			DiagramModel diagramModel = new DiagramModel(getProject());
			ResultsChainDiagram resultsChain = (ResultsChainDiagram) getProject().findObject(new ORef(ObjectType.RESULTS_CHAIN_DIAGRAM, resultsChains.get(i)));
			diagramModel.fillFrom(resultsChain);
			
			DiagramComponent resultsChaindiagram = new DiagramComponent(getMainWindow());
			resultsChaindiagram.setModel(diagramModel);
			//FIXME need to create selectionModel and cache
			//resultsChaindiagram.setGraphLayoutCache(getProject().getGraphLayoutCache());
			//getProject().setSelectionModel(resultsChaindiagram.getEAMGraphSelectionModel());
			
			ResultsChainDiagramSplitPane splitPane = new ResultsChainDiagramSplitPane(getMainWindow(), resultsChaindiagram);

			//TODO fix tab name
			addTab("Results Chain "+i, splitPane);
		}
	}
	
	//FIXME should return the model of the currently selected tab
	public DiagramModel getDiagramModel()
	{
		return diagram.getDiagramModel();
	}

	public WizardPanel createWizardPanel() throws Exception
	{
		return wizardPanel;
	}

	public void deleteTabs() throws Exception
	{
	}
	
	public void becomeActive() throws Exception
	{
		super.becomeActive();

		updateToolBar();
		setMode(getViewData().getData(ViewData.TAG_CURRENT_MODE));
	}

	public void becomeInactive() throws Exception
	{
		super.becomeInactive();
		
		// TODO: This should completely tear down the view
		disposeOfNodePropertiesDialog();
		diagram.clearSelection();

		getDiagramModel().dispose();
		getProject().setSelectionModel(null);
		diagram = null;
		updateToolBar();
	}
	
	
	public void setMode(String newMode)
	{
		ORefList hiddenORefs = new ORefList();
		getDiagramComponent().setToDefaultBackgroundColor();
		if (newMode.equals(ViewData.MODE_STRATEGY_BRAINSTORM))
		{
			hiddenORefs = getORefsToHide();
			getDiagramComponent().setBackground(Color.LIGHT_GRAY);
		}
			

		LayerManager manager = getProject().getLayerManager();
		manager.setHiddenORefs(hiddenORefs);
		manager.setMode(newMode);
		mode = newMode;
		updateToolBar();
		getMainWindow().updateStatusBar();
		getDiagramComponent().clearSelection();
		updateVisibilityOfFactors();
	}
	
	public void updateVisibilityOfFactors()
	{
		if (!getMainWindow().getCurrentView().cardName().equals(getViewName()))
				return;
		
		try
		{
			DiagramModel model = getDiagramModel();
			model.updateVisibilityOfFactors();
			
			EAMGraphSelectionModel selectionModel = (EAMGraphSelectionModel) getDiagramComponent().getSelectionModel();
			// TODO: Find a way to avoid the need to test for null here
			if(selectionModel != null)
				selectionModel.clearSelection();
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}


	private ORefList getORefsToHide()
	{
		ORefList oRefsToHide = new ORefList();
		try
		{
			ViewData viewData = getProject().getCurrentViewData();
			ORefList visibleFactorORefs = new ORefList(viewData.getData(ViewData.TAG_CHAIN_MODE_FACTOR_REFS));
			visibleFactorORefs.addAll(getRelatedDraftInterventions(visibleFactorORefs));
			DiagramFactor[] allDiagramFactors = getProject().getAllDiagramFactors();
			for (int i = 0; i < allDiagramFactors.length; ++i)
			{
				DiagramFactor diagramFactor = allDiagramFactors[i];
				ORef ref = diagramFactor.getWrappedORef();
				if (!visibleFactorORefs.contains(ref))
					oRefsToHide.add(ref);
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		return oRefsToHide;
	}
	
	ORefList getRelatedDraftInterventions(ORefList factorORefs) throws Exception
	{
		ORefList draftsToAdd = new ORefList();
		
		DiagramModel diagramModel = getDiagramModel();
		for(int i = 0; i < factorORefs.size(); ++i)
		{
			FactorId nodeId = new FactorId(factorORefs.get(i).getObjectId().asInt());
			Factor node = diagramModel.getFactorCellByWrappedId(nodeId).getUnderlyingObject();
			FactorSet possibleDraftStrategies = diagramModel.getDirectlyLinkedUpstreamNodes(node);
			Iterator iter = possibleDraftStrategies.iterator();
			while(iter.hasNext())
			{
				ORef possibleStrategyORef = ((Factor)iter.next()).getRef();
				if(factorORefs.contains(possibleStrategyORef))
					continue;
				Factor possibleIntervention = getProject().findNode(new FactorId(possibleStrategyORef.getObjectId().asInt()));
				if(possibleIntervention.isStrategy() && possibleIntervention.isStatusDraft())
					draftsToAdd.add(possibleIntervention.getRef());
			}
		}
		
		return draftsToAdd;
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		Command rawCommand = event.getCommand();
		if(!rawCommand.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return;

		try
		{
			CommandSetObjectData cmd = (CommandSetObjectData)rawCommand;
			String newValue = cmd.getDataValue();
			setModeIfRelevant(cmd, newValue);
			updateFactorBoundsIfRelevant(cmd);
			updateFactorLinkIfRelevant(cmd);
			updateScopeIfNeeded(cmd);
			refreshIfNeeded(cmd);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private void updateFactorLinkIfRelevant(CommandSetObjectData cmd) throws Exception
	{
		if (cmd.getObjectType() != ObjectType.DIAGRAM_LINK)
			return;
		
		DiagramFactorLinkId diagramFactorLinkId = (DiagramFactorLinkId) cmd.getObjectId();
		DiagramModel diagramModel = getDiagramModel();
		diagramModel.updateCellFromDiagramFactorLink(diagramFactorLinkId);
	}
	
	private void updateFactorBoundsIfRelevant(CommandSetObjectData cmd) throws Exception
	{
		if (cmd.getObjectType() != ObjectType.DIAGRAM_FACTOR)
			return;
		
		DiagramFactorId diagramFactorId = (DiagramFactorId) cmd.getObjectId();
		DiagramModel diagramModel = getDiagramModel();
		diagramModel.updateCellFromDiagramFactor(diagramFactorId);
	}

	private void setModeIfRelevant(CommandSetObjectData cmd, String newMode)
	{
		try
		{
			ViewData ourViewData = getViewData();
			if(cmd.getObjectType() != ourViewData.getType())
				return;
			if(cmd.getObjectId() != ourViewData.getId())
				return;
			if(!cmd.getFieldTag().equals(ViewData.TAG_CURRENT_MODE))
				return;
			
			setMode(newMode);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unknown error prevented this operation");
		}
	}
	
	private void refreshIfNeeded(CommandSetObjectData cmd)
	{
		// may have added or removed a stress, modified an Annotation short label, etc.
		diagram.repaint(diagram.getBounds());
	}
	
	void updateScopeIfNeeded(CommandSetObjectData cmd)
	{
		if (isScopeTextChange(cmd) || isFactorBoundsChange(cmd))
			getDiagramModel().updateProjectScopeBox();
	}

	private boolean isScopeTextChange(CommandSetObjectData cmd)
	{
		if (cmd.getObjectType() != ObjectType.PROJECT_METADATA)
			return false;
		
		return (cmd.getFieldTag().equals(ProjectMetadata.TAG_SHORT_PROJECT_SCOPE) ||
				cmd.getFieldTag().equals(ProjectMetadata.TAG_SHORT_PROJECT_VISION));
	}

	private boolean isFactorBoundsChange(CommandSetObjectData cmd)
	{
		if (cmd.getObjectType() != ObjectType.DIAGRAM_FACTOR)
			return false;
		return (cmd.getFieldTag().equals(DiagramFactor.TAG_LOCATION) || 
				cmd.getFieldTag().equals(DiagramFactor.TAG_SIZE));
	}

	public void jump(Class stepMarker) throws Exception
	{
		wizardPanel.jump(stepMarker);
	}

	public void showFloatingPropertiesDialog(ModelessDialogWithClose newDialog)
	{
		if(nodePropertiesDlg != null)
			disposeOfNodePropertiesDialog();
		super.showFloatingPropertiesDialog(newDialog);
	}

	public void showNodeProperties(DiagramFactor node, int startingTabIdentifier)
	{
		closeActivePropertiesDialog();
		if(nodePropertiesDlg != null)
			disposeOfNodePropertiesDialog();
		
		nodePropertiesPanel = new FactorPropertiesPanel(getMainWindow(), diagram);
		String title = EAM.text("Title|Factor Properties");
		nodePropertiesDlg = new ModelessDialogWithClose(getMainWindow(), nodePropertiesPanel, title);
		
		nodePropertiesPanel.setCurrentDiagramFactor(diagram, node);
		nodePropertiesPanel.selectTab(startingTabIdentifier);
		nodePropertiesDlg.pack();
		
		Utilities.centerDlg(nodePropertiesDlg);
		nodePropertiesDlg.setVisible(true);
		nodePropertiesPanel.updateAllSplitterLocations();
	}

	private void disposeOfNodePropertiesDialog()
	{
		if(nodePropertiesDlg != null)
			nodePropertiesDlg.dispose();
		nodePropertiesDlg = null;
		nodePropertiesPanel = null;
	}
	
	public void selectionWasChanged()
	{
		if(nodePropertiesDlg == null)
			return;
		
		FactorCell selectedNode = diagram.getSelectedFactor();
		if(selectedNode == null || !selectedNode.equals(nodePropertiesPanel.getCurrentDiagramFactor()))
			disposeOfNodePropertiesDialog();
	}
	
	public String getCurrentMode()
	{
		return mode;
	}

	
	JSplitPane bigSplitter;
	DiagramComponent diagram;
	Properties propertiesDoer;
	String mode;
	
	ModelessDialogWithClose nodePropertiesDlg;
	FactorPropertiesPanel nodePropertiesPanel;
}
