/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeCluster;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeFactor;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeIntervention;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

abstract public class ConceptualModelNode extends EAMBaseObject
{
	protected ConceptualModelNode(BaseId idToUse, NodeType nodeType)
	{
		super(idToUse);
		type = nodeType;
	}
	
	protected ConceptualModelNode(ModelNodeId idToUse, NodeType nodeType, EnhancedJsonObject json) throws Exception
	{
		super(idToUse, json);
		type = nodeType;
	}
	
	public ModelNodeId getModelNodeId()
	{
		return new ModelNodeId(getId().asInt());
	}
	
	public int getType()
	{
		return ObjectType.MODEL_NODE;
	}
	
	public NodeType getNodeType()
	{
		if(isDirectThreat() || isIndirectFactor())
			return new NodeTypeFactor();
		return type;
	}
	
	public void setNodeType(NodeType typeToUse)
	{
		type = typeToUse;
	}
	
	public String getComment()
	{
		return comment.get();
	}
	
	public void setComment(String newComment) throws Exception
	{
		comment.set(newComment);
	}
	
	public boolean isStatusDraft()
	{
		return false;
	}

	public IdList getIndicators()
	{
		return indicators.getIdList();
	}
	
	public void setIndicators(IdList indicatorsToUse)
	{
		indicators.set(indicatorsToUse);
	}

	public IdList getObjectives()
	{
		return objectives.getIdList();
	}

	public void setObjectives(IdList objectivesToUse)
	{
		objectives.set(objectivesToUse);
	}
	
	public IdList getGoals()
	{
		return goals.getIdList();
	}

	public void setGoals(IdList goalsToUse)
	{
		goals.set(goalsToUse);
	}

	public boolean isIntervention()
	{
		return false;
	}
	
	public boolean isFactor()
	{
		return false;
	}
	
	public boolean isTarget()
	{
		return false;
	}
	
	public boolean isIndirectFactor()
	{
		return false;
	}
	
	public boolean isDirectThreat()
	{
		return false;
	}
	
	public boolean isStress()
	{
		return false;
	}
	
	public boolean isCluster()
	{
		return false;
	}
	
	public boolean canHaveObjectives()
	{
		return false;
	}

	public boolean canHaveGoal()
	{
		return false;
	}

	public CreateObjectParameter getCreationExtraInfo()
	{
		return new CreateModelNodeParameter(getNodeType());
	}

	public static ConceptualModelNode createFrom(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		String typeString = json.getString(TAG_NODE_TYPE);
		if(typeString.equals(NodeTypeIntervention.INTERVENTION_TYPE))
			return new ConceptualModelIntervention(new ModelNodeId(idAsInt), json);
		if(typeString.equals(NodeTypeFactor.FACTOR_TYPE))
			return new ConceptualModelFactor(new ModelNodeId(idAsInt), json);
		if(typeString.equals(NodeTypeTarget.TARGET_TYPE))
			return new ConceptualModelTarget(new ModelNodeId(idAsInt), json);
		if(typeString.equals(NodeTypeCluster.CLUSTER_TYPE))
			return new ConceptualModelCluster(new ModelNodeId(idAsInt), json);
		
		throw new RuntimeException("Read unknown node type: " + typeString);
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject superJson = super.toJson();
		superJson.put(TAG_NODE_TYPE , type.toString());
		
		return superJson;
	}

	public String toString()
	{
		return getLabel();
	}
	
	public static ConceptualModelNode createConceptualModelObject(ModelNodeId idToCreate, CreateModelNodeParameter parameter)
	{
		NodeType nodeType = parameter.getNodeType();
		if(nodeType.isIntervention())
			return new ConceptualModelIntervention(idToCreate);
		else if(nodeType.isFactor())
			return new ConceptualModelFactor(idToCreate);
		else if(nodeType.isTarget())
			return new ConceptualModelTarget(idToCreate);
		else if(nodeType.isCluster())
			return new ConceptualModelCluster(idToCreate);
	
		throw new RuntimeException("Tried to create unknown node type: " + nodeType);
	}

	void clear()
	{
		super.clear();
		comment = new StringData();
	    indicators = new IdListData();
		objectives = new IdListData();
		goals = new IdListData();
		
		addField(TAG_COMMENT, comment);
		addField(TAG_INDICATOR_IDS, indicators);
		addField(TAG_OBJECTIVE_IDS, objectives);
		addField(TAG_GOAL_IDS, goals);
	}

	public static final NodeType TYPE_INVALID = null;
	public static final NodeType TYPE_TARGET = new NodeTypeTarget();
	public static final NodeType TYPE_FACTOR = new NodeTypeFactor();
	public static final NodeType TYPE_INTERVENTION = new NodeTypeIntervention();
	public static final NodeType TYPE_CLUSTER = new NodeTypeCluster();
	
	public static final String TAG_NODE_TYPE = "Type";
	public static final String TAG_COMMENT = "Comment";
	public static final String TAG_INDICATOR_IDS = "IndicatorIds";
	public static final String TAG_OBJECTIVE_IDS = "ObjectiveIds";
	public static final String TAG_GOAL_IDS = "GoalIds";
	
	private NodeType type;
	private StringData comment;

	private IdListData indicators;
	private IdListData objectives;
	private IdListData goals;
}
