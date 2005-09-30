/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.jenkov.mrpersister.PersistenceManager;
import com.jenkov.mrpersister.itf.IGenericDao;

public class Database
{
	public Database() throws Exception
	{
		forceLoadJdbcDrivers();
		persistenceManager = new PersistenceManager();
	}
	
	public void openMemoryDatabase(String name) throws Exception
	{
		open("mem", name);
	}
	
	public void openDiskDatabase(File base) throws Exception
	{
		open("file", base.getAbsolutePath());
	}
	
	public void close() throws Exception
	{
		dao = null;
		connection.close();
		connection = null;
	}
	
	public boolean rawExecute(String sql) throws Exception
	{
		Statement statement = connection.createStatement();
		return statement.execute(sql);
	}
	
	public ResultSet rawSelect(String sqlSelect) throws Exception
	{
		Statement selectAll = connection.createStatement();
		selectAll.execute(sqlSelect);
		return selectAll.getResultSet();
	}
	
	public void insert(Object object) throws Exception
	{
		dao.insert(object);
	}
	
	public Object read(Object mapId, int key) throws Exception
	{
		return dao.readByPrimaryKey(SampleObject.class, new Integer(key));
	}
	
	public Object select(Object mapId, String sqlSelect) throws Exception
	{
		return dao.read(mapId, sqlSelect);
	}

	private void open(String databaseType, String fileNamePrefix) throws Exception
	{
		String database = "jdbc:hsqldb:" + databaseType + ":" + fileNamePrefix;
		connection = DriverManager.getConnection(database);
		dao = getPersistenceManager().getGenericDaoFactory().createDao(connection);
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
