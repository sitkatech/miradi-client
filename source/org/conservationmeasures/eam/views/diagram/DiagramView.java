/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionCloneGoal;
import org.conservationmeasures.eam.actions.ActionCloneIndicator;
import org.conservationmeasures.eam.actions.ActionCloneObjective;
import org.conservationmeasures.eam.actions.ActionConfigureLayers;
import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCreateActivity;
import org.conservationmeasures.eam.actions.ActionCreateBendPoint;
import org.conservationmeasures.eam.actions.ActionCreateConceptualModel;
import org.conservationmeasures.eam.actions.ActionCreateGoal;
import org.conservationmeasures.eam.actions.ActionCreateIndicator;
import org.conservationmeasures.eam.actions.ActionCreateKeyEcologicalAttribute;
import org.conservationmeasures.eam.actions.ActionCreateKeyEcologicalAttributeIndicator;
import org.conservationmeasures.eam.actions.ActionCreateObjective;
import org.conservationmeasures.eam.actions.ActionCreateOrShowResultsChain;
import org.conservationmeasures.eam.actions.ActionCreateResultsChain;
import org.conservationmeasures.eam.actions.ActionCreateSlide;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionDeleteActivity;
import org.conservationmeasures.eam.actions.ActionDeleteBendPoint;
import org.conservationmeasures.eam.actions.ActionDeleteConceptualModel;
import org.conservationmeasures.eam.actions.ActionDeleteGoal;
import org.conservationmeasures.eam.actions.ActionDeleteIndicator;
import org.conservationmeasures.eam.actions.ActionDeleteKeyEcologicalAttribute;
import org.conservationmeasures.eam.actions.ActionDeleteKeyEcologicalAttributeIndicator;
import org.conservationmeasures.eam.actions.ActionDeleteObjective;
import org.conservationmeasures.eam.actions.ActionDeleteResultsChain;
import org.conservationmeasures.eam.actions.ActionDeleteSlide;
import org.conservationmeasures.eam.actions.ActionInsertContributingFactor;
import org.conservationmeasures.eam.actions.ActionInsertDirectThreat;
import org.conservationmeasures.eam.actions.ActionInsertDraftStrategy;
import org.conservationmeasures.eam.actions.ActionInsertFactorLink;
import org.conservationmeasures.eam.actions.ActionInsertIntermediateResult;
import org.conservationmeasures.eam.actions.ActionInsertStrategy;
import org.conservationmeasures.eam.actions.ActionInsertTarget;
import org.conservationmeasures.eam.actions.ActionInsertTextBox;
import org.conservationmeasures.eam.actions.ActionMoveSlideDown;
import org.conservationmeasures.eam.actions.ActionMoveSlideUp;
import org.conservationmeasures.eam.actions.ActionNudgeDown;
import org.conservationmeasures.eam.actions.ActionNudgeLeft;
import org.conservationmeasures.eam.actions.ActionNudgeRight;
import org.conservationmeasures.eam.actions.ActionNudgeUp;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionPasteWithoutLinks;
import org.conservationmeasures.eam.actions.ActionPrint;
import org.conservationmeasures.eam.actions.ActionProperties;
import org.conservationmeasures.eam.actions.ActionRenameResultsChain;
import org.conservationmeasures.eam.actions.ActionSaveImage;
import org.conservationmeasures.eam.actions.ActionSelectAll;
import org.conservationmeasures.eam.actions.ActionSelectChain;
import org.conservationmeasures.eam.actions.ActionShowConceptualModel;
import org.conservationmeasures.eam.actions.ActionShowFullModelMode;
import org.conservationmeasures.eam.actions.ActionShowResultsChain;
import org.conservationmeasures.eam.actions.ActionShowSelectedChainMode;
import org.conservationmeasures.eam.actions.ActionToggleSlideShowPanel;
import org.conservationmeasures.eam.actions.ActionZoomIn;
import org.conservationmeasures.eam.actions.ActionZoomOut;
import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.actions.ToggleSlideShowPanelDoer;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.EAMGraphSelectionModel;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.dialogs.DiagramPanel;
import org.conservationmeasures.eam.dialogs.FactorPropertiesDialog;
import org.conservationmeasures.eam.dialogs.FactorPropertiesPanel;
import org.conservationmeasures.eam.dialogs.ModelessDialogWithClose;
import org.conservationmeasures.eam.dialogs.slideshow.SlideListManagementPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
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
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.objects.SlideShow;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.PointList;
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

	public WizardPanel createWizardPanel() throws Exception
	{
		return wizardPanel;
	}
	
	public void becomeActive() throws Exception
	{
		super.becomeActive();
		createSlideShowPanel();
		add(slideShowPoolManagementPanel, BorderLayout.AFTER_LINE_ENDS);
		hideSidePanel();
	}
	
	//FIXME: need a dispose for project close...then there will  be not need to create/delete the panel each time 
	public void becomeInactive() throws Exception
	{
		slideShowPoolManagementPanel.dispose();
		remove(slideShowPoolManagementPanel);
		slideShowPoolManagementPanel = null;
		super.becomeInactive();
	}
	
	public void hideSidePanel()
	{
		slideShowPoolManagementPanel.setVisible(false);
	}
	
	public void showSidePanel()
	{
		slideShowPoolManagementPanel.setVisible(true);
	}
	
	public boolean isSidePanelVisible()
	{
		if (slideShowPoolManagementPanel==null)
			return false;
		return slideShowPoolManagementPanel.isVisible();
	}
	
	
	private SlideListManagementPanel createSlideShowPanel() throws Exception
	{
		ORef slideShowRef = createSlideShowIfNeeded().getRef(); 
		slideShowPoolManagementPanel =  new SlideListManagementPanel(getProject(), getMainWindow(), slideShowRef, getActions());
		slideShowPoolManagementPanel.updateSplitterLocationToMiddle();
		return slideShowPoolManagementPanel;
	}

	public SlideShow getSlideShow() throws CommandFailedException
	{
		EAMObjectPool pool = getProject().getPool(SlideShow.getObjectType());
		if (pool.size()==0)
		{
			throw new CommandFailedException("Slide Show not found in pool");
		}

		return (SlideShow) getProject().findObject(SlideShow.getObjectType(), pool.getIds()[0]);
	}
	
	private BaseObject createSlideShowIfNeeded() throws CommandFailedException
	{
		ORef oref = ORef.INVALID;
		EAMObjectPool pool = getProject().getPool(SlideShow.getObjectType());
		if (pool.size()==0)
		{
			CommandCreateObject cmd = new CommandCreateObject(SlideShow.getObjectType());
			getProject().executeCommand(cmd);
			oref = cmd.getObjectRef();
		}
		else
			oref = pool.getORefList().get(0);
		
		return getProject().findObject(oref);
	}
	
	private void updateToolBar()
	{
		getMainWindow().updateToolBar();
	}
	
	public DiagramComponent getDiagramComponent()
	{
		if(getCurrentDiagramPanel() == null)
			return null;
		
		return getCurrentDiagramPanel().getdiagramComponent();
	}

	public String cardName()
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.DIAGRAM_VIEW_NAME;
	}
	
	public JToolBar createToolBar()
	{
		return new DiagramToolBar(getActions(), this);
	}

	public BufferedImage getImage()
	{
		return getDiagramComponent().getImage();
	}
	
	public JComponent getPrintableComponent()
	{
		return getDiagramComponent().getPrintableComponent();
	}
	
	public BaseObject getSelectedObject()
	{
		FactorCell node = getDiagramComponent().getSelectedFactor();
		if(node == null)
			return null;
		return node.getUnderlyingObject();
	}

	public PropertiesDoer getPropertiesDoer()
	{
		return propertiesDoer;
	}

	private void addDiagramViewDoersToMap()
	{
		propertiesDoer = new PropertiesDoer();

		addDoerToMap(ActionInsertIntermediateResult.class, new InsertIntermediateResultDoer());
		addDoerToMap(ActionInsertTarget.class, new InsertTargetDoer());
		addDoerToMap(ActionInsertContributingFactor.class, new InsertContributingFactorDoer());
		addDoerToMap(ActionInsertDirectThreat.class, new InsertDirectThreatDoer());
		addDoerToMap(ActionInsertStrategy.class, new InsertStrategyDoer());
		addDoerToMap(ActionInsertDraftStrategy.class, new InsertDraftStrategyDoer());
		addDoerToMap(ActionInsertFactorLink.class, new InsertFactorLinkDoer());
		addDoerToMap(ActionCreateBendPoint.class, new CreateBendPointDoer());
		addDoerToMap(ActionDeleteBendPoint.class, new DeleteBendPointDoer());
		addDoerToMap(ActionRenameResultsChain.class, new RenameResultsChainDoer());
		addDoerToMap(ActionCopy.class, new Copy());
		addDoerToMap(ActionSelectAll.class, new SelectAllDoer());
		addDoerToMap(ActionCut.class, new Cut());
		addDoerToMap(ActionDelete.class, new DeleteSelectedItemDoer());
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
		addDoerToMap(ActionCloneObjective.class, new CloneObjectiveDoer());
		addDoerToMap(ActionDeleteObjective.class, new DeleteObjective());
		
		addDoerToMap(ActionCreateIndicator.class, new CreateIndicator());
		addDoerToMap(ActionCloneIndicator.class, new CloneIndicatorDoer());
		addDoerToMap(ActionDeleteIndicator.class, new DeleteIndicator());
		
		addDoerToMap(ActionCreateGoal.class, new CreateGoal());
		addDoerToMap(ActionCloneGoal.class, new CloneGoalDoer());
		addDoerToMap(ActionDeleteGoal.class, new DeleteGoal());
		
		addDoerToMap(ActionCreateKeyEcologicalAttribute.class, new CreateKeyEcologicalAttributeDoer());
		addDoerToMap(ActionDeleteKeyEcologicalAttribute.class, new DeleteKeyEcologicalAttributeDoer());
		addDoerToMap(ActionCreateKeyEcologicalAttributeIndicator.class, new CreateKeyEcologicalAttributeIndicatorDoer());
		addDoerToMap(ActionDeleteKeyEcologicalAttributeIndicator.class, new DeleteKeyEcologicalAttributeIndicatorDoer());
		addDoerToMap(ActionCreateResultsChain.class, new CreateResultsChainDoer());
		addDoerToMap(ActionShowResultsChain.class, new ShowResultsChainDoer());
		addDoerToMap(ActionDeleteResultsChain.class, new DeleteResultsChainDoer());
		addDoerToMap(ActionShowConceptualModel.class, new ShowConceptualModelDoer());
		addDoerToMap(ActionCreateOrShowResultsChain.class, new CreateOrShowResultsChainDoer());
		addDoerToMap(ActionInsertTextBox.class, new InsertTextBoxDoer());
		addDoerToMap(ActionCreateConceptualModel.class, new CreateConceptualModelDoer());
		addDoerToMap(ActionDeleteConceptualModel.class, new DeleteConceptualModelDoer());
		
		addDoerToMap(ActionCreateSlide.class, new CreateSlideDoer());
		addDoerToMap(ActionDeleteSlide.class, new DeleteSlideDoer());
		addDoerToMap(ActionMoveSlideDown.class, new MoveSlideDownDoer());
		addDoerToMap(ActionMoveSlideUp.class, new MoveSlideUpDoer());
		addDoerToMap(ActionToggleSlideShowPanel.class, new ToggleSlideShowPanelDoer());
	}
	
	public void tabWasSelected()
	{
		if (isStategyBrainstormMode())
		{
			try
			{
				EAMAction actionShowFullModelMode = getActions().get(ActionShowFullModelMode.class);
				actionShowFullModelMode.doAction();
			}
			catch (Exception e)
			{
				EAM.logException(e);
			}
		}
		super.tabWasSelected();
		try
		{
			getMainWindow().preventActionUpdates();
			updateVisibilityOfFactors();
			if (getDiagramComponent()!=null)
				getDiagramComponent().updateDiagramZoomSetting();
		}
		finally
		{
			getMainWindow().allowActionUpdates();
		}
	}

	public void createTabs() throws Exception
	{
		getMainWindow().preventActionUpdates();
		try
		{
			addConceptualModelDiagramTab();
			addResultsChainTabs();
			
			setMode(getViewData().getData(ViewData.TAG_CURRENT_MODE));
		}
		finally
		{
			getMainWindow().allowActionUpdates();
		}
	}

	private void addConceptualModelDiagramTab() throws Exception
	{
		DiagramPanel diagramPanel = new DiagramPanel(getMainWindow(), getProject(), getDiagramObject());
		addTab(EAM.text("Conceptual Model"), diagramPanel);
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

	public DiagramPanel getDiagramPanel()
	{
		return getCurrentDiagramPanel();
	}

	public DiagramPanel getCurrentDiagramPanel()
	{
		return (DiagramPanel) getCurrentTabContents();
	}

	private void addResultsChainTabs() throws Exception
	{
		removeAllResultsChainTabs();
		IdList resultsChains = getProject().getResultsChainDiagramPool().getIdList();
		resultsChains.sort();
		
		for (int i = 0; i < resultsChains.size(); i++)
		{
			DiagramModel diagramModel = new DiagramModel(getProject());
			ResultsChainDiagram resultsChain = (ResultsChainDiagram) getProject().findObject(new ORef(ObjectType.RESULTS_CHAIN_DIAGRAM, resultsChains.get(i)));
			diagramModel.fillFrom(resultsChain);
			
			DiagramComponent resultsChainDiagram = new DiagramComponent(getMainWindow());
			resultsChainDiagram.setModel(diagramModel);

			DiagramPanel diagramPanel = new DiagramPanel(getMainWindow(), getProject(), resultsChain);
			String resultsChainLabel = resultsChain.getData(DiagramObject.TAG_LABEL);
			addTab(resultsChainLabel, diagramPanel);
		}
	}

	public boolean isResultsChainTab()
	{
		int index = getSelectedTabIndex();
		if (index  < 0)
			return false;
		
		DiagramPanel panel = (DiagramPanel)getTabContents(index);
		DiagramObject diagramObject = panel.getDiagramObject();
		if (diagramObject.getType() == ResultsChainDiagram.getObjectType())
			return true;
		
		return false;
	}
	
	private void removeAllResultsChainTabs()
	{
		final int CONCEPTUAL_MODEL_TAB_COUNT = 1;
		while(getTabCount() > CONCEPTUAL_MODEL_TAB_COUNT)
		{
			disposeOfTabPriorToRemovingIt(CONCEPTUAL_MODEL_TAB_COUNT);
			removeTab(CONCEPTUAL_MODEL_TAB_COUNT);
		}
	}
	
	public int getTabIndex(ORef ref)
	{
		for (int i = 0; i < getTabCount(); ++i)
		{
			DiagramPanel panel = (DiagramPanel)getTabContents(i);
			DiagramObject diagramObject = panel.getDiagramObject();
			if (diagramObject.getRef().equals(ref))
				return i;
		}
		
		return 0;
	}
	
	public void setTab(int newTab)
	{
		super.setTab(newTab);
		updateLegendPanelCheckBoxes();	
	}

	private void updateLegendPanelCheckBoxes()
	{
		getDiagramPanel().getDiagramLegendPanel().resetCheckBoxes();
	}

	public void setTabToConceptualModel()
	{
		final int CONCEPTUAL_MODEL_INDEX = 0;
		setTab(CONCEPTUAL_MODEL_INDEX);
	}
	
	public void setDiagramTab(ORef resultsChainRef)
	{
		setTab(getTabIndex(resultsChainRef));
	}
	
	public DiagramModel getDiagramModel()
	{
		DiagramComponent diagramComponent = getDiagramComponent();
		if(diagramComponent == null)
			return null;
		return diagramComponent.getDiagramModel();
	}

	public void deleteTabs() throws Exception
	{
		// TODO: This should completely tear down the view
		disposeOfNodePropertiesDialog();
		
		for(int i = 0; i < getTabCount(); ++i)
		{
			disposeOfTabPriorToRemovingIt(i);
		}
	}

	private void disposeOfTabPriorToRemovingIt(int i)
	{
		DiagramPanel panel = (DiagramPanel)getTabContents(i);
		panel.dispose();
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
		updateLegendPanelCheckBoxes();
		updateVisibilityOfFactors();
	}
	
	public void updateVisibilityOfFactors()
	{
		if (!getMainWindow().getCurrentView().cardName().equals(getViewName()))
				return;
		
		try
		{
			DiagramModel model = getDiagramModel();
			if(model == null)
				return;
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
	
	public JPopupMenu getTabPopupMenu()
	{
		DiagramTabMouseMenuHandler handler = new DiagramTabMouseMenuHandler(this);
		return handler.getPopupMenu();
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		Command rawCommand = event.getCommand();
		
		if (isCreateResultsChain(rawCommand))
		{
			try
			{
				addResultsChainTabs();
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}
		
		if(!rawCommand.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return;

		try
		{
			updateAllTabs(rawCommand);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private void updateAllTabs(Command rawCommand) throws Exception
	{
		CommandSetObjectData cmd = (CommandSetObjectData)rawCommand;
		String newValue = cmd.getDataValue();
		setModeIfRelevant(cmd, newValue);
		
		DiagramComponent[] diagramComponents = getAllDiagramComponents();
		for (int i = 0; i < diagramComponents.length; ++i)
		{
			DiagramModel model = diagramComponents[i].getDiagramModel();
			updateFactorBoundsIfRelevant(model, cmd);
			updateFactorLinkIfRelevant(model, cmd);
			updateScopeIfNeeded(model, cmd);
			updateTabTitleIfNeeded(cmd);
			refreshIfNeeded(diagramComponents[i], cmd);
		}
	}

	private DiagramComponent[] getAllDiagramComponents()
	{
		int tabCount = getTabCount();
		DiagramComponent[] diagramComponents = new DiagramComponent[tabCount];
		for (int i = 0; i < diagramComponents.length; ++i)
		{
			DiagramPanel panel = (DiagramPanel) getTabContents(i);
			diagramComponents[i] = panel.getdiagramComponent();
		}

		return diagramComponents;
	}


	private boolean isCreateResultsChain(Command rawCommand)
	{
		return isAddResultsChainCommand(rawCommand) || isDeleteResultsChainCommand(rawCommand);
	}

	private boolean isAddResultsChainCommand(Command rawCommand)
	{
		if (!rawCommand.getCommandName().equals(CommandCreateObject.COMMAND_NAME))
			return false;
		
		if ( ObjectType.RESULTS_CHAIN_DIAGRAM != ((CommandCreateObject)rawCommand).getObjectType())   
			return false;
		
		return true;
	}

	private boolean isDeleteResultsChainCommand(Command rawCommand)
	{
		if (!rawCommand.getCommandName().equals(CommandDeleteObject.COMMAND_NAME))
			return false;
		
		if ( ObjectType.RESULTS_CHAIN_DIAGRAM != ((CommandDeleteObject)rawCommand).getObjectType())   
			return false;
		
		return true;
	}
	
	private void updateFactorLinkIfRelevant(DiagramModel model, CommandSetObjectData cmd) throws Exception
	{
		DiagramFactorLinkId diagramFactorLinkId = null;
		
		if(cmd.getObjectType() == ObjectType.DIAGRAM_LINK)
		{
			diagramFactorLinkId = (DiagramFactorLinkId) cmd.getObjectId();
		}
		else if(cmd.getObjectType() == ObjectType.FACTOR_LINK)
		{
			diagramFactorLinkId = getDiagramFactorLinkIdFromFactorLinkId((FactorLinkId)cmd.getObjectId());
		}
		
		if(diagramFactorLinkId == null)
			return;
		
		LinkCell cell = model.updateCellFromDiagramFactorLink(diagramFactorLinkId);
		if(cell == null)
			return;

		clearBendPointSelectionList(cell, cmd);
		
		cell.update(getDiagramComponent());
		model.updateCell(cell);
	}
	
	private void clearBendPointSelectionList(LinkCell cell, CommandSetObjectData cmd) throws Exception
	{
		if (! cmd.getFieldTag().equals(DiagramLink.TAG_BEND_POINTS))
			return;
		
		PointList newPointList = new PointList(cmd.getDataValue());
		PointList oldPointList = new PointList(cmd.getPreviousDataValue());
		
		if (newPointList.size() == oldPointList.size())
			return;
			
		cell.getBendPointSelectionHelper().clearSelection();
	}

	private DiagramFactorLinkId getDiagramFactorLinkIdFromFactorLinkId(FactorLinkId factorLinkId) throws Exception
	{
		if(!getDiagramModel().doesDiagramFactorLinkExist(factorLinkId))
			return null;
		
		DiagramLink link = getDiagramModel().getDiagramFactorLinkbyWrappedId(factorLinkId);
		if(link == null)
			return null;
		
		return link.getDiagramLinkageId();
	}

	private void updateFactorBoundsIfRelevant(DiagramModel model, CommandSetObjectData cmd) throws Exception
	{
		if (cmd.getObjectType() != ObjectType.DIAGRAM_FACTOR)
			return;
		
		DiagramFactorId diagramFactorId = (DiagramFactorId) cmd.getObjectId();
		model.updateCellFromDiagramFactor(diagramFactorId);
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
	
	private void refreshIfNeeded(DiagramComponent diagramComponent, CommandSetObjectData cmd)
	{
		// may have added or removed a stress, modified an Annotation short label, etc.
		diagramComponent.repaint(diagramComponent.getBounds());
	}
	
	
	void updateTabTitleIfNeeded(CommandSetObjectData cmd)
	{
		if (isTabTitleTextChange(cmd))
		{
			int tabIndex = getTabIndex(cmd.getObjectORef());
			setTabTitle(cmd.getDataValue(), tabIndex);
		}
	}	
	
	private boolean isTabTitleTextChange(CommandSetObjectData cmd)
	{
		if (cmd.getObjectType() != ObjectType.RESULTS_CHAIN_DIAGRAM)
			return false;
		
		return (cmd.getFieldTag().equals(DiagramObject.TAG_LABEL));
	}
	
	void updateScopeIfNeeded(DiagramModel model, CommandSetObjectData cmd)
	{
		if (isScopeTextChange(cmd) || isFactorBoundsChange(cmd))
			model.updateProjectScopeBox();
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
		
		nodePropertiesPanel = new FactorPropertiesPanel(getMainWindow(), getDiagramComponent());
		String title = EAM.text("Title|Factor Properties");
		nodePropertiesDlg = new FactorPropertiesDialog(getMainWindow(), nodePropertiesPanel, title);
		
		nodePropertiesPanel.setCurrentDiagramFactor(getDiagramComponent(), node);
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
		
		FactorCell selectedNode = getDiagramComponent().getSelectedFactor();
		if(selectedNode == null || !selectedNode.equals(nodePropertiesPanel.getCurrentDiagramFactor()))
			disposeOfNodePropertiesDialog();
	}
	
	public String getCurrentMode()
	{
		return mode;
	}

	public boolean isStategyBrainstormMode()
	{
		return getCurrentMode().equals(ViewData.MODE_STRATEGY_BRAINSTORM);
	}
	
	JSplitPane bigSplitter;
	
	PropertiesDoer propertiesDoer;
	String mode;
	
	SlideListManagementPanel slideShowPoolManagementPanel;
	ModelessDialogWithClose nodePropertiesDlg;
	FactorPropertiesPanel nodePropertiesPanel;

}
