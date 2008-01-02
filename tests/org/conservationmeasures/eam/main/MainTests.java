/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.io.File;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.conservationmeasures.eam.commands.TestCommandBeginTransaction;
import org.conservationmeasures.eam.commands.TestCommandCreateObject;
import org.conservationmeasures.eam.commands.TestCommandDeleteObject;
import org.conservationmeasures.eam.commands.TestCommandEndTransaction;
import org.conservationmeasures.eam.commands.TestCommandSetFactorSize;
import org.conservationmeasures.eam.commands.TestCommandSetObjectData;
import org.conservationmeasures.eam.commands.TestCommandSetThreatRating;
import org.conservationmeasures.eam.commands.TestCommands;
import org.conservationmeasures.eam.database.TestDataUpgrader;
import org.conservationmeasures.eam.database.TestProjectServer;
import org.conservationmeasures.eam.diagram.TestBendPointSelectionHelper;
import org.conservationmeasures.eam.diagram.TestDelete;
import org.conservationmeasures.eam.diagram.TestDiagramAddFactor;
import org.conservationmeasures.eam.diagram.TestDiagramAddFactorLink;
import org.conservationmeasures.eam.diagram.TestDiagramComponent;
import org.conservationmeasures.eam.diagram.TestDiagramModel;
import org.conservationmeasures.eam.diagram.TestDiagramView;
import org.conservationmeasures.eam.diagram.TestEamGraphCell;
import org.conservationmeasures.eam.diagram.TestLinkCreator;
import org.conservationmeasures.eam.diagram.TestProjectScopeBox;
import org.conservationmeasures.eam.diagram.TestSetFactorSize;
import org.conservationmeasures.eam.diagram.TestUndoAndRedo;
import org.conservationmeasures.eam.diagram.cells.TestDiagramFactor;
import org.conservationmeasures.eam.diagram.cells.TestLinkCell;
import org.conservationmeasures.eam.ids.TestBaseId;
import org.conservationmeasures.eam.ids.TestIdList;
import org.conservationmeasures.eam.objectdata.TestDateData;
import org.conservationmeasures.eam.objecthelpers.TestDateRangeEffortList;
import org.conservationmeasures.eam.objecthelpers.TestMapList;
import org.conservationmeasures.eam.objecthelpers.TestORef;
import org.conservationmeasures.eam.objecthelpers.TestORefList;
import org.conservationmeasures.eam.objecthelpers.TestObjectDeepCopier;
import org.conservationmeasures.eam.objects.TestAccountingCode;
import org.conservationmeasures.eam.objects.TestAssignment;
import org.conservationmeasures.eam.objects.TestBaseObject;
import org.conservationmeasures.eam.objects.TestCause;
import org.conservationmeasures.eam.objects.TestConceptualModelDiagram;
import org.conservationmeasures.eam.objects.TestConceptualModelThreatRatings;
import org.conservationmeasures.eam.objects.TestCostAllocationRule;
import org.conservationmeasures.eam.objects.TestDiagramContentsObject;
import org.conservationmeasures.eam.objects.TestDiagramLink;
import org.conservationmeasures.eam.objects.TestDiagramObject;
import org.conservationmeasures.eam.objects.TestFactor;
import org.conservationmeasures.eam.objects.TestFactorLink;
import org.conservationmeasures.eam.objects.TestFundingSource;
import org.conservationmeasures.eam.objects.TestGoal;
import org.conservationmeasures.eam.objects.TestIndicator;
import org.conservationmeasures.eam.objects.TestIntermediateResult;
import org.conservationmeasures.eam.objects.TestKeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.TestMeasurement;
import org.conservationmeasures.eam.objects.TestObjectFindOwnerAndFindReferrer;
import org.conservationmeasures.eam.objects.TestObjectGetTypesThatCanOwnUs;
import org.conservationmeasures.eam.objects.TestObjectGetTypesThatCanReferToUs;
import org.conservationmeasures.eam.objects.TestObjectManager;
import org.conservationmeasures.eam.objects.TestObjective;
import org.conservationmeasures.eam.objects.TestPlanningViewConfiguration;
import org.conservationmeasures.eam.objects.TestProjectChainObject;
import org.conservationmeasures.eam.objects.TestProjectMetadata;
import org.conservationmeasures.eam.objects.TestProjectResource;
import org.conservationmeasures.eam.objects.TestRatingCriterion;
import org.conservationmeasures.eam.objects.TestResultsChainDiagram;
import org.conservationmeasures.eam.objects.TestSlide;
import org.conservationmeasures.eam.objects.TestSlideShow;
import org.conservationmeasures.eam.objects.TestStrategy;
import org.conservationmeasures.eam.objects.TestStress;
import org.conservationmeasures.eam.objects.TestTask;
import org.conservationmeasures.eam.objects.TestTextBox;
import org.conservationmeasures.eam.objects.TestThreatRatingBundle;
import org.conservationmeasures.eam.objects.TestThreatReductionResult;
import org.conservationmeasures.eam.objects.TestThreatStressRating;
import org.conservationmeasures.eam.objects.TestValueOption;
import org.conservationmeasures.eam.objects.TestViewData;
import org.conservationmeasures.eam.objects.TestWwfProjectData;
import org.conservationmeasures.eam.project.TestCausePool;
import org.conservationmeasures.eam.project.TestChainManager;
import org.conservationmeasures.eam.project.TestFactorDeleteHelper;
import org.conservationmeasures.eam.project.TestFactorLinkPool;
import org.conservationmeasures.eam.project.TestGroupBoxPool;
import org.conservationmeasures.eam.project.TestIdAssigner;
import org.conservationmeasures.eam.project.TestIntermediateResultPool;
import org.conservationmeasures.eam.project.TestProject;
import org.conservationmeasures.eam.project.TestProjectInfo;
import org.conservationmeasures.eam.project.TestProjectRepairer;
import org.conservationmeasures.eam.project.TestProjectUnzipper;
import org.conservationmeasures.eam.project.TestProjectZipper;
import org.conservationmeasures.eam.project.TestRealProject;
import org.conservationmeasures.eam.project.TestSimpleThreatFormula;
import org.conservationmeasures.eam.project.TestSlidePool;
import org.conservationmeasures.eam.project.TestStrategyPool;
import org.conservationmeasures.eam.project.TestStressBasedThreatFormula;
import org.conservationmeasures.eam.project.TestTNCViabilityFormula;
import org.conservationmeasures.eam.project.TestTargetPool;
import org.conservationmeasures.eam.project.TestTextBoxPool;
import org.conservationmeasures.eam.project.TestThreatRatingFramework;
import org.conservationmeasures.eam.project.TestThreatReductionResultPool;
import org.conservationmeasures.eam.questions.TestChoiceItem;
import org.conservationmeasures.eam.questions.TestChoiceQuestion;
import org.conservationmeasures.eam.ratings.TestRatingChoice;
import org.conservationmeasures.eam.ratings.TestRatingQuestion;
import org.conservationmeasures.eam.ratings.TestStrategyRatingSummary;
import org.conservationmeasures.eam.utils.TestBendPointList;
import org.conservationmeasures.eam.utils.TestCodeList;
import org.conservationmeasures.eam.utils.TestDateRange;
import org.conservationmeasures.eam.utils.TestDelimitedFileLoader;
import org.conservationmeasures.eam.utils.TestEnhancedJsonObject;
import org.conservationmeasures.eam.utils.TestJTextile;
import org.conservationmeasures.eam.utils.TestLogging;
import org.conservationmeasures.eam.utils.TestPointList;
import org.conservationmeasures.eam.utils.TestStringMapData;
import org.conservationmeasures.eam.utils.TestTaxonomyFileLoader;
import org.conservationmeasures.eam.utils.TestTranslations;
import org.conservationmeasures.eam.views.budget.ImportAccountingCodesDoerTest;
import org.conservationmeasures.eam.views.diagram.TestLinkBendPointsMoveHandler;
import org.conservationmeasures.eam.views.diagram.doers.TestDeleteAnnotationDoer;
import org.conservationmeasures.eam.views.planning.TestPlanningTreeActivityNode;
import org.conservationmeasures.eam.views.planning.TestPlanningTreeConceptualModelPageNode;
import org.conservationmeasures.eam.views.planning.TestPlanningTreeDirectThreatNode;
import org.conservationmeasures.eam.views.planning.TestPlanningTreeGoalNode;
import org.conservationmeasures.eam.views.planning.TestPlanningTreeIndicatorNode;
import org.conservationmeasures.eam.views.planning.TestPlanningTreeMetadataGoals;
import org.conservationmeasures.eam.views.planning.TestPlanningTreeObjectiveNode;
import org.conservationmeasures.eam.views.planning.TestPlanningTreeStrategyNode;
import org.conservationmeasures.eam.views.planning.TestPlanningTreeTargetNode;
import org.conservationmeasures.eam.views.threatmatrix.TestNonEditableThreatMatrixTableModel;
import org.conservationmeasures.eam.views.umbrella.TestUndoRedo;
import org.conservationmeasures.eam.views.workplan.TestDeleteActivity;
import org.martus.util.TestMultiCalendar;
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
		TestSuite suite= new MainTestSuite("All eAM Tests");

		// database package
		suite.addTest(new TestSuite(TestDataUpgrader.class));
		suite.addTest(new TestSuite(TestProjectServer.class));
		
		// main package
		suite.addTest(new TestSuite(TestCommandExecutedEvents.class));
		suite.addTest(new TestSuite(TestMainMenu.class));
		suite.addTest(new TestSuite(TestTransferableEamList.class));
		
		// project package
		suite.addTest(new TestSuite(TestThreatReductionResultPool.class));
		suite.addTest(new TestSuite(TestTextBoxPool.class));
		suite.addTest(new TestSuite(TestIntermediateResultPool.class));
		suite.addTest(new TestSuite(TestFactorLinkPool.class));
		suite.addTest(new TestSuite(TestStrategyPool.class));
		suite.addTest(new TestSuite(TestTargetPool.class));
		suite.addTest(new TestSuite(TestCausePool.class));
		suite.addTest(new TestSuite(TestProject.class));
		suite.addTest(new TestSuite(TestProjectRepairer.class));
		suite.addTest(new TestSuite(TestRealProject.class));
		suite.addTest(new TestSuite(TestIdAssigner.class));
		suite.addTest(new TestSuite(TestProjectInfo.class));
		suite.addTest(new TestSuite(TestThreatRatingFramework.class));
		suite.addTest(new TestSuite(TestSimpleThreatFormula.class));
		suite.addTest(new TestSuite(TestTNCViabilityFormula.class));
		suite.addTest(new TestSuite(TestProjectZipper.class));
		suite.addTest(new TestSuite(TestProjectUnzipper.class));
		suite.addTest(new TestSuite(TestSlidePool.class));
		suite.addTest(new TestSuite(TestFactorDeleteHelper.class));
		suite.addTest(new TestSuite(TestStressBasedThreatFormula.class));
		suite.addTest(new TestSuite(TestGroupBoxPool.class));
		
		//questions package
		suite.addTest(new TestSuite(TestChoiceItem.class));
		suite.addTest(new TestSuite(TestChoiceQuestion.class));
		
		// utils package
		suite.addTest(new TestSuite(TestEnhancedJsonObject.class));
		suite.addTest(new TestSuite(TestLogging.class));
		suite.addTest(new TestSuite(TestTranslations.class));
		suite.addTest(new TestSuite(TestDelimitedFileLoader.class));
		suite.addTest(new TestSuite(TestTaxonomyFileLoader.class));
		suite.addTest(new TestSuite(TestDateRange.class));
		
		// diagram package
		suite.addTest(new TestSuite(TestDiagramModel.class));
		suite.addTest(new TestSuite(TestDiagramView.class));
		suite.addTest(new TestSuite(TestProjectScopeBox.class));
		suite.addTest(new TestSuite(TestDiagramComponent.class));
		suite.addTest(new TestSuite(TestBendPointSelectionHelper.class));
		suite.addTest(new TestSuite(TestLinkBendPointsMoveHandler.class));
		suite.addTest(new TestSuite(TestLinkCreator.class));

		// factors package
		suite.addTest(new TestSuite(TestDiagramAddFactorLink.class));
		suite.addTest(new TestSuite(TestDelete.class));
		suite.addTest(new TestSuite(TestEamGraphCell.class));
		suite.addTest(new TestSuite(TestDiagramAddFactor.class));
		suite.addTest(new TestSuite(TestDiagramFactor.class));
		suite.addTest(new TestSuite(TestSetFactorSize.class));
		suite.addTest(new TestSuite(TestUndoAndRedo.class));
		suite.addTest(new TestSuite(TestLinkCell.class));
		
		//objectdata package
		suite.addTest(new TestSuite(TestDateData.class));
		
		//objecthelpers package
		suite.addTest(new TestSuite(TestMapList.class));
		suite.addTest(new TestSuite(TestORef.class));
		suite.addTest(new TestSuite(TestObjectDeepCopier.class));
		suite.addTest(new TestSuite(TestDateRangeEffortList.class));
		
		// objects package
		suite.addTest(new TestSuite(TestStrategy.class));
		suite.addTest(new TestSuite(TestCause.class));
		suite.addTest(new TestSuite(TestDiagramLink.class));
		suite.addTest(new TestSuite(TestFactorLink.class));
		suite.addTest(new TestSuite(TestFactor.class));
		suite.addTest(new TestSuite(TestConceptualModelThreatRatings.class));
		suite.addTest(new TestSuite(TestGoal.class));
		suite.addTest(new TestSuite(TestIdList.class));
		suite.addTest(new TestSuite(TestBaseId.class));
		suite.addTest(new TestSuite(TestCodeList.class));
		suite.addTest(new TestSuite(TestStringMapData.class));
		suite.addTest(new TestSuite(TestIndicator.class));
		suite.addTest(new TestSuite(TestKeyEcologicalAttribute.class));
		suite.addTest(new TestSuite(TestObjective.class));
		suite.addTest(new TestSuite(TestObjectManager.class));
		suite.addTest(new TestSuite(TestProjectChainObject.class));
		suite.addTest(new TestSuite(TestProjectMetadata.class));
		suite.addTest(new TestSuite(TestProjectResource.class));
		suite.addTest(new TestSuite(TestTask.class));
		suite.addTest(new TestSuite(TestThreatRatingBundle.class));
		suite.addTest(new TestSuite(TestRatingCriterion.class));
		suite.addTest(new TestSuite(TestValueOption.class));
		suite.addTest(new TestSuite(TestViewData.class));
		suite.addTest(new TestSuite(TestORefList.class));
		suite.addTest(new TestSuite(TestAssignment.class));
		suite.addTest(new TestSuite(TestProjectResource.class));
		suite.addTest(new TestSuite(TestFundingSource.class));
		suite.addTest(new TestSuite(TestAccountingCode.class));
		suite.addTest(new TestSuite(TestPointList.class));
		suite.addTest(new TestSuite(TestBendPointList.class));
		suite.addTest(new TestSuite(TestDiagramContentsObject.class));
		suite.addTest(new TestSuite(TestObjectGetTypesThatCanReferToUs.class));
		suite.addTest(new TestSuite(TestObjectGetTypesThatCanOwnUs.class));
		suite.addTest(new TestSuite(TestObjectFindOwnerAndFindReferrer.class));
		suite.addTest(new TestSuite(TestBaseObject.class));
		suite.addTest(new TestSuite(TestConceptualModelDiagram.class));
		suite.addTest(new TestSuite(TestResultsChainDiagram.class));
		suite.addTest(new TestSuite(TestThreatReductionResult.class));
		suite.addTest(new TestSuite(TestIntermediateResult.class));
		suite.addTest(new TestSuite(TestTextBox.class));
		suite.addTest(new TestSuite(TestChainManager.class));
		suite.addTest(new TestSuite(TestSlide.class));
		suite.addTest(new TestSuite(TestSlideShow.class));
		suite.addTest(new TestSuite(TestPlanningViewConfiguration.class));
		suite.addTest(new TestSuite(TestDiagramObject.class));
		suite.addTest(new TestSuite(TestWwfProjectData.class));
		suite.addTest(new TestSuite(TestCostAllocationRule.class));
		suite.addTest(new TestSuite(TestMeasurement.class));
		suite.addTest(new TestSuite(TestStress.class));
		suite.addTest(new TestSuite(TestThreatStressRating.class));
		
		// commands package
		suite.addTest(new TestSuite(TestCommands.class));
		suite.addTest(new TestSuite(TestCommandCreateObject.class));
		suite.addTest(new TestSuite(TestCommandSetObjectData.class));
		suite.addTest(new TestSuite(TestCommandDeleteObject.class));
		suite.addTest(new TestSuite(TestCommandSetFactorSize.class));
		suite.addTest(new TestSuite(TestCommandSetThreatRating.class));
		suite.addTest(new TestSuite(TestCommandBeginTransaction.class));
		suite.addTest(new TestSuite(TestCommandEndTransaction.class));
		
		// ratings package
		suite.addTest(new TestSuite(TestRatingChoice.class));
		suite.addTest(new TestSuite(TestRatingQuestion.class));
		suite.addTest(new TestSuite(TestStrategyRatingSummary.class));
		
		// view.diagram package
		suite.addTest(new TestSuite(org.conservationmeasures.eam.views.diagram.TestInsertFactorLinkDoer.class));
		suite.addTest(new TestSuite(org.conservationmeasures.eam.views.diagram.TestInsertFactorDoer.class));
		suite.addTest(new TestSuite(org.conservationmeasures.eam.views.diagram.TestLayerManager.class));
		suite.addTest(new TestSuite(org.conservationmeasures.eam.views.diagram.TestDiagramPaster.class));
		suite.addTest(new TestSuite(TestDeleteAnnotationDoer.class));
		
		// view.planning package
		suite.addTest(new TestSuite(TestPlanningTreeGoalNode.class));
		suite.addTest(new TestSuite(TestPlanningTreeTargetNode.class));
		suite.addTest(new TestSuite(TestPlanningTreeDirectThreatNode.class));
		suite.addTest(new TestSuite(TestPlanningTreeConceptualModelPageNode.class));
		suite.addTest(new TestSuite(TestPlanningTreeObjectiveNode.class));
		suite.addTest(new TestSuite(TestPlanningTreeStrategyNode.class));
		suite.addTest(new TestSuite(TestPlanningTreeActivityNode.class));
		suite.addTest(new TestSuite(TestPlanningTreeIndicatorNode.class));
		suite.addTest(new TestSuite(TestPlanningTreeMetadataGoals.class));
		
		// view.strategicplan package
		suite.addTest(new TestSuite(TestDeleteActivity.class));
		
		// view.buget
		suite.addTest(new TestSuite(ImportAccountingCodesDoerTest.class));
		
		// view.threatmatrix package
		suite.addTest(new TestSuite(TestNonEditableThreatMatrixTableModel.class));
		
		// view.summary.doers package

		// view.umbrella package
		suite.addTest(new TestSuite(TestUndoRedo.class));
		

		
		// martus-utils
		suite.addTest(new TestSuite(TestSimpleXmlParser.class));
		suite.addTest(new TestSuite(TestMultiCalendar.class));
		suite.addTest(new TestSuite(TestJTextile.class));

	    return suite;
	}
}

class MainTestSuite extends TestSuite
{
	public MainTestSuite(String name)
	{
		super(name);
	}

	public void run(TestResult result)
	{
		reportAnyTempFiles("Existing temp file: ");
		super.run(result);
		reportAnyTempFiles("Orphaned temp file: ");
	}
	
	public void reportAnyTempFiles(String message)
	{
		File systemTempDirectory = getSystemTempDirectory();
		
		String[] allTempFileNames = systemTempDirectory.list();
		for(int i = 0; i < allTempFileNames.length; ++i)
		{
			String fileName = allTempFileNames[i];
			if(fileName.startsWith("$$$"))
				System.out.println("WARNING: " + message + fileName);
		}
	}

	private File getSystemTempDirectory()
	{
		File merelyToFindTempDirectory = createTempFileToLocateTempDirectory();
		File systemTempDirectory = merelyToFindTempDirectory.getParentFile();
		merelyToFindTempDirectory.delete();
		return systemTempDirectory;
	}

	private File createTempFileToLocateTempDirectory()
	{
		try
		{
			return File.createTempFile("$$$MainTests", null);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Unable to create temp file!");
		}
	}
	
}