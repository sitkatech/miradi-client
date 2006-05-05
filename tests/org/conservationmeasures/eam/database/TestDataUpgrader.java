/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.database;

import java.io.File;
import java.io.IOException;

import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeDirectThreat;
import org.conservationmeasures.eam.objects.ConceptualModelFactor;
import org.conservationmeasures.eam.objects.ConceptualModelIntervention;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ConceptualModelTarget;
import org.conservationmeasures.eam.project.IdAssigner;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.json.JSONObject;
import org.martus.util.DirectoryUtils;

public class TestDataUpgrader extends EAMTestCase
{
	public TestDataUpgrader(String name)
	{
		super(name);
	}

	public void testRenameNodeTagFromNameToLabel() throws Exception
	{
		File tempDirectory = createTempDirectory();
		try
		{
			ProjectServer.getNodesDirectory(tempDirectory).mkdirs();
			NodeManifest manifest = new NodeManifest();

			Version2ConceptualModelTarget target = new Version2ConceptualModelTarget("Target");
			File targetFile = writeNode(tempDirectory, manifest, target);
			
			Version2ConceptualModelThreat threat = new Version2ConceptualModelThreat("Threat");
			File threatFile = writeNode(tempDirectory, manifest, threat);

			Version2ConceptualModelIntervention intervention = new Version2ConceptualModelIntervention("Intervention");
			File interventionFile = writeNode(tempDirectory, manifest, intervention);

			manifest.write(ProjectServer.getManifestFile(ProjectServer.getNodesDirectory(tempDirectory)));

			DataUpgrader upgrader = new DataUpgrader(tempDirectory);
			upgrader.renameNodeTagFromNameToLabel();
			
			ConceptualModelTarget gotTarget = new ConceptualModelTarget(JSONFile.read(targetFile));
			assertEquals(target.getLabel(), gotTarget.getLabel());
			ConceptualModelTarget gotThreat = new ConceptualModelTarget(JSONFile.read(threatFile));
			assertEquals(threat.getLabel(), gotThreat.getLabel());
			ConceptualModelTarget gotIntervention = new ConceptualModelTarget(JSONFile.read(interventionFile));
			assertEquals(intervention.getLabel(), gotIntervention.getLabel());
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(tempDirectory);
		}
	}

	private File writeNode(File tempDirectory, NodeManifest manifest, ConceptualModelNode node) throws IOException
	{
		manifest.put(node.getId());
		File targetFile = ProjectServer.getNodeFile(tempDirectory, node.getId());
		JSONFile.write(targetFile, node.toJson());
		return targetFile;
	}
	
	static class Version2ConceptualModelTarget extends ConceptualModelTarget
	{
		public Version2ConceptualModelTarget(String label)
		{
			setId(idAssigner.takeNextId());
			setLabel(label);
		}
		
		public JSONObject toJson()
		{
			return TestDataUpgrader.makeOld(super.toJson());
		}
		
	}
	
	static class Version2ConceptualModelThreat extends ConceptualModelFactor
	{
		public Version2ConceptualModelThreat(String label)
		{
			super(new NodeTypeDirectThreat());
			setId(idAssigner.takeNextId());
			setLabel(label);
		}
		
		public JSONObject toJson()
		{
			return TestDataUpgrader.makeOld(super.toJson());
		}
	}
	
	static class Version2ConceptualModelIntervention extends ConceptualModelIntervention
	{
		public Version2ConceptualModelIntervention(String label)
		{
			setId(idAssigner.takeNextId());
			setLabel(label);
		}
		
		public JSONObject toJson()
		{
			return TestDataUpgrader.makeOld(super.toJson());
		}
	}
	
	public static JSONObject makeOld(JSONObject json)
	{
		final String TAG_NAME = "Name";

		json.put(TAG_NAME, json.get(ConceptualModelNode.TAG_LABEL));
		json.put(ConceptualModelNode.TAG_LABEL, null);
		return json;

	}
	
	public static IdAssigner idAssigner = new IdAssigner();
}
