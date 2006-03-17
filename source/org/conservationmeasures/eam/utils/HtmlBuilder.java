/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.awt.Dimension;


public class HtmlBuilder
{
	static public String font(String font, String text)
	{
		return "<div style='font-family: arial;'>" + text + "</div>";
	}
	
	static public String heading(String text)
	{
		return "<h2>" + text + "</h2>";
	}
	
	static public String horizontalLine()
	{
		return "<hr></hr>";
	}
	
	static public String button(String name, String text)
	{
		return "<input type='submit' name='" + name + "' value='" + text + "'></input>";
		//return "<input class='z' type='submit' name='z' value=" + text + ">";
	}
	
	static public String paragraph(String text)
	{
		return "<p>" + text + "</p>";
	}
	
	static public String bold(String text)
	{
		return "<strong>" + text + "</strong>";
	}
	
	static public String anchorTag(String target, String text)
	{
		return "<a href='" + target + "'>" + text + "</a>";
	}
	
	static public String image(String imageLocation, Dimension size)
	{
		return "<img src='" + imageLocation + "' width='" + size.width + "' height='" + size.height + "'></img>";
	}
	
	static public String table(String tableData)
	{
		return "<table>" + tableData + "</table>";
	}
	
	static public String tableRow(String cells)
	{
		return "<tr>" + cells + "</tr>";
	}
	
	static public String tableCell(String text)
	{
		return "<td align='left' valign='top'>" + text + "</td>";
	}
	
	static public String tableHeader(String text)
	{
		return "<th align='left'>" + text + "</th>";
	}
	
	static public String radioButton(String group, String text)
	{
		return "<input type='radio' name=" + group + ">" + text + "</input>";
	}
	
	static public String centered(String text)
	{
		return "<p align='center'>" + text + "</p>";
	}
	
	static public String indent(String text)
	{
		return "<table><tr><td width='25'></td><td>" + text + "</td><td width='25'></td></tr></table>";
	}
	
	static public String newline()
	{
		return "<br></br>";
	}
	
	static public String dropDown(String name, String[] choices)
	{
		String result =  "<select name='" + name + "'>";
		for(int i = 0; i < choices.length; ++i)
		{
			result += "<option>" + choices[i] + "</option>";
		}
		result += "</select>";
		return result;
	}
	
}