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

package org.miradi.xml.wcs;

import java.io.ByteArrayOutputStream;
import java.util.Vector;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.martus.util.UnicodeStringWriter;
import org.martus.util.UnicodeWriter;
import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.martus.util.inputstreamwithseek.StringInputStreamWithSeek;
import org.miradi.exceptions.ValidationException;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Assignment;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.project.ProjectForTesting;
import org.miradi.project.TestSimpleThreatRatingFramework;
import org.miradi.project.TestStressBasedThreatRatingFramework;
import org.miradi.questions.ThreatRatingModeChoiceQuestion;
import org.miradi.utils.NullProgressMeter;
import org.miradi.xml.TestXmpzXmlImporter;
import org.miradi.xml.xmpz.XmpzXmlImporter;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class TestXmpzExporter extends TestCaseWithProject
{
	public TestXmpzExporter(String name)
	{
		super(name);
	}
	
	public void testValidateEmptyProject() throws Exception
	{
		validateProject();
	}
	
	public void testExtraDataNameSplitChar()
	{
		assertContains("Mismatch? ", ExtraDataExporter.TYPE_ID_TAG_SPLIT_TOKEN, ExtraDataExporter.TYPE_ID_TAG_SPLIT_TOKEN_FOR_REGULAR_EXPRESSION);
	}
	
	public void testIfWeDoBigSchemaChangesWeShouldIncludeMinorChangesToo() throws Exception
	{
		if("73".equals(XmpzXmlConstants.NAME_SPACE_VERSION))
			return;

		fail("If the schema version number changes, make sure we also do all the\n" +
				"pending small changes at the same time. Then update this test.");
	}
	
	public void testValidateFilledProject() throws Exception
	{
		getProject().populateEverything();
		DiagramFactor diagramFactor1 = getProject().createAndPopulateDiagramFactor();
		DiagramFactor diagramFactor2 = getProject().createAndPopulateDiagramFactor();
		getProject().tagDiagramFactor(diagramFactor2.getWrappedORef());
		getProject().createDiagramFactorLinkAndAddToDiagram(diagramFactor1, diagramFactor2);
		getProject().createResourceAssignment();
		validateProject();
	}
	
	public void testProjectWithStressBasedThreatRatingData() throws Exception
	{
		getProject().setMetadata(ProjectMetadata.TAG_THREAT_RATING_MODE, ThreatRatingModeChoiceQuestion.STRESS_BASED_CODE);
		DiagramFactor targetDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		DiagramFactor causeDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		getProject().enableAsThreat((Cause) causeDiagramFactor.getWrappedFactor());
		TestStressBasedThreatRatingFramework.createThreatFactorLink(getProject(), causeDiagramFactor, targetDiagramFactor);
		validateProject();
	}
	
	public void testProjectWithSimpleThreatRatingData() throws Exception
	{
		getProject().setMetadata(ProjectMetadata.TAG_THREAT_RATING_MODE, ThreatRatingModeChoiceQuestion.SIMPLE_BASED_CODE);
		
		DiagramFactor threatDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
		getProject().enableAsThreat(threatDiagramFactor.getWrappedORef());

		DiagramFactor targetDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		getProject().createFactorLink(threatDiagramFactor.getWrappedORef(), targetDiagramFactor.getWrappedORef());
		
		TestSimpleThreatRatingFramework.populateBundle(getProject().getSimpleThreatRatingFramework(), threatDiagramFactor.getWrappedId(), targetDiagramFactor.getWrappedId(), getProject().getSimpleThreatRatingFramework().getValueOptions()[0]);
		
		validateProject();
	}
	
	public void testExpenseTimePeriodCost() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		getProject().setProjectStartDate(2008);
		ExpenseAssignment expense = getProject().createAndPopulateExpenseAssignment();
		getProject().fillObjectUsingCommand(strategy, Strategy.TAG_EXPENSE_ASSIGNMENT_REFS, new ORefList(expense));
		XmpzXmlImporter xmlImporter = createProjectImporter(getProject());
		Node timePeriodCostsNode = getTimePeriodCostsNode(xmlImporter); 
		verifyNodeValue(xmlImporter, timePeriodCostsNode, XmpzXmlConstants.CALCULATED_START_DATE, "2008-01-01");
		verifyNodeValue(xmlImporter, timePeriodCostsNode, XmpzXmlConstants.CALCULATED_END_DATE, "2008-12-31");
		verifyNodeValue(xmlImporter, timePeriodCostsNode, XmpzXmlConstants.CALCULATED_TOTAL_BUDGET_COST, "10");
		verifyNodeValue(xmlImporter, timePeriodCostsNode, XmpzXmlConstants.CALCULATED_EXPENSE_TOTAL, "10");
		verifyNodeValue(xmlImporter, timePeriodCostsNode, XmpzXmlConstants.CALCULATED_WORK_UNITS_TOTAL, "");
		verifyExpensesEntriesNode(xmlImporter, timePeriodCostsNode);
		verifyExpenseEntryDateUnitNode(xmlImporter, timePeriodCostsNode);
		verifyExpenseCategoryValue(xmlImporter, expense, timePeriodCostsNode, ExpenseAssignment.TAG_FUNDING_SOURCE_REF, XmpzXmlConstants.FUNDING_SOURCE_ID);
		verifyExpenseCategoryValue(xmlImporter, expense, timePeriodCostsNode, ExpenseAssignment.TAG_ACCOUNTING_CODE_REF, XmpzXmlConstants.ACCOUNTING_CODE_ID);
		verifyExpenseCategoryValue(xmlImporter, expense, timePeriodCostsNode, ExpenseAssignment.TAG_CATEGORY_ONE_REF, XmpzXmlConstants.BUDGET_CATEGORY_ONE_ID);
		verifyExpenseCategoryValue(xmlImporter, expense, timePeriodCostsNode, ExpenseAssignment.TAG_CATEGORY_TWO_REF, XmpzXmlConstants.BUDGET_CATEGORY_TWO_ID);
	}
	
	public void testWorkUnitsTimePeriodCost() throws Exception
	{
		Strategy strategy  = getProject().createStrategy();
		getProject().setProjectStartDate(2007);
		getProject().setProjectEndDate(2008);
		ResourceAssignment assignment = getProject().createAndPopulateResourceAssignment();
		getProject().fillObjectUsingCommand(strategy, Strategy.TAG_RESOURCE_ASSIGNMENT_IDS, new IdList(assignment));
		XmpzXmlImporter xmlImporter = createProjectImporter(getProject());
		Node timePeriodCostsNode = getTimePeriodCostsNode(xmlImporter); 
		verifyNodeValue(xmlImporter, timePeriodCostsNode, XmpzXmlConstants.CALCULATED_START_DATE, "2007-01-01");
		verifyNodeValue(xmlImporter, timePeriodCostsNode, XmpzXmlConstants.CALCULATED_END_DATE, "2008-01-01");
		verifyNodeValue(xmlImporter, timePeriodCostsNode, XmpzXmlConstants.CALCULATED_TOTAL_BUDGET_COST, "110");
		verifyNodeValue(xmlImporter, timePeriodCostsNode, XmpzXmlConstants.CALCULATED_EXPENSE_TOTAL, "");
		verifyNodeValue(xmlImporter, timePeriodCostsNode, XmpzXmlConstants.CALCULATED_WORK_UNITS_TOTAL, "11");
		verifyWorkUnitEntriesNode(xmlImporter, timePeriodCostsNode);
		verifyWorkUnitEntryDateUnitNode(xmlImporter, timePeriodCostsNode);
		verifyWorkUnitCategoryValue(xmlImporter, assignment, timePeriodCostsNode, ResourceAssignment.TAG_FUNDING_SOURCE_ID, XmpzXmlConstants.FUNDING_SOURCE_ID);
		verifyWorkUnitCategoryValue(xmlImporter, assignment, timePeriodCostsNode, ResourceAssignment.TAG_ACCOUNTING_CODE_ID, XmpzXmlConstants.ACCOUNTING_CODE_ID);
		verifyWorkUnitCategoryValue(xmlImporter, assignment, timePeriodCostsNode, ResourceAssignment.TAG_CATEGORY_ONE_REF, XmpzXmlConstants.BUDGET_CATEGORY_ONE_ID);
		verifyWorkUnitCategoryValue(xmlImporter, assignment, timePeriodCostsNode, ResourceAssignment.TAG_CATEGORY_TWO_REF, XmpzXmlConstants.BUDGET_CATEGORY_TWO_ID);
	}
	
	public void testWorkUnitsTimePeriodCostWithoutProjectResourceUnitCost() throws Exception
	{
		Strategy strategy  = getProject().createStrategy();
		getProject().setProjectStartDate(2007);
		getProject().setProjectEndDate(2008);
		ResourceAssignment assignment = getProject().createAndPopulateResourceAssignment();
		ORef resourceRef = assignment.getResourceRef();
		getProject().fillObjectUsingCommand(resourceRef, ProjectResource.TAG_COST_PER_UNIT, "");
		getProject().fillObjectUsingCommand(strategy, Strategy.TAG_RESOURCE_ASSIGNMENT_IDS, new IdList(assignment));
		XmpzXmlImporter xmlImporter = createProjectImporter(getProject());
		Node timePeriodCostsNode = getTimePeriodCostsNode(xmlImporter); 
		verifyNodeValue(xmlImporter, timePeriodCostsNode, XmpzXmlConstants.CALCULATED_START_DATE, "2007-01-01");
		verifyNodeValue(xmlImporter, timePeriodCostsNode, XmpzXmlConstants.CALCULATED_END_DATE, "2008-01-01");
		verifyNodeValue(xmlImporter, timePeriodCostsNode, XmpzXmlConstants.CALCULATED_TOTAL_BUDGET_COST, "0");
		verifyNodeValue(xmlImporter, timePeriodCostsNode, XmpzXmlConstants.CALCULATED_EXPENSE_TOTAL, "");
		verifyNodeValue(xmlImporter, timePeriodCostsNode, XmpzXmlConstants.CALCULATED_WORK_UNITS_TOTAL, "11");
		verifyResourceIds(xmlImporter, timePeriodCostsNode, XmpzXmlConstants.CALCULATED_WHO, new ORefList(resourceRef));
		verifyWorkUnitEntriesNode(xmlImporter, timePeriodCostsNode);
		verifyWorkUnitEntryDateUnitNode(xmlImporter, timePeriodCostsNode);
		verifyWorkUnitCategoryValue(xmlImporter, assignment, timePeriodCostsNode, ResourceAssignment.TAG_FUNDING_SOURCE_ID, XmpzXmlConstants.FUNDING_SOURCE_ID);
		verifyWorkUnitCategoryValue(xmlImporter, assignment, timePeriodCostsNode, ResourceAssignment.TAG_ACCOUNTING_CODE_ID, XmpzXmlConstants.ACCOUNTING_CODE_ID);
		verifyWorkUnitCategoryValue(xmlImporter, assignment, timePeriodCostsNode, ResourceAssignment.TAG_CATEGORY_ONE_REF, XmpzXmlConstants.BUDGET_CATEGORY_ONE_ID);
		verifyWorkUnitCategoryValue(xmlImporter, assignment, timePeriodCostsNode, ResourceAssignment.TAG_CATEGORY_TWO_REF, XmpzXmlConstants.BUDGET_CATEGORY_TWO_ID);
	}
	
	private void verifyResourceIds(XmpzXmlImporter xmlImporter, Node timePeriodCostsNode, String calculatedWho, ORefList resourceRefs) throws Exception
	{
		XPathExpression expression = xmlImporter.getXPath().compile(xmlImporter.getPrefixedElement(XmpzXmlConstants.CALCULATED_WHO));
		Node whoNode = (Node) expression.evaluate(timePeriodCostsNode, XPathConstants.NODE);
		String elementNameToMatch = XmpzXmlConstants.RESOURCE_ID;

		Vector<Node> matchingNodes = getMatchingChildElementNodes(whoNode, elementNameToMatch);
		
		assertEquals("Wrong number of resources?", resourceRefs.size(), matchingNodes.size());
		for(int index = 0; index < matchingNodes.size(); ++index)
		{
			ORef nodeRef = new ORef(ProjectResource.getObjectType(), new BaseId(matchingNodes.get(index).getTextContent()));
			assertEquals("Wrong resource ref?", resourceRefs.get(index), nodeRef);
		}
	}

	private Vector<Node> getMatchingChildElementNodes(Node parentNode,	String elementNameToMatch)
	{
		NodeList resourceNodes = parentNode.getChildNodes();
		Vector<Node> matchingNodes = new Vector<Node>();
		for(int index = 0; index < resourceNodes.getLength(); ++index)
		{
			Node node = resourceNodes.item(index);
			String elementName = node.getLocalName();
			if(elementName != null && elementName.equals(elementNameToMatch))
				matchingNodes.add(node);
		}
		return matchingNodes;
	}

	private void verifyWorkUnitEntriesNode(XmpzXmlImporter xmlImporter, Node timePeriodCostsNode) throws Exception
	{
		String value = xmlImporter.getPathData(timePeriodCostsNode, new String[] {
				XmpzXmlConstants.CALCULATED_WORK_UNITS_ENTRIES, 
				XmpzXmlConstants.WORK_UNITS_ENTRY,  
				XmpzXmlConstants.WORK_UNITS_ENTRY + XmpzXmlConstants.DETAILS, 
				XmpzXmlConstants.DATE_UNIT_WORK_UNITS, 
				XmpzXmlConstants.WORK_UNITS, });
		
		assertEquals("Incorrect work units?", "11", value);		
	}
	
	private void verifyWorkUnitEntryDateUnitNode(XmpzXmlImporter xmlImporter, Node timePeriodCostsNode) throws Exception
	{
		Node dateUnitNode = xmlImporter.getNode(timePeriodCostsNode, new String[] {
				XmpzXmlConstants.CALCULATED_WORK_UNITS_ENTRIES, 
				XmpzXmlConstants.WORK_UNITS_ENTRY,  
				XmpzXmlConstants.WORK_UNITS_ENTRY + XmpzXmlConstants.DETAILS, 
				XmpzXmlConstants.DATE_UNIT_WORK_UNITS, 
				XmpzXmlConstants.WORK_UNITS_DATE_UNIT, 
				XmpzXmlConstants.WORK_UNITS_FULL_PROJECT_TIMESPAN});
		
		assertEquals("Incorrect work units?", "Total", xmlImporter.getAttributeValue(dateUnitNode, XmpzXmlConstants.FULL_PROJECT_TIMESPAN));		
	}

	private void verifyExpenseEntryDateUnitNode(XmpzXmlImporter xmlImporter, Node timePeriodCostsNode) throws Exception
	{
		Node dateUnitNode = xmlImporter.getNode(timePeriodCostsNode, new String[] {
				XmpzXmlConstants.CALCULATED_EXPENSE_ENTRIES, 
				XmpzXmlConstants.EXPENSE_ENTRY,  
				XmpzXmlConstants.EXPENSE_ENTRY + XmpzXmlConstants.DETAILS, 
				XmpzXmlConstants.DATE_UNITS_EXPENSE, 
				XmpzXmlConstants.EXPENSES_DATE_UNIT, 
				XmpzXmlConstants.EXPENSES_YEAR,
				});
		
		assertEquals("Incorrect work units?", "2008", xmlImporter.getAttributeValue(dateUnitNode, "StartYear"));
		assertEquals("Incorrect work units?", "1", xmlImporter.getAttributeValue(dateUnitNode, "StartMonth"));
		
	}
	
	private void verifyWorkUnitCategoryValue(XmpzXmlImporter xmlImporter, Assignment assignment, Node timePeriodCostsNode, final String categoryRefTag, final String categoryElementName) throws Exception
	{
		String value = xmlImporter.getPathData(timePeriodCostsNode, new String[] {
				XmpzXmlConstants.CALCULATED_WORK_UNITS_ENTRIES, 
				XmpzXmlConstants.WORK_UNITS_ENTRY,  
				XmpzXmlConstants.WORK_UNITS_ENTRY + categoryElementName,
				categoryElementName,
				});
		
		assertEquals("Incorrect category id?", assignment.getRef(categoryRefTag).getObjectId().toString(), value);
	}
	
	private void verifyExpenseCategoryValue(XmpzXmlImporter xmlImporter, Assignment assignment, Node timePeriodCostsNode, final String categoryRefTag, final String categoryElementName) throws Exception
	{
		String value = xmlImporter.getPathData(timePeriodCostsNode, new String[] {
				XmpzXmlConstants.CALCULATED_EXPENSE_ENTRIES, 
				XmpzXmlConstants.EXPENSE_ENTRY,  
				XmpzXmlConstants.EXPENSE_ENTRY + categoryElementName,
				categoryElementName,
				});
		
		assertEquals("Incorrect category id?", assignment.getRef(categoryRefTag).getObjectId().toString(), value);
	}
	
	private void verifyExpensesEntriesNode(XmpzXmlImporter xmlImporter, Node timePeriodCostsNode) throws Exception
	{
		String value = xmlImporter.getPathData(timePeriodCostsNode, new String[] {
				XmpzXmlConstants.CALCULATED_EXPENSE_ENTRIES, 
				XmpzXmlConstants.EXPENSE_ENTRY,  
				XmpzXmlConstants.EXPENSE_ENTRY + XmpzXmlConstants.DETAILS, 
				XmpzXmlConstants.DATE_UNITS_EXPENSE, 
				XmpzXmlConstants.EXPENSE, });
		
		assertEquals("Incorrect expense?", "10", value);		
	}

	private Node getTimePeriodCostsNode(XmpzXmlImporter xmlImporter)	throws Exception
	{
		String pathElements = xmlImporter.generatePath(new String[]
		                                                            {
			XmpzXmlConstants.CONSERVATION_PROJECT, 
			XmpzXmlConstants.STRATEGY + XmpzXmlConstants.POOL_ELEMENT_TAG, 
			XmpzXmlConstants.STRATEGY, 
			XmpzXmlConstants.STRATEGY + XmpzXmlConstants.TIME_PERIOD_COSTS, 
			XmpzXmlConstants.TIME_PERIOD_COSTS, 
		   });
		
		return xmlImporter.getNode(pathElements);
	}

	private void verifyNodeValue(final XmpzXmlImporter importer, final Node node, final String elementName, final String expectedValue) throws XPathExpressionException
	{
		assertEquals("Wrong node value for element " + elementName + "?", expectedValue, importer.getPathData(node, elementName));
	}
	
	private XmpzXmlImporter createProjectImporter(final ProjectForTesting projectToExport) throws Exception
	{
		UnicodeStringWriter writer = TestXmpzXmlImporter.createWriter(projectToExport);		
		ProjectForTesting projectToImportInto = ProjectForTesting.createProjectWithoutDefaultObjects("ProjectToImportInto");
		XmpzXmlImporter xmlImporter = new XmpzXmlImporter(projectToImportInto, new NullProgressMeter());
		StringInputStreamWithSeek stringInputputStream = new StringInputStreamWithSeek(writer.toString());
		xmlImporter.importProject(stringInputputStream);
		
		return xmlImporter;
	}

	private void validateProject() throws Exception
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		UnicodeWriter writer = new UnicodeWriter(bytes);
		new XmpzXmlExporter(getProject()).exportProject(writer);
		writer.close();
		String xml = new String(bytes.toByteArray(), "UTF-8");

		// NOTE: Uncomment for debugging only
//		File file = createTempFile();
//		file.createNewFile();
//		UnicodeWriter tempWriter = new UnicodeWriter(file);
//		tempWriter.writeln(xml);
//		tempWriter.close();
		
		InputStreamWithSeek inputStream = new StringInputStreamWithSeek(xml);
		if (!new WcsMiradiXmlValidator().isValid(inputStream))
		{
			throw new ValidationException(EAM.text("File to import does not validate."));
		}
	}
}
