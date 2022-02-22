import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;

import javax.swing.JTable;
/**
 *
 * @author Muhammad Junaid Aslam
 * @version 1.0
 * @since 2016/09/25
 */
/*This Print support java class was implemented to get printout.
* This class was specially designed to print a Jtable content to a paper.
* Specially this class formated to print 7cm width paper.
* Generally for pos thermel printer.
* Free to customize this source code as you want.
* Illustration of basic invoice is in this code.
* demo by gayan liyanaarachchi
 
 */

public class PrintSupport {
 
	static JTable itemsTable;
	public static  int total_item_count=0;
             
	public PageFormat getPageFormat(PrinterJob pj){
        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();         
        paper = new Paper();
        paper.setSize(595, 842);	//For a4 paper
        paper.setImageableArea(0, 0, 595, 842);
            
        pf.setOrientation(PageFormat.PORTRAIT);           //select orientation portrait or landscape but for this time portrait
        pf.setPaper(paper);
            
        return pf;
	}
        
        
	protected static double convert_CM_To_PPI(double cm) {            
		return toPPI(cm * 0.393600787);            
	}
	
	protected static double toPPI(double inch) {            
		return inch * 72d;            
	}

}