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
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.conservationmeasures.eam.actions.ActionConfigureLayers;
import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCreateActivity;
import org.conservationmeasures.eam.actions.ActionCreateGoal;
import org.conservationmeasures.eam.actions.ActionCreateIndicator;
import org.conservationmeasures.eam.actions.ActionCreateObjective;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionDeleteActivity;
import org.conservationmeasures.eam.actions.ActionDeleteGoal;
import org.conservationmeasures.eam.actions.ActionDeleteIndicator;
import org.conservationmeasures.eam.actions.ActionDeleteObjective;
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
import org.conservationmeasures.eam.actions.ActionShowSelectedChainMode;
import org.conservationmeasures.eam.actions.ActionZoomIn;
import org.conservationmeasures.eam.actions.ActionZoomOut;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.diagram.cells.DiagramFactorCluster;
import org.conservationmeasures.eam.dialogs.FactorPropertiesPanel;
import org.conservationmeasures.eam.dialogs.ModelessDialogWithClose;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorCluster;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardPanel;
import org.conservationmeasures.eam.views.umbrella.DeleteActivity;
import org.conservationmeasures.eam.views.umbrella.SaveImage;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.conservationmeasures.eam.views.umbrella.ViewSplitPane;
import org.martus.swing.UiScrollPane;
import org.martus.swing.Utilities;


public class DiagramView extends UmbrellaView implements CommandExecutedListener
{
	public DiagramView(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse);
		mode = ViewData.MODE_DEFAULT;
		diagram = new DiagramComponent(getMainWindow());
		legendDialog = new DiagramLegendPanel(getMainWindow());
		getProject().setSelectionModel(diagram.getEAMGraphSelectionModel());
		
		addDiagramViewDoersToMap();
		
		updateToolBar();

		getProject().addCommandExecutedListener(this);
	}

	private void updateToolBar()
	{
		setToolBar(new DiagramToolBar(getActions(), mode));
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
	
	public BufferedImage getImage()
	{
		return diagram.getImage();
	}
	
	public JComponent getPrintableComponent()
	{
		return diagram.getPrintableComponent();
	}
	
	public EAMObject getSelectedObject()
	{
		DiagramFactor node = diagram.getSelectedFactor();
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
		addDoerToMap(ActionCopy.class, new Copy());
		addDoerToMap(ActionSelectAll.class, new SelectAllDoer());
		addDoerToMap(ActionCut.class, new Cut());
		addDoerToMap(ActionDelete.class, new Delete());
		addDoerToMap(ActionPaste.class, new Paste());
		addDoerToMap(ActionPasteWithoutLinks.class, new PasteWithoutLinks());
		addDoerToMap(ActionSelectChain.class, new SelectChain());
		addDoerToMap(ActionProperties.class, propertiesDoer);
		addDoerToMap(ActionPrint.class, new Print());
		addDoerToMap(ActionSaveImage.class, new SaveImage());
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
	}
	
	public void becomeActive() throws Exception
	{
		super.becomeActive();
		removeAll();

		UiScrollPane diagramComponent = createDiagramPanel();

		JSplitPane bottomHalf = new JSplitPane();
		bottomHalf.setRightComponent(diagramComponent);
		bottomHalf.setLeftComponent(legendDialog);
		bottomHalf.setDividerLocation(legendDialog.getPreferredSize().width);
		
		bigSplitter = new ViewSplitPane(getMainWindow(), getProject().getCurrentView(), createWizard(), bottomHalf);
		add(bigSplitter, BorderLayout.CENTER);
		
		setMode(getViewData().getData(ViewData.TAG_CURRENT_MODE));
	}
	
	private UiScrollPane createDiagramPanel()
	{
		UiScrollPane uiScrollPane = new UiScrollPane(diagram);
		uiScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		uiScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		uiScrollPane.getHorizontalScrollBar().setUnitIncrement(getProject().getGridSize());
		uiScrollPane.getVerticalScrollBar().setUnitIncrement(getProject().getGridSize());
		return uiScrollPane;
	}


	public JPanel createWizard() throws Exception
	{
		wizardPanel = new DiagramWizardPanel(getMainWindow());
		return wizardPanel;
	}
	

	public void becomeInactive() throws Exception
	{
		// TODO: This should completely tear down the view
		disposeOfNodePropertiesDialog();
		diagram.clearSelection();
		super.becomeInactive();
	}
	
	
	public void setMode(String newMode)
	{
		IdList hiddenIds = new IdList();
		getDiagramComponent().setToDefaultBackgroundColor();
		if (newMode.equals(ViewData.MODE_STRATEGY_BRAINSTORM))
		{
			hiddenIds = getIdsToHide();
			getDiagramComponent().setBackground(Color.LIGHT_GRAY);
		}
			

		LayerManager manager = getProject().getLayerManager();
		manager.setHiddenIds(hiddenIds);
		manager.setMode(newMode);
		mode = newMode;
		updateToolBar();
		getMainWindow().updateStatusBar();
		getDiagramComponent().clearSelection();
		getProject().updateVisibilityOfFactors();
	}

	private IdList getIdsToHide()
	{
		IdList idsToHide = new IdList();
		try
		{
			ViewData viewData = getProject().getCurrentViewData();
			IdList visibleFactorIds = new IdList(viewData.getData(ViewData.TAG_BRAINSTORM_NODE_IDS));
			visibleFactorIds.addAll(getRelatedDraftInterventions(visibleFactorIds));
			Vector allNodes = getProject().getDiagramModel().getAllDiagramFactors();
			for (int i = 0; i < allNodes.size(); ++i)
			{
				DiagramFactor node = (DiagramFactor) allNodes.get(i);
				FactorId id = node.getWrappedId();
				if (!visibleFactorIds.contains(id))
					idsToHide.add(id);
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		return idsToHide;
	}
	
	IdList getRelatedDraftInterventions(IdList factorIds) throws Exception
	{
		IdList draftsToAdd = new IdList();
		
		DiagramModel diagramModel = getProject().getDiagramModel();
		for(int i = 0; i < factorIds.size(); ++i)
		{
			FactorId nodeId = new FactorId(factorIds.get(i).asInt());
			Factor node = diagramModel.getDiagramFactorByWrappedId(nodeId).getUnderlyingObject();
			FactorSet possibleDraftStrategies = diagramModel.getDirectlyLinkedUpstreamNodes(node);
			Iterator iter = possibleDraftStrategies.iterator();
			while(iter.hasNext())
			{
				FactorId possibleStrategyId = ((Factor)iter.next()).getModelNodeId();
				if(factorIds.contains(possibleStrategyId))
					continue;
				Factor possibleIntervention = getProject().findNode(possibleStrategyId);
				if(possibleIntervention.isStrategy() && possibleIntervention.isStatusDraft())
					draftsToAdd.add(possibleIntervention.getId());
			}
		}
		
		return draftsToAdd;
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		Command rawCommand = event.getCommand();
		if(!rawCommand.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return;

		CommandSetObjectData cmd = (CommandSetObjectData)rawCommand;
		String newMode = cmd.getDataValue();
		setModeIfRelevant(cmd, newMode);
		refreshIfNeeded(cmd);
		updateScopeIfNeeded(cmd);
		
		try
		{
			captureClusterIfNeeded(cmd);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			EAM.errorDialog("WARNING: Unexpected problem occurred during grouping");
		}
	}

//	TODO remove commented code after removing Undone method proves to work
//	public void commandUndone(CommandExecutedEvent event)
//	{
//		Command rawCommand = event.getCommand();
//		if(!rawCommand.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
//			return;
//
//		CommandSetObjectData cmd = (CommandSetObjectData)rawCommand;
//		String newMode = cmd.getPreviousDataValue();
//		setModeIfRelevant(cmd, newMode);
//		refreshIfNeeded(cmd);
//		updateScopeIfNeeded(cmd);
//		try
//		{
//			updateClusterAfterUndoIfNeeded(cmd);
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//			EAM.errorDialog("WARNING: Unexpected problem occurred during grouping");
//		}
//	}

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
	
//	TODO remove commented code after removing Undone method proves to work
//	private void updateClusterAfterUndoIfNeeded(CommandSetObjectData cmd) throws Exception
//	{
//		if(cmd.getObjectType() != ObjectType.FACTOR)
//			return;
//		
//		FactorId nodeId = new FactorId(cmd.getObjectId().asInt());
//		Factor cmNode = getProject().findNode(nodeId);
//		if(!cmNode.isFactorCluster())
//			return;
//		
//		if(!cmd.getFieldTag().equals(FactorCluster.TAG_MEMBER_IDS))
//			return;
//		
//		IdList newMembers = new IdList(cmd.getPreviousDataValue());
//		IdList oldMembers = new IdList(cmd.getDataValue());
//		
//		updateCluster((FactorId)cmd.getObjectId(), newMembers, oldMembers);
//	}
	
	private void captureClusterIfNeeded(CommandSetObjectData cmd) throws Exception
	{
		if(cmd.getObjectType() != ObjectType.FACTOR)
			return;
		
		FactorId nodeId = new FactorId(cmd.getObjectId().asInt());
		Factor cmNode = getProject().findNode(nodeId);
		if(!cmNode.isFactorCluster())
			return;
		
		if(!cmd.getFieldTag().equals(FactorCluster.TAG_MEMBER_IDS))
			return;
		
		FactorId clusterId = (FactorId)cmd.getObjectId();
		IdList newMembers = new IdList(cmd.getDataValue());
		DiagramModel model = getDiagramComponent().getDiagramModel();
		DiagramFactorCluster cluster = (DiagramFactorCluster)model.getDiagramFactorByWrappedId(clusterId);
		IdList oldMembers = new IdList(cluster.getUnderlyingObject().getData(FactorCluster.TAG_MEMBER_IDS));
		
		updateCluster(cluster.getWrappedId(), newMembers, oldMembers);
	}

	private void updateCluster(FactorId clusterId, IdList newMembers, IdList oldMembers) throws Exception
	{
		IdList idsToAdd = new IdList(newMembers);
		idsToAdd.subtract(oldMembers);
		
		IdList idsToRemove = new IdList(oldMembers);
		idsToRemove.subtract(newMembers);

		DiagramModel model = getDiagramComponent().getDiagramModel();
		DiagramFactorCluster cluster = (DiagramFactorCluster)model.getDiagramFactorByWrappedId(clusterId);
		
		for(int i = 0; i < idsToRemove.size(); ++i)
		{
			BaseId memberId = idsToRemove.get(i);
			DiagramFactor memberNode = model.getDiagramFactorByWrappedId((FactorId)memberId);
			getProject().removeDiagramFactorFromCluster(cluster, memberNode);
		}
		
		for(int i = 0; i < idsToAdd.size(); ++i)
		{
			BaseId memberId = idsToAdd.get(i);
			DiagramFactor memberNode = model.getDiagramFactorByWrappedId((FactorId)memberId);
			getProject().addDiagramFactorToCluster(cluster, memberNode);
		}
		
		model.updateCell(cluster);
	}
	
	void updateScopeIfNeeded(CommandSetObjectData cmd)
	{
		if(cmd.getObjectType() != ObjectType.PROJECT_METADATA)
			return;
		if(cmd.getFieldTag().equals(ProjectMetadata.TAG_SHORT_PROJECT_SCOPE) ||
				cmd.getFieldTag().equals(ProjectMetadata.TAG_SHORT_PROJECT_VISION))
		{
			getDiagramComponent().getDiagramModel().updateProjectScopeBox();
		}
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
		
		DiagramFactor selectedNode = diagram.getSelectedFactor();
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
	DiagramWizardPanel wizardPanel;
	String mode;
	DiagramLegendPanel legendDialog;
	
	ModelessDialogWithClose nodePropertiesDlg;
	FactorPropertiesPanel nodePropertiesPanel;

}
