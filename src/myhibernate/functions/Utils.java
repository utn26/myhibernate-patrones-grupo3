package myhibernate.functions;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import myhibernate.ann.Column;
import myhibernate.ann.Entity;
import myhibernate.ann.Id;
import myhibernate.ann.JoinColumn;
import myhibernate.ann.ManyToOne;
import myhibernate.ann.Table;

import myhibernate.model.TableEntity;
import myhibernate.model.TableForeignEntity;

public final class Utils
{

	public static <T> String queryGenerator(Class<T> clazz, int id)
	{

		TableEntity table = queryBuilder(clazz, 97);
		System.out.println("");
		String queryResult = "select \n";
		queryResult += columsGenerator(table, true) + "\n";
		queryResult += "from " + table.getTableName() + " " + table.getAlias() + "\n";
		queryResult += joinsGenerator(table, "");
		queryResult += "and " + table.getAlias() + "." + table.getPrimaryKey() + " = " + id + "\n";	
		System.out.println(queryResult);
		return null;	
		
	}
	public static String joinsGenerator(TableEntity table, String idComparator){
		String joinsString = "";
		if(!idComparator.equals("")){
			joinsString += "left join " + table.getTableName() + " " + table.getAlias() + "\n";
			joinsString += "on " + idComparator + "= " + table.getAlias() + "." + table.getPrimaryKey() + "\n";
		}
		if(table.getForeignTables().size() > 0){
			for(TableForeignEntity t : table.getForeignTables()){
				joinsString += joinsGenerator(t.getTableEntity(),table.getAlias()+"."+t.getJoinId());
			}		
		}
		return joinsString;
	}
	public static String columsGenerator(TableEntity table, boolean isPrimeraVez){
		String columns = "";
		int i = 0;
		
		for(String column: table.getColumns()){
			if(isPrimeraVez && i == 0){
				columns += table.getAlias() + "." + column;
			}else{
				columns+=",\n" +  table.getAlias() + "." + column;
			}
			i++;
		}
		if(table.getForeignTables().size() > 0){
			for(TableForeignEntity t : table.getForeignTables()){
				columns+= columsGenerator(t.getTableEntity(),false);
			}			
		}
		return columns;
	}
	public static <T> TableEntity queryBuilder(Class<T> clazz, int aliasCod){
		
		
		TableEntity table = new TableEntity();
		
		table.setAlias(String.valueOf((char) aliasCod));
		
		Annotation[] annotations = clazz.getAnnotations();
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
			boolean isId = false;
			boolean isForeignKey = false;

			for(Annotation annotation:fieldAnnotations)
			{				
				if(annotation instanceof Id)
				{
					isId = true;
				}
				if(annotation instanceof Column)
				{
					Column myAnnotation=(Column)annotation;
					if(isId){
						table.setPrimaryKey(myAnnotation.name());
						isId = false;
					}
					table.getColumns().add(myAnnotation.name());
				}
				if(annotation instanceof ManyToOne)
				{
					isForeignKey = true;
				}
				if(annotation instanceof JoinColumn)
				{
					JoinColumn myAnnotation=(JoinColumn)annotation;
					if(isForeignKey){
						TableForeignEntity tableForeignEntity = new TableForeignEntity();
						tableForeignEntity.setJoinId(myAnnotation.name());
						tableForeignEntity.setTableEntity(queryBuilder(field.getType(), aliasCod+1));
						table.getForeignTables().add(tableForeignEntity);
						isForeignKey = false;
						aliasCod += 1; 
					}
				}

			}
		}
		
		return table;		
	}
	
}
