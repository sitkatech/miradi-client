/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/
package org.miradi.main;

import java.io.File;
import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.martus.util.TestMultiCalendar;
import org.martus.util.xml.TestSimpleXmlParser;
import org.miradi.commands.TestCommandBeginTransaction;
import org.miradi.commands.TestCommandCreateObject;
import org.miradi.commands.TestCommandDeleteObject;
import org.miradi.commands.TestCommandEndTransaction;
import org.miradi.commands.TestCommandSetFactorSize;
import org.miradi.commands.TestCommandSetObjectData;
import org.miradi.commands.TestCommandSetThreatRating;
import org.miradi.commands.TestCommands;
import org.miradi.database.TestDataUpgrader;
import org.miradi.database.TestProjectServer;
import org.miradi.diagram.TestBendPointSelectionHelper;
import org.miradi.diagram.TestDelete;
import org.miradi.diagram.TestDiagramAddFactor;
import org.miradi.diagram.TestDiagramAddFactorLink;
import org.miradi.diagram.TestDiagramComponent;
import org.miradi.diagram.TestDiagramModel;
import org.miradi.diagram.TestDiagramView;
import org.miradi.diagram.TestEAMGraphCellByFactorTypeSorter;
import org.miradi.diagram.TestEamGraphCell;
import org.miradi.diagram.TestGroupBoxLinking;
import org.miradi.diagram.TestLayerSorter;
import org.miradi.diagram.TestLinkCreator;
import org.miradi.diagram.TestSetFactorSize;
import org.miradi.diagram.TestUndoAndRedo;
import org.miradi.diagram.cells.TestDiagramFactor;
import org.miradi.diagram.cells.TestLinkCell;
import org.miradi.export.TestReportXmlExporter;
import org.miradi.forms.TestFieldPanelSpec;
import org.miradi.forms.TestFormConstant;
import org.miradi.forms.TestFormFieldData;
import org.miradi.forms.TestFormFieldLabel;
import org.miradi.forms.TestFormRow;
import org.miradi.forms.TestPanelHolderSpec;
import org.miradi.ids.TestBaseId;
import org.miradi.ids.TestIdList;
import org.miradi.objectdata.TestDateData;
import org.miradi.objectdata.TestDateRangeData;
import org.miradi.objectdata.TestIntegerData;
import org.miradi.objecthelpers.TestDateRangeEffortList;
import org.miradi.objecthelpers.TestMapList;
import org.miradi.objecthelpers.TestORef;
import org.miradi.objecthelpers.TestORefList;
import org.miradi.objecthelpers.TestORefSet;
import org.miradi.objecthelpers.TestObjectDeepCopier;
import org.miradi.objecthelpers.TestRelevancyOverride;
import org.miradi.objecthelpers.TestRelevancyOverrideSet;
import org.miradi.objecthelpers.TestStringRefMap;
import org.miradi.objecthelpers.TestThreatStressRatingEnsurer;
import org.miradi.objects.TestAccountingCode;
import org.miradi.objects.TestAssignment;
import org.miradi.objects.TestBaseObject;
import org.miradi.objects.TestCause;
import org.miradi.objects.TestConceptualModelDiagram;
import org.miradi.objects.TestConceptualModelThreatRatings;
import org.miradi.objects.TestCostAllocationRule;
import org.miradi.objects.TestDiagramChainObject;
import org.miradi.objects.TestDiagramContentsObject;
import org.miradi.objects.TestDiagramLink;
import org.miradi.objects.TestDiagramObject;
import org.miradi.objects.TestFactor;
import org.miradi.objects.TestFactorLink;
import org.miradi.objects.TestFosProjectData;
import org.miradi.objects.TestFundingSource;
import org.miradi.objects.TestGoal;
import org.miradi.objects.TestIndicator;
import org.miradi.objects.TestIntermediateResult;
import org.miradi.objects.TestKeyEcologicalAttribute;
import org.miradi.objects.TestMeasurement;
import org.miradi.objects.TestObjectFindOwnerAndFindReferrer;
import org.miradi.objects.TestObjectGetTypesThatCanOwnUs;
import org.miradi.objects.TestObjectManager;
import org.miradi.objects.TestObjective;
import org.miradi.objects.TestOrganization;
import org.miradi.objects.TestPlanningViewConfiguration;
import org.miradi.objects.TestProgressPercent;
import org.miradi.objects.TestProgressReport;
import org.miradi.objects.TestProjectChainObject;
import org.miradi.objects.TestProjectMetadata;
import org.miradi.objects.TestProjectResource;
import org.miradi.objects.TestRareProjectData;
import org.miradi.objects.TestRatingCriterion;
import org.miradi.objects.TestReportTemplate;
import org.miradi.objects.TestResultsChainDiagram;
import org.miradi.objects.TestScopeBox;
import org.miradi.objects.TestSlide;
import org.miradi.objects.TestSlideShow;
import org.miradi.objects.TestStrategy;
import org.miradi.objects.TestStress;
import org.miradi.objects.TestSubTarget;
import org.miradi.objects.TestTableSettings;
import org.miradi.objects.TestTaggedObjectSet;
import org.miradi.objects.TestTask;
import org.miradi.objects.TestTextBox;
import org.miradi.objects.TestThreatRatingBundle;
import org.miradi.objects.TestThreatRatingCommentsData;
import org.miradi.objects.TestThreatReductionResult;
import org.miradi.objects.TestThreatStressRating;
import org.miradi.objects.TestThreatTargetChainObject;
import org.miradi.objects.TestTncProjectData;
import org.miradi.objects.TestValueOption;
import org.miradi.objects.TestViewData;
import org.miradi.objects.TestWcpaProjectData;
import org.miradi.objects.TestWcsProjectData;
import org.miradi.objects.TestWwfProjectData;
import org.miradi.objects.TestXenodata;
import org.miradi.project.TestBudgetTimePeriodChanger;
import org.miradi.project.TestCausePool;
import org.miradi.project.TestChainManager;
import org.miradi.project.TestFactorDeleteHelper;
import org.miradi.project.TestFactorLinkPool;
import org.miradi.project.TestGroupBoxPool;
import org.miradi.project.TestIdAssigner;
import org.miradi.project.TestIntermediateResultPool;
import org.miradi.project.TestProject;
import org.miradi.project.TestProjectCalendar;
import org.miradi.project.TestProjectCommandExecutions;
import org.miradi.project.TestProjectInfo;
import org.miradi.project.TestProjectRepairer;
import org.miradi.project.TestProjectUnzipper;
import org.miradi.project.TestProjectZipper;
import org.miradi.project.TestRealProject;
import org.miradi.project.TestScopeBoxPool;
import org.miradi.project.TestSimpleThreatFormula;
import org.miradi.project.TestSimpleThreatRatingFramework;
import org.miradi.project.TestSlidePool;
import org.miradi.project.TestStrategyPool;
import org.miradi.project.TestStressBasedThreatFormula;
import org.miradi.project.TestStressBasedThreatRatingFramework;
import org.miradi.project.TestTNCViabilityFormula;
import org.miradi.project.TestTargetPool;
import org.miradi.project.TestTextBoxPool;
import org.miradi.project.TestThreatReductionResultPool;
import org.miradi.questions.TestChoiceItem;
import org.miradi.questions.TestChoiceQuestion;
import org.miradi.ratings.TestRatingChoice;
import org.miradi.ratings.TestRatingQuestion;
import org.miradi.ratings.TestStrategyRatingSummary;
import org.miradi.utils.TestBaseObjectDateAndIdComparator;
import org.miradi.utils.TestBendPointList;
import org.miradi.utils.TestCodeList;
import org.miradi.utils.TestConproMiradiHabitatCodeMap;
import org.miradi.utils.TestDateRange;
import org.miradi.utils.TestDelimitedFileLoader;
import org.miradi.utils.TestDiagramCorruptionDetector;
import org.miradi.utils.TestEnhancedJsonObject;
import org.miradi.utils.TestLogging;
import org.miradi.utils.TestMiradiMap;
import org.miradi.utils.TestPointList;
import org.miradi.utils.TestRtfWriter;
import org.miradi.utils.TestStringMap;
import org.miradi.utils.TestTaxonomyFileLoader;
import org.miradi.utils.TestThreatStressRatingHelper;
import org.miradi.utils.TestTranslations;
import org.miradi.views.budget.ImportAccountingCodesDoerTest;
import org.miradi.views.diagram.TestLinkBendPointsMoveHandler;
import org.miradi.views.diagram.doers.TestDeleteAnnotationDoer;
import org.miradi.views.planning.TestPlanningTreeActivityNode;
import org.miradi.views.planning.TestPlanningTreeConceptualModelPageNode;
import org.miradi.views.planning.TestPlanningTreeDirectThreatNode;
import org.miradi.views.planning.TestPlanningTreeGoalNode;
import org.miradi.views.planning.TestPlanningTreeIndicatorNode;
import org.miradi.views.planning.TestPlanningTreeObjectiveNode;
import org.miradi.views.planning.TestPlanningTreeStrategyNode;
import org.miradi.views.planning.TestPlanningTreeTargetNode;
import org.miradi.views.umbrella.TestUndoRedo;
import org.miradi.views.workplan.TestDeleteActivity;
import org.miradi.xml.conpro.TestConProCodeMapHelper;
import org.miradi.xml.conpro.exporter.TestConproXmlExporter;
import org.miradi.xml.conpro.exporter.TestConproXmlExporterVersion2;
import org.miradi.xml.conpro.importer.TestConProXmlImporter;
import org.miradi.xml.conpro.importer.TestConproXmlImporterVersion2;

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
		
		// forms package
		suite.addTest(new TestSuite(TestFieldPanelSpec.class));
		suite.addTest(new TestSuite(TestPanelHolderSpec.class));
		suite.addTest(new TestSuite(TestFormConstant.class));
		suite.addTest(new TestSuite(TestFormFieldLabel.class));
		suite.addTest(new TestSuite(TestFormFieldData.class));
		suite.addTest(new TestSuite(TestFormRow.class));
		
		// main package
		suite.addTest(new TestSuite(TestCommandExecutedEvents.class));
		suite.addTest(new TestSuite(TestMainMenu.class));
		suite.addTest(new TestSuite(TestTransferableEamList.class));
		suite.addTest(new TestSuite(TestEAM.class));
		
		// project package
		suite.addTest(new TestSuite(TestBudgetTimePeriodChanger.class));
		suite.addTest(new TestSuite(TestThreatReductionResultPool.class));
		suite.addTest(new TestSuite(TestTextBoxPool.class));
		suite.addTest(new TestSuite(TestScopeBoxPool.class));
		suite.addTest(new TestSuite(TestIntermediateResultPool.class));
		suite.addTest(new TestSuite(TestFactorLinkPool.class));
		suite.addTest(new TestSuite(TestStrategyPool.class));
		suite.addTest(new TestSuite(TestTargetPool.class));
		suite.addTest(new TestSuite(TestCausePool.class));
		suite.addTest(new TestSuite(TestProject.class));
		suite.addTest(new TestSuite(TestProjectCommandExecutions.class));
		suite.addTest(new TestSuite(TestProjectCalendar.class));
		suite.addTest(new TestSuite(TestProjectRepairer.class));
		suite.addTest(new TestSuite(TestRealProject.class));
		suite.addTest(new TestSuite(TestIdAssigner.class));
		suite.addTest(new TestSuite(TestProjectInfo.class));
		suite.addTest(new TestSuite(TestSimpleThreatRatingFramework.class));
		suite.addTest(new TestSuite(TestSimpleThreatFormula.class));
		suite.addTest(new TestSuite(TestTNCViabilityFormula.class));
		suite.addTest(new TestSuite(TestProjectZipper.class));
		suite.addTest(new TestSuite(TestProjectUnzipper.class));
		suite.addTest(new TestSuite(TestSlidePool.class));
		suite.addTest(new TestSuite(TestFactorDeleteHelper.class));
		suite.addTest(new TestSuite(TestStressBasedThreatFormula.class));
		suite.addTest(new TestSuite(TestStressBasedThreatRatingFramework.class));
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
		suite.addTest(new TestSuite(TestConproMiradiHabitatCodeMap.class));
		suite.addTest(new TestSuite(TestMiradiMap.class));
		suite.addTest(new TestSuite(TestBaseObjectDateAndIdComparator.class));
		suite.addTest(new TestSuite(TestDiagramCorruptionDetector.class));
		suite.addTest(new TestSuite(TestRtfWriter.class));
		suite.addTest(new TestSuite(TestThreatStressRatingHelper.class));
		
		// diagram package
		suite.addTest(new TestSuite(TestDiagramModel.class));
		suite.addTest(new TestSuite(TestDiagramView.class));
		suite.addTest(new TestSuite(TestDiagramComponent.class));
		suite.addTest(new TestSuite(TestBendPointSelectionHelper.class));
		suite.addTest(new TestSuite(TestLinkBendPointsMoveHandler.class));
		suite.addTest(new TestSuite(TestLinkCreator.class));
		suite.addTest(new TestSuite(TestGroupBoxLinking.class));
		suite.addTest(new TestSuite(TestLayerSorter.class));
		suite.addTest(new TestSuite(TestEAMGraphCellByFactorTypeSorter.class));		

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
		suite.addTest(new TestSuite(TestDateRangeData.class));
		suite.addTest(new TestSuite(TestIntegerData.class));
		
		//objecthelpers package
		suite.addTest(new TestSuite(TestMapList.class));
		suite.addTest(new TestSuite(TestORef.class));
		suite.addTest(new TestSuite(TestObjectDeepCopier.class));
		suite.addTest(new TestSuite(TestDateRangeEffortList.class));
		suite.addTest(new TestSuite(TestORefSet.class));
		suite.addTest(new TestSuite(TestRelevancyOverride.class));
		suite.addTest(new TestSuite(TestRelevancyOverrideSet.class));
		suite.addTest(new TestSuite(TestStringRefMap.class));
		suite.addTest(new TestSuite(TestThreatStressRatingEnsurer.class));
		
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
		suite.addTest(new TestSuite(TestStringMap.class));
		suite.addTest(new TestSuite(TestIndicator.class));
		suite.addTest(new TestSuite(TestKeyEcologicalAttribute.class));
		suite.addTest(new TestSuite(TestObjective.class));
		suite.addTest(new TestSuite(TestObjectManager.class));
		suite.addTest(new TestSuite(TestProjectChainObject.class));
		suite.addTest(new TestSuite(TestDiagramChainObject.class));
		suite.addTest(new TestSuite(TestThreatTargetChainObject.class));
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
		suite.addTest(new TestSuite(TestObjectGetTypesThatCanOwnUs.class));
		suite.addTest(new TestSuite(TestObjectFindOwnerAndFindReferrer.class));
		suite.addTest(new TestSuite(TestBaseObject.class));
		suite.addTest(new TestSuite(TestConceptualModelDiagram.class));
		suite.addTest(new TestSuite(TestResultsChainDiagram.class));
		suite.addTest(new TestSuite(TestThreatReductionResult.class));
		suite.addTest(new TestSuite(TestIntermediateResult.class));
		suite.addTest(new TestSuite(TestTextBox.class));
		suite.addTest(new TestSuite(TestScopeBox.class));
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
		suite.addTest(new TestSuite(TestSubTarget.class));
		suite.addTest(new TestSuite(TestProgressReport.class));
		suite.addTest(new TestSuite(TestRareProjectData.class));
		suite.addTest(new TestSuite(TestWcsProjectData.class));
		suite.addTest(new TestSuite(TestTncProjectData.class));
		suite.addTest(new TestSuite(TestFosProjectData.class));
		suite.addTest(new TestSuite(TestOrganization.class));
		suite.addTest(new TestSuite(TestWcpaProjectData.class));
		suite.addTest(new TestSuite(TestXenodata.class));
		suite.addTest(new TestSuite(TestProgressPercent.class));
		suite.addTest(new TestSuite(TestReportTemplate.class));
		suite.addTest(new TestSuite(TestTaggedObjectSet.class));
		suite.addTest(new TestSuite(TestTableSettings.class));
		suite.addTest(new TestSuite(TestThreatRatingCommentsData.class));
			
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
		suite.addTest(new TestSuite(org.miradi.views.diagram.TestInsertFactorLinkDoer.class));
		suite.addTest(new TestSuite(org.miradi.views.diagram.TestInsertFactorDoer.class));
		suite.addTest(new TestSuite(org.miradi.views.diagram.TestLayerManager.class));
		suite.addTest(new TestSuite(org.miradi.views.diagram.TestDiagramPaster.class));
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
		
		// view.strategicplan package
		suite.addTest(new TestSuite(TestDeleteActivity.class));
		
		// view.buget
		suite.addTest(new TestSuite(ImportAccountingCodesDoerTest.class));
		
		// view.threatmatrix package
		
		// view.summary.doers package

		// view.umbrella package
		suite.addTest(new TestSuite(TestUndoRedo.class));
		

		
		// martus-utils
		suite.addTest(new TestSuite(TestSimpleXmlParser.class));
		suite.addTest(new TestSuite(TestMultiCalendar.class));
		
		//xml.export
		suite.addTest(new TestSuite(TestReportXmlExporter.class));
		
		//xml.conpro
		suite.addTest(new TestSuite(TestConProCodeMapHelper.class));
		
		//xml.conpro.export
		suite.addTest(new TestSuite(TestConproXmlExporter.class));
		suite.addTest(new TestSuite(TestConproXmlExporterVersion2.class));
		
		//xml.conpro.importer
		suite.addTest(new TestSuite(TestConProXmlImporter.class));
		suite.addTest(new TestSuite(TestConproXmlImporterVersion2.class));

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