import java.io.File;
import java.util.List;


public class Presentation {
	// ############ STATICS ############
	private static Presentation instance = null;

	private static Presentation getInstance() {
		if (null == instance)
			instance = new Presentation();
		return instance;
	}

	public static void load(File path) {

	}

	public static Slide next() {
		return get(++getInstance().index);
	}

	public static Slide getCurrent() {
		return get(getInstance().index);
	}

	public static Slide previous() {
		return get(--getInstance().index);
	}

	public static Slide get(int index) {
		return getInstance().slides.get(index);
	}

	// ########## NON-STATICS ##########

	private List<Slide> slides;
	private int index;

	public Presentation() {
		// TODO Auto-generated constructor stub
	}

}
