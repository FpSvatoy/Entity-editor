package exception;

public class DuplicateEntryException extends EntityListIntegrityException {

	public DuplicateEntryException() {
		super();
	}
	
	public DuplicateEntryException(String message) {
		super(message);
	}
	
}
