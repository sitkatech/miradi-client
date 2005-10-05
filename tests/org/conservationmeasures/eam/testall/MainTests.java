/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.testall;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.conservationmeasures.eam.commands.TestCommands;
import org.conservationmeasures.eam.database.TestDatabase;
import org.conservationmeasures.eam.database.TestEAMDatabase;
import org.conservationmeasures.eam.diagram.TestDiagramModel;
import org.conservationmeasures.eam.diagram.TestDiagramView;
import org.conservationmeasures.eam.diagram.nodes.TestCommandLinkNodes;
import org.conservationmeasures.eam.diagram.nodes.TestDelete;
import org.conservationmeasures.eam.diagram.nodes.TestEamGraphicCell;
import org.conservationmeasures.eam.diagram.nodes.TestInsertNode;
import org.conservationmeasures.eam.diagram.nodes.TestLinkage;
import org.conservationmeasures.eam.diagram.nodes.TestLinkageData;
import org.conservationmeasures.eam.diagram.nodes.TestNode;
import org.conservationmeasures.eam.diagram.nodes.TestNodeData;
import org.conservationmeasures.eam.diagram.nodes.TestSetNodeText;
import org.conservationmeasures.eam.diagram.nodes.TestUndoAndRedo;
import org.conservationmeasures.eam.main.TestCommandExecutedEvents;
import org.conservationmeasures.eam.main.TestMainMenu;
import org.conservationmeasures.eam.main.TestProject;
import org.conservationmeasures.eam.main.TestRealProject;
import org.conservationmeasures.eam.main.TestTransferableEamList;
import org.conservationmeasures.eam.utils.TestLogging;
import org.conservationmeasures.eam.utils.TestTranslations;
import org.conservationmeasures.eam.vies.umbrella.TestUndoRedo;
import org.conservationmeasures.eam.views.diagram.TestInsertConnection;
import org.conservationmeasures.eam.views.interview.TestInterviewModel;
import org.conservationmeasures.eam.views.interview.TestInterviewStepModel;
import org.conservationmeasures.eam.views.interview.TestWizardStepLoader;
import org.martus.util.xml.TestSimpleXmlParser;

public class MainTests extends TestCase
{
	public static void main(String[] args)
	{
		runTests();
	}

	public static void runTests ()
	{
		junit.textui.TestRunner.run (suite());
	}

	public static Test suite ( )
	{
		TestSuite suite= new TestSuite("All eAM Tests");

		// database package
		suite.addTest(new TestSuite(TestDatabase.class));
		suite.addTest(new TestSuite(TestEAMDatabase.class));
		
		// main package
		suite.addTest(new TestSuite(TestCommandExecutedEvents.class));
		suite.addTest(new TestSuite(TestMainMenu.class));
		suite.addTest(new TestSuite(TestProject.class));
		suite.addTest(new TestSuite(TestRealProject.class));
		suite.addTest(new TestSuite(TestTransferableEamList.class));
		
		// utils package
		suite.addTest(new TestSuite(TestLogging.class));
		suite.addTest(new TestSuite(TestTranslations.class));
		
		// diagram package
		suite.addTest(new TestSuite(TestDiagramModel.class));
		suite.addTest(new TestSuite(TestDiagramView.class));

		// nodes package
		suite.addTest(new TestSuite(TestCommandLinkNodes.class));
		suite.addTest(new TestSuite(TestDelete.class));
		suite.addTest(new TestSuite(TestEamGraphicCell.class));
		suite.addTest(new TestSuite(TestInsertNode.class));
		suite.addTest(new TestSuite(TestLinkage.class));
		suite.addTest(new TestSuite(TestLinkageData.class));
		suite.addTest(new TestSuite(TestNode.class));
		suite.addTest(new TestSuite(TestNodeData.class));
		suite.addTest(new TestSuite(TestSetNodeText.class));
		suite.addTest(new TestSuite(TestUndoAndRedo.class));
		
		// commands package
		suite.addTest(new TestSuite(TestCommands.class));
		
		// view.diagram package
		suite.addTest(new TestSuite(TestUndoRedo.class));
		suite.addTest(new TestSuite(TestInsertConnection.class));
		
		// view.interview package
		suite.addTest(new TestSuite(TestInterviewModel.class));
		suite.addTest(new TestSuite(TestWizardStepLoader.class));
		suite.addTest(new TestSuite(TestInterviewStepModel.class));
		
		// martus-utils
		suite.addTest(new TestSuite(TestSimpleXmlParser.class));

	    return suite;
	}
}
