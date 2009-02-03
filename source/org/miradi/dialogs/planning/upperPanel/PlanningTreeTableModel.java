/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.planning.upperPanel;

import org.miradi.dialogs.planning.treenodes.AbstractPlanningTreeNode;
import org.miradi.dialogs.planning.treenodes.PlanningTreeErrorNode;
import org.miradi.dialogs.planning.treenodes.PlanningTreeRootNode;
import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;
import org.miradi.views.planning.ColumnManager;
import org.miradi.views.planning.RowManager;

public class PlanningTreeTableModel extends GenericTreeTableModel
{	
	public PlanningTreeTableModel(Project projectToUse) throws Exception
	{
		this(projectToUse, getVisibleRowCodes(projectToUse), getVisibleColumnCodes(projectToUse));
	}
	
	public PlanningTreeTableModel(Project projectToUse, CodeList visibleRowCodesToUse, CodeList visibleColumnCodesToUse) throws Exception
	{
		super(createPlanningTreeRootNode(projectToUse, visibleRowCodesToUse));
		
		project = projectToUse;
		updateColumnsToShow(visibleColumnCodesToUse);
	}

	private static TreeTableNode createPlanningTreeRootNode(Project projectToUse, CodeList visibleRowCodesToUse) throws Exception
	{
		try
		{
			return new PlanningTreeRootNode(projectToUse, visibleRowCodesToUse);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("<HTML>A severe error has prevented this Planning Subview from being shown.<BR>Please contact Miradi Support (<a href=\"mailto:support@miradi.org\">support@miradi.org<a>) for assistance</HTML>"));
			return new PlanningTreeErrorNode(projectToUse); 
		}
	}

	private static CodeList getVisibleColumnCodes(Project projectToUse) throws Exception
	{
		return ColumnManager.getVisibleColumnCodes(projectToUse.getCurrentViewData());
	}
	
	public static CodeList getVisibleRowCodes(Project projectToUse) throws Exception
	{
		return RowManager.getVisibleRowCodes(projectToUse.getCurrentViewData());
	}

	public void updateColumnsToShow() throws Exception
	{
		CodeList visibleColumnCodes = getVisibleColumnCodes(project);
		updateColumnsToShow(visibleColumnCodes);
	}

	private void updateColumnsToShow(CodeList visibleColumnCodes)
	{
		columnsToShow = new CodeList();
		columnsToShow.add(DEFAULT_COLUMN);
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
	
	public String getColumnTagForNode(int nodeType, int column)
	{
		return getColumnTag(column);
	}

	protected void rebuildNode()
	{
		try
		{
			((AbstractPlanningTreeNode) getRootNode()).setVisibleRowCodes(getVisibleRowCodes(project));
			super.rebuildNode();
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
	
	//TODO the nodes need to implement the content of this method
	public Object getValueAt(Object rawNode, int col)
	{
		AbstractPlanningTreeNode treeNode = (AbstractPlanningTreeNode) rawNode;
		BaseObject baseObject = treeNode.getObject();
		return getValueAt(baseObject, col);
	}

	public Object getValueAt(BaseObject baseObject, int col)
	{
		try
		{	
			if(baseObject == null)
				return null;

			return baseObject.toString();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return "[Error]";
		}
	}

	public CodeList getColumnTags()
	{
		return columnsToShow;	
	}
	
	protected Project getProject()
	{
		return project;
	}
	
	private Project project;
	private CodeList columnsToShow;
}
