import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;


public class EditBoundingBoxDialog extends Dialog {

	private PresenterWindow parent;
	private Slider xPosition;
	private Slider xWidth;
	private Slider yPosition;
	private Slider yHeight;

	protected EditBoundingBoxDialog(Shell parentShell, PresenterWindow parent) {
		super(parentShell);
		this.parent = parent;
	}
	
	protected Control createDialogArea(Composite parent) {
		// set title
		getShell().setText("Edit Boundingbox");
		
		// extend
		Composite composite = (Composite) super.createDialogArea(parent);
		
		// new controls
		composite.setLayout(new GridLayout(2, false));

		Label labelXPosition = new Label(composite, SWT.NONE);
		labelXPosition.setText("horizontal Position [%]");
		xPosition = new Slider(composite, SWT.NONE);
		xPosition.setMinimum(0);
		xPosition.setMaximum(110);
		xPosition.setSelection(this.parent.getXOffset());
		xPosition.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label labelXWidth = new Label(composite, SWT.NONE);
		labelXWidth.setText("Width [%]");
		xWidth = new Slider(composite, SWT.NONE);
		xWidth.setMinimum(0);
		xWidth.setMaximum(110);
		xWidth.setSelection(this.parent.getWidth());
		xWidth.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label labelYPosition = new Label(composite, SWT.NONE);
		labelYPosition.setText("vertical Position [%]");
		yPosition = new Slider(composite, SWT.NONE);
		yPosition.setMinimum(0);
		yPosition.setMaximum(110);
		yPosition.setSelection(this.parent.getYOffset());
		yPosition.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		Label labelYHeight = new Label(composite, SWT.NONE);
		labelYHeight.setText("Height [%]");
		yHeight = new Slider(composite, SWT.NONE);
		yHeight.setMinimum(0);
		yHeight.setMaximum(110);
		yHeight.setSelection(this.parent.getHeight());
		yHeight.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		return composite;
	}
	
	protected void okPressed() {
		parent.setLayout(xPosition.getSelection(), yPosition.getSelection(), xWidth.getSelection(), yHeight.getSelection());
		super.okPressed();
	}

}
