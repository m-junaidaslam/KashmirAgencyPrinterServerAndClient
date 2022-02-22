
public class OrderBill {

	/*------------------------------------------------*
	 *--------------Members Declaration---------------*
	 *------------------------------------------------*/
	private int id;
	private String date;
	private String customerName;
	private String companyName;
	private int itemsCount;
	private BillItem[] items;
	private double totalAmount;
	private double discount;
	private double netAmount;
	private double paidAmount;
	private double balance;
	
	
	/*------------------------------------------------*
	 *------------------Constructors------------------*
	 *------------------------------------------------*/
	public OrderBill() {
		this.id = 0;
		this.date = "";
		this.customerName = "";
		this.companyName = "";
		this.itemsCount = 0;
		this.items = new BillItem[0];
		this.totalAmount = 0;
		this.discount = 0;
		this.netAmount = 0;
		this.paidAmount = 0;
		this.balance = 0;
	}
	
	public OrderBill(int id, String date, String customerName,
			String companyName, int itemsCount, BillItem[] items, double discount, double paidAmount) {
		this.id = id;
		this.date = date;
		this.customerName = customerName;
		this.companyName = companyName;
		this.itemsCount = itemsCount;
		this.items = items;
		totalAmount = 0;
		for(int i = 0; i < items.length; i++) {
			totalAmount = totalAmount + items[i].getTotalPrice();
		}
		this.discount = discount;
		this.netAmount = totalAmount - discount;
		this.paidAmount = paidAmount;
		this.balance = paidAmount - netAmount;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getItemsCount() {
		return itemsCount;
	}

	public void setItemsCount(int itemsCount) {
		this.itemsCount = itemsCount;
	}

	public BillItem[] getItems() {
		return items;
	}

	public void setItems(BillItem[] items) {
		this.items = items;
		this.itemsCount = items.length;
		this.totalAmount = 0;
		for(int i = 0; i < this.itemsCount; i++) {
			this.totalAmount = this.totalAmount + items[i].getTotalPrice();
		}
		this.netAmount = this.totalAmount - this.discount;
		this.balance = this.paidAmount - this.netAmount;
	}
	
	public BillItem getItem(int i) {
		return this.items[i];
	}
	
	public void setItem(int i, BillItem item) {
		this.items[i] = item;
		this.totalAmount = 0;
		for(int j = 0; j < this.itemsCount; i++) {
			this.totalAmount = this.totalAmount + this.items[j].getTotalPrice();
		}
		this.netAmount = this.totalAmount - this.discount;
		this.balance = this.paidAmount - this.netAmount;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
		this.netAmount = this.totalAmount - discount;
		this.balance = this.paidAmount - this.netAmount;
	}

	public double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
		this.balance = this.paidAmount - netAmount;
	}

	public double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
		this.balance = paidAmount - this.netAmount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	
}
