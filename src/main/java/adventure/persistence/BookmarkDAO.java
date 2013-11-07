package adventure.persistence;

import java.util.List;

import adventure.domain.Bookmark;
import br.gov.frameworkdemoiselle.stereotype.PersistenceController;
import br.gov.frameworkdemoiselle.template.Crud;

@PersistenceController
// public class BookmarkDAO extends JPACrud<Bookmark, Long> {
public class BookmarkDAO implements Crud<Bookmark, Long> {

	private static final long serialVersionUID = 1L;

	@Override
	public void delete(Long arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Bookmark> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bookmark insert(Bookmark arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bookmark load(Long arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bookmark update(Bookmark arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
