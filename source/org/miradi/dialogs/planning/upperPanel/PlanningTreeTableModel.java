/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.dialogs.planning.treenodes.PlanningRootNode;
import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.PlanningTreeConfiguration;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

abstract public class PlanningTreeTableModel extends GenericTreeTableModel
{	
	public PlanningTreeTableModel(Project projectToUse, PlanningTreeConfiguration rowColumnProvider) throws Exception
	{
		this(projectToUse, createPlanningTreeRootNode(projectToUse, rowColumnProvider.getRowCodesToShow()), rowColumnProvider);
	}

	public PlanningTreeTableModel(Project projectToUse, TreeTableNode rootNode, PlanningTreeConfiguration rowColumnProviderToUse) throws Exception
	{
		super(rootNode);
		
		project = projectToUse;
		rowColumnProvider = rowColumnProviderToUse;
	}
	
	public PlanningTreeConfiguration getRowColumnProvider()
	{
		return rowColumnProvider;
	}

	private static TreeTableNode createPlanningTreeRootNode(Project projectToUse, CodeList visibleRowCodesToUse) throws Exception
	{
		return new PlanningRootNode(projectToUse, visibleRowCodesToUse);
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

	@Override
	protected void rebuildNode()
	{
		try
		{
			((AbstractPlanningTreeNode) getRootNode()).setVisibleRowCodes(getRowCodesToShow());
			super.rebuildNode();
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
	
	//NOTE: PTTM is always just one column wide
	@Override
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
		CodeList columnsToShow = new CodeList();
		columnsToShow.add(DEFAULT_COLUMN);
		return columnsToShow;
	}
	
	public CodeList getRowCodesToShow()
	{
		try
		{
			return rowColumnProvider.getRowCodesToShow();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return new CodeList();
		}
	}
	
	protected Project getProject()
	{
		return project;
	}
	
	private Project project;
	private PlanningTreeConfiguration rowColumnProvider;
}
