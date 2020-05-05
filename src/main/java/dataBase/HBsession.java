package dataBase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import tables.ArraysTable;
import tables.ExpTable;
import tables.FrameSettingTable;
import tables.OptionsDataTable;
import tables.SettingTable;
import tables.StatusTable;
import tables.TA35Data;
import tables.TA35_sum;

public class HBsession {

	static Session session = null;
	static SessionFactory factory = null;

	private HBsession() {
	}

	// Create the session
	public static SessionFactory getSessionInstance() {
		if (factory == null) {
			factory = new Configuration().configure( "hibernateRds.cfg.xml" ).addAnnotatedClass(SettingTable.class)
					.addAnnotatedClass(TA35_sum.class).addAnnotatedClass(ExpTable.class)
					.addAnnotatedClass(StatusTable.class).addAnnotatedClass(FrameSettingTable.class)
					.addAnnotatedClass(ArraysTable.class).addAnnotatedClass(OptionsDataTable.class)
					.addAnnotatedClass(TA35Data.class).buildSessionFactory();
		}
		return factory;
	}

	// Close connection
	public static void close_connection() {
		if (session != null) {
			session.close();
			factory.close();
		}
	}

}
