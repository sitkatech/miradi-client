/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.viability;

import java.util.Vector;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.Target;
import org.miradi.project.Project;
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
		return null;
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
		return new ORef(ObjectType.FAKE, new BaseId(Target.getObjectType()));
	}
	
	public int getType()
	{
		return ObjectType.FAKE;
	}

	public Object getValueAt(int column)
	{
		if (ViabilityTreeModel.columnTags[column].equals(Indicator.TAG_STATUS))
		{
			String code = Target.computeTNCViability(project);
			return statusQuestion.findChoiceByCode(code);
		}
		
		return null;
	}

	public String toString()
	{
		return project.getFilename();
	}
	
	public BaseId getId()
	{
		return null;
	}
	
	public void rebuild() throws Exception
	{
		Vector vector = new Vector();
		Target[] factors  = project.getTargetPool().getTargets();
		for (int i=0; i< factors.length; ++i)
		{
			vector.add(new TargetViabilityNode(project, (FactorId) factors[i].getId()));
		}
		children = vector;
	}
	
	
	private StatusQuestion statusQuestion;
	private Project project;
	private Vector children;
}
