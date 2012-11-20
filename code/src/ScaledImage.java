import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;


public class ScaledImage {
    private Image image;
    
    public ScaledImage(Image image, int boundingwidth, int boundingheight) {        
    	int width = image.getBounds().width, height = image.getBounds().height;
    	
    	//aspect ratio
    	double ratio = (double)(width)/height;
    	
    	// breite einpassen
    	width = boundingwidth;
    	height = (int)(width/ratio);
    	
    	// höhe einpassen
    	if(boundingheight < height) {
    		height = boundingheight;
    		width = (int)(ratio*height);
    	}
    	
    	this.image = new Image(Display.getCurrent(), image.getImageData().scaledTo(width, height));
    }
    
    public int centerX(int width) {
    	return (int)((width - image.getBounds().width)/2.0);
    }
    
    public int centerY(int height) {
    	return (int)((height - image.getBounds().height)/2.0);
    }
    
    public Image getImage() {
    	return image;
    }
}
