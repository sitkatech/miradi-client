/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.EAMGraphSelectionModel;
import org.conservationmeasures.eam.diagram.cells.EAMGraphCell;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.ConceptualModelDiagramPool;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.diagram.ConceptualModelDiagramSplitPane;

public class DiagramPanel extends ObjectDataInputPanel
{
	public DiagramPanel(MainWindow mainWindowToUse, Project project, ORef orefToUse) throws Exception
	{
		super(project, getDiagramObject(project).getRef());
		mainWindow = mainWindowToUse;
	
		createAndAddDiagram();
		ConceptualModelDiagramSplitPane splitPane = new ConceptualModelDiagramSplitPane(mainWindow, diagram);
		
		add(splitPane);
	}
	
	private void createAndAddDiagram() throws Exception
	{
		DiagramModel diagramModel = new DiagramModel(getProject());
		getProject().setDiagramModel(diagramModel);
		ConceptualModelDiagram conceptualModelDiagram = getDiagramObject(getProject());
		diagramModel.fillFrom(conceptualModelDiagram);
		diagramModel.updateProjectScopeBox();
		
		diagram = new DiagramComponent(mainWindow);
		diagram.setModel(diagramModel);
		diagram.setGraphLayoutCache(diagramModel.getGraphLayoutCache());
		selectionModel = diagram.getEAMGraphSelectionModel();
		
		//FIXME remove project dependency on selection model
		getProject().setSelectionModel(selectionModel);
	}
	
	private static ConceptualModelDiagram getDiagramObject(Project project) throws Exception
	{
		ConceptualModelDiagramPool diagramContentsPool = (ConceptualModelDiagramPool) project.getPool(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
		ORefList oRefs = diagramContentsPool.getORefList();
		return getDiagramContentsObject(project, oRefs);
	}
	
	private static ConceptualModelDiagram getDiagramContentsObject(Project project, ORefList oRefs) throws Exception
	{
		if (oRefs.size() == 0)
		{
			BaseId id = project.createObject(ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
			return (ConceptualModelDiagram) project.findObject(new ORef(ObjectType.CONCEPTUAL_MODEL_DIAGRAM, id));
		}
		if (oRefs.size() > 1)
		{
			EAM.logVerbose("Found more than one diagram contents inside pool");
		}

		ORef oRef = oRefs.get(0);
		return (ConceptualModelDiagram) project.findObject(oRef);
	}
	
	public void setSelectionModel(EAMGraphSelectionModel selectionModelToUse)
	{
		selectionModel = selectionModelToUse;
	}
	
	public EAMGraphCell[] getSelectedAndRelatedCells()
	{
		Object[] selectedCells = selectionModel.getSelectionCells();
		Vector cellVector = getAllSelectedCellsWithRelatedLinkages(selectedCells);
		return (EAMGraphCell[])cellVector.toArray(new EAMGraphCell[0]);
	}
	
	//FIXME this same method exists inside project
	public Vector getAllSelectedCellsWithRelatedLinkages(Object[] selectedCells) 
	{
		DiagramModel model = getDiagramModel();
		Vector selectedCellsWithLinkages = new Vector();
		for(int i=0; i < selectedCells.length; ++i)
		{
			EAMGraphCell cell = (EAMGraphCell)selectedCells[i];
			if(cell.isFactorLink())
			{
				if(!selectedCellsWithLinkages.contains(cell))
					selectedCellsWithLinkages.add(cell);
			}
			else if(cell.isFactor())
			{
				Set linkages = model.getFactorLinks((FactorCell)cell);
				for (Iterator iter = linkages.iterator(); iter.hasNext();) 
				{
					EAMGraphCell link = (EAMGraphCell) iter.next();
					if(!selectedCellsWithLinkages.contains(link))
						selectedCellsWithLinkages.add(link);
				}
				selectedCellsWithLinkages.add(cell);
			}
		}
		return selectedCellsWithLinkages;
	}


	public DiagramModel getDiagramModel()
	{
		return getdiagramComponent().getDiagramModel();
	}
	
	public DiagramComponent getdiagramComponent()
	{
		return diagram;
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Diagram Panel");
	}

	public void dispose()
	{
		super.dispose();
		//FIXME dispose properly
		diagram = null;
		selectionModel = null;
	}
	
	private EAMGraphSelectionModel selectionModel;
	private DiagramComponent diagram;
	private MainWindow mainWindow;
}
