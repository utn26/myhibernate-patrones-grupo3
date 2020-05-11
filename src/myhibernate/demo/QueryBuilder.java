package myhibernate.demo;

public class QueryBuilder
{
	private String columns = "";
	private String nameFirstTable = "";
	private String innerJoins = "";
	private String finalCondition = "";
	
	public String getColumns()
	{
		return columns;
	}
	public void setColumns(String columns)
	{
		this.columns=columns;
	}
	public String getNameFirstTable()
	{
		return nameFirstTable;
	}
	public void setNameFirstTable(String nameFirstTable)
	{
		this.nameFirstTable=nameFirstTable;
	}
	public String getInnerJoins()
	{
		return innerJoins;
	}
	public void setInnerJoins(String innerJoins)
	{
		this.innerJoins=innerJoins;
	}
	public String getFinalCondition()
	{
		return finalCondition;
	}
	public void setFinalCondition(String finalCondition)
	{
		this.finalCondition=finalCondition;
	}
	
}
