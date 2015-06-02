package brian.soundpool.factory;

public class SoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5922867273478292980L;

	public SoundException() {
		super();
	}

	public SoundException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public SoundException(String detailMessage) {
		super(detailMessage);
	}

	public SoundException(Throwable throwable) {
		super(throwable);
	}

}
