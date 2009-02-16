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
package org.miradi.diagram;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoableEdit;

import org.jgraph.event.GraphModelListener;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.ExecutableChange;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.ParentMap;
import org.miradi.diagram.layeredDiagramModels.TargetLayerModel;
import org.miradi.project.Project;

public class MultiDiagramModel implements GraphModel
{
	public MultiDiagramModel(Project projectToUse)
	{
		project = projectToUse;
		reloadModels();
	}
	
	private void reloadModels()
	{
		models = new Vector();
		
		models.add(new TargetLayerModel(getProject()));
	}

	public int getRootCount()
	{
		int totalCount = 0;
		for (int index = 0; index < models.size(); ++index)
		{
			DiagramModel diagramModel = models.get(index);
			totalCount += diagramModel.getRootCount();
		}
		
		return totalCount;
	}
	
	public int getIndexOfRoot(Object root)
	{
		return -1;
	}
	
	public boolean acceptsSource(Object edge, Object port)
	{
		return false;
	}

	public boolean acceptsTarget(Object edge, Object port)
	{
		return false;
	}

	public void addGraphModelListener(GraphModelListener l)
	{
		for (int index = 0; index < models.size(); ++index)
		{
			DiagramModel diagramModel = models.get(index);
			diagramModel.addGraphModelListener(l);
		}
	}

	public void addUndoableEditListener(UndoableEditListener listener)
	{
		for (int index = 0; index < models.size(); ++index)
		{
			DiagramModel diagramModel = models.get(index);
			diagramModel.addUndoableEditListener(listener);
		}
	}

	public void beginUpdate()
	{
		for (int index = 0; index < models.size(); ++index)
		{
			DiagramModel diagramModel = models.get(index);
			diagramModel.beginUpdate();
		}
	}

	public Map cloneCells(Object[] cells)
	{
		return null;
	}

	public boolean contains(Object node)
	{
		for (int index = 0; index < models.size(); ++index)
		{
			DiagramModel diagramModel = models.get(index);
			if (diagramModel.contains(node))
				return true;
		}
		
		return false;
	}

	public Iterator edges(Object port)
	{
		return null;
	}

	public void edit(Map attributes, ConnectionSet cs, ParentMap pm, UndoableEdit[] e)
	{
		for (int index = 0; index < models.size(); ++index)
		{
			DiagramModel diagramModel = models.get(index);
			diagramModel.edit(attributes, cs, pm, e);
		}
	}

	public void endUpdate()
	{
		for (int index = 0; index < models.size(); ++index)
		{
			DiagramModel diagramModel = models.get(index);
			diagramModel.endUpdate();
		}
	}

	public void execute(ExecutableChange change)
	{
		for (int index = 0; index < models.size(); ++index)
		{
			DiagramModel diagramModel = models.get(index);
			diagramModel.execute(change);
		}
	}

	public AttributeMap getAttributes(Object node)
	{
		return null;
	}

	public Object getChild(Object parent, int index)
	{
		return null;
	}

	public int getChildCount(Object parent)
	{
		return 0;
	}

	public int getIndexOfChild(Object parent, Object child)
	{
		return 0;
	}

	public Object getParent(Object child)
	{
		return null;
	}

	public Object getRootAt(int index)
	{
		return null;
	}

	public Object getSource(Object edge)
	{
		return null;
	}

	public Object getTarget(Object edge)
	{
		return null;
	}

	public Object getValue(Object node)
	{
		return null;
	}

	public void insert(Object[] roots, Map attributes, ConnectionSet cs, ParentMap pm, UndoableEdit[] e)
	{
	}

	public boolean isEdge(Object edge)
	{
		return false;
	}

	public boolean isLeaf(Object node)
	{
		return false;
	}

	public boolean isPort(Object port)
	{
		return false;
	}

	public void remove(Object[] roots)
	{
		for (int index = 0; index < models.size(); ++index)
		{
			DiagramModel diagramModel = models.get(index);
			diagramModel.remove(roots);
		}
	}

	public void removeGraphModelListener(GraphModelListener l)
	{
		for (int index = 0; index < models.size(); ++index)
		{
			DiagramModel diagramModel = models.get(index);
			diagramModel.removeGraphModelListener(l);
		}
	}

	public void removeUndoableEditListener(UndoableEditListener listener)
	{
		for (int index = 0; index < models.size(); ++index)
		{
			DiagramModel diagramModel = models.get(index);
			diagramModel.removeUndoableEditListener(listener);
		}
	}

	public void toBack(Object[] cells)
	{
	}

	public void toFront(Object[] cells)
	{
	}

	public Object valueForCellChanged(Object cell, Object newValue)
	{
		return null;
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project;
	private Vector<DiagramModel> models;
}
