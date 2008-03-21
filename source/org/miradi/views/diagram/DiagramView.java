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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import org.martus.swing.Utilities;
import org.miradi.actions.ActionCloneGoal;
import org.miradi.actions.ActionCloneIndicator;
import org.miradi.actions.ActionCloneObjective;
import org.miradi.actions.ActionCloneStress;
import org.miradi.actions.ActionConfigureLayers;
import org.miradi.actions.ActionCopy;
import org.miradi.actions.ActionCreateActivity;
import org.miradi.actions.ActionCreateBendPoint;
import org.miradi.actions.ActionCreateConceptualModel;
import org.miradi.actions.ActionCreateGoal;
import org.miradi.actions.ActionCreateIndicator;
import org.miradi.actions.ActionCreateIndicatorMeasurement;
import org.miradi.actions.ActionCreateKeyEcologicalAttribute;
import org.miradi.actions.ActionCreateKeyEcologicalAttributeIndicator;
import org.miradi.actions.ActionCreateKeyEcologicalAttributeMeasurement;
import org.miradi.actions.ActionCreateObjective;
import org.miradi.actions.ActionCreateOrShowResultsChain;
import org.miradi.actions.ActionCreateResultsChain;
import org.miradi.actions.ActionCreateSlide;
import org.miradi.actions.ActionCreateStress;
import org.miradi.actions.ActionCreateStressFromKea;
import org.miradi.actions.ActionCreateSubTarget;
import org.miradi.actions.ActionCut;
import org.miradi.actions.ActionDelete;
import org.miradi.actions.ActionDeleteActivity;
import org.miradi.actions.ActionDeleteBendPoint;
import org.miradi.actions.ActionDeleteConceptualModel;
import org.miradi.actions.ActionDeleteGoal;
import org.miradi.actions.ActionDeleteGroupBox;
import org.miradi.actions.ActionDeleteIndicator;
import org.miradi.actions.ActionDeleteIndicatorMeasurement;
import org.miradi.actions.ActionDeleteKeyEcologicalAttribute;
import org.miradi.actions.ActionDeleteKeyEcologicalAttributeIndicator;
import org.miradi.actions.ActionDeleteKeyEcologicalAttributeMeasurement;
import org.miradi.actions.ActionDeleteObjective;
import org.miradi.actions.ActionDeleteResultsChain;
import org.miradi.actions.ActionDeleteSlide;
import org.miradi.actions.ActionDeleteStress;
import org.miradi.actions.ActionDeleteSubTarget;
import org.miradi.actions.ActionDiagramProperties;
import org.miradi.actions.ActionGroupBoxAddFactor;
import org.miradi.actions.ActionGroupBoxRemoveFactor;
import org.miradi.actions.ActionInsertContributingFactor;
import org.miradi.actions.ActionInsertDirectThreat;
import org.miradi.actions.ActionInsertDraftStrategy;
import org.miradi.actions.ActionInsertFactorLink;
import org.miradi.actions.ActionInsertGroupBox;
import org.miradi.actions.ActionInsertIntermediateResult;
import org.miradi.actions.ActionInsertStrategy;
import org.miradi.actions.ActionInsertTarget;
import org.miradi.actions.ActionInsertTextBox;
import org.miradi.actions.ActionInsertThreatReductionResult;
import org.miradi.actions.ActionManageStresses;
import org.miradi.actions.ActionMoveSlideDown;
import org.miradi.actions.ActionMoveSlideUp;
import org.miradi.actions.ActionNudgeDown;
import org.miradi.actions.ActionNudgeLeft;
import org.miradi.actions.ActionNudgeRight;
import org.miradi.actions.ActionNudgeUp;
import org.miradi.actions.ActionPaste;
import org.miradi.actions.ActionPasteWithoutLinks;
import org.miradi.actions.ActionProperties;
import org.miradi.actions.ActionRenameConceptualModel;
import org.miradi.actions.ActionRenameResultsChain;
import org.miradi.actions.ActionSelectAll;
import org.miradi.actions.ActionSelectChain;
import org.miradi.actions.ActionShareActivity;
import org.miradi.actions.ActionShowConceptualModel;
import org.miradi.actions.ActionShowFullModelMode;
import org.miradi.actions.ActionShowResultsChain;
import org.miradi.actions.ActionShowSelectedChainMode;
import org.miradi.actions.ActionSlideShowViewer;
import org.miradi.actions.ActionToggleSlideShowPanel;
import org.miradi.actions.ActionZoomIn;
import org.miradi.actions.ActionZoomOut;
import org.miradi.actions.ActionZoomToFit;
import org.miradi.actions.EAMAction;
import org.miradi.commands.Command;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramComponent;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.EAMGraphSelectionModel;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.dialogs.base.ModelessDialogWithClose;
import org.miradi.dialogs.diagram.ConceptualModelDiagramPanel;
import org.miradi.dialogs.diagram.DiagramPanel;
import org.miradi.dialogs.diagram.FactorPropertiesDialog;
import org.miradi.dialogs.diagram.FactorPropertiesPanel;
import org.miradi.dialogs.diagram.ResultsChainDiagramPanel;
import org.miradi.dialogs.slideshow.SlideListManagementPanel;
import org.miradi.dialogs.slideshow.SlideShowDialog;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.DiagramFactorLinkId;
import org.miradi.ids.FactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.FactorSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.Factor;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.SlideShow;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.utils.PointList;
import org.miradi.views.TabbedView;
import org.miradi.views.diagram.doers.CloneStressDoer;
import org.miradi.views.diagram.doers.CreateStressDoer;
import org.miradi.views.diagram.doers.CreateStressFromKeaDoer;
import org.miradi.views.diagram.doers.DeleteGroupBoxDoer;
import org.miradi.views.diagram.doers.DeleteStressDoer;
import org.miradi.views.diagram.doers.DiagramPropertiesShowDoer;
import org.miradi.views.diagram.doers.GroupBoxAddDiagramFactorDoer;
import org.miradi.views.diagram.doers.GroupBoxRemoveDiagramFactorDoer;
import org.miradi.views.diagram.doers.InsertGroupBoxDoer;
import org.miradi.views.diagram.doers.SelectChainDoer;
import org.miradi.views.diagram.doers.ShareActivityDoer;
import org.miradi.views.diagram.doers.ZoomToFitDoer;
import org.miradi.views.targetviability.doers.CreateKeyEcologicalAttributeMeasurementDoer;
import org.miradi.views.targetviability.doers.DeleteKeyEcologicalAttributeMeasurementDoer;
import org.miradi.views.threatmatrix.doers.ManageStressesDoer;
import org.miradi.views.umbrella.DeleteActivity;


public class DiagramView extends TabbedView implements CommandExecutedListener
{
	public DiagramView(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse);
		mode = ViewData.MODE_DEFAULT;
		
		addDiagramViewDoersToMap();
	}

	
	public boolean isSlideShowVisible()
	{
		return (slideShowDlg!=null) && (slideShowDlg.isVisible());
	}

	public void showSlideShowPanel() throws Exception
	{
		disposeOfSlideShowDialog();
		ORef slideShowRef = createSlideShowIfNeeded().getRef(); 
		SlideListManagementPanel slideShowPoolManagementPanel =  new SlideListManagementPanel(getProject(), getMainWindow(), slideShowRef, getActions());
		slideShowPoolManagementPanel.updateSplitterLocationToMiddle();
		slideShowDlg = new SlideShowDialog(getMainWindow(), slideShowPoolManagementPanel, slideShowPoolManagementPanel.getPanelDescription());
		slideShowDlg.pack();
		
		Utilities.centerDlg(slideShowDlg);
		slideShowDlg.setVisible(true);
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
		addDoerToMap(ActionInsertThreatReductionResult.class, new InsertThreatReductionResultDoer());
		addDoerToMap(ActionInsertTarget.class, new InsertTargetDoer());
		addDoerToMap(ActionInsertContributingFactor.class, new InsertContributingFactorDoer());
		addDoerToMap(ActionInsertDirectThreat.class, new InsertDirectThreatDoer());
		addDoerToMap(ActionInsertStrategy.class, new InsertStrategyDoer());
		addDoerToMap(ActionInsertDraftStrategy.class, new InsertDraftStrategyDoer());
		addDoerToMap(ActionInsertFactorLink.class, new InsertFactorLinkDoer());
		addDoerToMap(ActionCreateBendPoint.class, new CreateBendPointDoer());
		addDoerToMap(ActionDeleteBendPoint.class, new DeleteBendPointDoer());
		addDoerToMap(ActionRenameResultsChain.class, new RenameResultsChainDoer());
		addDoerToMap(ActionRenameConceptualModel.class, new RenameConceptualModelDoer());
		addDoerToMap(ActionCopy.class, new Copy());
		addDoerToMap(ActionSelectAll.class, new SelectAllDoer());
		addDoerToMap(ActionCut.class, new Cut());
		addDoerToMap(ActionDelete.class, new DeleteSelectedItemDoer());
		addDoerToMap(ActionPaste.class, new Paste());
		addDoerToMap(ActionPasteWithoutLinks.class, new PasteWithoutLinks());
		addDoerToMap(ActionSelectChain.class, new SelectChainDoer());
		addDoerToMap(ActionProperties.class, propertiesDoer);
		addDoerToMap(ActionConfigureLayers.class, new ConfigureLayers());
		addDoerToMap(ActionShowSelectedChainMode.class, new ShowSelectedChainModeDoer());
		addDoerToMap(ActionShowFullModelMode.class, new ShowFullModelModeDoer());
		addDoerToMap(ActionZoomIn.class, new ZoomIn());
		addDoerToMap(ActionZoomOut.class, new ZoomOut());
		addDoerToMap(ActionZoomToFit.class, new ZoomToFitDoer());
		addDoerToMap(ActionNudgeUp.class, new NudgeDoer(KeyEvent.VK_UP)); 
		addDoerToMap(ActionNudgeDown.class, new NudgeDoer(KeyEvent.VK_DOWN));
		addDoerToMap(ActionNudgeLeft.class, new NudgeDoer(KeyEvent.VK_LEFT));
		addDoerToMap(ActionNudgeRight.class, new NudgeDoer(KeyEvent.VK_RIGHT));
		
		addDoerToMap(ActionCreateActivity.class, new CreateActivityDoer());
		addDoerToMap(ActionDeleteActivity.class, new DeleteActivity());
		addDoerToMap(ActionShareActivity.class, new ShareActivityDoer());

		addDoerToMap(ActionCreateObjective.class, new CreateObjective());
		addDoerToMap(ActionCloneObjective.class, new CloneObjectiveDoer());
		addDoerToMap(ActionDeleteObjective.class, new DeleteObjective());
		
		addDoerToMap(ActionCreateIndicator.class, new CreateIndicator());
		addDoerToMap(ActionCloneIndicator.class, new CloneIndicatorDoer());
		addDoerToMap(ActionDeleteIndicator.class, new DeleteIndicator());
		
		addDoerToMap(ActionCreateGoal.class, new CreateGoal());
		addDoerToMap(ActionCloneGoal.class, new CloneGoalDoer());
		addDoerToMap(ActionDeleteGoal.class, new DeleteGoal());
		
		addDoerToMap(ActionCreateStress.class, new CreateStressDoer());
		addDoerToMap(ActionDeleteStress.class, new DeleteStressDoer());
		addDoerToMap(ActionCloneStress.class, new CloneStressDoer());
		addDoerToMap(ActionCreateStressFromKea.class, new CreateStressFromKeaDoer());
		
		addDoerToMap(ActionCreateKeyEcologicalAttribute.class, new CreateKeyEcologicalAttributeDoer());
		addDoerToMap(ActionDeleteKeyEcologicalAttribute.class, new DeleteKeyEcologicalAttributeDoer());
		addDoerToMap(ActionCreateKeyEcologicalAttributeIndicator.class, new CreateKeyEcologicalAttributeIndicatorDoer());
		addDoerToMap(ActionDeleteKeyEcologicalAttributeIndicator.class, new DeleteKeyEcologicalAttributeIndicatorDoer());
		addDoerToMap(ActionCreateKeyEcologicalAttributeMeasurement.class, new CreateKeyEcologicalAttributeMeasurementDoer());
		addDoerToMap(ActionDeleteKeyEcologicalAttributeMeasurement.class, new DeleteKeyEcologicalAttributeMeasurementDoer());
		
		addDoerToMap(ActionCreateIndicatorMeasurement.class, new CreateKeyEcologicalAttributeMeasurementDoer());
		addDoerToMap(ActionDeleteIndicatorMeasurement.class, new DeleteKeyEcologicalAttributeMeasurementDoer());
		
		addDoerToMap(ActionCreateResultsChain.class, new CreateResultsChainDoer());
		addDoerToMap(ActionShowResultsChain.class, new ShowResultsChainDoer());
		addDoerToMap(ActionDeleteResultsChain.class, new DeleteResultsChainDoer());
		addDoerToMap(ActionShowConceptualModel.class, new ShowConceptualModelDoer());
		addDoerToMap(ActionCreateOrShowResultsChain.class, new CreateOrShowResultsChainDoer());
		addDoerToMap(ActionInsertTextBox.class, new InsertTextBoxDoer());
		addDoerToMap(ActionInsertGroupBox.class, new InsertGroupBoxDoer());
		addDoerToMap(ActionCreateConceptualModel.class, new CreateConceptualModelPageDoer());
		addDoerToMap(ActionDeleteConceptualModel.class, new DeleteConceptualModelPageDoer());
		addDoerToMap(ActionGroupBoxAddFactor.class, new GroupBoxAddDiagramFactorDoer());
		addDoerToMap(ActionGroupBoxRemoveFactor.class, new GroupBoxRemoveDiagramFactorDoer());
		addDoerToMap(ActionDeleteGroupBox.class, new DeleteGroupBoxDoer());
		
		addDoerToMap(ActionCreateSlide.class, new CreateSlideDoer());
		addDoerToMap(ActionDeleteSlide.class, new DeleteSlideDoer());
		addDoerToMap(ActionMoveSlideDown.class, new MoveSlideDownDoer());
		addDoerToMap(ActionMoveSlideUp.class, new MoveSlideUpDoer());
		addDoerToMap(ActionToggleSlideShowPanel.class, new ToggleSlideShowPanelDoer());
		addDoerToMap(ActionSlideShowViewer.class, new SlideShowViewerDoer());
		
		addDoerToMap(ActionManageStresses.class, new ManageStressesDoer());
		
		addDoerToMap(ActionCreateSubTarget.class, new CreateSubTargetDoer());
		addDoerToMap(ActionDeleteSubTarget.class, new DeleteSubTargetDoer());
		addDoerToMap(ActionDiagramProperties.class, new DiagramPropertiesShowDoer());
	}
	
	public void tabWasSelected()
	{
		getMainWindow().preventActionUpdates();
		try
		{
			super.tabWasSelected();
			getCurrentDiagramPanel().showCurrentDiagram();
			updateVisibilityOfFactorsAndClearSelectionModel();
			if (getDiagramComponent()!=null)
			{
				getDiagramComponent().updateDiagramZoomSetting();
			}
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Unknown error displaying diagram"));
		}
		finally
		{
			getMainWindow().allowActionUpdates();
			getMainWindow().updateActionsAndStatusBar();
		}
	}

	private void switchToFullMode() throws Exception
	{
		if (!isStategyBrainstormMode())
			return;
	
		EAMAction actionShowFullModelMode = getActions().get(ActionShowFullModelMode.class);
		actionShowFullModelMode.doAction();
	}

	public void prepareForTabSwitch()
	{
		super.prepareForTabSwitch();
		try
		{
			switchToFullMode();
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}

	public void createTabs() throws Exception
	{
		getMainWindow().preventActionUpdates();
		try
		{
			createConceptualModelDiagramTab();
			createResultsChainTab();
			
			//TODO get tag using object type, diagram splitter has this info.  
			ensureDiagramIsSelected(ConceptualModelDiagram.getObjectType(), ViewData.TAG_CURRENT_CONCEPTUAL_MODEL_REF);
			ensureDiagramIsSelected(ResultsChainDiagram.getObjectType(), ViewData.TAG_CURRENT_RESULTS_CHAIN_REF);
			
			conceptualDiagramPanel.restoreSavedLocation();
			resultsChainPanel.restoreSavedLocation();
		}
		finally
		{
			getMainWindow().allowActionUpdates();
			getMainWindow().updateActionsAndStatusBar();
		}
	}
	
	public void becomeActive() throws Exception
	{
		super.becomeActive();
		setMode(getViewData().getData(ViewData.TAG_CURRENT_MODE));
	}

	private void createResultsChainTab() throws Exception
	{
		resultsChainPanel = new ResultsChainDiagramPanel(getMainWindow());
		addNonScrollingTab(resultsChainPanel);
	}

	private void createConceptualModelDiagramTab() throws Exception
	{
		conceptualDiagramPanel = new ConceptualModelDiagramPanel(getMainWindow());
		addNonScrollingTab(conceptualDiagramPanel);
	}
	
	private void ensureDiagramIsSelected(int objectType, String tag) throws Exception
	{
		ViewData viewData = getViewData();
		String orefAsJsonString = viewData.getData(tag);
		ORef currentDiagramRef = ORef.createFromString(orefAsJsonString);
		if (!currentDiagramRef.isInvalid())
			return;
			
		EAMObjectPool objectPool = getProject().getPool(objectType);
		if (objectPool.size() == 0)
			return;
		
		ORefList orefList = objectPool.getORefList();
		ORef firstRef = orefList.get(0);
		
		//NOTE: Since we are inside commandExecuted, we can't execute another command here,
		// which is ok, because we are switching away from the absence of a diagram, 
		// so there would be no requirement for undo to restore it
		getProject().setObjectData(viewData.getRef(), tag, firstRef.toString());
	}
	
	public DiagramPanel getDiagramPanel()
	{
		return getCurrentDiagramPanel();
	}

	public DiagramPanel getCurrentDiagramPanel()
	{
		return (DiagramPanel) getCurrentTabContents();
	}

	public boolean isResultsChainTab()
	{
		int index = getSelectedTabIndex();
		if (index  < 0)
			return false;
		
		DiagramPanel panel = (DiagramPanel)getTabContents(index);
		DiagramSplitPane diagramSplitPane = panel.getDiagramSplitPane();
		return diagramSplitPane.getDiagramPageList().isResultsChainPageList();
	}
		
	public int getTabIndex(ORef ref)
	{
		for (int i = 0; i < getTabCount(); ++i)
		{
			DiagramPanel panel = (DiagramPanel)getTabContents(i);
			int diagramObjectType = panel.getDiagramSplitPane().getDiagramPageList().getManagedDiagramType();
			if (diagramObjectType == ref.getObjectType())
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
		getMainWindow().preventActionUpdates();
		try
		{
			getDiagramPanel().getDiagramLegendPanel().resetCheckBoxes();
		}
		finally
		{
			getMainWindow().allowActionUpdates();
			getMainWindow().updateActionsAndStatusBar();
		}
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
		disposeOfSlideShowDialog();
		
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
		DiagramComponent diagramComponent = getDiagramComponent();
		if (diagramComponent == null)
			return;
		
		diagramComponent.setToDefaultBackgroundColor();
		if (newMode.equals(ViewData.MODE_STRATEGY_BRAINSTORM))
		{
			hiddenORefs = getORefsToHide();
			diagramComponent.setBackground(Color.LIGHT_GRAY);
		}
			

		LayerManager manager = getProject().getLayerManager();
		manager.setHiddenORefs(hiddenORefs);
		manager.setMode(newMode);
		mode = newMode;
		updateToolBar();
		getMainWindow().updateStatusBar();
		diagramComponent.clearSelection();
		updateLegendPanelCheckBoxes();
		updateVisibilityOfFactorsAndClearSelectionModel();
	}
	
	public void updateVisibilityOfFactorsAndLinks()
	{
		if (!isCurrentViewDiagramView())
			return;
	
		try
		{
			DiagramModel model = getDiagramModel();
			if(model == null)
				return;
			model.updateVisibilityOfFactorsAndLinks();
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public void updateVisibilityOfFactorsAndClearSelectionModel()
	{
		if (!isCurrentViewDiagramView())
			return;
		
		updateVisibilityOfFactorsAndLinks();
		DiagramComponent diagramComponent = getDiagramComponent();
		if (diagramComponent == null)
			return;
		
		EAMGraphSelectionModel selectionModel = (EAMGraphSelectionModel) diagramComponent.getSelectionModel();
		// TODO: Find a way to avoid the need to test for null here
		if(selectionModel != null)
			selectionModel.clearSelection();
	}


	private boolean isCurrentViewDiagramView()
	{
		return getMainWindow().getCurrentView().cardName().equals(getViewName());
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
			DiagramFactor diagramFactor = diagramModel.getFactorCellByWrappedId(nodeId).getDiagramFactor();
			FactorSet possibleDraftStrategies = diagramModel.getDirectlyLinkedUpstreamNodes(diagramFactor);
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
		
		if(!rawCommand.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return;

		try
		{
			CommandSetObjectData cmd = (CommandSetObjectData)rawCommand;
			updateAllTabs(cmd);
			setToDefaultMode(cmd);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private void setToDefaultMode(CommandSetObjectData cmd) throws Exception
	{
		if (cmd.getObjectType() != ObjectType.VIEW_DATA)
			return;
		
		if (! isDiagramObjectTag(cmd.getFieldTag()))
			return;
		
		ViewData viewData = getProject().getCurrentViewData();
		String currentDiagramPage = viewData.getData(ViewData.TAG_CURRENT_CONCEPTUAL_MODEL_REF);
		if (cmd.getDataValue().equals(currentDiagramPage))
			return;
		
		String currentResultsChainPageRef = viewData.getData(ViewData.TAG_CURRENT_RESULTS_CHAIN_REF); 
		if (cmd.getDataValue().equals(currentResultsChainPageRef))
			return;
				
		switchToFullMode();
	}


	private boolean isDiagramObjectTag(String tag)
	{
		return (tag.equals(ViewData.TAG_CURRENT_CONCEPTUAL_MODEL_REF)) && (tag.equals(ViewData.TAG_CURRENT_RESULTS_CHAIN_REF));
	}


	private void updateAllTabs(CommandSetObjectData cmd) throws Exception
	{
		
		String newValue = cmd.getDataValue();
		setModeIfRelevant(cmd, newValue);
		
		DiagramComponent[] diagramComponents = getAllDiagramComponents();
		for (int i = 0; i < diagramComponents.length; ++i)
		{
			DiagramModel model = diagramComponents[i].getDiagramModel();
			updateFactorBoundsIfRelevant(model, cmd);
			updateFactorLinkIfRelevant(model, cmd);
			updateScopeIfNeeded(model, cmd);
			refreshIfNeeded(diagramComponents[i], cmd);
		}
	}

	private DiagramComponent[] getAllDiagramComponents()
	{
		int tabCount = getTabCount();
		Vector allDiagramComponents = new Vector();
		for (int i = 0; i < tabCount; ++i)
		{
			DiagramPanel panel = (DiagramPanel) getTabContents(i);
			DiagramComponent[] panelDiagramComponents = panel.getAllSplitterDiagramComponents();
			allDiagramComponents.addAll(Arrays.asList(panelDiagramComponents));
		}

		return (DiagramComponent[]) allDiagramComponents.toArray(new DiagramComponent[0]);
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
		String fieldTag = cmd.getFieldTag();
		try
		{
			ViewData ourViewData = getViewData();
			if(cmd.getObjectType() != ourViewData.getType())
				return;
			if(cmd.getObjectId() != ourViewData.getId())
				return;
			boolean modeChange = fieldTag.equals(ViewData.TAG_CURRENT_MODE);
			boolean cmChange = fieldTag.equals(ViewData.TAG_CURRENT_CONCEPTUAL_MODEL_REF);
			boolean rcChange = fieldTag.equals(ViewData.TAG_CURRENT_RESULTS_CHAIN_REF);
			if(modeChange || cmChange || rcChange)
			{
				setMode(newMode);
			}
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
		
		getDiagramComponent().requestFocusInWindow();
		
		nodePropertiesPanel = new FactorPropertiesPanel(getMainWindow(), getDiagramComponent());
		String title = EAM.text("Title|Factor Properties");
		nodePropertiesDlg = new FactorPropertiesDialog(getMainWindow(), nodePropertiesPanel, title);
		
		nodePropertiesPanel.setCurrentDiagramFactor(getDiagramComponent(), node);
		nodePropertiesPanel.selectTab(startingTabIdentifier);
		nodePropertiesDlg.pack();
		Rectangle screenRect = Utilities.getViewableRectangle();
		int width = Math.min(nodePropertiesDlg.getWidth(), screenRect.width * 9 / 10);
		int height = Math.min(nodePropertiesDlg.getHeight(), screenRect.height * 9 / 10);
		Dimension size = new Dimension(width, height);
		nodePropertiesDlg.setSize(size);
		nodePropertiesDlg.setLocation(Utilities.center(size, screenRect));
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
	
	public void disposeOfSlideShowDialog()
	{
		if(slideShowDlg != null)
			slideShowDlg.dispose();
		slideShowDlg = null;
	}
	
	
	public void selectionWasChanged()
	{
		closeActivePropertiesDialog();
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
	

	
	private PropertiesDoer propertiesDoer;
	private String mode;
	
	private SlideShowDialog slideShowDlg;
	private ModelessDialogWithClose nodePropertiesDlg;
	private FactorPropertiesPanel nodePropertiesPanel;
	
	private ConceptualModelDiagramPanel conceptualDiagramPanel;
	private ResultsChainDiagramPanel resultsChainPanel;
}
