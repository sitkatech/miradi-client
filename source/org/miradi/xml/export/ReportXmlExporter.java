/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.xml.export;

import java.io.File;
import java.io.IOException;

import org.martus.util.MultiCalendar;
import org.martus.util.UnicodeWriter;
import org.martus.util.xml.XmlUtilities;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objects.FactorLink;
import org.miradi.project.PlanningTreeXmlExporter;
import org.miradi.project.Project;


public class ReportXmlExporter
{
	public ReportXmlExporter(Project projectToUse, File destination) throws Exception
	{
		project = projectToUse;

		UnicodeWriter out = new UnicodeWriter(destination);
		try
		{
			exportProject(out);
		}
		finally
		{
			out.close();
		}
	}
	
	private void exportProject(UnicodeWriter out) throws IOException, Exception
	{
		out.writeln("<MiradiProject>");
		out.writeln("<FileName>" + XmlUtilities.getXmlEncoded(getProject().getFilename()) + "</FileName>");
		out.writeln("<ExportDate>" + new MultiCalendar().toIsoDateString() + "</ExportDate>");
		
		FactorLink.writeRating(getProject(), out, getProject().getProjectSummaryThreatRating(), "OverallProjectThreatRating");
		exportPools(out);
		new PlanningTreeXmlExporter(getProject()).toXmlPlanningTreeTables(out);
		out.writeln("</MiradiProject>");
	}

	private void exportPools(UnicodeWriter out) throws IOException, Exception
	{
		out.writeln("<ObjectPools>");
		for (int typeIndex = 1; typeIndex < ObjectType.OBJECT_TYPE_COUNT; ++typeIndex)
		{
			EAMObjectPool pool = getProject().getPool(typeIndex);
			exportPoolObjects(out, pool);

		}
		out.writeln("</ObjectPools>");
	}

	private void exportPoolObjects(UnicodeWriter out, EAMObjectPool pool) throws IOException, Exception
	{
		out.writeln("<Pool objectType='" + pool.getObjectType() + "'>");
		ORefList ids = pool.getSortedRefList();
		for(int i = 0; i < ids.size(); ++i)
		{
			pool.findObject(ids.get(i)).toXml(out);
		}
		out.writeln("</Pool>");
	}

	public static void main(String[] commandLineArguments)
	{	
		if (incorrectArgumentCount(commandLineArguments))
			throw new RuntimeException("Incorrect number of arguments");

		try
		{
			Project newProject = new Project();
			newProject.openProject(getProjectDirectory(commandLineArguments));
			new ReportXmlExporter(newProject, getXmlDestination(commandLineArguments));
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}	
	
	public static File getProjectDirectory(String[] commandLineArguments) throws Exception
	{
		return new File(EAM.getHomeDirectory(), commandLineArguments[0]);
	}
	
	public static File getXmlDestination(String[] commandLineArguments) throws Exception
	{
		return new File(commandLineArguments[1]);
	}

	public static boolean incorrectArgumentCount(String[] commandLineArguments)
	{
		return commandLineArguments.length != 2;
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project; 
}
