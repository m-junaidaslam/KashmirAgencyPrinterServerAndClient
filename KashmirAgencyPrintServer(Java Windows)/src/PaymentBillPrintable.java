import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PaymentBillPrintable implements Printable {
	
	//public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss a";
	PaymentBill paymentBill;
	Font sizedAlviFont;
	
	public PaymentBillPrintable(PaymentBill paymentBill) {
		// TODO Auto-generated constructor stub
		this.paymentBill = paymentBill;
		java.io.InputStream is = PaymentBillPrintable.class.getResourceAsStream("nafeesweb.ttf");
		try {
			Font alviFont = Font.createFont(Font.TRUETYPE_FONT, is);
			sizedAlviFont = alviFont.deriveFont(12f);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public int print(Graphics invoice, PageFormat pageFormat, 
	                int pageIndex) throws PrinterException {    
		int result = NO_SUCH_PAGE;
		
		//System.out.println("Page Index: " + pageIndex);
	    if (pageIndex == 0) {                    
	    	Graphics2D invoice2D = (Graphics2D) invoice;
	    	
	    	invoice2D.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
	    	int x = 10;
	    	int y = 20;
	    	
	        //TOP LEFT
	        //Invoice writing at the top left
	        invoice.setColor(Color.BLACK);
	        invoice2D.setFont( new Font("Times New Roman", Font.BOLD, 12));
	        invoice2D.drawString("List Id: ", x, y);
	        x = x + 130;
	        invoice2D.drawString(paymentBill.getId() + "", x, y);
	        
	        //Write date at top right
	        x = x + 230;
	        invoice2D.drawString("Date: ", x, y);
	        x = x + 50;
	        invoice2D.drawString(paymentBill.getDate(), x, y);
	        
	        //Blue "bill to" writing
	        y = y + 15;
	        x = 10;
	        invoice2D.drawString("Customer Name: " , x, y);
	        //Receiver's surname and comma under the "bill to" writing
	        invoice2D.setFont(sizedAlviFont);
	        x = x + 130;
	        invoice2D.drawString(paymentBill.getCustomerName(), x, y);
	        //Receiver's name under the "bill to" writing
	        x = 10;
	        invoice2D.setFont( new Font("Times New Roman", Font.BOLD, 12));
	        y = y + 15;
	        invoice2D.drawString("Company: ", x, y);
	        x = x + 130;
	        invoice2D.setFont(sizedAlviFont);
	        invoice2D.drawString(paymentBill.getCompanyName() , x, y);
	        invoice2D.setFont( new Font("Times New Roman", Font.BOLD, 12));
	        //TABLE FORMAT
	        //FIRST COLUMN
	        //Four blue & empty rectangles for main titles
	        x = 10;
	        y = y + 10;
	        Rectangle serial = new Rectangle(x, y, 50, 17);
	        x = x + 50;
	        Rectangle date = new Rectangle(x, y, 170, 17);
	        x = x + 170;
	        Rectangle due = new Rectangle(x, y, 110, 17);
	        x = x + 110;
	        Rectangle done = new Rectangle(x, y, 110, 17);
	        x = x + 110;
	        Rectangle remaining = new Rectangle(x, y, 110, 17);
	        invoice2D.draw(serial);
	        invoice2D.draw(date);
	        invoice2D.draw(due);
	        invoice2D.draw(done);
	        invoice2D.draw(remaining);
	        //Description title
	        invoice.setColor(Color.BLACK);
	        invoice2D.setFont( new Font("Times New Roman", Font.PLAIN, 14));
	        x = 15;
	        y = y + 14;
	        invoice2D.drawString("No." , x, y);
	        //Unit Price title
	        x = x + 50;
	        invoice2D.drawString("Payment Date" , x, y);
	        x = x + 170;
	        invoice2D.drawString("Due Amount", x, y);
	        x = x + 110;
	        invoice2D.drawString("Paid Amount", x, y);
	        x = x + 110;
	        invoice2D.drawString("Remaining Amout", x, y);
	        
	        y = y + 4;
	        
	        invoice.setColor(Color.BLACK);
	        invoice2D.setFont( new Font("Times New Roman", Font.PLAIN, 12));
	        
	        int paymentsLength = paymentBill.getPayments().length;
	        
	        for(int i = 0; i < paymentsLength ; i++) {
	        	if(!paymentBill.getPayment(i).getDate().matches("")) {
		        	x = 10;
		        	serial.setBounds(x, y, 50, 15);
			        x = x + 50;
			        date.setBounds(x, y, 170, 15);
			        x = x + 170;
			        due.setBounds(x, y, 110, 15);
			        x = x + 110;
			        done.setBounds(x, y, 110, 15);
			        x = x + 110;
			        remaining.setBounds(x, y, 110, 15);
			        invoice2D.draw(serial);
			        invoice2D.draw(date);
			        invoice2D.draw(due);
			        invoice2D.draw(done);
			        invoice2D.draw(remaining);
			        x = 15;
			        y = y + 11;
			        invoice2D.drawString(paymentBill.getPayment(i).getId() + "" , x, y);
			        //Unit Price title
			        x = x + 50;
			        invoice2D.drawString(paymentBill.getPayment(i).getDate() , x, y);
			        x = x + 170;
			        invoice2D.drawString(paymentBill.getPayment(i).getDueAmount() + "", x, y);
			        x = x + 110;
			        invoice2D.drawString(paymentBill.getPayment(i).getDoneAmount() + "", x, y);
			        x = x + 110;
			        invoice2D.drawString(paymentBill.getPayment(i).getRemainingAmount() + "", x, y);
			        y = y + 4;
	        	}
	        } 
	        
	        x = 5;
	        y = y + 10;
	        invoice2D.drawLine(x, y, x + 560, y + 1);
	        
	        result = PAGE_EXISTS;
	    }
	    //System.out.println(result+"");
		return result;
  	}
	
	
/*----------------------------Function to get current Date and Time-------------------*/
	public static String now() {
		//get current date and time as a String output   
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return sdf.format(cal.getTime());

	}
 
}
         
/*
 ################# THIS IS HOW TO USE THIS CLASS #######################
 
 Printsupport ps=new Printsupport();
 Object printitem [][]=ps.getTableData(jTable);
 ps.setItems(printitem);
       
 PrinterJob pj = PrinterJob.getPrinterJob();
 pj.setPrintable(new MyPrintable(),ps.getPageFormat(pj));
       try {
            pj.print();
           
            }
        catch (PrinterException ex) {
                ex.printStackTrace();
            }
 ################## JOIN TO SHARE KNOWLADGE ########################### */
 
