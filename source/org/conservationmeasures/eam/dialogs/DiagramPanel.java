/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.diagram.DiagramModel;
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
		getProject().setSelectionModel(diagram.getEAMGraphSelectionModel());
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
		//FIXME dispose properly
		diagram = null;
	}
	
	private DiagramComponent diagram;
	private MainWindow mainWindow;
}
