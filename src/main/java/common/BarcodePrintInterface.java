package common;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;

final public class BarcodePrintInterface implements Printable {
    BufferedImage chartToPrint;
    
    BarcodePrintInterface(BufferedImage graph) {
         chartToPrint = graph;
    }
    
    public int print(Graphics graphicsObj, PageFormat pageForm, int pageNo) {
         if(pageNo > 0) {
              return NO_SUCH_PAGE;
         }

         else {
              Graphics2D obj2D = (Graphics2D)graphicsObj;
              obj2D.translate(pageForm.getImageableX(), pageForm.getImageableY());
              //print our BufferedImage to the printer page, with an identity transform
              obj2D.drawRenderedImage(this.chartToPrint, new AffineTransform());

              return PAGE_EXISTS;
         }
    }
}

