/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.martus.swing.UiLabel;

abstract public class DiagramSplitPane extends JSplitPane implements CommandExecutedListener
{
	public DiagramSplitPane(MainWindow mainWindowToUse, int objectType) throws Exception
	{
		mainWindow = mainWindowToUse;
		project = mainWindow.getProject();
		diagramCards = new DiagramCards();
		reloadDiagramCards(objectType);
		
		setLeftComponent(createLeftPanel(objectType));
		setRightComponent(new FastScrollPane(diagramCards));
		
		project.addCommandExecutedListener(this);
		
		int scrollBarWidth = ((Integer)UIManager.get("ScrollBar.width")).intValue();
		setDividerLocation(scrollableLegendPanel.getPreferredSize().width + scrollBarWidth);
	}

	public void showCurrentCard() throws Exception
	{
		showCard(selectionPanel.getCurrentDiagramViewDataRef());
	}
	
	public void dispose()
	{
		project.removeCommandExecutedListener(this);
	}
	
	private void reloadDiagramCards(int objectType) throws Exception
	{
		ORefList diagramObjectRefList = getDiagramObjects(objectType);
		diagramCards.clear();
		for (int i = 0; i < diagramObjectRefList.size(); ++i)
		{
			ORef diagramObjectRef = diagramObjectRefList.get(i);
			DiagramObject diagramObject = (DiagramObject) project.findObject(diagramObjectRef);
			DiagramComponent diagramComponentToAdd = createDiagram(mainWindow, diagramObject);
			diagramCards.addDiagram(diagramComponentToAdd);
		}
	}

	private ORefList getDiagramObjects(int objectType) throws Exception
	{
		EAMObjectPool pool = project.getPool(objectType);
		return pool.getORefList();
	}
	
	public static DiagramComponent createDiagram(MainWindow mainWindow, DiagramObject diagramObject) throws Exception
	{
		DiagramModel diagramModel = new DiagramModel(diagramObject.getProject());
		diagramModel.fillFrom(diagramObject);
		diagramModel.updateProjectScopeBox();
		DiagramComponent diagram = new DiagramComponent(mainWindow);
		diagram.setModel(diagramModel);
		diagram.setGraphLayoutCache(diagramModel.getGraphLayoutCache());
		return diagram;
	}

	protected JComponent createLeftPanel(int objectType) throws Exception
	{
		// NOTE: This code is convoluted, but I couldn't find a simpler way to have 
		// the table at the top be fixed-height but variable width
		
		selectionPanel = createPageList(mainWindow);
		selectionPanel.listChanged();
		JScrollPane selectionScrollPane = new FastScrollPane(selectionPanel);
		selectionScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		selectionScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		Border cushion = BorderFactory.createEmptyBorder(5,5,5,5);
		Border newBorder = BorderFactory.createCompoundBorder(cushion, selectionScrollPane.getBorder());
		selectionScrollPane.setBorder(newBorder);
		selectionScrollPane.setMinimumSize(new Dimension(0,0));

		legendPanel = createLegendPanel(mainWindow);
		scrollableLegendPanel = new FastScrollPane(legendPanel);
		scrollableLegendPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollableLegendPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollableLegendPanel.setMinimumSize(new Dimension(0,0));

		JPanel topPanel = new JPanel(new BorderLayout());
		UiLabel title = createControlPanelTitle();
		topPanel.add(title, BorderLayout.BEFORE_FIRST_LINE);
		topPanel.add(selectionScrollPane, BorderLayout.AFTER_LAST_LINE);
		
		JPanel box = new JPanel(new BorderLayout());
		box.add(topPanel, BorderLayout.BEFORE_FIRST_LINE);
		box.add(scrollableLegendPanel, BorderLayout.CENTER);
		
		return box;
	}
	
	// TODO: Should be combined with similar code in ControlPanel
	public UiLabel createControlPanelTitle()
	{
		UiLabel title = new PanelTitleLabel(EAM.text("Control Bar"));
		title.setFont(title.getFont().deriveFont(Font.BOLD));
		title.setBorder(new LineBorder(Color.BLACK, 2));
		title.setHorizontalAlignment(UiLabel.CENTER);
		title.setMinimumSize(new Dimension(0,0));
		return title;
	}
	
	public DiagramLegendPanel getLegendPanel()
	{
		return legendPanel;
	}
	
	public DiagramObject getDiagramObject()
	{
		return getDiagramModel().getDiagramObject();
	}
	
	public DiagramModel getDiagramModel()
	{
		DiagramComponent diagram = getDiagramComponent();
		if (diagram != null)
			return getDiagramComponent().getDiagramModel();
		
		return null;
	}
	
	public DiagramComponent getDiagramComponent()
	{
		return diagramCards.findByRef(getCurrentDiagramObjectRef());
	}
	
	public DiagramComponent[] getAllOwenedDiagramComponents()
	{
		return diagramCards.getAllDiagramComponents();
	}
	
	public ORef getCurrentDiagramObjectRef()
	{
		return currentRef;
	}
	
	public class DiagramCards extends JPanel
	{
		public DiagramCards()
		{
			super(new BorderLayout());
			cards = new Vector();
		}
		
		public void clear()
		{
			removeAll();
			cards = new Vector();
		}

		public void showDiagram(ORef ref) throws Exception
		{
			removeAll();
			
			showFoundReloadedDiagram(ref);
			
			invalidate();
			repaint();
		}

		private void showFoundReloadedDiagram(ORef ref) throws Exception
		{
			if (ref.isInvalid())
				return;
			
			//FIXME nima,  why does loading all the cards work (shows newly created RC)
			reloadDiagramCards(ref.getObjectType());
			DiagramComponent diagramComponent = findByRef(ref);
			if (diagramComponent != null)			
				add(diagramComponent);
		}
		
		public void addDiagram(DiagramComponent diagramComponent)
		{
			cards.add(diagramComponent);
		}

		public DiagramComponent findByRef(ORef ref)
		{
			for (int i = 0; i < cards.size(); ++i)
			{
				DiagramComponent diagramComponent = (DiagramComponent) cards.get(i);
				ORef diagramObjectRef = diagramComponent.getDiagramModel().getDiagramObject().getRef();
				if (diagramObjectRef.equals(ref))
				{
					return diagramComponent;
				}
			}

			return null;
		}
		
		public DiagramComponent[] getAllDiagramComponents()
		{
			return (DiagramComponent[]) cards.toArray(new DiagramComponent[0]);
		}
		
		public int getCardCount()
		{
			return cards.size();
		}
		
		Vector cards;
	}
		
	public void showCard(ORef diagramObjectRef)
	{
		try
		{
			setCurrentDiagramObjectRef(diagramObjectRef);
			diagramCards.showDiagram(diagramObjectRef);
			DiagramComponent diagramComponent = diagramCards.findByRef(diagramObjectRef);
			if (diagramComponent == null)
				return;

			mainWindow.getDiagramView().updateVisibilityOfFactorsAndLinks();
			selectionPanel.setSelectedRow(diagramObjectRef);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("An error is preventing this diagram from displaying correctly. " +
									 "Most likely, the project has gotten corrupted. Please contact " +
									 "the Miradi team for help and advice. We recommend that you not " +
									 "make any changes to this project until this problem has been resolved."));
		}		
	}
		
	public void setCurrentDiagramObjectRef(ORef currentDiagramObjectRef)
	{
		currentRef = currentDiagramObjectRef;
	}

	public DiagramPageList getDiagramPageList()
	{
		return selectionPanel;
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			if (event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
				handleCommandSetObjectData((CommandSetObjectData) event.getCommand());

			if (event.getCommandName().equals(CommandCreateObject.COMMAND_NAME))
				handleCommandCreateObject((CommandCreateObject) event.getCommand());

			if (event.getCommandName().equals(CommandDeleteObject.COMMAND_NAME))
				handleCommandDeleteObject((CommandDeleteObject) event.getCommand());
		}
		catch(Exception e)
		{
			EAM.panic(e); 
		}
	}

	private void handleCommandDeleteObject(CommandDeleteObject commandDeleteObject) throws Exception
	{
		int objectTypeFromCommand = commandDeleteObject.getObjectType();
		if (getContentType() != objectTypeFromCommand)
			return;
		
		reload();
	}

	private void handleCommandCreateObject(CommandCreateObject commandCreateObject) throws Exception
	{
		int objectTypeFromCommand = commandCreateObject.getObjectType();
		if (getContentType() != objectTypeFromCommand)
			return;
		
		reload();
	}

	private void handleCommandSetObjectData(CommandSetObjectData commandSetObjectData) throws Exception
	{
		if (commandSetObjectData.getObjectType() == getContentType())
			handleDiagramContentsChange(commandSetObjectData);
		
		if (commandSetObjectData.getObjectType()== ObjectType.VIEW_DATA)
			handleViewDataContentsChange(commandSetObjectData);
		
		if (commandSetObjectData.getObjectType() == DiagramLink.getObjectType())
			handleDiagramLinkContentsChange(commandSetObjectData);
		
		handleGroupBoxTypes(commandSetObjectData);
		
	}

	private void handleDiagramLinkContentsChange(CommandSetObjectData commandSetObjectData) throws Exception
	{
		if(!commandSetObjectData.getFieldTag().equals(DiagramLink.TAG_GROUPED_DIAGRAM_LINK_REFS))
			return;
		
		DiagramModel diagramModel = getDiagramModel();
		if(diagramModel == null)
			return;

		diagramModel.updateVisibilityOfFactorsAndLinks();
	}

	private void handleGroupBoxTypes(CommandSetObjectData commandSetObjectData)
	{
		if (commandSetObjectData.getObjectType() != DiagramFactor.getObjectType())
			return;
		
		if (getDiagramModel() == null)
			return;
		
		if (commandSetObjectData.getFieldTag().equals(DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS))
			getDiagramModel().updateGroupBoxCells();
		
		if (commandSetObjectData.getFieldTag().equals(DiagramFactor.TAG_LOCATION))
			getDiagramModel().updateGroupBoxCells();
	
		if (commandSetObjectData.getFieldTag().equals(DiagramFactor.TAG_SIZE))
			getDiagramModel().updateGroupBoxCells();
	}

	private void handleViewDataContentsChange(CommandSetObjectData commandSetObjectData)
	{
		if (commandSetObjectData.getFieldTag() != selectionPanel.getCurrentDiagramViewDataTag())
			return;
		
		ViewData viewData = (ViewData) project.findObject(commandSetObjectData.getObjectORef());
		ORef viewDataCurrentDiagramRef = getCurrentDiagramRef(viewData);
		showCard(viewDataCurrentDiagramRef);
	}

	private void handleDiagramContentsChange(CommandSetObjectData setCommand) throws Exception
	{
		DiagramModel diagramModel = getDiagramModel();
		if (diagramModel == null)
			return;

		DiagramModelUpdater modelUpdater = new DiagramModelUpdater(project, diagramModel);
		modelUpdater.commandSetObjectDataWasExecuted(setCommand);
	}
	
	private void reload() throws Exception
	{
		reloadDiagramCards(getContentType());
		getDiagramPageList().listChanged();
	}

	public int getContentType()
	{
		return getDiagramPageList().getManagedDiagramType();
	}
	
	private ORef getCurrentDiagramRef(ViewData viewData)
	{
		if (getContentType() == ObjectType.CONCEPTUAL_MODEL_DIAGRAM)
			return viewData.getCurrentConceptualModelRef();
		
		if (getContentType() == ObjectType.RESULTS_CHAIN_DIAGRAM)
			return viewData.getCurrentResutlstChainRef();
		
		return ORef.INVALID;
	}
	
	abstract public DiagramPageList createPageList(MainWindow mainWindowToUse);
	
	abstract public DiagramLegendPanel createLegendPanel(MainWindow mainWindowToUse);
	
	protected DiagramLegendPanel legendPanel;
	private DiagramPageList selectionPanel;
	private JScrollPane scrollableLegendPanel;
	private MainWindow mainWindow;
	private Project project;
	private DiagramCards diagramCards;
	private ORef currentRef;
}
