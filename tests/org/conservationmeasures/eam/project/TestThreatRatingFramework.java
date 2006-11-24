/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeCause;
import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.CreateModelLinkageParameter;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ConceptualModelCause;
import org.conservationmeasures.eam.objects.RatingCriterion;
import org.conservationmeasures.eam.objects.ValueOption;
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
			BaseId createdId = realProject.createObject(ObjectType.RATING_CRITERION);
			
			ModelNodeId threatId = new ModelNodeId(283);
			ModelNodeId targetId = new ModelNodeId(983);
			ThreatRatingBundle bundle = realProject.getThreatRatingFramework().getBundle(threatId, targetId);
			bundle.setValueId(createdId, new BaseId(838));
			IdList realOptionIds = realProject.getThreatRatingFramework().getValueOptionIds();
			realProject.getThreatRatingFramework().saveBundle(bundle);
			realProject.getDatabase().writeThreatRatingFramework(realProject.getThreatRatingFramework());
			realProject.close();

			Project loadedProject = new Project();
			loadedProject.createOrOpen(tempDir);
			IdList loadedOptionIds = loadedProject.getThreatRatingFramework().getValueOptionIds();
			ThreatRatingFramework loadedFramework = loadedProject.getThreatRatingFramework();
			assertEquals("didn't reload framework?", createdId, loadedFramework.getCriterion(createdId).getId());
			ThreatRatingBundle gotBundle = loadedProject.getThreatRatingFramework().getBundle(threatId, targetId);
			assertEquals("didn't load bundles?", bundle.getValueId(createdId), gotBundle.getValueId(createdId));
			assertEquals("didn't load options?", realOptionIds, loadedOptionIds);
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
		ValueOption result = framework.getBundleValue(bundle);
		assertEquals("didn't default correctly? ", 0, result.getNumericValue());
	}

	public void testRatingValueOptions() throws Exception
	{
		ValueOption[] options = framework.getValueOptions();
		assertEquals("wrong number of default options?", 5, options.length);
		assertEquals("wrong order or label?", "Very High", options[0].getLabel());
		assertEquals("wrong numeric value? ", 3, options[1].getNumericValue());
		assertEquals("bad color?", Color.YELLOW, options[2].getColor());
	}
	
	public void testFindValueOptionByNumericValue()
	{
		ValueOption optionNone = framework.findValueOptionByNumericValue(0);
		assertEquals(Color.WHITE, optionNone.getColor());
	}

	public void testDefaultValue() throws Exception
	{
		BaseId id = framework.getDefaultValueId();
		ValueOption option = framework.getValueOption(id);
		assertEquals("Didn't default to zero?", 0, option.getNumericValue());
	}
	
	public void testThreatRatingCriteria() throws Exception
	{
		RatingCriterion[] criteria = framework.getCriteria();
		assertEquals("wrong number of default criteria?", 3, criteria.length);
		List expectedLabels = Arrays.asList(new String[] {"Scope", "Severity", "Irreversibility", });
		for(int i = 0; i < criteria.length; ++i)
		{
			String actual = criteria[i].getLabel();
			assertContains("Missing a criterion?", actual, expectedLabels);
		}
	}
	
	public void testIdAssignment() throws Exception
	{
		assertNotEquals("reused ids?", framework.getCriteria()[0].getId(), framework.getValueOptions()[0].getId());
	}
	
	public void testBundlesForDeletedNodes() throws Exception
	{
		ThreatRatingBundle bundle1 = createThreatTargetAndBundle();
		project.deleteObject(ObjectType.MODEL_NODE, bundle1.getThreatId());
		assertFalse("deleted threatId case failed?", framework.isBundleForLinkedThreatAndTarget(bundle1));
		
		ThreatRatingBundle bundle2 = createThreatTargetAndBundle();
		project.deleteObject(ObjectType.MODEL_NODE, bundle2.getTargetId());
		assertFalse("deleted targetId case failed?", framework.isBundleForLinkedThreatAndTarget(bundle2));
		
	}

	private ThreatRatingBundle createThreatTargetAndBundle() throws Exception
	{
		ModelNodeId threatId = createThreat(project);
		ModelNodeId targetId = createTarget(project);
		populateBundle(framework, threatId, targetId, framework.getValueOptions()[0]);
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
		framework.createDefaultObjectsIfNeeded();
		
		ModelNodeId threatId1 = createThreat(project);
		ModelNodeId threatId2 = createThreat(project);
		ModelNodeId targetId1 = createTarget(project);
		ModelNodeId targetId2 = createTarget(project);

		ValueOption none = framework.findValueOptionByNumericValue(0);
		ValueOption high = framework.findValueOptionByNumericValue(3);
		ValueOption veryHigh = framework.findValueOptionByNumericValue(4);
		
		assertEquals("threat1 not none?", none, framework.getThreatThreatRatingValue(threatId1));
		assertEquals("threat2 not none?", none, framework.getThreatThreatRatingValue(threatId2));
		assertEquals("target1 not none?", none, framework.getTargetThreatRatingValue(targetId1));
		assertEquals("target2 not none?", none, framework.getTargetThreatRatingValue(targetId2));

		createLinkageAndBundle(project, threatId1, targetId1, veryHigh);
		createLinkageAndBundle(project, threatId1, targetId2, veryHigh);
		assertEquals("target1 not high?", high, framework.getTargetThreatRatingValue(targetId1));
		createLinkageAndBundle(project, threatId2, targetId1, veryHigh);
		assertEquals("threat2 not high?", high, framework.getThreatThreatRatingValue(threatId2));
		createLinkageAndBundle(project, threatId2, targetId2, veryHigh);
		
		assertEquals("threat1 not very high?", veryHigh, framework.getThreatThreatRatingValue(threatId1));
		assertEquals("threat2 not very high?", veryHigh, framework.getThreatThreatRatingValue(threatId2));
		assertEquals("target1 not very high?", veryHigh, framework.getTargetThreatRatingValue(targetId1));
		assertEquals("target2 not very high?", veryHigh, framework.getTargetThreatRatingValue(targetId2));
	}
	
	void createLinkageAndBundle(Project projectToUse, ModelNodeId threatId, ModelNodeId targetId, ValueOption value) throws Exception
	{
		CreateModelLinkageParameter parameter = new CreateModelLinkageParameter(threatId, targetId);
		projectToUse.createObject(ObjectType.MODEL_LINKAGE, BaseId.INVALID, parameter);
		populateBundle(projectToUse.getThreatRatingFramework(), threatId, targetId, value);
	}
	
	public void testGetThreatRatingSummaryUnlinked() throws Exception
	{
		ModelNodeId threatId = (ModelNodeId)project.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, new CreateModelNodeParameter(new NodeTypeCause()));
		ModelNodeId targetId = createTarget(project);

		ValueOption none = framework.findValueOptionByNumericValue(0);
		ValueOption high = framework.findValueOptionByNumericValue(3);
		ValueOption veryHigh = framework.findValueOptionByNumericValue(4);

		populateBundle(framework, threatId, targetId, veryHigh);
		assertEquals("included unlinked bundle in threat value?", none, framework.getThreatThreatRatingValue(threatId));
		assertEquals("included unlinked bundle in target value?", none, framework.getTargetThreatRatingValue(targetId));
		CreateModelLinkageParameter parameter = new CreateModelLinkageParameter(threatId, targetId);
		project.createObject(ObjectType.MODEL_LINKAGE, BaseId.INVALID, parameter);
		
		assertEquals("linking didn't include value for threat?", high, framework.getThreatThreatRatingValue(threatId));
		assertEquals("linking didn't include value for target?", high, framework.getTargetThreatRatingValue(targetId));

		((ConceptualModelCause)project.findNode(threatId)).decreaseTargetCount();
		assertEquals("threat value included contributing factor?", none, framework.getThreatThreatRatingValue(threatId));
		assertEquals("target value included contributing factor?", none, framework.getTargetThreatRatingValue(targetId));
	}
	
	public void testGetHighestValueForThreat() throws Exception
	{
		int[][] bundleValues = { {3}, {4} };
		ThreatRatingFramework trf = createFramework(bundleValues);
		try
		{
			ModelNodeId threatId = trf.getProject().getNodePool().getDirectThreats()[0].getModelNodeId();
			assertEquals(4, trf.getHighestValueForThreat(threatId).getNumericValue());
		}
		finally
		{
			trf.getProject().close();
		}
	}
	
	public void testGetPureMajorityProjectRating() throws Exception
	{
		int[][] bundlesEmpty = { {-1} };
		verifyPureMajority("Empty", 0, bundlesEmpty);
		
		int[][] bundlesPlurality = { {2, 3, 4, 2, 3, 4, 2},	};
		verifyPureMajority("Plurality", 3, bundlesPlurality);
		
		int[][] bundlesMajority = { {2, 2, 1},	};
		verifyPureMajority("Majority", 2, bundlesMajority);

		int[][] bundlesTwoRows = {
			{1, 4, 1, },
			{4, 1, 4, },
		};
		verifyPureMajority("TwoRows", 4, bundlesTwoRows);
	}
	
	public void testGetProjectRating() throws Exception
	{
		int[][] bundlesEmpty = { {-1} };
		verifyOverallProjectRating("Empty", 0, bundlesEmpty);
		
		int[][] bundlesPlurality = { {4, 4, 1, 1},	};
		verifyOverallProjectRating("Plurality", 3, bundlesPlurality);
		
		int[][] bundlesMajority = { {3, 3, 1},	};
		verifyOverallProjectRating("Majority", 3, bundlesMajority);
		
	}
	
	private void verifyPureMajority(String message, int expected, int[][] bundleValues) throws Exception
	{
		ThreatRatingFramework trf = createFramework(bundleValues);
		try
		{
			assertEquals(message, expected, trf.getProjectMajorityRating().getNumericValue());
		}
		finally
		{
			trf.getProject().close();
		}
	}

	private void verifyOverallProjectRating(String message, int expected, int[][] bundleValues) throws Exception
	{
		ThreatRatingFramework trf = createFramework(bundleValues);
		try
		{
			assertEquals(message, expected, trf.getOverallProjectRating().getNumericValue());
		}
		finally
		{
			trf.getProject().close();
		}
	}

	private ModelNodeId createTarget(Project projectToUse) throws Exception
	{
		ModelNodeId targetId = (ModelNodeId)projectToUse.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, new CreateModelNodeParameter(new NodeTypeTarget()));
		return targetId;
	}

	private ModelNodeId createThreat(Project projectToUse) throws Exception
	{
		ModelNodeId threatId = (ModelNodeId)projectToUse.createObject(ObjectType.MODEL_NODE, BaseId.INVALID, new CreateModelNodeParameter(new NodeTypeCause()));
		((ConceptualModelCause)projectToUse.findNode(threatId)).increaseTargetCount();
		return threatId;
	}
	
	void populateBundle(ThreatRatingFramework frameworkToUse, ModelNodeId threatId, ModelNodeId targetId, ValueOption value) throws Exception
	{
		ThreatRatingBundle bundle = frameworkToUse.getBundle(threatId, targetId);
		RatingCriterion criteria[] = frameworkToUse.getCriteria();
		for(int i = 0; i < criteria.length; ++i)
			bundle.setValueId(criteria[i].getId(), value.getId());
	}
	
	private ThreatRatingFramework createFramework(int[][] bundleValues) throws Exception
	{
		Project tempProject = new ProjectForTesting(getName());
		ThreatRatingFramework trf = tempProject.getThreatRatingFramework();
		
		int targetCount = bundleValues.length;
		ModelNodeId[] targetIds = new ModelNodeId[targetCount];
		for(int i = 0; i < targetCount; ++i)
			targetIds[i] = createTarget(tempProject);

		int threatCount = bundleValues[0].length;
		ModelNodeId[] threatIds = new ModelNodeId[threatCount];
		for(int i = 0; i < threatCount; ++i)
			threatIds[i] = createThreat(tempProject);
		
		for(int targetIndex = 0; targetIndex < targetCount; ++targetIndex)
		{
			int[] valuesForTarget = bundleValues[targetIndex];
			for(int threatIndex = 0; threatIndex < threatCount; ++threatIndex)
			{
				int numericValue = valuesForTarget[threatIndex];
				if(numericValue < 0)
					continue;
				ValueOption valueOption = trf.findValueOptionByNumericValue(numericValue);
				ModelNodeId threatId = threatIds[threatIndex];
				ModelNodeId targetId = targetIds[targetIndex];
				createLinkageAndBundle(tempProject, threatId, targetId, valueOption);
				
			}
		}
		return trf;
	}
	
	ThreatRatingFramework framework;
	private ProjectForTesting project;
}
