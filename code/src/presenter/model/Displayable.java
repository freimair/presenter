package presenter.model;

public abstract class Displayable {
	protected Object content = null;

	public void setContent(Object content) {
		this.content = content;
	}

	public Object getContent() {
		return content;
	}

	public abstract Class<? extends Object> getContentType();
}
