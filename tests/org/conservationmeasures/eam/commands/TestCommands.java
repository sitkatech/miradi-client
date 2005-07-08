/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestCommands extends EAMTestCase
{
	public TestCommands(String name)
	{
		super(name);
	}

	public void testCommandDiagramMove() throws Exception
	{
		CommandDiagramMove cmd = new CommandDiagramMove(25, -68);
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		cmd.writeTo(dest);
		byte[] result = dest.toByteArray();
		ByteArrayInputStream source = new ByteArrayInputStream(result);
		CommandDiagramMove loaded = (CommandDiagramMove)Command.readFrom(source);
		assertEquals("didn't restore deltaX?", cmd.getDeltaX(), loaded.getDeltaX());
		assertEquals("didn't restore deltaY?", cmd.getDeltaY(), loaded.getDeltaY());
	}
	
	public void testCommandDiagramSelectCells() throws Exception
	{
		int[] sampleIds = {1, 9, 55};
		CommandDiagramSelectCells cmd = new CommandDiagramSelectCells(sampleIds);
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		cmd.writeTo(dest);
		byte[] result = dest.toByteArray();
		ByteArrayInputStream source = new ByteArrayInputStream(result);
		CommandDiagramSelectCells loaded = (CommandDiagramSelectCells)Command.readFrom(source);
		int[] loadedIds = loaded.getIds();
		assertEquals(sampleIds.length, loadedIds.length);
		for(int i=0; i < sampleIds.length; ++i)
			assertEquals("id " + i + " not the same?", sampleIds[i], loadedIds[i]);
	}
	
	public void testCommandInsertThreat() throws Exception
	{
		Point at = new Point(1234, 5678);
		String text = "Pretend threat";
		CommandInsertThreat cmd = new CommandInsertThreat(at, text);
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		cmd.writeTo(dest);
		byte[] result = dest.toByteArray();
		ByteArrayInputStream source = new ByteArrayInputStream(result);
		CommandInsertThreat loaded = (CommandInsertThreat)Command.readFrom(source);
		assertEquals("wrong location?", at, loaded.getLocation());
		assertEquals("wrong text?", text, loaded.getText());
	}
}
