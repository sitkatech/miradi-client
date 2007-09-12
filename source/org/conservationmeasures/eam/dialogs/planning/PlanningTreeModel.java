/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.dialogs.planning.treenodes.PlanningTreeRootNode;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableNode;

public class PlanningTreeModel extends GenericTreeTableModel
{	
	public PlanningTreeModel(Project projectToUse) throws Exception
	{
		super(new PlanningTreeRootNode(projectToUse));
		project = projectToUse;
		rebuildCodeList();
	}

	public void rebuildCodeList() throws Exception
	{
		String codeListAsString = project.getCurrentViewData().getData(ViewData.TAG_PLANNING_VISIBLE_COL_TYPES);
		CodeList listToShow = new CodeList(codeListAsString);
		columnsToShow = new CodeList();
		final String DEFAULT_COLUM = "Item";
		columnsToShow.add(DEFAULT_COLUM);
		columnsToShow.addAll(listToShow);
	}

	public int getColumnCount()
	{
		return getColumnTags().size();
	}
	
	public String getColumnName(int column)
	{
		return EAM.fieldLabel(ObjectType.FAKE, getColumnTag(column));
	}
	
	public String getColumnTag(int column)
	{
		return getColumnTags().get(column);
	}
	
	public Object getValueAt(Object rawNode, int col)
	{
		TreeTableNode treeNode = (TreeTableNode) rawNode;
		String columnTag = getColumnTag(col);
		BaseObject baseObject = treeNode.getObject();
		if(baseObject == null)
			return "";
		
		if (! baseObject.doesFieldExist(columnTag))
			return "";
		
		return baseObject.getData(columnTag);
	}
	
	public CodeList getColumnTags()
	{
		return columnsToShow;	
	}
	
	Project project;
	CodeList columnsToShow;	
}
