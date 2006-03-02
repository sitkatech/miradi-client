package org.conservationmeasures.eam.views.task;

import java.io.IOException;

import javax.swing.JLabel;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.util.UnicodeReader;

public class TaskView extends UmbrellaView
{
	public TaskView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new TaskToolBar(mainWindowToUse.getActions()));
		try
		{
			add(new TaskTable());
		}
		catch (IOException e)
		{
			EAM.logException(e);
		}
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "Task";
	}

}

class TaskTable extends JLabel
{
	public TaskTable() throws IOException
	{
		setVerticalAlignment(JLabel.NORTH);
		TableData data = new TableData();
		data.loadData(new UnicodeReader(getClass().getResourceAsStream("tasks.txt")));
		StringBuffer htmlTable = new StringBuffer();
		htmlTable.append("<table border='1'>");
		
		htmlTable.append("<th>");
		for(int col = 0; col < data.columnCount(); ++col)
		{
			htmlTable.append("<td>");
			htmlTable.append(data.getHeader(col));
			htmlTable.append("</td>");
		}
		htmlTable.append("</th>");

		for(int row=0; row < data.rowCount(); ++row)
		{
			htmlTable.append("<tr>");
			htmlTable.append("<td>" + (row+1) + "</td>");
			for(int col = 0; col < data.columnCount(); ++col)
			{
				htmlTable.append("<td>");
				htmlTable.append(data.getCellData(row, col));
				htmlTable.append("</td>");
			}
			htmlTable.append("</tr>");
		}
		htmlTable.append("</table>");
		setText("<html>" + htmlTable + "</html>");
	}
}