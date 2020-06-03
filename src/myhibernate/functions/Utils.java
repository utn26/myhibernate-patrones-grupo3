package myhibernate.functions;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

import myhibernate.ann.Column;
import myhibernate.ann.Entity;
import myhibernate.ann.Id;
import myhibernate.ann.JoinColumn;
import myhibernate.ann.ManyToOne;
import myhibernate.ann.Table;
import myhibernate.model.ColumnEntity;
import myhibernate.model.TableEntity;
import myhibernate.model.TableForeignEntity;

public final class Utils
{

	public static <T> String queryGenerator(Class<T> clazz, int id, TableEntity table)
	{
		String queryResult="select \n";
		queryResult+=columsGenerator(table,true)+"\n";
		queryResult+="from "+table.getTableName()+" "+table.getAlias()+"\n";
		queryResult+=joinsGenerator(table,"");
		queryResult+="and "+table.getAlias()+"."+table.getPrimaryKey()+" = "+id+"\n";
		System.out.println(queryResult);
		return queryResult;

	}

	public static String joinsGenerator(TableEntity table, String idComparator)
	{
		String joinsString="";
		if(!idComparator.equals(""))
		{
			joinsString+="inner join "+table.getTableName()+" "+table.getAlias()+"\n";
			joinsString+="on "+idComparator+"= "+table.getAlias()+"."+table.getPrimaryKey()+"\n";
		}
		if(table.getForeignTables().size()>0)
		{
			for(TableForeignEntity t:table.getForeignTables())
			{
				joinsString+=joinsGenerator(t.getTableEntity(),table.getAlias()+"."+t.getJoinId());
			}
		}
		return joinsString;
	}

	public static String columsGenerator(TableEntity table, boolean isPrimeraVez)
	{
		String columns="";
		int i=0;

		for(ColumnEntity column:table.getColumns())
		{
			if(isPrimeraVez&&i==0)
			{
				columns+=table.getAlias()+"."+column.getNameColumn();
			}
			else
			{
				columns+=",\n"+table.getAlias()+"."+column.getNameColumn();
			}
			i++;
		}
		if(table.getForeignTables().size()>0)
		{
			for(TableForeignEntity t:table.getForeignTables())
			{
				columns+=columsGenerator(t.getTableEntity(),false);
			}
		}
		return columns;
	}

	public static <T> TableEntity queryBuilder(Class<T> clazz, int aliasCod)
	{

		TableEntity table=new TableEntity();

		table.setAlias(String.valueOf((char)aliasCod));

		Annotation[] annotations=clazz.getAnnotations();
		for(Annotation annotation:annotations)
		{
			if(annotation instanceof Entity)
			{
			}
			if(annotation instanceof Table)
			{
				Table myAnnotation=(Table)annotation;
				table.setTableName(myAnnotation.name());
			}
		}

		// annotations for fields
		Field[] fields=clazz.getDeclaredFields();
		for(Field field:fields)
		{
			Annotation[] fieldAnnotations=field.getDeclaredAnnotations();
			boolean isId=false;
			boolean isForeignKey=false;

			for(Annotation annotation:fieldAnnotations)
			{
				if(annotation instanceof Id)
				{
					isId=true;
				}
				if(annotation instanceof Column)
				{
					Column myAnnotation=(Column)annotation;
					ColumnEntity columnEntity=new ColumnEntity();
					if(isId)
					{
						table.setPrimaryKey(myAnnotation.name());
						isId=false;
					}
					columnEntity.setNameColumn(myAnnotation.name());
					columnEntity.setNameFieldColumn(field.getName());
					table.getColumns().add(columnEntity);

				}
				if(annotation instanceof ManyToOne)
				{
					isForeignKey=true;
				}
				if(annotation instanceof JoinColumn)
				{
					JoinColumn myAnnotation=(JoinColumn)annotation;
					if(isForeignKey)
					{
						TableForeignEntity tableForeignEntity=new TableForeignEntity();
						tableForeignEntity.setJoinId(myAnnotation.name());
						tableForeignEntity.setTableEntity(queryBuilder(field.getType(),aliasCod+1));
						table.getForeignTables().add(tableForeignEntity);
						isForeignKey=false;
						aliasCod+=1;
					}

				}

			}
		}

		return table;
	}

	public static <T> T loadObject(ResultSet resultset, Class<T> clazz, TableEntity table)
	{
		T objectMapped=null;
		try
		{
			objectMapped=clazz.newInstance();
		}
		catch(InstantiationException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		catch(IllegalAccessException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try
		{

			while(resultset.next())
			{
				//issue recorrer el resultset y mapear datos a el objectMapped
				for(ColumnEntity column:table.getColumns())
				{
//					String value=resultset.getString(column.getNameColumn());
//					invokeSetter(objectMapped,column.getNameFieldColumn(),value);
				}

			}

		}
		catch(SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return objectMapped;
	}

//	public static <T> T setObjectMapper(ResultSet resultset, Class<T> clazz, TableEntity table)
//	{
//		System.out.println("----------------");
//		return null;
//	}

	// issue lograr que invokeSetter setee lo valores con typos dinamicos
	public static void invokeSetter(Object obj, String propertyName, Object variableValue)
	{
		PropertyDescriptor pd;
		try
		{
			pd=new PropertyDescriptor(propertyName,obj.getClass());
			Method setter=pd.getWriteMethod();
			try
			{
				setter.invoke(obj,variableValue);
			}
			catch(IllegalAccessException|IllegalArgumentException|InvocationTargetException e)
			{
				e.printStackTrace();
			}
		}
		catch(IntrospectionException e)
		{
			e.printStackTrace();
		}

	}

}
