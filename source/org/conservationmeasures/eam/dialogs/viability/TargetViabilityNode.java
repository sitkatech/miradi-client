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
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.KeyEcologicalAttributeTypeQuestion;
import org.conservationmeasures.eam.questions.StatusQuestion;
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
			if (!code.equals(""))
				return statusQuestion.findChoiceByCode(code);
		}
		return "";
	}

	public String toString()
	{
		return "Target";
	}
	
	public BaseId getId()
	{
		return target.getId();
	}
	
	public void rebuild()
	{
		Vector vector = new Vector();
		ChoiceItem[] items = question.getChoices();
		for (int i=0; i< items.length; ++i)
		{
			vector.add(new KeyEcologicalAttributeTypeNode(project, items[i].getCode(), target));
		}
		children = vector;
	}
	
	private Project project;
	private Target target;
	private Vector children;
	private StatusQuestion statusQuestion;
	private KeyEcologicalAttributeTypeQuestion question = new KeyEcologicalAttributeTypeQuestion(KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE);
}
