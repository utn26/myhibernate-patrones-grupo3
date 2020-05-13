package myhibernate.model;

public class TableForeignEntity
{
	private String joinId;
	private TableEntity tableEntity;
	
	public String getJoinId()
	{
		return joinId;
	}
	public void setJoinId(String joinId)
	{
		this.joinId=joinId;
	}
	public TableEntity getTableEntity()
	{
		return tableEntity;
	}
	public void setTableEntity(TableEntity tableEntity)
	{
		this.tableEntity=tableEntity;
	}
}
