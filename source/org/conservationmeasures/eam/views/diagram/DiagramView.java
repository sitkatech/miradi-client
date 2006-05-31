/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.conservationmeasures.eam.actions.ActionConfigureLayers;
import org.conservationmeasures.eam.actions.ActionCopy;
import org.conservationmeasures.eam.actions.ActionCut;
import org.conservationmeasures.eam.actions.ActionDelete;
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
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.IdList;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.utils.HtmlBuilder;
import org.conservationmeasures.eam.utils.HyperlinkHandler;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.martus.swing.UiScrollPane;

public class DiagramView extends UmbrellaView implements CommandExecutedListener
{
	public DiagramView(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse);
		diagram = new DiagramComponent(getProject(), getActions());
		getProject().setSelectionModel(diagram.getSelectionModel());
		
		addDiagramViewDoersToMap();
		
		setToolBar(new DiagramToolBar(getActions()));

		setLayout(new BorderLayout());
		
		getProject().addCommandExecutedListener(this);

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
		try
		{
			if(newMode.equals(ViewData.MODE_STRATEGY_BRAINSTORM))
			{
				IdList visibleIds = new IdList(getProject().getCurrentViewData().getData(ViewData.TAG_BRAINSTORM_NODE_IDS));
				Vector allNodes = getProject().getDiagramModel().getAllNodes();
				for(int i = 0; i < allNodes.size(); ++i)
				{
					DiagramNode node = (DiagramNode)allNodes.get(i);
					if(!visibleIds.contains(node.getId()))
						hiddenIds.add(node.getId());
				}
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}

		LayerManager manager = getProject().getLayerManager();
		manager.setHiddenIds(hiddenIds);
		getMainWindow().updateStatusBar();
		getDiagramComponent().clearSelection();
		getDiagramComponent().repaint();
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
		if(cmd.getObjectType() == ObjectType.MODEL_LINKAGE)
		{
			// may have added or removed a stress
			diagram.repaint(diagram.getBounds());
		}
	}

	JSplitPane bigSplitter;
	DiagramComponent diagram;
	Properties propertiesDoer;
}

class DoNothingHyperLinkHandler implements HyperlinkHandler
{
	public void linkClicked(String linkDescription)
	{
		// TODO Auto-generated method stub
		
	}

	public void valueChanged(String widget, String newValue)
	{
		// TODO Auto-generated method stub
		
	}

	public void buttonPressed(String buttonName)
	{
		// TODO Auto-generated method stub
		
	}

}
class DiagramWizardHtml extends HtmlBuilder
{
	public static String text()
	{
		return "";
	}
}