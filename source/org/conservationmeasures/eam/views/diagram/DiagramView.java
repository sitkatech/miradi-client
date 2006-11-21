/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.BorderLayout;
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
import org.conservationmeasures.eam.actions.ActionCreateGoal;
import org.conservationmeasures.eam.actions.ActionCreateIndicator;
import org.conservationmeasures.eam.actions.ActionCreateObjective;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionDeleteGoal;
import org.conservationmeasures.eam.actions.ActionDeleteIndicator;
import org.conservationmeasures.eam.actions.ActionDeleteObjective;
import org.conservationmeasures.eam.actions.ActionInsertConnection;
import org.conservationmeasures.eam.actions.ActionInsertDirectThreat;
import org.conservationmeasures.eam.actions.ActionInsertDraftIntervention;
import org.conservationmeasures.eam.actions.ActionInsertIndirectFactor;
import org.conservationmeasures.eam.actions.ActionInsertIntervention;
import org.conservationmeasures.eam.actions.ActionInsertTarget;
import org.conservationmeasures.eam.actions.ActionNormalDiagramMode;
import org.conservationmeasures.eam.actions.ActionNudgeNodeDown;
import org.conservationmeasures.eam.actions.ActionNudgeNodeLeft;
import org.conservationmeasures.eam.actions.ActionNudgeNodeRight;
import org.conservationmeasures.eam.actions.ActionNudgeNodeUp;
import org.conservationmeasures.eam.actions.ActionPaste;
import org.conservationmeasures.eam.actions.ActionPasteWithoutLinks;
import org.conservationmeasures.eam.actions.ActionPrint;
import org.conservationmeasures.eam.actions.ActionProperties;
import org.conservationmeasures.eam.actions.ActionSaveImage;
import org.conservationmeasures.eam.actions.ActionSelectAll;
import org.conservationmeasures.eam.actions.ActionSelectChain;
import org.conservationmeasures.eam.actions.ActionStrategyBrainstormMode;
import org.conservationmeasures.eam.actions.ActionZoomIn;
import org.conservationmeasures.eam.actions.ActionZoomOut;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.DiagramCluster;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.dialogs.ModelessDialogWithClose;
import org.conservationmeasures.eam.dialogs.NodePropertiesPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramNodeId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelCluster;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.diagram.wizard.DiagramWizardPanel;
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
		legendDialog = new DiagramLegendPanel();
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
		DiagramNode node = diagram.getSelectedNode();
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

		addDoerToMap(ActionInsertTarget.class, new InsertTarget());
		addDoerToMap(ActionInsertIndirectFactor.class, new InsertIndirectFactor());
		addDoerToMap(ActionInsertDirectThreat.class, new InsertDirectThreat());
		addDoerToMap(ActionInsertIntervention.class, new InsertIntervention());
		addDoerToMap(ActionInsertDraftIntervention.class, new InsertDraftIntervention());
		addDoerToMap(ActionInsertConnection.class, new InsertConnection());
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
		addDoerToMap(ActionStrategyBrainstormMode.class, new StrategyBrainstormMode());
		addDoerToMap(ActionNormalDiagramMode.class, new NormalDiagramMode());
		addDoerToMap(ActionZoomIn.class, new ZoomIn());
		addDoerToMap(ActionZoomOut.class, new ZoomOut());
		addDoerToMap(ActionNudgeNodeUp.class, new NudgeNode(KeyEvent.VK_UP)); 
		addDoerToMap(ActionNudgeNodeDown.class, new NudgeNode(KeyEvent.VK_DOWN));
		addDoerToMap(ActionNudgeNodeLeft.class, new NudgeNode(KeyEvent.VK_LEFT));
		addDoerToMap(ActionNudgeNodeRight.class, new NudgeNode(KeyEvent.VK_RIGHT));
		
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
		
		bigSplitter =new ViewSplitPane(createWizard(), bottomHalf, bigSplitter);
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
		wizardPanel = new DiagramWizardPanel(getMainWindow().getActions());
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
		if (newMode.equals(ViewData.MODE_STRATEGY_BRAINSTORM))
			hiddenIds = getIdsToHide();

		LayerManager manager = getProject().getLayerManager();
		manager.setHiddenIds(hiddenIds);
		manager.setMode(newMode);
		mode = newMode;
		updateToolBar();
		getMainWindow().updateStatusBar();
		getDiagramComponent().clearSelection();
		getProject().updateVisibilityOfNodes();
	}

	private IdList getIdsToHide()
	{
		IdList idsToHide = new IdList();
		try
		{
			ViewData viewData = getProject().getCurrentViewData();
			IdList visibleDiagramNodeIds = new IdList(viewData.getData(ViewData.TAG_BRAINSTORM_NODE_IDS));
			visibleDiagramNodeIds.addAll(getRelatedDraftInterventions(visibleDiagramNodeIds));
			Vector allNodes = getProject().getDiagramModel().getAllNodes();
			for (int i = 0; i < allNodes.size(); ++i)
			{
				DiagramNode node = (DiagramNode) allNodes.get(i);
				BaseId id = node.getDiagramNodeId();
				if (!visibleDiagramNodeIds.contains(id))
					idsToHide.add(id);
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		return idsToHide;
	}
	
	IdList getRelatedDraftInterventions(IdList diagramNodeIds) throws Exception
	{
		IdList draftsToAdd = new IdList();
		
		DiagramModel diagramModel = getProject().getDiagramModel();
		for(int i = 0; i < diagramNodeIds.size(); ++i)
		{
			DiagramNodeId nodeId = new DiagramNodeId(diagramNodeIds.get(i).asInt());
			ConceptualModelNode node = diagramModel.getNodeById(nodeId).getUnderlyingObject();
			ConceptualModelNodeSet possibleDraftInterventionIds = diagramModel.getDirectlyLinkedUpstreamNodes(node);
			Iterator iter = possibleDraftInterventionIds.iterator();
			while(iter.hasNext())
			{
				ModelNodeId possibleInterventionId = ((ConceptualModelNode)iter.next()).getModelNodeId();
				if(diagramNodeIds.contains(possibleInterventionId))
					continue;
				ConceptualModelNode possibleIntervention = getProject().findNode(possibleInterventionId);
				if(possibleIntervention.isIntervention() && possibleIntervention.isStatusDraft())
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

	public void commandUndone(CommandExecutedEvent event)
	{
		Command rawCommand = event.getCommand();
		if(!rawCommand.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return;

		CommandSetObjectData cmd = (CommandSetObjectData)rawCommand;
		String newMode = cmd.getPreviousDataValue();
		setModeIfRelevant(cmd, newMode);
		refreshIfNeeded(cmd);
		updateScopeIfNeeded(cmd);
		try
		{
			updateClusterAfterUndoIfNeeded(cmd);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			EAM.errorDialog("WARNING: Unexpected problem occurred during grouping");
		}
	}

	public void commandFailed(Command command, CommandFailedException e)
	{
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
	
	private void updateClusterAfterUndoIfNeeded(CommandSetObjectData cmd) throws Exception
	{
		if(cmd.getObjectType() != ObjectType.MODEL_NODE)
			return;
		
		ModelNodeId nodeId = new ModelNodeId(cmd.getObjectId().asInt());
		ConceptualModelNode cmNode = getProject().findNode(nodeId);
		if(!cmNode.isCluster())
			return;
		
		if(!cmd.getFieldTag().equals(ConceptualModelCluster.TAG_MEMBER_IDS))
			return;
		
		IdList newMembers = new IdList(cmd.getPreviousDataValue());
		IdList oldMembers = new IdList(cmd.getDataValue());
		
		updateCluster((ModelNodeId)cmd.getObjectId(), newMembers, oldMembers);
	}
	
	private void captureClusterIfNeeded(CommandSetObjectData cmd) throws Exception
	{
		if(cmd.getObjectType() != ObjectType.MODEL_NODE)
			return;
		
		ModelNodeId nodeId = new ModelNodeId(cmd.getObjectId().asInt());
		ConceptualModelNode cmNode = getProject().findNode(nodeId);
		if(!cmNode.isCluster())
			return;
		
		if(!cmd.getFieldTag().equals(ConceptualModelCluster.TAG_MEMBER_IDS))
			return;
		
		ModelNodeId clusterId = (ModelNodeId)cmd.getObjectId();
		IdList newMembers = new IdList(cmd.getDataValue());
		DiagramModel model = getDiagramComponent().getDiagramModel();
		DiagramCluster cluster = (DiagramCluster)model.getNodeById(clusterId);
		IdList oldMembers = new IdList(cluster.getUnderlyingObject().getData(ConceptualModelCluster.TAG_MEMBER_IDS));
		
		updateCluster(cluster.getWrappedId(), newMembers, oldMembers);
	}

	private void updateCluster(ModelNodeId clusterId, IdList newMembers, IdList oldMembers) throws Exception
	{
		IdList idsToAdd = new IdList(newMembers);
		idsToAdd.subtract(oldMembers);
		
		IdList idsToRemove = new IdList(oldMembers);
		idsToRemove.subtract(newMembers);

		DiagramModel model = getDiagramComponent().getDiagramModel();
		DiagramCluster cluster = (DiagramCluster)model.getNodeById(clusterId);
		
		for(int i = 0; i < idsToRemove.size(); ++i)
		{
			BaseId memberId = idsToRemove.get(i);
			DiagramNode memberNode = model.getNodeById((ModelNodeId)memberId);
			getProject().removeNodeFromCluster(cluster, memberNode);
		}
		
		for(int i = 0; i < idsToAdd.size(); ++i)
		{
			BaseId memberId = idsToAdd.get(i);
			DiagramNode memberNode = model.getNodeById((ModelNodeId)memberId);
			getProject().addNodeToCluster(cluster, memberNode);
		}
		
		model.updateCell(cluster);
	}
	
	void updateScopeIfNeeded(CommandSetObjectData cmd)
	{
		if(cmd.getObjectType() != ObjectType.PROJECT_METADATA)
			return;
		if(!cmd.getFieldTag().equals(ProjectMetadata.TAG_SHORT_PROJECT_SCOPE))
			return;
		
		updateProjectScope();
	}

	public void updateProjectScope()
	{
		getDiagramComponent().getDiagramModel().updateProjectScope();
	}

	public void jump(Class stepMarker) throws Exception
	{
		wizardPanel.jump(stepMarker);
	}

	public void showNodeProperties(DiagramNode node, int startingTabIdentifier)
	{
		closeActivePropertiesDialog();
		if(nodePropertiesDlg != null)
			disposeOfNodePropertiesDialog();
		String title = EAM.text("Title|Properties");
		nodePropertiesPanel = new NodePropertiesPanel(getMainWindow(), diagram);
		nodePropertiesDlg = new ModelessDialogWithClose(getMainWindow(), nodePropertiesPanel, title);
		
		nodePropertiesPanel.setCurrentNode(diagram, node);
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
		
		DiagramNode selectedNode = diagram.getSelectedNode();
		if(selectedNode == null || !selectedNode.equals(nodePropertiesPanel.getCurrentNode()))
			disposeOfNodePropertiesDialog();
	}

	
	JSplitPane bigSplitter;
	DiagramComponent diagram;
	Properties propertiesDoer;
	DiagramWizardPanel wizardPanel;
	String mode;
	DiagramLegendPanel legendDialog;
	
	ModelessDialogWithClose nodePropertiesDlg;
	NodePropertiesPanel nodePropertiesPanel;

}
