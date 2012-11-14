/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

import org.martus.util.inputstreamwithseek.StringInputStreamWithSeek;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.project.Project;
import org.miradi.project.ProjectForTesting;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.NullProgressMeter;
import org.miradi.utils.PointList;
import org.miradi.utils.UnicodeXmlWriter;
import org.miradi.views.diagram.TestLinkBendPointsMoveHandler;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlExporter;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;

public class TestXmpz2XmlImporter extends TestCaseWithProject
{
	public TestXmpz2XmlImporter(String name)
	{
		super(name);
	}
	
	public void testXmpz2NameSpaceContext() throws Exception
	{
		Xmpz2XmlImporter xmlImporter = new Xmpz2XmlImporter(getProject(), new NullProgressMeter());
		AbstractXmlNamespaceContext namespaceContext = xmlImporter.getNamespaceContext();
		assertEquals("incorrect namespace?", Xmpz2XmlConstants.NAME_SPACE, namespaceContext.getNameSpace());
		assertEquals("incorrect prefix?", Xmpz2XmlConstants.RAW_PREFIX, namespaceContext.getPrefix());
	}
	
	public void testImportEmptyProject() throws Exception
	{
		validateUsingStringWriter();
	}
	
	public void testImportWithNoBudgetDetailsElement() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		getProject().addExpenseAssignment(strategy, new DateUnitEffortList());
		validateUsingStringWriter();
	}
	
	public void testThreatReductionResultRelatedThreatImport() throws Exception
	{
		ORef threatRef = getProject().createThreat();
		ThreatReductionResult threatReductionResult = getProject().createThreatReductionResult();
		getProject().fillObjectUsingCommand(threatReductionResult, ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF, threatRef);
		validateUsingStringWriter();
	}
	
	public void testThreatReductionResultRelatedThreatId() throws Exception
	{
		ThreatReductionResult threatReductionResult = getProject().createThreatReductionResult();
		final ORef threatRef = getProject().createThreat();
		getProject().fillObjectUsingCommand(threatReductionResult, ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF, threatRef);
		Project filledProject = validateUsingStringWriter();
		ORefList allThreatReductionResultRefs = filledProject.getThreatReductionResultPool().getRefList();
		assertEquals("Threat reduction result was not imported?", 1, allThreatReductionResultRefs.size());
		ORef threatReductionResultRef = allThreatReductionResultRefs.getFirstElement();
		ThreatReductionResult importedThreatReductionResult = ThreatReductionResult.find(filledProject, threatReductionResultRef);
		assertEquals("incorrect related threat ref used?", threatRef, importedThreatReductionResult.getRelatedThreatRef());
	}
	
	public void testImportFilledProject() throws Exception
	{
		getProject().populateEverything();
		
		AbstractTarget target = getProject().createAndPopulateHumanWelfareTarget();
		Strategy strategy = getProject().createStrategy();
		Indicator indicator = getProject().createAndPopulateIndicator(strategy);
		getProject().addThresholdWithXmlEscapedData(indicator);
		Task task = getProject().createAndPopulateTask(indicator, "TASK");
		Goal goal = getProject().createAndPopulateGoal(target);
		getProject().addProgressReport(task);
		getProject().addProgressReport(indicator);
		getProject().addProgressReport(strategy);
		getProject().addProgressPercent(goal);
		getProject().addExpenseWithValue(strategy);
		getProject().addResourceAssignment(strategy);
		getProject().createandpopulateThreatReductionResult();
		
		PointList bendPointList = TestLinkBendPointsMoveHandler.createBendPointList();
		getProject().createLinkCellWithBendPoints(bendPointList);
		getProject().createAndPopulateIndicator(strategy);
		validateUsingStringWriter();
	}
	
	private ProjectForTesting validateUsingStringWriter() throws Exception
	{
		UnicodeXmlWriter projectWriter = createWriter(getProject());
		
		ProjectForTesting projectToImportInto = ProjectForTesting.createProjectWithoutDefaultObjects("ProjectToImportInto");
		Xmpz2XmlImporter xmlImporter = new Xmpz2XmlImporter(projectToImportInto, new NullProgressMeter());
		
		String exportedProjectXml = projectWriter.toString();
		StringInputStreamWithSeek stringInputputStream = new StringInputStreamWithSeek(exportedProjectXml);
		try
		{
			xmlImporter.importProject(stringInputputStream);
		}
		finally
		{
			stringInputputStream.close();	
		}
		
		UnicodeXmlWriter secondWriter = createWriter(projectToImportInto);
		assertEquals("Exports from projects do not match?", exportedProjectXml, secondWriter.toString());
		
		return projectToImportInto;
	}

	private UnicodeXmlWriter createWriter(ProjectForTesting projectToUse) throws Exception
	{
		Xmpz2XmlExporter exporter = new Xmpz2XmlExporter(projectToUse);
		UnicodeXmlWriter writer = UnicodeXmlWriter.create();
		exporter.exportProject(writer);
		writer.flush();
		
		return writer;
	}
}
