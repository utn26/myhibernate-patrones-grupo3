package myhibernate.model;

import java.util.ArrayList;
import java.util.List;

public class TableEntity
{
	private String tableName;
	private String primaryKey;
	private List<TableForeignEntity> foreignTables = new ArrayList<TableForeignEntity>();
	private List<ColumnEntity> columns = new ArrayList<ColumnEntity>();
	private String alias;
	public String getTableName()
	{
		return tableName;
	}
	public void setTableName(String tableName)
	{
		this.tableName=tableName;
	}
	public String getPrimaryKey()
	{
		return primaryKey;
	}
	public void setPrimaryKey(String primaryKey)
	{
		this.primaryKey=primaryKey;
	}
	public List<TableForeignEntity> getForeignTables()
	{
		return foreignTables;
	}
	public void setForeignTables(List<TableForeignEntity> foreignTables)
	{
		this.foreignTables=foreignTables;
	}
	public List<ColumnEntity> getColumns()
	{
		return columns;
	}
	public void setColumns(List<ColumnEntity> columns)
	{
		this.columns=columns;
	}
	public String getAlias()
	{
		return alias;
	}
	public void setAlias(String alias)
	{
		this.alias=alias;
	}
	
}
