/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.conservationmeasures.eam.main.EAM;

import com.jenkov.mrpersister.PersistenceManager;
import com.jenkov.mrpersister.itf.IGenericDao;

public class DatabaseWrapper
{
	public DatabaseWrapper() throws IOException
	{
		try
		{
			forceLoadJdbcDrivers();
		}
		catch (ClassNotFoundException e)
		{
			EAM.logException(e);
			throw new IOException(e.getMessage());
		}
		persistenceManager = new PersistenceManager();
	}
	
	public void openMemoryDatabase(String name) throws IOException
	{
		open("mem", name);
	}
	
	public void openDiskDatabase(File base) throws IOException
	{
		open("file", base.getAbsolutePath());
	}
	
	public void flush() throws IOException
	{
		try
		{
			rawExecute("CHECKPOINT");
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new IOException(e.getMessage());
		}
	}
	
	public void close() throws IOException
	{
		if(connection == null)
			return;
		
		dao = null;
		try
		{
			rawExecute("SHUTDOWN");
			connection.close();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new IOException(e.getMessage());
		}
		connection = null;
	}
	
	public boolean isOpen()
	{
		return (connection != null);
	}
	
	public boolean rawExecute(String sql) throws IOException
	{
		try
		{
			Statement statement = connection.createStatement();
			return statement.execute(sql);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new IOException(e.getMessage());
		}
	}
	
	public ResultSet rawSelect(String sqlSelect) throws IOException
	{
		try
		{
			Statement selectAll = connection.createStatement();
			selectAll.execute(sqlSelect);
			return selectAll.getResultSet();
		}
		catch (Exception e)
		{
			throw new IOException(e.getMessage());
		}
	}
	
	public void insert(Object object) throws IOException
	{
		try
		{
			dao.insert(object);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new IOException(e.getMessage());
		}
	}
	
	public Object read(Object mapId, int key) throws Exception
	{
		return dao.readByPrimaryKey(mapId, new Integer(key));
	}
	
	public Object select(Object mapId, String sqlSelect) throws Exception
	{
		return dao.read(mapId, sqlSelect);
	}

	private void open(String databaseType, String fileNamePrefix) throws IOException
	{
		try
		{
			String database = "jdbc:hsqldb:" + databaseType + ":" + fileNamePrefix;
			connection = DriverManager.getConnection(database);
			dao = getPersistenceManager().getGenericDaoFactory().createDao(connection);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new IOException(e.getMessage());
		}
	}
	
	private PersistenceManager getPersistenceManager()
	{
		return persistenceManager;
	}
	
	private void forceLoadJdbcDrivers() throws ClassNotFoundException
	{
		// simply referring to a class causes it to be loaded/initialized
		Class.forName("org.hsqldb.jdbcDriver");
	}
	
	private PersistenceManager persistenceManager = new PersistenceManager();
	private Connection connection;
	private IGenericDao dao;
}
