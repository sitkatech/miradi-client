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
import org.conservationmeasures.eam.diagram.TestCommandLinkNodes;
import org.conservationmeasures.eam.diagram.TestDelete;
import org.conservationmeasures.eam.diagram.TestDiagramModel;
import org.conservationmeasures.eam.diagram.TestInsertNode;
import org.conservationmeasures.eam.diagram.TestLinkage;
import org.conservationmeasures.eam.diagram.TestNode;
import org.conservationmeasures.eam.diagram.TestSetNodeText;
import org.conservationmeasures.eam.diagram.TestUndoAndRedo;
import org.conservationmeasures.eam.main.TestCommandExecutedEvents;
import org.conservationmeasures.eam.main.TestFileStorage;
import org.conservationmeasures.eam.main.TestMainMenu;
import org.conservationmeasures.eam.utils.TestLogging;
import org.conservationmeasures.eam.utils.TestTranslations;
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

//		suite.addTest(TestCommon.suite());
//		suite.addTest(TestClient.suite());
		
		// main package
		suite.addTest(new TestSuite(TestCommandExecutedEvents.class));
		suite.addTest(new TestSuite(TestFileStorage.class));
		suite.addTest(new TestSuite(TestMainMenu.class));
		
		// utils package
		suite.addTest(new TestSuite(TestLogging.class));
		suite.addTest(new TestSuite(TestTranslations.class));
		
		// diagram package
		suite.addTest(new TestSuite(TestCommandLinkNodes.class));
		suite.addTest(new TestSuite(TestDelete.class));
		suite.addTest(new TestSuite(TestDiagramModel.class));
		suite.addTest(new TestSuite(TestInsertNode.class));
		suite.addTest(new TestSuite(TestLinkage.class));
		suite.addTest(new TestSuite(TestNode.class));
		suite.addTest(new TestSuite(TestSetNodeText.class));
		suite.addTest(new TestSuite(TestUndoAndRedo.class));
		
		// commands package
		suite.addTest(new TestSuite(TestCommands.class));
		
		// martus-utils
		suite.addTest(new TestSuite(TestSimpleXmlParser.class));

	    return suite;
	}
}
