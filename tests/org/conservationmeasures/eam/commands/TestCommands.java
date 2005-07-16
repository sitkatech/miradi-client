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

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestCommands extends EAMTestCase
{
	public TestCommands(String name)
	{
		super(name);
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
		int id = 98;
		String text = "peace";
		CommandSetNodeText cmd = new CommandSetNodeText(id, text);
		CommandSetNodeText loaded = (CommandSetNodeText)saveAndReload(cmd);
		assertEquals("didn't restore id?", id, loaded.getId());
		assertEquals("didn't restore text?", text, loaded.getText());
	}
	
	public void testCommandInsertGoal() throws Exception
	{
		Command cmd = new CommandInsertGoal();
		CommandInsertGoal loaded = (CommandInsertGoal)saveAndReload(cmd);
		assertNotNull(loaded);
	}

	public void testCommandInsertThreat() throws Exception
	{
		CommandInsertThreat cmd = new CommandInsertThreat();
		CommandInsertThreat loaded = (CommandInsertThreat)saveAndReload(cmd);
		assertNotNull(loaded);
	}

	public void testCommandInsertIntervention() throws Exception
	{
		CommandInsertIntervention cmd = new CommandInsertIntervention();
		CommandInsertIntervention loaded = (CommandInsertIntervention)saveAndReload(cmd);
		assertNotNull(loaded);
	}

	public void testCommandInsertConnection() throws Exception
	{
		int from = 882;
		int to = 212;
		CommandLinkNodes cmd = new CommandLinkNodes(from, to);
		CommandLinkNodes loaded = (CommandLinkNodes)saveAndReload(cmd);
		assertEquals("didn't restore from?", from, loaded.getFromId());
		assertEquals("didn't restore to?", to, loaded.getToId());
	}
	
	public void testDeleteLinkage() throws Exception
	{
		int id = 212;
		CommandDeleteLinkage cmd = new CommandDeleteLinkage(id);
		CommandDeleteLinkage loaded = (CommandDeleteLinkage)saveAndReload(cmd);
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
}
