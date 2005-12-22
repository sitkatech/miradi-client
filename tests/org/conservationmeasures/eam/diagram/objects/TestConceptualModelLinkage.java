package org.conservationmeasures.eam.diagram.objects;

import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.conservationmeasures.eam.objects.ConceptualModelTarget;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.json.JSONObject;

public class TestConceptualModelLinkage extends EAMTestCase
{
	public TestConceptualModelLinkage(String name) 
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		int id = 1;
		ConceptualModelLinkage linkageData = createSampleLinkageData(id);
		assertEquals("Id not the same?", id, linkageData.getId());
		assertEquals("From Node Ids don't match", nodeBId, linkageData.getFromNodeId());
		assertEquals("To Node Ids don't match", nodeAId, linkageData.getToNodeId());
	}

	public void testToJson() throws Exception
	{
		int id = 1;
		ConceptualModelLinkage original = createSampleLinkageData(id);
		JSONObject json = original.toJson();
		ConceptualModelLinkage gotBack = new ConceptualModelLinkage(json);
		assertEquals("wrong id?", original.getId(), gotBack.getId());
		assertEquals("wrong from?", original.getFromNodeId(), gotBack.getFromNodeId());
		assertEquals("wrong to?", original.getToNodeId(), gotBack.getToNodeId());
	}
	
	private ConceptualModelLinkage createSampleLinkageData(int id) throws Exception
	{
		ConceptualModelIntervention cmIntervention = new ConceptualModelIntervention();
		ConceptualModelTarget cmTarget = new ConceptualModelTarget();

		nodeAId = 1;
		DiagramNode nodeA = DiagramNode.wrapConceptualModelObject(cmIntervention);
		cmIntervention.setId(nodeAId);
		
		nodeBId = 2;
		DiagramNode nodeB = DiagramNode.wrapConceptualModelObject(cmTarget);
		cmTarget.setId(nodeBId);
		
		DiagramLinkage linkage = new DiagramLinkage(nodeB, nodeA);
		linkage.setId(id);
		ConceptualModelLinkage linkageData = new ConceptualModelLinkage(linkage);
		return linkageData;
	}
	
	private int nodeAId;
	private int nodeBId;

}
