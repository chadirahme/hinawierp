package common;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
 
/**
 * This class demonstrates how to load an Image from an external file
 */
public class BarcodePreview extends Component {
           
    BufferedImage img;
    private int zoom = 0;
    private static final double ZOOM_AMOUNT_X = 1.08,ZOOM_AMOUNT_Y = 1.05;
    
 
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, getWidth(), getHeight(),null);
    }
 
    public BarcodePreview(BufferedImage previewImage) {
    	img = previewImage;
    	
//    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    	
    	sizeToZoom();

        addMouseWheelListener(new MouseWheelListener() {
          public void mouseWheelMoved(MouseWheelEvent e) {
            int steps = e.getWheelRotation();
            zoom += steps;
            sizeToZoom();
          }
        });
    }
 
    public Dimension getPreferredSize() {
        if (img == null) {
             return new Dimension(100,100);
        } else {
           return new Dimension(img.getWidth(null), img.getHeight(null));
       }
    }
 
    
    public void showPreviewImage(BufferedImage previewImage) {
 
        JFrame f = new JFrame("Printing Preview");
             
//        f.addWindowListener(new WindowAdapter(){
//                public void windowClosing(WindowEvent e) {
//                    System.exit(0);
//                }
//            });
 
        f.add(new BarcodePreview(previewImage));
        f.pack();
        f.setVisible(true);
    }
    
    
    private void sizeToZoom() {
    	
    	double factorX = Math.pow(ZOOM_AMOUNT_X, zoom);
    	double factorY = Math.pow(ZOOM_AMOUNT_Y, zoom);
    	//setSize( (img.getWidth() * factorX),  (img.getHeight() * factorY));
    	
    }
    
   
}