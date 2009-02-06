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
package org.miradi.dialogs.viability.nodes;

import java.util.Vector;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.dialogs.viability.ViabilityTreeModel;
import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.StatusQuestion;

public class ViabilityProjectNode extends TreeTableNode
{
	public ViabilityProjectNode(Project projectToUse) throws Exception
	{
		project = projectToUse;
		statusQuestion = new StatusQuestion();
		rebuild();
	}
	
	public BaseObject getObject()
	{
		return getProject().getMetadata();
	}

	public TreeTableNode getChild(int index)
	{
		return (TreeTableNode)children.get(index);
	}

	public int getChildCount()
	{
		return children.size();
	}

	public ORef getObjectReference()
	{
		return getProject().getMetadata().getRef();
	}
	
	public int getType()
	{
		return ProjectMetadata.getObjectType();
	}

	public Object getValueAt(int column)
	{
		if (ViabilityTreeModel.columnTags[column].equals(Indicator.TAG_STATUS))
		{
			String code = Target.computeTNCViability(project);
			return statusQuestion.findChoiceByCode(code);
		}
		
		return new EmptyChoiceItem();
	}

	@Override
	public String toRawString()
	{
		return project.getFilename();	
	}
	
	public BaseId getId()
	{
		return getObjectReference().getObjectId();
	}
	
	public void rebuild() throws Exception
	{
		Vector vector = new Vector();
		Target[] factors  = project.getTargetPool().getTargets();
		for (int i=0; i< factors.length; ++i)
		{
			vector.add(new TargetViabilityNode(project, factors[i].getRef()));
		}
		children = vector;
	}
	
	private Project getProject()
	{
		return project;		
	}
	
	private StatusQuestion statusQuestion;
	private Project project;
	private Vector children;
}
