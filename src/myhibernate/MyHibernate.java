package myhibernate;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import myhibernate.connect.ConnectorSQL;
import myhibernate.demo.Producto;
import myhibernate.functions.Utils;
import myhibernate.model.TableEntity;

public class MyHibernate
{
	public static <T> T find(Class<T> clazz, int id)
	{
		// PROGRAMAR AQUI
		
		Connection connection=ConnectorSQL.conectar();
		T ObjectResult = null;
		try
		{
			Statement statement=(Statement)connection.createStatement();
			
			
			// 1. generamis la query string
			// queryGenerator(Class<T> clazz, int id);
			TableEntity table = Utils.queryBuilder(clazz, 97);
			ResultSet resultset = statement.executeQuery(Utils.queryGenerator(clazz,id,table));
			
			//2.generamos el objeto con el resultado de la query
			ObjectResult = (T)Utils.loadObject(resultset, clazz, table);
			
		}
		catch(SQLException e)
		{
			throw new RuntimeException();
		}
		
		return ObjectResult;
	}

	public static <T> List<T> findAll(Class<T> clazz)
	{
		// PROGRAMAR AQUI
		return null;
	}

	public static Query createQuery(String hql)
	{
		// PROGRAMAR AQUI
		return null;
	}

}
