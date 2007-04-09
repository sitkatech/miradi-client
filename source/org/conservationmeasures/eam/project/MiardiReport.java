package org.conservationmeasures.eam.project;

import java.util.HashMap;
import java.util.Iterator;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRRtfExporter;

import org.conservationmeasures.eam.objects.ProjectMetadata;

public class MiardiReport
{
	public MiardiReport(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public void getReport()
	{
		try
		{
			String type = "PDF";
			JasperPrint print = JasperFillManager.fillReport("D:/Projects/workspace/miradi/source/JasperReports/MiradisReport.jasper",new HashMap(),new MardisDataSource());
			if (type.equals("PDF"))
			{
				JasperExportManager.exportReportToPdfFile(print,"C:/MardisReport.pdf");
			}
			else if (type.equals("RTF"))
			{
				JRRtfExporter exporter = new JRRtfExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "C:/MardisReport.rtf");		    		   
				exporter.exportReport();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();	
		}
	}
	
	public class MardisDataSource implements JRDataSource
	{
		public Object getFieldValue(JRField field) throws JRException
		{
			System.out.println(field.getName());
			return ((ProjectMetadata)data).getData(field.getName());
		}


		public boolean next() throws JRException 
		{
			if (iterator.hasNext()) 
			{
				data = iterator.next();
				return true;
			}

			return false;
		}

		MardisProjectData iterator = new MardisProjectData();

		Object data;
	} 
	

	public class MardisProjectData implements Iterator
	{
		public boolean hasNext() 
		{
			--count;
			return (count!=0);
			
		}

		public Object next() 
		{
			return project.getMetadata();
		}

		public void remove() 
		{
		}
	}
	
	static int count = 2;
	Project project;
}
