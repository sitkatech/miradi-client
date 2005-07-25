/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.Project;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestCommands extends EAMTestCase
{
	public TestCommands(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		project = new Project();
		Command consumeCellIdZero = new CommandInsertGoal();
		consumeCellIdZero.execute(project);
		super.setUp();
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void testCommandDiagramMove() throws Exception
	{
		int[] ids = {1, 4, 16, 64};
		CommandDiagramMove cmd = new CommandDiagramMove(25, -68, ids);
		CommandDiagramMove loaded = (CommandDiagramMove)saveAndReload(cmd);
		assertEquals("didn't restore deltaX?", cmd.getDeltaX(), loaded.getDeltaX());
		assertEquals("didn't restore deltaY?", cmd.getDeltaY(), loaded.getDeltaY());
		assertTrue("didn't restore ids?", Arrays.equals(ids, loaded.getIds()));
	}
	
	public void testCommandSetNodeText() throws Exception
	{
		int id = insertGoal();
		
		String originalText = "original text";
		CommandSetNodeText starter = new CommandSetNodeText(id, originalText);
		starter.execute(project);
		assertEquals("wasn't blank to start?", "", starter.getPreviousText());
		
		String newText = "much better text!";
		CommandSetNodeText cmd = new CommandSetNodeText(id, newText);
		cmd.execute(project);
		assertEquals("didn't memorize old text?", originalText, cmd.getPreviousText());

		CommandSetNodeText loaded = (CommandSetNodeText)saveAndReload(cmd);
		assertEquals("didn't restore id?", id, loaded.getId());
		assertEquals("didn't restore new text?", newText, loaded.getNewText());
		assertEquals("didn't restore previous text?", originalText, loaded.getPreviousText());
	}

	private int insertGoal()
	{
		CommandInsertGoal insertGoal = new CommandInsertGoal();
		insertGoal.execute(project);
		int id = insertGoal.getId();
		return id;
	}
	
	public void testCommandInsertGoal() throws Exception
	{
		CommandInsertGoal cmd = new CommandInsertGoal();
		assertEquals("already have an id?", -1, cmd.getId());
		
		cmd.execute(project);
		int insertedId = cmd.getId();
		Node inserted = project.getDiagramModel().getNodeById(insertedId);
		assertTrue("didn't insert a goal?", inserted.isGoal());

		CommandInsertGoal loaded = (CommandInsertGoal)saveAndReload(cmd);
		assertNotNull(loaded);
		assertEquals("didn't load id?", cmd.getId(), loaded.getId());
	}

	public void testCommandInsertThreat() throws Exception
	{
		CommandInsertThreat cmd = new CommandInsertThreat();
		assertEquals("already have an id?", -1, cmd.getId());
		
		cmd.execute(project);
		int insertedId = cmd.getId();
		Node inserted = project.getDiagramModel().getNodeById(insertedId);
		assertTrue("didn't insert a threat?", inserted.isThreat());

		CommandInsertThreat loaded = (CommandInsertThreat)saveAndReload(cmd);
		assertNotNull(loaded);
		assertEquals("didn't load id?", cmd.getId(), loaded.getId());
	}

	public void testCommandInsertIntervention() throws Exception
	{
		CommandInsertIntervention cmd = new CommandInsertIntervention();
		assertEquals("already have an id?", -1, cmd.getId());
		
		cmd.execute(project);
		int insertedId = cmd.getId();
		Node inserted = project.getDiagramModel().getNodeById(insertedId);
		assertTrue("didn't insert an intervention?", inserted.isIntervention());

		CommandInsertIntervention loaded = (CommandInsertIntervention)saveAndReload(cmd);
		assertNotNull(loaded);
		assertEquals("didn't load id?", cmd.getId(), loaded.getId());
	}

	public void testCommandInsertLinkage() throws Exception
	{
		DiagramModel model = project.getDiagramModel();

		int from = insertGoal();
		int to = insertGoal();
		CommandLinkNodes cmd = new CommandLinkNodes(from, to);
		cmd.execute(project);
		int linkageId = cmd.getLinkageId();

		Linkage inserted = model.getLinkageById(linkageId);
		assertEquals("wrong source?", from, model.getNodeId(inserted.getFromNode()));
		assertEquals("wrong dest?", to, model.getNodeId(inserted.getToNode()));

		CommandLinkNodes loaded = (CommandLinkNodes)saveAndReload(cmd);
		assertEquals("didn't restore from?", from, loaded.getFromId());
		assertEquals("didn't restore to?", to, loaded.getToId());
		assertEquals("didn't restore linkage?", linkageId, loaded.getLinkageId());
	}
	
	public void testDeleteLinkage() throws Exception
	{
		int from = insertGoal();
		int to = insertGoal();
		CommandLinkNodes link = new CommandLinkNodes(from, to);
		link.execute(project);
		int linkageId = link.getLinkageId();
	
		CommandDeleteLinkage cmd = new CommandDeleteLinkage(linkageId);
		cmd.execute(project);
		assertEquals("didn't set from?", from, cmd.getWasFromId());
		assertEquals("didn't set to?", to, cmd.getWasToId());

		CommandDeleteLinkage loaded = (CommandDeleteLinkage)saveAndReload(cmd);
		assertEquals("didn't restore id?", linkageId, loaded.getId());
		assertEquals("didn't restore wasFrom?", from, loaded.getWasFromId());
		assertEquals("didn't restore wasTo?", to, loaded.getWasToId());
	}

	public void testDeleteNode() throws Exception
	{
		int id = 366;
		CommandDeleteNode cmd = new CommandDeleteNode(id);
		CommandDeleteNode loaded = (CommandDeleteNode)saveAndReload(cmd);
		assertEquals("didn't restore from?", id, loaded.getId());
	}

	private Command saveAndReload(Command cmd) throws IOException
	{
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		cmd.writeTo(new DataOutputStream(dest));
		byte[] result = dest.toByteArray();
		DataInputStream dataIn = new DataInputStream(new ByteArrayInputStream(result));
		return Command.readFrom(dataIn);
	}
	
	Project project;
}
