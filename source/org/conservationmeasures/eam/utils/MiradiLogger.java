/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.io.PrintStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.conservationmeasures.eam.main.EAM;

public class MiradiLogger
{
	public MiradiLogger()
	{
		log = Logger.getLogger("org.miradi");
		log.setUseParentHandlers(false);
		consoleLogger = new ConsoleHandler();
		setLogLevel(LOG_NORMAL);
		setLogToConsole();
	}
	
	public void setExceptionLoggingDestination(PrintStream destination)
	{
		exceptionDestination = destination;
	}
	
	public PrintStream getExceptionLoggingDestination()
	{
		return exceptionDestination;
	}
	
	public void setLogToString()
	{
		removeAllHandlers();
		log.addHandler(new StringHandler());
	}
	
	public void setLogToConsole()
	{
		removeAllHandlers();
		log.addHandler(consoleLogger);
	}
	
	public String getLoggedString()
	{
		StringHandler handler = (StringHandler)log.getHandlers()[0];
		return handler.getLoggedString();
	}
	
	class StringHandler extends Handler
	{
		public StringHandler()
		{
			buffer = new StringBuffer();
		}
		
		public void close() throws SecurityException
		{
		}

		public void flush()
		{
		}

		public void publish(LogRecord record)
		{
			buffer.append(record.getLevel().getName());
			buffer.append(": ");
			buffer.append(record.getMessage());
			buffer.append(EAM.NEWLINE);
		}
		
		public String getLoggedString()
		{
			return buffer.toString();
		}
		
		StringBuffer buffer;
	}
	
	public void setLogLevel(int logLevel)
	{
		Level javaLogLevel = levels[logLevel];
		log.setLevel(javaLogLevel);
		consoleLogger.setLevel(javaLogLevel);
	}
	
	public void logException(Exception e)
	{
		log.log(Level.SEVERE, "Exception", e);
		if(exceptionDestination != null)
		{
			e.printStackTrace(exceptionDestination);
			exceptionDestination.flush();
		}
	}
	
	public void logError(String text)
	{
		log.log(Level.SEVERE, text);
	}
	
	public void logWarning(String text)
	{
		log.log(Level.WARNING, text);
	}
	
	public void logDebug(String text)
	{
		log.log(Level.INFO, text);
	}
	
	public void logVerbose(String text)
	{
		log.log(Level.FINE, text);
	}
	
	private void removeAllHandlers()
	{
		Handler[] handlers = log.getHandlers();
		for(int i = 0; i < handlers.length; ++i)
			log.removeHandler(handlers[i]);
	}
	
	private Logger log;
	private Handler consoleLogger;
	private PrintStream exceptionDestination;
	
	private static final Level[] levels = {Level.SEVERE, Level.WARNING, Level.INFO, Level.FINE, };
	public static final int LOG_QUIET = 0;
	public static final int LOG_NORMAL = 1;
	public static final int LOG_DEBUG = 2;
	public static final int LOG_VERBOSE = 3;

}
