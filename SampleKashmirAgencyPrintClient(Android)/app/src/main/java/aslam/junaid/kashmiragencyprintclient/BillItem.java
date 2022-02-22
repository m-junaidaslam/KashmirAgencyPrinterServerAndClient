package aslam.junaid.kashmiragencyprintclient;

public class BillItem {
	/*------------------------------------------------*
	 *--------------Members Declaration---------------*
	 *------------------------------------------------*/
	private int id;
	private String name;
	private int qty;
	private double unitPrice;
	private double totalPrice;
	
	/*------------------------------------------------*
	 *------------------Constructors------------------*
	 *------------------------------------------------*/
	BillItem() {
		this.id = 0;
		this.name = "";
		this.qty = 0;
		this.unitPrice = 0;
		this.totalPrice = 0;
	}
	
	BillItem(int id, String name, int qty, double rate) {
		this.id = id;
		this.name = name;
		this.qty = qty;
		this.unitPrice = rate;
		this.totalPrice = qty * rate;
	}

	/*------------------------------------------------*
	 *--------------Getters and Setters---------------*
	 *------------------------------------------------*/
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
		this.totalPrice = this.unitPrice * qty;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
		this.totalPrice = unitPrice * this.qty;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
}
