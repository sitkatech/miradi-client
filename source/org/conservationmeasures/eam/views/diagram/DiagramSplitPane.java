/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.awt.CardLayout;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.FastScrollPane;

abstract public class DiagramSplitPane extends JSplitPane
{
	public DiagramSplitPane(MainWindow mainWindowToUse, int objectType) throws Exception
	{
		mainWindow = mainWindowToUse;
		project = mainWindow.getProject();
		diagramCards = createDiagramCards(objectType);
		
		
		setLeftComponent(createLeftPanel(objectType));
		setRightComponent(new FastScrollPane(diagramCards));
		
		int scrollBarWidth = ((Integer)UIManager.get("ScrollBar.width")).intValue();
		setDividerLocation(scrollableLegendPanel.getPreferredSize().width + scrollBarWidth);
	}
	
	private DiagramCards createDiagramCards(int objectType) throws Exception
	{
		ORefList diagramObjectRefList = getDiagramObjects(objectType);
		DiagramCards diagramComponentCards = new DiagramCards();
		for (int i = 0; i < diagramObjectRefList.size(); ++i)
		{
			ORef diagramObjectRef = diagramObjectRefList.get(i);
			DiagramObject diagramObject = (DiagramObject) project.findObject(diagramObjectRef);
			DiagramComponent diagramComponentToAdd = createDiagram(mainWindow, diagramObject);

			String cardName = diagramObjectRef.toString();
			diagramComponentCards.add(diagramComponentToAdd, cardName);
		}
	
		return diagramComponentCards;
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

	private JScrollPane createLegendScrollPane()
	{
		JScrollPane scrollPane = new JScrollPane(legendPanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		return scrollPane;
	}
	
	protected JSplitPane createLeftPanel(int objectType) throws Exception
	{
		legendPanel = createLegendPanel(mainWindow);
		scrollableLegendPanel = createLegendScrollPane();
		selectionPanel = createPageList(mainWindow.getProject());
		selectionPanel.fillList();
		selectionPanel.addListSelectionListener(new DiagramObjectListSelectionListener(project, objectType));
		
		JSplitPane leftSideSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		leftSideSplit.setTopComponent(selectionPanel);
		leftSideSplit.setBottomComponent(scrollableLegendPanel);
		
		return leftSideSplit;
	}

	public void setDefaultSelection() throws Exception
	{
		ViewData viewData = project.getCurrentViewData();
		ORef currentDiagramObjectRef = viewData.getCurrentDiagramRef();
		if (! currentDiagramObjectRef.isInvalid())
		{
			DiagramObject diagramObject = (DiagramObject) project.findObject(currentDiagramObjectRef);
			selectionPanel.setSelectedValue(diagramObject, true);
		}
		else if (selectionPanel.getListSize() > 0)
		{
			selectionPanel.setSelectedIndex(0);
		}
	}
	
	public DiagramLegendPanel getLegendPanel()
	{
		return legendPanel;
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
			super(new CardLayout());
			cards = new Vector();
		}
		
		public void add(DiagramComponent diagramComponent, String name)
		{
			super.add(diagramComponent, name);
			
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
	
	public class DiagramObjectListSelectionListener  implements ListSelectionListener
	{
		public DiagramObjectListSelectionListener(Project projectToUse, int objectType)
		{
			project = projectToUse;
			diagramObjectType = objectType;
		}

		public void valueChanged(ListSelectionEvent event)
		{
			setCurrentDiagram();
		}

		private void setCurrentDiagram()
		{
			try
			{
				BaseObject selectedDiagramObject = (BaseObject) selectionPanel.getSelectedValue();
				if (selectedDiagramObject == null)
					return;
				
				ORef selectedRef = selectedDiagramObject.getRef();		
				setViewDataCurrentDiagramObjectRef(selectedRef);				
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}
		
		Project project;
		int diagramObjectType;
	}
	
	private void setViewDataCurrentDiagramObjectRef(ORef selectedRef) throws Exception
	{
		ViewData currentViewDat = project.getViewData(DiagramView.getViewName());
		CommandSetObjectData setCurrentDiagramObject = new CommandSetObjectData(currentViewDat.getRef(), ViewData.TAG_CURRENT_DIAGRAM_REF, selectedRef);
		project.executeCommand(setCurrentDiagramObject);
	}
	
	public void showCard(ORef diagramObjectRef)
	{
		DiagramComponent diagramComponent = diagramCards.findByRef(diagramObjectRef);
		if (diagramComponent == null)
			return;
		
		setCurrentDiagramObjectRef(diagramObjectRef);
		DiagramObject diagramObject = diagramComponent.getDiagramModel().getDiagramObject();
		CardLayout cardLayout = (CardLayout) diagramCards.getLayout();
		String cardName = diagramObjectRef.toString();
		cardLayout.show(diagramCards, cardName);
		mainWindow.getDiagramView().updateVisibilityOfFactors();
		selectionPanel.setSelectedValue(diagramObject, true);
	}
		
	public void setCurrentDiagramObjectRef(ORef currentDiagramObjectRef)
	{
		currentRef = currentDiagramObjectRef;
	}

	public DiagramPageList getDiagramPageList()
	{
		return selectionPanel;
	}
	
	abstract public DiagramPageList createPageList(Project projectToUse);
	
	abstract public DiagramLegendPanel createLegendPanel(MainWindow mainWindowToUse);
	
	protected DiagramLegendPanel legendPanel;
	private DiagramPageList selectionPanel;
	private JScrollPane scrollableLegendPanel;
	private MainWindow mainWindow;
	private Project project;
	private DiagramCards diagramCards;
	private ORef currentRef;
}
