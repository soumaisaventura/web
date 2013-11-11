//package adventure.service;
//
//import java.util.HashSet;
//import java.util.Set;
//
//public class ValidationException extends javax.validation.ValidationException {
//
//	private static final long serialVersionUID = 1L;
//
//	private Set<Violation> violations = new HashSet<ValidationException.Violation>();
//
//	public void addViolation(String property, String message) {
//		violations.add(new Violation(property, message));
//	}
//
//	public Set<Violation> getConstraintViolations() {
//		return violations;
//	}
//
//	static final class Violation {
//
//		private String property;
//
//		private String message;
//
//		private Violation(String property, String message) {
//			this.property = property;
//			this.message = message;
//		}
//
//		public String getProperty() {
//			return property;
//		}
//
//		public void setProperty(String property) {
//			this.property = property;
//		}
//
//		public String getMessage() {
//			return message;
//		}
//
//		public void setMessage(String message) {
//			this.message = message;
//		}
//	}
//}
