/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import javax.swing.BorderFactory;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.ObjectPoolTable;
import org.conservationmeasures.eam.dialogs.ObjectPoolTableModel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;

abstract public class DiagramPageList extends ObjectPoolTable
{
	public DiagramPageList(Project projectToUse, ObjectPoolTableModel objectPoolTableModel)
	{
		super(objectPoolTableModel);
		project = projectToUse;
		
		getSelectionModel().addListSelectionListener(new DiagramObjectListSelectionListener(project));
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setBorder(BorderFactory.createEtchedBorder());
	}
	
	public void fillListWithSelectedDiagramObject(DiagramObject diagramObject)
	{
		try
		{
			fillList();
			ViewData diagramViewData = project.getViewData(DiagramView.getViewName());
			CommandSetObjectData setViewData = new CommandSetObjectData(diagramViewData.getRef(), getCurrentDiagramViewDataTag(), diagramObject.getRef());
			project.executeCommand(setViewData);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	public void fillList()
	{
		getObjectPoolTableModel().rowsWereAddedOrRemoved();
	}
	
	private void setViewDataCurrentDiagramObjectRef(ORef selectedRef) throws Exception
	{
		ViewData currentViewDat = project.getViewData(DiagramView.getViewName());
		CommandSetObjectData setCurrentDiagramObject = new CommandSetObjectData(currentViewDat.getRef(), getCurrentDiagramViewDataTag(), selectedRef);
		project.executeCommand(setCurrentDiagramObject);
	}

	public String getCurrentDiagramViewDataTag()
	{
		if (getManagedDiagramType() == ObjectType.CONCEPTUAL_MODEL_DIAGRAM)
			return ViewData.TAG_CURRENT_CONCEPTUAL_MODEL_REF;
		
		if (getManagedDiagramType() == ObjectType.RESULTS_CHAIN_DIAGRAM)
			return ViewData.TAG_CURRENT_RESULTS_CHAIN_REF;
		
		throw new RuntimeException("Could not find corrent tag for " + getManagedDiagramType());
	}
	
	public class DiagramObjectListSelectionListener  implements ListSelectionListener
	{
		public DiagramObjectListSelectionListener(Project projectToUse)
		{
			project = projectToUse;
		}

		public void valueChanged(ListSelectionEvent event)
		{
			setCurrentDiagram();
		}

		private void setCurrentDiagram()
		{
			try
			{
				ORef selectedRef = getSelectedRef();
				setViewDataCurrentDiagramObjectRef(selectedRef);				
			}
			catch(Exception e)
			{
				//TODO nima do somethning with this exception
				EAM.logException(e);
			}
		}

		private ORef getSelectedRef()
		{
			if (getSelectedObjects().length == 0)
				return ORef.INVALID;
			
			BaseObject selectedDiagramObject = getSelectedObjects()[0];
			if (selectedDiagramObject == null)
				return ORef.INVALID;
			
			return selectedDiagramObject.getRef();
		}
		
		Project project;
	}
		
	abstract public boolean isResultsChainPageList();
	
	abstract public boolean isConceptualModelPageList();
	
	abstract public int getManagedDiagramType();
	
	Project project;
}