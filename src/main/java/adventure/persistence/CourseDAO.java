package adventure.persistence;

import adventure.entity.Course;
import br.gov.frameworkdemoiselle.template.JPACrud;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@Transactional
public class CourseDAO extends JPACrud<Course, Long> {

	private static final long serialVersionUID = 1L;
}
