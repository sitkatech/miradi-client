/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		cmd.writeTo(dest);
		byte[] result = dest.toByteArray();
		ByteArrayInputStream source = new ByteArrayInputStream(result);
		CommandDiagramMove loaded = (CommandDiagramMove)Command.readFrom(source);
		assertEquals("didn't restore deltaX?", cmd.getDeltaX(), loaded.getDeltaX());
		assertEquals("didn't restore deltaY?", cmd.getDeltaY(), loaded.getDeltaY());
		assertTrue("didn't restore ids?", Arrays.equals(ids, loaded.getIds()));
	}
	
	public void testCommandSetNodeText() throws Exception
	{
		int id = 98;
		String text = "peace";
		CommandSetNodeText cmd = new CommandSetNodeText(id, text);
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		cmd.writeTo(dest);
		byte[] result = dest.toByteArray();
		ByteArrayInputStream source = new ByteArrayInputStream(result);
		CommandSetNodeText loaded = (CommandSetNodeText)Command.readFrom(source);
		assertEquals("didn't restore id?", id, loaded.getId());
		assertEquals("didn't restore text?", text, loaded.getText());
	}
	
	public void testCommandInsertGoal() throws Exception
	{
		Command cmd = new CommandInsertGoal();
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		cmd.writeTo(dest);
		byte[] result = dest.toByteArray();
		ByteArrayInputStream source = new ByteArrayInputStream(result);
		CommandInsertGoal loaded = (CommandInsertGoal)Command.readFrom(source);
		assertNotNull(loaded);
	}

	public void testCommandInsertThreat() throws Exception
	{
		CommandInsertThreat cmd = new CommandInsertThreat();
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		cmd.writeTo(dest);
		byte[] result = dest.toByteArray();
		ByteArrayInputStream source = new ByteArrayInputStream(result);
		CommandInsertThreat loaded = (CommandInsertThreat)Command.readFrom(source);
		assertNotNull(loaded);
	}

	public void testCommandInsertIntervention() throws Exception
	{
		CommandInsertIntervention cmd = new CommandInsertIntervention();
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		cmd.writeTo(dest);
		byte[] result = dest.toByteArray();
		ByteArrayInputStream source = new ByteArrayInputStream(result);
		CommandInsertIntervention loaded = (CommandInsertIntervention)Command.readFrom(source);
		assertNotNull(loaded);
	}

	public void testCommandInsertConnection() throws Exception
	{
		int from = 882;
		int to = 212;
		CommandLinkNodes cmd = new CommandLinkNodes(from, to);
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		cmd.writeTo(dest);
		byte[] result = dest.toByteArray();
		ByteArrayInputStream source = new ByteArrayInputStream(result);
		CommandLinkNodes loaded = (CommandLinkNodes)Command.readFrom(source);
		assertEquals("didn't restore from?", from, loaded.getFromId());
		assertEquals("didn't restore to?", to, loaded.getToId());
	}
	
	public void testDeleteLinkage() throws Exception
	{
		int id = 212;
		CommandDeleteLinkage cmd = new CommandDeleteLinkage(id);
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		cmd.writeTo(dest);
		byte[] result = dest.toByteArray();
		ByteArrayInputStream source = new ByteArrayInputStream(result);
		CommandDeleteLinkage loaded = (CommandDeleteLinkage)Command.readFrom(source);
		assertEquals("didn't restore from?", id, loaded.getId());
	}
}
