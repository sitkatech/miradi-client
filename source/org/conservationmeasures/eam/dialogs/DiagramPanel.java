/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.Component;
import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.EAMGraphSelectionModel;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.DiagramLink;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.diagram.ConceptualModelDiagramSplitPane;
import org.conservationmeasures.eam.views.diagram.DiagramLegendPanel;
import org.conservationmeasures.eam.views.diagram.DiagramModelUpdater;
import org.conservationmeasures.eam.views.diagram.DiagramSplitPane;
import org.conservationmeasures.eam.views.diagram.ResultsChainDiagramSplitPane;

public class DiagramPanel extends DisposablePanel implements CommandExecutedListener
{
	
	public DiagramPanel(MainWindow mainWindowToUse, int objectType) throws Exception
	{
		try
		{
			mainWindow = mainWindowToUse;
			project = mainWindow.getProject();
			diagramSplitter = createDiagramSplitter(objectType);
			add(diagramSplitter);
			project.addCommandExecutedListener(this);
		}
		catch (Exception e)
		{
			dispose();
			throw e;
		}
	}
	
	public void dispose()
	{
		super.dispose();
		project.removeCommandExecutedListener(this);
		diagramSplitter.dispose();
	}
	
	private DiagramSplitPane createDiagramSplitter(int objectType) throws Exception
	{
		if (objectType == ResultsChainDiagram.getObjectType())
			return  new ResultsChainDiagramSplitPane(mainWindow);

		if (objectType == ConceptualModelDiagram.getObjectType())
			return new ConceptualModelDiagramSplitPane(mainWindow);

		throw new Exception("Found wrong type for splitter " +objectType);
	}
	
	public DiagramObject getDiagramObject()
	{
		return getDiagramModel().getDiagramObject();
	}

	private EAMGraphSelectionModel getSelectionModel()
	{
		DiagramComponent diagram = getDiagramSplitPane().getDiagramComponent();
		return (EAMGraphSelectionModel) diagram.getSelectionModel();
	}
	
	public void setSelectionModel(EAMGraphSelectionModel selectionModelToUse)
	{
		DiagramComponent diagram = getDiagramSplitPane().getDiagramComponent();
		diagram.setSelectionModel(selectionModelToUse);
	}
	
	public EAMGraphCell[] getSelectedAndRelatedCells()
	{
		Object[] selectedCells = getSelectionModel().getSelectionCells();
		Vector cellVector = getDiagramModel().getAllSelectedCellsWithRelatedLinkages(selectedCells);
		return (EAMGraphCell[])cellVector.toArray(new EAMGraphCell[0]);
	}
	
	public void selectFactor(FactorId idToUse)
	{
		try
		{
			FactorCell nodeToSelect = getDiagramModel().getFactorCellByWrappedId(idToUse);
			getSelectionModel().setSelectionCell(nodeToSelect);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public LinkCell[] getOnlySelectedLinkCells()
	{
		if(getSelectionModel() == null)
			return new LinkCell[0];
	
		Object[] rawCells = getSelectionModel().getSelectionCells();
		return getOnlySelectedLinkCells(rawCells);
	}
	
	public DiagramLink[] getOnlySelectedLinks()
	{
		if(getSelectionModel() == null)
			return new DiagramLink[0];
		
		Object[] rawCells = getSelectionModel().getSelectionCells();
		return getOnlySelectedLinks(rawCells);
	}
	
	public DiagramLink[] getOnlySelectedLinks(Object [] allSelectedCells)
	{
		Vector linkages = new Vector();
		for(int i = 0; i < allSelectedCells.length; ++i)
		{
			if(((EAMGraphCell)allSelectedCells[i]).isFactorLink())
			{
				LinkCell cell = (LinkCell)allSelectedCells[i];
				linkages.add(cell.getDiagramFactorLink());
			}
		}
		return (DiagramLink[])linkages.toArray(new DiagramLink[0]);
	}
	
	public LinkCell[] getOnlySelectedLinkCells(Object [] allSelectedCells)
	{
		Vector linkages = new Vector();
		for(int i = 0; i < allSelectedCells.length; ++i)
		{
			if(((EAMGraphCell)allSelectedCells[i]).isFactorLink())
			{
				LinkCell cell = (LinkCell)allSelectedCells[i];
				linkages.add(cell);
			}
		}
		return (LinkCell[])linkages.toArray(new LinkCell[0]);
	}

	public FactorCell[] getOnlySelectedFactorCells()
	{
		if(getSelectionModel() == null)
			return new FactorCell[0];
		
		Object[] rawCells = getSelectionModel().getSelectionCells();
		return getOnlySelectedFactorCells(rawCells);
	}
	
	public static FactorCell[] getOnlySelectedFactorCells(Object[] allSelectedCells)
	{
		Vector nodes = new Vector();
		for(int i = 0; i < allSelectedCells.length; ++i)
		{
			if(((EAMGraphCell)allSelectedCells[i]).isFactor())
				nodes.add(allSelectedCells[i]);
		}
		return (FactorCell[])nodes.toArray(new FactorCell[0]);
	}
	
	public Factor[] getOnlySelectedFactors()
	{
		if (getSelectionModel() == null)
			return new Factor[0];
		
		Object[] rawCells = getSelectionModel().getSelectionCells();
		return getOnlySelectedFactors(rawCells);
	}
	
	private Factor[] getOnlySelectedFactors(Object[] allSelectedFactors)
	{
		Vector nodes = new Vector();
		for(int i = 0; i < allSelectedFactors.length; ++i)
		{
			EAMGraphCell graphCell = ((EAMGraphCell)allSelectedFactors[i]);
			if(graphCell.isFactor())
			{
				ORef ref = graphCell.getDiagramFactor().getWrappedORef();
				Factor factor = (Factor) project.findObject(ref);
				nodes.add(factor);
			}
		}
		return (Factor[])nodes.toArray(new Factor[0]);

	}
	
	public EAMGraphCell[] getOnlySelectedCells()
	{
		Object[] rawCells = getSelectionModel().getSelectionCells();
		EAMGraphCell[] cells = new EAMGraphCell[rawCells.length];
		for(int i=0; i < cells.length; ++i)
			cells[i] = (EAMGraphCell)rawCells[i];
		return cells;
	}
	
	public void moveFactors(int deltaX, int deltaY, DiagramFactorId[] ids) throws Exception 
	{
		getDiagramModel().moveFactors(deltaX, deltaY, ids);
	}

	public DiagramModel getDiagramModel()
	{
		DiagramComponent diagramComponent = getdiagramComponent();
		if (diagramComponent==null)
			return null;
		return diagramComponent.getDiagramModel();
	}
	
	public DiagramComponent[] getAllSplitterDiagramComponents()
	{
		return getDiagramSplitPane().getAllOwenedDiagramComponents();
	}
	
	public DiagramComponent getdiagramComponent()
	{
		DiagramComponent diagramComponent = getDiagramSplitPane().getDiagramComponent();
		return diagramComponent;
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Diagram Panel");
	}

	public void addFieldComponent(Component component)
	{
		throw new RuntimeException("Not yet implemented");
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		if (! event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return;
		
		try
		{
			CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
			updateCurrentDiagramObject(setCommand);
			DiagramModelUpdater modelUpdater = new DiagramModelUpdater(project, getDiagramModel(), getDiagramObject());
			modelUpdater.commandSetObjectDataExecuted(setCommand);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private void updateCurrentDiagramObject(CommandSetObjectData setObjectDataCommand)
	{
		if (setObjectDataCommand.getObjectType()!= ObjectType.VIEW_DATA)
			return;
		
		if (setObjectDataCommand.getFieldTag() == ViewData.TAG_CURRENT_DIAGRAM_REF)
		{	
			ViewData viewData = (ViewData) project.findObject(setObjectDataCommand.getObjectORef());
			ORef currentDiagramObjectRef = viewData.getCurrentDiagramRef();
			getDiagramSplitPane().setCurrentDiagramObjectRef(currentDiagramObjectRef);
		}

	}

	public DiagramSplitPane getDiagramSplitPane()
	{
		return diagramSplitter;
	}
	
	public DiagramLegendPanel getDiagramLegendPanel()
	{
		return getDiagramSplitPane().getLegendPanel();
	}
	
	DiagramSplitPane diagramSplitter;
	private Project project;
	private MainWindow mainWindow;
}
