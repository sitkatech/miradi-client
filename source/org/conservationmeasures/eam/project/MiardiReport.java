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

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectMetadata;

public class MiardiReport
{
	public MiardiReport(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public void getPDFReport(String reportFile, String fileOut)
	{
		try
		{
			HashMap parameters = new HashMap();
			//parameters.put("MyDatasource", new MiradiDataSource());
			JasperPrint print = JasperFillManager.fillReport(reportFile, parameters, new MiradiDataSource());
			JasperExportManager.exportReportToPdfFile(print,fileOut);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public void getRTFReport(String reportFile, String fileOut)
	{
		try
		{
			HashMap parameters = new HashMap();
			//parameters.put("MyDatasource", new MiradiDataSource());
			JasperPrint print = JasperFillManager.fillReport(reportFile ,parameters, new MiradiDataSource());
			JRRtfExporter exporter = new JRRtfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, fileOut);		    		   
			exporter.exportReport();
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public class MiradiDataSource implements JRDataSource
	{
		public Object getFieldValue(JRField field) throws JRException
		{
			System.out.println(field.getName() + "==" + ((ProjectMetadata)data).getData(field.getName()));
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

		MiradiProjectData iterator = new MiradiProjectData();

		Object data;
	} 
	

	public class MiradiProjectData implements Iterator
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
