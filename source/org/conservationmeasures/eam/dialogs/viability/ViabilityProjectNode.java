/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import java.util.Vector;

import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.StatusQuestion;

public class ViabilityProjectNode extends TreeTableNode
{
	public ViabilityProjectNode(Project projectToUse) throws Exception
	{
		project = projectToUse;
		statusQuestion = new StatusQuestion(Target.TAG_TARGET_STATUS);
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
