
public class BillPayment {
	/*------------------------------------------------*
	 *--------------Members Declaration---------------*
	 *------------------------------------------------*/
	private int id;
	private String date;
	private double dueAmount;
	private double doneAmount;
	private double remainingAmount;
	
	/*------------------------------------------------*
	 *------------------Constructors------------------*
	 *------------------------------------------------*/
	BillPayment() {
		this.id = 0;
		this.date = "";
		this.dueAmount = 0;
		this.doneAmount = 0;
		this.remainingAmount = 0;
	}

	public BillPayment(int id, String date, double dueAmount,
			double doneAmount) {
		super();
		this.id = id;
		this.date = date;
		this.dueAmount = dueAmount;
		this.doneAmount = doneAmount;
		this.remainingAmount = dueAmount - doneAmount;
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

	public double getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(double dueAmount) {
		this.dueAmount = dueAmount;
		this.remainingAmount = dueAmount - this.doneAmount;
	}

	public double getDoneAmount() {
		return doneAmount;
	}

	public void setDoneAmount(double doneAmount) {
		this.doneAmount = doneAmount;
		this.remainingAmount = this.dueAmount - doneAmount;
	}

	public double getRemainingAmount() {
		return remainingAmount;
	}

	public void setRemainingAmount(double remainingAmount) {
		this.remainingAmount = remainingAmount;
	}
	
}
