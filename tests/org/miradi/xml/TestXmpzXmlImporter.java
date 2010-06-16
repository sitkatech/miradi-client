/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;

import org.martus.util.UnicodeReader;
import org.martus.util.UnicodeStringWriter;
import org.martus.util.inputstreamwithseek.FileInputStreamWithSeek;
import org.martus.util.inputstreamwithseek.StringInputStreamWithSeek;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.project.ProjectForTesting;
import org.miradi.questions.DiagramFactorFontSizeQuestion;
import org.miradi.questions.DiagramFactorFontStyleQuestion;
import org.miradi.questions.DiagramLegendQuestion;
import org.miradi.questions.TextBoxZOrderQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.xml.wcs.WcsXmlExporter;
import org.miradi.xml.xmpz.XmpzXmlImporter;

public class TestXmpzXmlImporter extends TestCaseWithProject
{
	public TestXmpzXmlImporter(String name)
	{
		super(name);
	}
	
	public void testValidateEmptyProject() throws Exception
	{
		validateExportImportExportProject();
	}
	
	public void testExpenseAssignmentLifeCycle() throws Exception
	{
		ExpenseAssignment expense = getProject().createExpenseAssignment();
		getProject().createExpenseAssignment();
		DateUnitEffortList lis = new DateUnitEffortList();
		DateUnit month = new DateUnit("2008-09");
		DateUnitEffort dateUnitEffort = new DateUnitEffort(month, 22.9);
		lis.add(dateUnitEffort);
		getProject().fillObjectUsingCommand(expense, ExpenseAssignment.TAG_DATEUNIT_EFFORTS, lis.toString());
		
		validateUsingStringWriter();
	}

	public void testProjectWithStressBasedThreatRatingData() throws Exception
	{
		getProject().populateStressBasedThreatRatingCommentsData();
		getProject().createThreatTargetDiagramLinkWithRating();
		validateUsingStringWriter();
	}
	
	public void testValidateFilledProject() throws Exception
	{
		getProject().createAndPopulateDiagramLink();
		createFilledDiagramFactor();
		getProject().createDiagramFactorLink();
		getProject().createAndPopulateGroupBoxDiagramLink();
		getProject().createAndPopulateGoal(getProject().createTarget());
		getProject().createandpopulateThreatReductionResult();
		getProject().populateEverything();
		createFilledResultsChainDiagram();
		getProject().createObjective(getProject().createCause());		
		getProject().createAndPopulateExpenseAssignment();
		
		validateExportImportExportProject();
	}

	private void createFilledDiagramFactor() throws Exception
	{
		DiagramFactor diagramFactor = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		getProject().fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_LOCATION, EnhancedJsonObject.convertFromPoint(new Point(100, 12)));
		getProject().fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_SIZE, EnhancedJsonObject.convertFromDimension(new Dimension(45, 45)));
		getProject().fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_TEXT_BOX_Z_ORDER_CODE, TextBoxZOrderQuestion.FRONT_CODE);
		getProject().fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_FONT_STYLE, DiagramFactorFontStyleQuestion.BOLD_CODE);
		getProject().fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_FONT_SIZE, DiagramFactorFontSizeQuestion.LARGEST_FONT_SIZE_CODE);
	}

	private void createFilledResultsChainDiagram() throws Exception
	{
		ORef resultsChainRef = getProject().createResultsChainDiagram();
		TaggedObjectSet taggedOjectSet = getProject().createTaggedObjectSet();
		ORefList taggedObjectSetRefs = new ORefList(taggedOjectSet);
		getProject().fillObjectUsingCommand(resultsChainRef, DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS, taggedObjectSetRefs);
		getProject().fillObjectUsingCommand(resultsChainRef, ResultsChainDiagram.TAG_LABEL, "SomeLabel");
		getProject().fillObjectUsingCommand(resultsChainRef, ResultsChainDiagram.TAG_SHORT_LABEL, "SomeShortLabel");
		getProject().fillObjectUsingCommand(resultsChainRef, ResultsChainDiagram.TAG_DETAIL, "SomeDetails");
		getProject().fillObjectUsingCommand(resultsChainRef, ResultsChainDiagram.TAG_ZOOM_SCALE, "2.0");
		
		CodeList hiddentTypeCodes = new CodeList();
		hiddentTypeCodes.add(DiagramLegendQuestion.STRESS_HIDDEN_TYPE_CODE);
		getProject().fillObjectUsingCommand(resultsChainRef, ResultsChainDiagram.TAG_HIDDEN_TYPES, hiddentTypeCodes.toString());
	}
	
	private void validateExportImportExportProject() throws Exception
	{
		File beforeXmlOutFile = createTempFileFromName("XmlBeforeImport.xml");		
		File afterXmlOutFile = createTempFileFromName("XmlAfterFirstImport.xml");
		ProjectForTesting projectToImportInto = ProjectForTesting.createProjectWithoutDefaultObjects("ProjectToFill1");
		try
		{
			exportProject(beforeXmlOutFile, getProject());
			String firstExport = convertFileContentToString(beforeXmlOutFile);
			
			importProject(beforeXmlOutFile, projectToImportInto);
			
			exportProject(afterXmlOutFile, projectToImportInto);
			String exportedAfterImport = convertFileContentToString(afterXmlOutFile);
			assertEquals("incorrect project values after first import?", firstExport, exportedAfterImport);
		}
		finally
		{
			beforeXmlOutFile.delete();
			afterXmlOutFile.delete();
			projectToImportInto.close();
		}	
	}
	
	private void exportProject(File afterXmlOutFile, ProjectForTesting projectToExport) throws Exception
	{
		new WcsXmlExporter(projectToExport).export(afterXmlOutFile);
	}

	private void importProject(File beforeXmlOutFile, ProjectForTesting projectToFill1) throws Exception
	{		
		XmpzXmlImporter xmlImporter = new XmpzXmlImporter(projectToFill1);
		FileInputStreamWithSeek fileInputStream = new FileInputStreamWithSeek(beforeXmlOutFile); 
		try
		{
			xmlImporter.importProject(fileInputStream);
		}
		finally
		{
			fileInputStream.close();	
		}
	}
	
	private String convertFileContentToString(File fileToConvert) throws Exception
	{
	    return new UnicodeReader(fileToConvert).readAll();
	}
	
	private void validateUsingStringWriter() throws Exception
	{
		UnicodeStringWriter firstWriter = createWriter(getProject());
		
		ProjectForTesting projectToImportInto = ProjectForTesting.createProjectWithoutDefaultObjects("ProjectToImportInto");
		XmpzXmlImporter xmlImporter = new XmpzXmlImporter(projectToImportInto);
		StringInputStreamWithSeek stringInputputStream = new StringInputStreamWithSeek(firstWriter.toString());
		try
		{
			xmlImporter.importProject(stringInputputStream);
		}
		finally
		{
			stringInputputStream.close();	
		}
		
		UnicodeStringWriter secondWriter = createWriter(projectToImportInto);
		
		assertEquals("Exports from projects do not match?", firstWriter.toString(), secondWriter.toString());
	}

	private UnicodeStringWriter createWriter(ProjectForTesting project) throws Exception
	{
		WcsXmlExporter exporter = new WcsXmlExporter(project);
		UnicodeStringWriter writer = UnicodeStringWriter.create();
		exporter.setWriter(writer);
		exporter.exportProject(writer);
		writer.flush();
		
		return writer;
	}
}
