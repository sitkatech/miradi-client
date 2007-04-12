/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import java.util.Vector;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.StatusQuestion;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.TreeTableNode;

public class TargetViabilityNode extends TreeTableNode
{
	public TargetViabilityNode(Project projectToUse, FactorId targetId)
	{
		project = projectToUse;
		target = (Target)project.findNode(targetId);
		statusQuestion = new StatusQuestion(Target.TAG_TARGET_STATUS);
		rebuild();
	}
	
	public BaseObject getObject()
	{
		return target;
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
		return target.getRef();
	}
	
	public int getType()
	{
		return Target.getObjectType();
	}

	public Object getValueAt(int column)
	{
		if (ViabilityTreeModel.columnTags[column].equals("Status"))
		{
			String code = target.computeTNCViability();
			return statusQuestion.findChoiceByCode(code);
		}
		return "";
	}

	public String toString()
	{
		return target.getLabel();
	}
	
	public BaseId getId()
	{
		return target.getId();
	}
	
	public void rebuild()
	{
		Vector vector = new Vector();
		CodeList types = target.getActiveKeyEcologicalAttributeTypes();
		for (int i=0; i< types.size(); ++i)
		{
			vector.add(new KeyEcologicalAttributeTypeNode(project, target, types.get(i)));
		}
		children = vector;
	}
	
	private Project project;
	private Target target;
	private Vector children;
	private StatusQuestion statusQuestion;
}
