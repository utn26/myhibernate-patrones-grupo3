package myhibernate.functions;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import myhibernate.ann.Column;
import myhibernate.ann.Entity;
import myhibernate.ann.Id;
import myhibernate.ann.JoinColumn;
import myhibernate.ann.ManyToOne;
import myhibernate.ann.Table;
import myhibernate.demo.QueryBuilder;

public final class Utils
{
	public static <T> String generateQuery(Class<T> clazz, int id)
	{

		QueryBuilder query=buildQuery(new QueryBuilder(),clazz,97);
		String finalCondition = "and "+query.getFinalCondition()+"="+String.valueOf(id);

		String queryResult = query.getColumns();
		queryResult+=query.getNameFirstTable();
		queryResult+=query.getInnerJoins();
		queryResult+=finalCondition;
		System.out.println(queryResult);
		return queryResult;
	}

	public static <T> QueryBuilder buildQuery(QueryBuilder query, Class<T> clazz, int aliasCod)
	{
		String alias = String.valueOf((char) aliasCod);
		// annotations for classes
		Annotation[] annotations=clazz.getAnnotations();
		for(Annotation annotation:annotations)
		{
			if(annotation instanceof Entity)
			{

			}
			if(annotation instanceof Table)
			{
				Table myAnnotation=(Table)annotation;
				if(query.getNameFirstTable().equals(""))
				{
					query.setNameFirstTable("\nfrom "+myAnnotation.name()+" "+alias+"\n");
				}
				if(query.getInnerJoins().indexOf("XXNAMEXX")>0){
					query.setInnerJoins(query.getInnerJoins().replace("XXNAMEXX",myAnnotation.name()));
				}
				if(query.getInnerJoins().indexOf("XXALIASXX")>0){
					query.setInnerJoins(query.getInnerJoins().replace("XXALIASXX",alias));
				}
			}
		}

		// annotations for fields
		Field[] fields=clazz.getDeclaredFields();
		for(Field field:fields)
		{
			Annotation[] fieldAnnotations=field.getDeclaredAnnotations();
			for(Annotation annotation:fieldAnnotations)
			{
				if(annotation instanceof Id)
				{

				}
				if(annotation instanceof Column)
				{
					Column myAnnotation=(Column)annotation;
					if(query.getFinalCondition().equals(""))
					{
						query.setFinalCondition(alias+"."+myAnnotation.name());
					}
					if(query.getInnerJoins().indexOf("XXIDXX")>0){
						query.setInnerJoins(query.getInnerJoins().replace("XXIDXX",alias+"."+myAnnotation.name()));
					}
				}
				if(annotation instanceof Column)
				{
					Column myAnnotation=(Column)annotation;
					String split ="";
					if(!query.getColumns().equals("")){
						split = ",\n";
					}else{
						query.setColumns("select \n");
					}
					query.setColumns(query.getColumns()+split+alias+"."+myAnnotation.name());
					
				}
				if(annotation instanceof ManyToOne)
				{

				}
				if(annotation instanceof JoinColumn)
				{
					JoinColumn myAnnotation=(JoinColumn)annotation;
					String join="inner join XXNAMEXX XXALIASXX\n";
					join+="on "+alias+"."+myAnnotation.name()+"=XXIDXX\n";
					query.setInnerJoins(query.getInnerJoins()+join);
					query=buildQuery(query,field.getType(),aliasCod+1);

				}

			}
		}

		return query;
	}
}
