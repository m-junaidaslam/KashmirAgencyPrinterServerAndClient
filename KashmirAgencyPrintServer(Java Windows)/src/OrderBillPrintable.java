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

public class OrderBillPrintable implements Printable {
	
	//public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss a";
	OrderBill customerOrderBill;
	Font sizedAlviFont;
	
	public OrderBillPrintable(OrderBill customerOrderBill) {
		// TODO Auto-generated constructor stub
		this.customerOrderBill = customerOrderBill;
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
	        invoice2D.drawString("Invoice Id: ", x, y);
	        x = x + 130;
	        invoice2D.drawString(customerOrderBill.getId() + "", x, y);
	        
	        //Write date at top right
	        x = x + 230;
	        invoice2D.drawString("Date: ", x, y);
	        x = x + 50;
	        invoice2D.drawString(customerOrderBill.getDate(), x, y);
	        
	        //Blue "bill to" writing
	        y = y + 15;
	        x = 10;
	        invoice2D.drawString("Customer Name: " , x, y);
	        //Receiver's surname and comma under the "bill to" writing
	        x = x + 130;
	        invoice2D.setFont(sizedAlviFont);
	        invoice2D.drawString(customerOrderBill.getCustomerName(), x, y);
	        //Receiver's name under the "bill to" writing
	        x = 10;
	        y = y + 15;
	        invoice2D.setFont( new Font("Times New Roman", Font.BOLD, 12));
	        invoice2D.drawString("Company: ", x, y);
	        x = x + 130;
	        invoice2D.setFont(sizedAlviFont);
	        invoice2D.drawString(customerOrderBill.getCompanyName() , x, y);
	        invoice2D.setFont( new Font("Times New Roman", Font.BOLD, 12));
	        
	        //TABLE FORMAT
	        //FIRST COLUMN
	        //Four blue & empty rectangles for main titles
	        x = 10;
	        y = y + 10;
	        Rectangle serial = new Rectangle(x, y, 50, 17);
	        x = x + 50;
	        Rectangle product = new Rectangle(x, y, 210, 17);
	        x = x + 210;
	        Rectangle qty = new Rectangle(x, y, 70, 17);
	        x = x + 70;
	        Rectangle rate = new Rectangle(x, y, 100, 17);
	        x = x + 100;
	        Rectangle amount = new Rectangle(x, y, 120, 17);
	        invoice2D.draw(serial);
	        invoice2D.draw(product);
	        invoice2D.draw(qty);
	        invoice2D.draw(rate);
	        invoice2D.draw(amount);
	        //Description title
	        invoice.setColor(Color.BLACK);
	        invoice2D.setFont( new Font("Times New Roman", Font.PLAIN, 14));
	        x = 15;
	        y = y + 14;
	        invoice2D.drawString("No." , x, y);
	        //Unit Price title
	        x = x + 50;
	        invoice2D.drawString("Product" , x, y);
	        x = x + 210;
	        invoice2D.drawString("Units", x, y);
	        x = x + 70;
	        invoice2D.drawString("Unit Price", x, y);
	        //Amount title
	        x = x + 100;
	        invoice2D.drawString("Total" , x, y);
	        //Total title
	        
	        y = y + 4;
	        
	        invoice.setColor(Color.BLACK);
	        invoice2D.setFont( new Font("Times New Roman", Font.PLAIN, 12));
	        
	        for(int i = 0; i < customerOrderBill.getItems().length; i++) {
	        	x = 10;
	        	serial.setBounds(x, y, 50, 15);
		        x = x + 50;
		        product.setBounds(x, y, 210, 15);
		        x = x + 210;
		        qty.setBounds(x, y, 70, 15);
		        x = x + 70;
		        rate.setBounds(x, y, 100, 15);
		        x = x + 100;
		        amount.setBounds(x, y, 120, 15);
		        invoice2D.draw(serial);
		        invoice2D.draw(product);
		        invoice2D.draw(qty);
		        invoice2D.draw(rate);
		        invoice2D.draw(amount);
		        x = 15;
		        y = y + 11;
		        invoice2D.drawString((customerOrderBill.getItem(i).getId() + 1) + "" , x, y);
		        //Unit Price title
		        x = x + 50;
		        invoice2D.setFont(sizedAlviFont);
		        invoice2D.drawString(customerOrderBill.getItem(i).getName() , x, y);
		        x = x + 210;
		        invoice2D.setFont( new Font("Times New Roman", Font.PLAIN, 12));
		        invoice2D.drawString(customerOrderBill.getItem(i).getQty() + "", x, y);
		        x = x + 70;
		        invoice2D.drawString(customerOrderBill.getItem(i).getUnitPrice() + "", x, y);
		        //Amount title
		        x = x + 100;
		        invoice2D.drawString(customerOrderBill.getItem(i).getTotalPrice() + "" , x, y);
		        //Total title
		        y = y + 4;
	        } 
	        
	        //SUBTOTAL
	        //Subtotal writing
	        x = 300;
	        y = y + 17;
	        invoice2D.setFont( new Font("Times New Roman", Font.PLAIN, 14));
	        invoice2D.drawString("Subtotal: " , x, y);
	        //Subtotal Value
	        x = x + 115;
	        invoice2D.drawString("Rs. " + customerOrderBill.getTotalAmount(), x, y);
	        
	        //TAX
	        //Tax writing
	        //Tax Value
	        y = y + 15;
	        x = 300;
	        invoice2D.drawString("Discount: ", x, y);
	        x = x + 115;
	        invoice2D.drawString("Rs. " + customerOrderBill.getDiscount(), x, y);
	        
	        //TOTAL DUE
	        //Total Due writing
	        y = y + 15;
	        x = 300;
	        invoice2D.drawString("Net Amount: " , x, y);
	        //Total Due Value
	        x = x + 115;
	        invoice2D.drawString("Rs. " + customerOrderBill.getNetAmount(), x, y);
	        
	        if(!customerOrderBill.getCustomerName().matches("اسلم کشمیر ایجنسی")) {
		        y = y + 15;
		        x = 300;
		        invoice2D.drawString("Received Amount: " , x, y);
		        //Total Due Value
		        x = x + 115;
		        invoice2D.drawString("Rs. " + customerOrderBill.getPaidAmount(), x, y);
		        
		        y = y + 15;
		        x = 300;
		        invoice2D.drawString("Balance: " , x, y);
		        //Total Due Value
		        x = x + 115;
		        invoice2D.drawString("Rs. " + customerOrderBill.getBalance(), x, y);
	        }
	        
	        x = 10;
	        invoice2D.drawString("Signature: ", x, y);
	        x = 70;
	        invoice2D.drawLine(x, y, x + 100, y+1);
	        
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
 
