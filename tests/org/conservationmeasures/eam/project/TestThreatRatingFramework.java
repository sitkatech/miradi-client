/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Color;
import java.io.File;

import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeFactor;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objects.ConceptualModelFactor;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.objects.ThreatRatingBundle;
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;
import org.conservationmeasures.eam.objects.ThreatRatingValueOption;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.martus.util.DirectoryUtils;

public class TestThreatRatingFramework extends EAMTestCase
{
	public TestThreatRatingFramework(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
		framework = project.getThreatRatingFramework();
	}
	
	public void tearDown() throws Exception
	{
		project.close();
		super.tearDown();
	}
	
	public void testJson()
	{
		JSONObject json = framework.toJson();
		IdList criterionIds = new IdList(json.getJSONObject(ThreatRatingFramework.TAG_CRITERION_IDS));
		assertEquals("didn't jsonize criteria?", framework.getCriteria().length, criterionIds.size());
		IdList valueOptionIds = new IdList(json.getJSONObject(ThreatRatingFramework.TAG_VALUE_OPTION_IDS));
		assertEquals("didn't jsonize value options?", framework.getValueOptions().length, valueOptionIds.size());
		JSONArray bundleKeys = json.getJSONArray(ThreatRatingFramework.TAG_BUNDLE_KEYS);
		assertEquals("didn't jsonize bundle keys?", framework.getBundleCount(), bundleKeys.length());
	}
	
	public void testWriteAndRead() throws Exception
	{
		File tempDir = createTempDirectory();
		try
		{
			Project realProject = new Project();
			realProject.createOrOpen(tempDir);
			BaseId createdId = realProject.createObject(ObjectType.THREAT_RATING_CRITERION);
			
			ModelNodeId threatId = new ModelNodeId(283);
			ModelNodeId targetId = new ModelNodeId(983);
			ThreatRatingBundle bundle = realProject.getThreatRatingFramework().getBundle(threatId, targetId);
			bundle.setValueId(createdId, new BaseId(838));
			realProject.getThreatRatingFramework().saveBundle(bundle);
			realProject.getDatabase().writeThreatRatingFramework(realProject.getThreatRatingFramework());
			realProject.close();

			Project loadedProject = new Project();
			loadedProject.createOrOpen(tempDir);
			ThreatRatingFramework loadedFramework = loadedProject.getThreatRatingFramework();
			assertEquals("didn't reload framework?", createdId, loadedFramework.getCriterion(createdId).getId());
			ThreatRatingBundle gotBundle = loadedProject.getThreatRatingFramework().getBundle(threatId, targetId);
			assertEquals("didn't load bundles?", bundle.getValueId(createdId), gotBundle.getValueId(createdId));
			loadedProject.close();
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(tempDir);
		}
	}
	
	public void testGetBundleValue()
	{
		BaseId noneId = framework.findValueOptionByNumericValue(0).getId();
		ThreatRatingBundle bundle = new ThreatRatingBundle(new ModelNodeId(1), new ModelNodeId(2), noneId);
		ThreatRatingValueOption result = framework.getBundleValue(bundle);
		assertEquals("didn't default correctly? ", 0, result.getNumericValue());
	}

	public void testRatingValueOptions() throws Exception
	{
		ThreatRatingValueOption[] options = framework.getValueOptions();
		assertEquals("wrong number of default options?", 5, options.length);
		assertEquals("wrong order or label?", "Very High", options[1].getLabel());
		assertEquals("wrong numeric value? ", 3, options[2].getNumericValue());
		assertEquals("bad color?", Color.YELLOW, options[3].getColor());
	}
	
	public void testFindValueOptionByNumericValue()
	{
		ThreatRatingValueOption optionNone = framework.findValueOptionByNumericValue(0);
		assertEquals(Color.WHITE, optionNone.getColor());
	}
	
	public void testThreatRatingCriteria() throws Exception
	{
		ThreatRatingCriterion[] criteria = framework.getCriteria();
		assertEquals("wrong number of default criteria?", 3, criteria.length);
		assertEquals("Scope", criteria[0].getLabel());
		assertEquals("Severity", criteria[1].getLabel());
		assertEquals("Irreversibility", criteria[2].getLabel());
	}
	
	public void testIdAssignment() throws Exception
	{
		assertNotEquals("reused ids?", framework.getCriteria()[0].getId(), framework.getValueOptions()[0].getId());
	}
	
	public void testBundlesForDeletedNodes() throws Exception
	{
		ThreatRatingBundle bundle1 = createThreatTargetAndBundle();
		project.deleteNode(bundle1.getThreatId());
		assertFalse("deleted threatId case failed?", framework.isBundleForLinkedThreatAndTarget(bundle1));
		
		ThreatRatingBundle bundle2 = createThreatTargetAndBundle();
		project.deleteNode(bundle2.getTargetId());
		assertFalse("deleted targetId case failed?", framework.isBundleForLinkedThreatAndTarget(bundle2));
		
	}

	private ThreatRatingBundle createThreatTargetAndBundle() throws Exception
	{
		ModelNodeId threatId = createThreat();
		ModelNodeId targetId = createTarget();
		populateBundle(threatId, targetId, framework.getValueOptions()[0]);
		ThreatRatingBundle bundle = framework.getBundle(threatId, targetId);
		assertFalse("normal case failed?", framework.isBundleForLinkedThreatAndTarget(bundle));
		return bundle;
	}
	
	public void testBundles() throws Exception
	{
		ModelNodeId threatId = new ModelNodeId(77);
		ModelNodeId targetId = new ModelNodeId(292);
		BaseId criterionId = new BaseId(22);
		BaseId valueId = new BaseId(639);
		
		ThreatRatingBundle bundle = framework.getBundle(threatId, targetId);
		assertNotNull("Didn't write bundle?", project.getDatabase().readThreatRatingBundle(threatId, targetId));
		
		
		bundle.setValueId(criterionId, valueId);
		ThreatRatingBundle reGot = framework.getBundle(threatId, targetId);
		assertEquals("did't get same bundle?", bundle.getValueId(criterionId), reGot.getValueId(criterionId));
		
	}
	
	public void testGetThreatRatingSummary() throws Exception
	{
		framework.createDefaultObjects();
		
		ModelNodeId threatId1 = createThreat();
		ModelNodeId threatId2 = createThreat();
		ModelNodeId targetId1 = createTarget();
		ModelNodeId targetId2 = createTarget();

		ThreatRatingValueOption none = framework.findValueOptionByNumericValue(0);
		ThreatRatingValueOption high = framework.findValueOptionByNumericValue(3);
		ThreatRatingValueOption veryHigh = framework.findValueOptionByNumericValue(4);
		
		assertEquals("threat1 not none?", none, framework.getThreatThreatRatingValue(threatId1));
		assertEquals("threat2 not none?", none, framework.getThreatThreatRatingValue(threatId2));
		assertEquals("target1 not none?", none, framework.getTargetThreatRatingValue(targetId1));
		assertEquals("target2 not none?", none, framework.getTargetThreatRatingValue(targetId2));

		createLinkageAndBundle(threatId1, targetId1, veryHigh);
		createLinkageAndBundle(threatId1, targetId2, veryHigh);
		assertEquals("target1 not high?", high, framework.getTargetThreatRatingValue(targetId1));
		createLinkageAndBundle(threatId2, targetId1, veryHigh);
		assertEquals("threat2 not high?", high, framework.getThreatThreatRatingValue(threatId2));
		createLinkageAndBundle(threatId2, targetId2, veryHigh);
		
		assertEquals("threat1 not very high?", veryHigh, framework.getThreatThreatRatingValue(threatId1));
		assertEquals("threat2 not very high?", veryHigh, framework.getThreatThreatRatingValue(threatId2));
		assertEquals("target1 not very high?", veryHigh, framework.getTargetThreatRatingValue(targetId1));
		assertEquals("target2 not very high?", veryHigh, framework.getTargetThreatRatingValue(targetId2));
	}
	
	void createLinkageAndBundle(ModelNodeId threatId, ModelNodeId targetId, ThreatRatingValueOption value) throws Exception
	{
		project.insertLinkageAtId(BaseId.INVALID, threatId, targetId);
		populateBundle(threatId, targetId, value);
	}
	
	public void testGetThreatRatingSummaryUnlinked() throws Exception
	{
		ModelNodeId threatId = project.insertNodeAtId(new NodeTypeFactor(), BaseId.INVALID);
		ModelNodeId targetId = createTarget();

		ThreatRatingValueOption none = framework.findValueOptionByNumericValue(0);
		ThreatRatingValueOption high = framework.findValueOptionByNumericValue(3);
		ThreatRatingValueOption veryHigh = framework.findValueOptionByNumericValue(4);

		populateBundle(threatId, targetId, veryHigh);
		assertEquals("included unlinked bundle in threat value?", none, framework.getThreatThreatRatingValue(threatId));
		assertEquals("included unlinked bundle in target value?", none, framework.getTargetThreatRatingValue(targetId));
		
		project.insertLinkageAtId(BaseId.INVALID, threatId, targetId);
		assertEquals("linking didn't include value for threat?", high, framework.getThreatThreatRatingValue(threatId));
		assertEquals("linking didn't include value for target?", high, framework.getTargetThreatRatingValue(targetId));

		((ConceptualModelFactor)project.findNode(threatId)).decreaseTargetCount();
		assertEquals("threat value included indirect factor?", none, framework.getThreatThreatRatingValue(threatId));
		assertEquals("target value included indirect factor?", none, framework.getTargetThreatRatingValue(targetId));
	}

	private ModelNodeId createTarget() throws Exception
	{
		ModelNodeId targetId = project.insertNodeAtId(new NodeTypeTarget(), BaseId.INVALID);
		return targetId;
	}

	private ModelNodeId createThreat() throws Exception
	{
		ModelNodeId threatId = project.insertNodeAtId(new NodeTypeFactor(), BaseId.INVALID);
		((ConceptualModelFactor)project.findNode(threatId)).increaseTargetCount();
		return threatId;
	}
	
	void populateBundle(ModelNodeId threatId, ModelNodeId targetId, ThreatRatingValueOption value) throws Exception
	{
		ThreatRatingBundle bundle = framework.getBundle(threatId, targetId);
		ThreatRatingCriterion criteria[] = framework.getCriteria();
		for(int i = 0; i < criteria.length; ++i)
			bundle.setValueId(criteria[i].getId(), value.getId());
	}
	
	ThreatRatingFramework framework;
	private ProjectForTesting project;
}
