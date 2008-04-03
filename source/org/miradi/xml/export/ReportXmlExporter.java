/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.xml.export;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.martus.util.MultiCalendar;
import org.martus.util.UnicodeWriter;
import org.martus.util.xml.XmlUtilities;
import org.miradi.database.ProjectServer;
import org.miradi.main.EAM;
import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objects.BaseObject;
import org.miradi.objects.FactorLink;
import org.miradi.project.PlanningTreeXmlExporter;
import org.miradi.project.Project;


public class ReportXmlExporter
{
	public ReportXmlExporter(Project projectToUse) throws Exception
	{
		project = projectToUse;
	}

	public void export(File destination) throws Exception
	{
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
			if (pool != null)
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
			BaseObject foundObject = pool.findObject(ids.get(i));
			writeBaseObjectAsXml(out, foundObject);
		}
		out.writeln("</Pool>");
	}

	private void writeBaseObjectAsXml(UnicodeWriter out, BaseObject foundObject) throws IOException, Exception
	{
		out.write("<" + foundObject.getTypeName() + " ref='");
		foundObject.getRef().toXml(out);
		out.writeln("'>");
		Set fieldTags = foundObject.getFieldTagsToIncludeInXml();
		Iterator iter = fieldTags.iterator();
		while(iter.hasNext())
		{
			String tag = (String)iter.next();
			ObjectData data = foundObject.getField(tag);
			data.toXml(out);
		}
		foundObject.writeNonFieldXml(out);
		out.writeln("</" + foundObject.getTypeName() + ">");
	}

	public static void main(String[] commandLineArguments) throws Exception
	{	
		if (incorrectArgumentCount(commandLineArguments))
			throw new RuntimeException("Incorrect number of arguments " + commandLineArguments.length);

		Project newProject = new Project();
		try
		{
			File projectDirectory = getProjectDirectory(commandLineArguments);
			if(!ProjectServer.isExistingProject(projectDirectory))
				throw new RuntimeException("Project does not exist:" + projectDirectory);

			newProject.createOrOpen(projectDirectory);

			new ReportXmlExporter(newProject).export(getXmlDestination(commandLineArguments));
			System.out.println("Export complete");
		}
		finally
		{
			newProject.close();
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
