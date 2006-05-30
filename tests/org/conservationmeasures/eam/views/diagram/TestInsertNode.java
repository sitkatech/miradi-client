/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;
import java.io.IOException;
import java.text.ParseException;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.martus.util.TestCaseEnhanced;

public class TestInsertNode extends TestCaseEnhanced
{
	public TestInsertNode(String name)
	{
		super(name);
	}

	public void testDoIt() throws Exception
	{
		OurProject project = new OurProject(getName());
		OurMainWindow mainWindow = new OurMainWindow(project);
		try
		{
			assertEquals("already written?", null, project.lastWroteName);
			Point at = project.getSnapped(new Point(25,167));
			InsertInterventionWithFakePropertiesEditing inserter = new InsertInterventionWithFakePropertiesEditing();
			inserter.setMainWindow(mainWindow);
			inserter.setView(new DiagramView(mainWindow));
			inserter.setLocation(at);
			inserter.doIt();
			
			int id = project.getNodePool().getIds()[0];
			DiagramNode node = project.getDiagramModel().getNodeById(id);
			assertEquals("didn't set location?", inserter.getLocation(), node.getLocation());
			assertEquals("didn't set name?", inserter.getInitialText(), node.getLabel());
			
			assertEquals("didn't write after set name?", node.getLabel(), project.lastWroteName);
			
			assertTrue("didn't invoke editor?", inserter.wasPropertiesEditorLaunched);
		}
		finally
		{
			project.close();
		}
		
	}
	
	static class InsertInterventionWithFakePropertiesEditing extends InsertIntervention
	{
		void launchPropertiesEditor(int id) throws Exception, CommandFailedException
		{
			wasPropertiesEditorLaunched = true;
		}

		public boolean wasPropertiesEditorLaunched; 
	}
	
	static class OurMainWindow extends MainWindow
	{
		public OurMainWindow(Project projectToUse) throws IOException
		{
			super(projectToUse);
			actions = new Actions(this);
		}
		
	}


	static class OurProject extends ProjectForTesting
	{
		public OurProject(String testName) throws Exception
		{
			super(testName);
		}

		protected void writeNode(int nodeId) throws IOException, ParseException
		{
			super.writeNode(nodeId);
			lastWroteName = getNodePool().find(nodeId).getLabel();
		}
		
		public String lastWroteName;
	}
}
