
public class PaymentBill {

	/*------------------------------------------------*
	 *--------------Members Declaration---------------*
	 *------------------------------------------------*/
	private int id;
	private String date;
	private String customerName;
	private String companyName;
	private int paymentsCount;
	private BillPayment[] payments;
	
	/*------------------------------------------------*
	 *------------------Constructors------------------*
	 *------------------------------------------------*/
	public PaymentBill() {
		this.id = 0;
		this.date = "";
		this.customerName = "";
		this.companyName = "";
		this.paymentsCount = 0;
		this.payments = new BillPayment[0];
	}
	
	public PaymentBill(int id, String date, String customerName,
			String companyName, int paymentsCount, BillPayment[] payments) {
		this.id = id;
		this.date = date;
		this.customerName = customerName;
		this.companyName = companyName;
		this.paymentsCount = paymentsCount;
		this.payments = payments;
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

	public int getPaymentsCount() {
		return paymentsCount;
	}

	public void setPaymentsCount(int paymentsCount) {
		this.paymentsCount = paymentsCount;
	}

	public BillPayment[] getPayments() {
		return payments;
	}

	public void setPayments(BillPayment[] payments) {
		this.payments = payments;
		this.paymentsCount = payments.length;
	}
	
	public BillPayment getPayment(int i) {
		return this.payments[i];
	}
	
	public void setPayment(int i, BillPayment payment) {
		this.payments[i] = payment;
	}	
}
