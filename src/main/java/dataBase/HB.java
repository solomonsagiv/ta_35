package dataBase;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import api.ApiObject;
import tables.ExpTable;
import tables.TA35Data;

public class HB {

	boolean run = true;

	ApiObject apiObject = ApiObject.getInstance();

	public static void main(String[] args) {
		ExpTable sum = (ExpTable) get_line_by_id(ExpTable.class, 1);
		System.out.println(sum.getBasket_down());
	}

	// Only save
	public static synchronized void save(Object object, SessionFactory factory) {
		// Save the line
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		session.save(object);
		session.getTransaction().commit();
		session.close();
	}
	
	// Only save
		public static synchronized void saveMultiLines(ArrayList<TA35Data> lines, SessionFactory factory) {
			// Save the line
			Session session = factory.getCurrentSession();
			session.beginTransaction();
			
			for (TA35Data line : lines) {
				session.save(line);
			}
			
			session.getTransaction().commit();
			session.close();
		}
	

	// Validate if not null and
	private double validate_double(JTextField field) {
		// If not null
		if (!field.getText().isEmpty()) {
			return Double.parseDouble(field.getText());
		}
		return 0.0;
	}

	// Validate if not null and
	private int validate_int(JTextField field) {
		// If not null
		if (!field.getText().isEmpty()) {
			return Integer.parseInt(field.getText());
		}
		return 0;
	}

	// Get all the table lines as list
	public static synchronized List<?> getTable(Class c) {
		// The line list
		List<?> lines = new ArrayList<>();

		// Get table from the database
		SessionFactory factory = HBsession.getSessionInstance().getSessionFactory();
		Session session = factory.getCurrentSession();
		session.beginTransaction();
		lines = session.createQuery("from " + c.getName()).list();
		session.getTransaction().commit();
		session.close();
		return lines;
	}

	// Trunticate
	public static synchronized void trunticate(String table) {
		Session session = HBsession.getSessionInstance().getCurrentSession();
		session.beginTransaction();
		session.createQuery("DELETE FROM " + table).executeUpdate();
		session.getTransaction().commit();
		session.close();
	}

	// Line from table by id
	public static synchronized Object get_line_by_id(Class c, int id) {
		Session session = HBsession.getSessionInstance().getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Object line = session.get(c.getName(), id);
		session.getTransaction().commit();
		session.close();
		return line;
	}

	// Line from table by id
	public static synchronized Object get_line_by_id(String entityName, int id) {
		Session session = HBsession.getSessionInstance().getSessionFactory().getCurrentSession();
		session.beginTransaction();
		Object line = session.get(entityName, id);
		session.getTransaction().commit();
		session.close();
		return line;
	}

	// Save
	public static synchronized void save(Object object) {
		Session session = HBsession.getSessionInstance().getCurrentSession();
		session.beginTransaction();
		session.save(object);
		session.getTransaction().commit();
		session.close();
	}

	// Update
	public static synchronized void update(Object object) {
		Session session = HBsession.getSessionInstance().getCurrentSession();
		session.beginTransaction();
		session.update(object);
		session.getTransaction().commit();
		session.close();
	}

	// Get
	public static synchronized Object get(Session session, String table_name, int id) {
		session.beginTransaction();
		Object object = session.get(table_name, id);
		session.getTransaction().commit();
		session.close();
		return object;
	}
}
