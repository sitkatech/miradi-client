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

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.ObjectPoolTable;
import org.conservationmeasures.eam.dialogs.ObjectPoolTableModel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
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
	
	public void listChanged()
	{
		getObjectPoolTableModel().rowsWereAddedOrRemoved();
	}

	private void setViewDataCurrentDiagramObjectRef(ORef selectedRef) throws Exception
	{
		ORef currentDiagramRef = getCurrentDiagramViewDataRef();
		if (currentDiagramRef.equals(selectedRef))
			return;
	
		ViewData viewData = project.getDiagramViewData();
		CommandSetObjectData setCurrentDiagramObject = new CommandSetObjectData(viewData.getRef(), getCurrentDiagramViewDataTag(), selectedRef);
		project.executeCommand(setCurrentDiagramObject);
	}

	public static String getCurrentDiagramViewDataTag(int objectType)
	{
		if (objectType == ObjectType.CONCEPTUAL_MODEL_DIAGRAM)
			return ViewData.TAG_CURRENT_CONCEPTUAL_MODEL_REF;
		
		if (objectType == ObjectType.RESULTS_CHAIN_DIAGRAM)
			return ViewData.TAG_CURRENT_RESULTS_CHAIN_REF;
		
		throw new RuntimeException("Could not find corrent tag for " + objectType);
	}
	
	public String getCurrentDiagramViewDataTag()
	{
		return getCurrentDiagramViewDataTag(getManagedDiagramType());
	}
	
	public ORef getCurrentDiagramViewDataRef() throws Exception
	{
		ViewData viewData = project.getDiagramViewData();
		return getCurrentDiagramViewDataRef(viewData, getManagedDiagramType());
	}
	
	public static ORef getCurrentDiagramViewDataRef(ViewData viewData, int objectType) throws Exception
	{
		String currrentDiagramViewDataTag = getCurrentDiagramViewDataTag(objectType);
		String orefAsJsonString = viewData.getData(currrentDiagramViewDataTag);
		
		return ORef.createFromString(orefAsJsonString);
	}
	
	public class DiagramObjectListSelectionListener  implements ListSelectionListener
	{
		public DiagramObjectListSelectionListener(Project projectToUse)
		{
			project = projectToUse;
		}

		public void valueChanged(ListSelectionEvent event)
		{
			try
			{
				setCurrentDiagram();
			}
			catch(Exception e)
			{
				EAM.unexpectedErrorLoggedWithDialog(e);
			}
		}

		private void setCurrentDiagram() throws Exception
		{
			project.executeCommand(new CommandBeginTransaction());
			try
			{
				ORef selectedRef = getSelectedRef();
				ensureDefaultMode();
				setViewDataCurrentDiagramObjectRef(selectedRef);				
			}
			finally
			{
				project.executeCommand(new CommandEndTransaction());
			}
		}

		private void ensureDefaultMode() throws Exception
		{
			ViewData viewData = project.getCurrentViewData();
			if (viewData.getData(ViewData.TAG_CURRENT_MODE).equals(ViewData.MODE_DEFAULT))
				return;
			
			CommandSetObjectData setDefaultMode = new CommandSetObjectData(viewData.getRef(), ViewData.TAG_CURRENT_MODE, ViewData.MODE_DEFAULT);
			project.executeCommand(setDefaultMode);
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