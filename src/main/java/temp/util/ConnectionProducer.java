//package adventure.util;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//
//import javax.enterprise.inject.Default;
//import javax.enterprise.inject.Produces;
//import javax.inject.Inject;
//import javax.persistence.EntityManager;
//
//import org.hibernate.Session;
//
//public class ConnectionProducer {
//
//	@Inject
//	private EntityManager em;
//
//	private Connection conn;
//
//	@Default
//	@Produces
//	public Connection create() {
//		Session hibernateSession = em.unwrap(Session.class);
//		hibernateSession.doWork(new org.hibernate.jdbc.Work() {
//
//			@Override
//			public void execute(Connection connection) throws SQLException {
//				conn = connection;
//			}
//		});
//
//		return conn;
//	}
//}
