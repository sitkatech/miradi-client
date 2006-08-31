/*
 * Copyright 2005, The Benetech Initiative
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
import org.conservationmeasures.eam.actions.ActionCreateIndicator;
import org.conservationmeasures.eam.actions.ActionCreateObjective;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
import org.conservationmeasures.eam.actions.ActionInsertCluster;
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
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ConceptualModelCluster;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ConceptualModelNodeSet;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.views.umbrella.CreateIndicator;
import org.conservationmeasures.eam.views.umbrella.CreateObjective;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.martus.swing.UiScrollPane;

public class DiagramView extends UmbrellaView implements CommandExecutedListener
{
	public DiagramView(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse);
		mode = ViewData.MODE_DEFAULT;
		diagram = new DiagramComponent(getMainWindow());
		getProject().setSelectionModel(diagram.getSelectionModel());
		
		addDiagramViewDoersToMap();
		
		updateToolBar();

		setLayout(new BorderLayout());
		
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
		return "Diagram";
	}
	
	public BufferedImage getImage()
	{
		return diagram.getImage();
	}
	
	public JComponent getPrintableComponent()
	{
		return diagram.getPrintableComponent();
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
		addDoerToMap(ActionInsertCluster.class, new CreateCluster());
		addDoerToMap(ActionCopy.class, new Copy());
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
		addDoerToMap(ActionCreateIndicator.class, new CreateIndicator());
	}
	
	public void becomeActive() throws Exception
	{
		int dividerAt = -1;
		if(bigSplitter != null)
			dividerAt = bigSplitter.getDividerLocation();
		
		removeAll();
		
		UiScrollPane uiScrollPane = new UiScrollPane(diagram);
		uiScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		uiScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		uiScrollPane.getHorizontalScrollBar().setUnitIncrement(getProject().getGridSize());
		uiScrollPane.getVerticalScrollBar().setUnitIncrement(getProject().getGridSize());

		// NOTE: For reasons I don't understand, if we construct the splitter 
		// in the constructor, it always ignores the setDividerLocation and ends up
		// at zero.
		bigSplitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		bigSplitter.setResizeWeight(.5);
		bigSplitter.setTopComponent(createWizard());
		bigSplitter.setBottomComponent(uiScrollPane);
		
		if(dividerAt > 0)
			bigSplitter.setDividerLocation(dividerAt);
		
		add(bigSplitter, BorderLayout.CENTER);

		
		setMode(getViewData().getData(ViewData.TAG_CURRENT_MODE));
	}
	
	public void becomeInactive() throws Exception
	{
		// TODO: This should completely tear down the view
	}
	

	public JPanel createWizard() throws Exception
	{
		WizardPanel wizard = new WizardPanel();
		DiagramWizardOverviewStep step = new DiagramWizardOverviewStep();
		wizard.setContents(step);
		step.refresh();
		return wizard;
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
			IdList visibleIds = new IdList(viewData.getData(ViewData.TAG_BRAINSTORM_NODE_IDS));
			visibleIds.addAll(getRelatedDraftInterventions(visibleIds));
			Vector allNodes = getProject().getDiagramModel().getAllNodes();
			for (int i = 0; i < allNodes.size(); ++i)
			{
				DiagramNode node = (DiagramNode) allNodes.get(i);
				BaseId id = node.getId();
				if (!visibleIds.contains(id))
					idsToHide.add(id);
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		return idsToHide;
	}
	
	IdList getRelatedDraftInterventions(IdList chainIds)
	{
		IdList draftsToAdd = new IdList();
		
		for(int i = 0; i < chainIds.size(); ++i)
		{
			BaseId nodeId = chainIds.get(i);
			ConceptualModelNode node = getProject().findNode(nodeId);
			ConceptualModelNodeSet possibleDraftInterventionIds = getProject().getDiagramModel().getDirectlyLinkedUpstreamNodeIds(node);
			Iterator iter = possibleDraftInterventionIds.iterator();
			while(iter.hasNext())
			{
				BaseId possibleInterventionId = ((ConceptualModelNode)iter.next()).getId();
				if(chainIds.contains(possibleInterventionId))
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
		if(cmd.getObjectType() != ObjectType.MODEL_LINKAGE)
			return;
		
		// may have added or removed a stress
		diagram.repaint(diagram.getBounds());
	}
	
	private void updateClusterAfterUndoIfNeeded(CommandSetObjectData cmd) throws Exception
	{
		if(cmd.getObjectType() != ObjectType.MODEL_NODE)
			return;
		
		ConceptualModelNode cmNode = getProject().findNode(cmd.getObjectId());
		if(!cmNode.isCluster())
			return;
		
		if(!cmd.getFieldTag().equals(ConceptualModelCluster.TAG_MEMBER_IDS))
			return;
		
		IdList newMembers = new IdList(cmd.getPreviousDataValue());
		IdList oldMembers = new IdList(cmd.getDataValue());
		
		updateCluster(cmd.getObjectId(), newMembers, oldMembers);
	}
	
	private void captureClusterIfNeeded(CommandSetObjectData cmd) throws Exception
	{
		if(cmd.getObjectType() != ObjectType.MODEL_NODE)
			return;
		
		ConceptualModelNode cmNode = getProject().findNode(cmd.getObjectId());
		if(!cmNode.isCluster())
			return;
		
		if(!cmd.getFieldTag().equals(ConceptualModelCluster.TAG_MEMBER_IDS))
			return;
		
		BaseId clusterId = cmd.getObjectId();
		IdList newMembers = new IdList(cmd.getDataValue());
		DiagramModel model = getDiagramComponent().getDiagramModel();
		DiagramCluster cluster = (DiagramCluster)model.getNodeById(clusterId);
		IdList oldMembers = new IdList(cluster.getUnderlyingObject().getData(ConceptualModelCluster.TAG_MEMBER_IDS));
		
		updateCluster(cluster.getId(), newMembers, oldMembers);
	}

	private void updateCluster(BaseId clusterId, IdList newMembers, IdList oldMembers) throws Exception
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
			DiagramNode memberNode = model.getNodeById(memberId);
			getProject().removeNodeFromCluster(cluster, memberNode);
		}
		
		for(int i = 0; i < idsToAdd.size(); ++i)
		{
			BaseId memberId = idsToAdd.get(i);
			DiagramNode memberNode = model.getNodeById(memberId);
			getProject().addNodeToCluster(cluster, memberNode);
		}
		
		model.updateCell(cluster);
	}

	JSplitPane bigSplitter;
	DiagramComponent diagram;
	Properties propertiesDoer;
	String mode;
}

