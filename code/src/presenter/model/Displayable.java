package presenter.model;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public abstract class Displayable {
	protected Image image = null;

	protected void setImage(String path) {
		image = new Image(Display.getCurrent(), path);
	}

	public Image getContent() {
		return image;
	}

}
