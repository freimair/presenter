import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;


public class main {

	
  public static void main(String[] args) throws InterruptedException {
	  
	  Display display = new Display();
	  final PresenterWindow myDialog = new PresenterWindow();
	  myDialog.open();
	  
	  PresenterControl myControl = new PresenterControl(myDialog);
	  myControl.open();
  }
}