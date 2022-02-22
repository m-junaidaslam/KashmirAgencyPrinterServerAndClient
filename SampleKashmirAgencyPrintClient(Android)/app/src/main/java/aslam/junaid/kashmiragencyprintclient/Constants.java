package aslam.junaid.kashmiragencyprintclient;

/**
 * Description
 *
 * @author Catalin Prata
 *         Date: 2/12/13
 */
public class Constants {

    public static final String CLOSED_CONNECTION = "Client_Closed_Connection";
    public static final String LOGIN_NAME = "Android_Client";
    public static final String LOGIN_ACK = "Login_Ack";
    public static final String PRINTER_NACK = "Printer_Nack";
    
    /*********************Constants to Back Up and Restore*****************************/
    public static final String CMD_BACK_UP = "#CMD:back_up";
    public static final String ACK_BACK_UP = "#ACK:back_up";
    public static final String CMD_BACK_UP_COMPLETE = "CMD:back_up_complete";
    public static final String ACK_BACK_UP_COMPLETE = "ACK:back_up_complete";

    public static final String CMD_RESTORE = "#CMD:restore";
    public static final String ACK_RESTORE = "#ACK:restore";
    public static final String CMD_RESTORE_COMPLETE = "CMD:restore_complete";
    public static final String ACK_RESTORE_COMPLETE = "ACK:restore_complete";

    /********************************Constants for Order Bill****************************/
    public static final String CMD_NEW_BILL_ORDER = "#CMD:new_bill_order";
    public static final String CMD_ORDER_BILL_ID = "#CMD:order_bill_id";
    public static final String CMD_ORDER_BILL_DATE = "#CMD:order_bill_date";
    public static final String CMD_ORDER_CUSTOMER_NAME = "#CMD:order_customer_name";
    public static final String CMD_ORDER_COMPANY_NAME = "#CMD:order_company_name";
    public static final String CMD_ORDER_ITEMS_COUNT = "#CMD:order_items_count";
    public static final String CMD_ORDER_DISCOUNT = "#CMD:order_discount";
    public static final String CMD_ORDER_PAID_AMOUNT = "#CMD:order_paid_amount";
    public static final String CMD_ORDER_BILL_DONE = "#CMD:order_bill_done";
    public static final String CMD_ORDER_BILL_PRINT = "#CMD:order_bill_print";
    public static final String CMD_ORDER_BILL_ERROR = "#CMD:order_bill_error";

    public static final String KEY_ITEM_NUMBER = "#CMD:item_number";
    public static final String KEY_ITEM_NAME = "#CMD:item_name";
    public static final String KEY_ITEM_QUANTITY = "#CMD:item_quantity";
    public static final String KEY_ITEM_PRICE = "#CMD:item_price";
    
    public static final String ACK_NEW_BILL_ORDER = "#ACK:new_bill_order";
    public static final String ACK_ORDER_BILL_ID = "#ACK:order_bill_id";
    public static final String ACK_ORDER_BILL_DATE = "#ACK:order_bill_date";
    public static final String ACK_ORDER_CUSTOMER_NAME = "#ACK:order_customer_name";
    public static final String ACK_ORDER_COMPANY_NAME = "#ACK:order_company_name";
    public static final String ACK_ORDER_ITEMS_COUNT = "#ACK:order_items_count";
    public static final String ACK_ORDER_DISCOUNT = "#ACK:order_discount";
    public static final String ACK_ORDER_PAID_AMOUNT = "#ACK:order_paid_amount:";
    public static final String ACK_ORDER_BILL_DONE = "#ACK:order_bill_done";
    public static final String ACK_ORDER_BILL_PRINT = "#ACK:order_bill_print";

    public static final String ACK_ITEM_NUMBER = "#ACK:item_number:";
    public static final String ACK_ITEM_NAME = "#ACK:item_name:";
    public static final String ACK_ITEM_QUANTITY = "#ACK:item_quantity:";
    public static final String ACK_ITEM_PRICE = "#ACK:item_price:";
    /*********************************END Order Bill**********************************/

    /********************************Constants for Payments Bill****************************/
    public static final String CMD_NEW_BILL_PAYMENT = "#CMD:new_bill_payment";
    public static final String CMD_PAYMENT_BILL_ID = "#CMD:payment_bill_id";
    public static final String CMD_PAYMENT_BILL_DATE = "#CMD:payment_bill_date";
    public static final String CMD_PAYMENT_CUSTOMER_NAME = "#CMD:payment_customer_name";
    public static final String CMD_PAYMENT_COMPANY_NAME = "#CMD:payment_company_name";
    public static final String CMD_PAYMENTS_COUNT = "#CMD:payments_count";
    public static final String CMD_PAYMENT_BILL_DONE = "#CMD:payment_bill_done";
    public static final String CMD_PAYMENT_BILL_PRINT = "#CMD:payment_bill_print";
    public static final String CMD_PAYMENT_BILL_ERROR = "#CMD:payment_bill_error";

    public static final String KEY_PAYMENT_NUMBER = "#CMD:payment_number";
    public static final String KEY_PAYMENT_DATE = "#CMD:payment_date";
    public static final String KEY_PAYMENT_DUE_AMOUNT = "#CMD:payment_due_amount";
    public static final String KEY_PAYMENT_DONE_AMOUNT = "#CMD:payment_done_amount";
    
    public static final String ACK_NEW_BILL_PAYMENT = "#ACK:new_bill_payment";
    public static final String ACK_PAYMENT_BILL_ID = "#ACK:payment_bill_id";
    public static final String ACK_PAYMENT_BILL_DATE = "#ACK:payment_bill_date";
    public static final String ACK_PAYMENT_CUSTOMER_NAME = "#ACK:payment_customer_name";
    public static final String ACK_PAYMENT_COMPANY_NAME = "#ACK:payment_company_name";
    public static final String ACK_PAYMENTS_COUNT = "#ACK:payments_count";
    public static final String ACK_PAYMENT_BILL_DONE = "#ACK:payment_bill_done";
    public static final String ACK_PAYMENT_BILL_PRINT = "#ACK:payment_bill_print";
    public static final String ACK_PAYMENT_BILL_ERROR = "#ACK:payment_bill_error";

    public static final String ACK_PAYMENT_NUMBER = "#ACK:payment_number";
    public static final String ACK_PAYMENT_DATE = "#ACK:payment_date";
    public static final String ACK_PAYMENT_DUE_AMOUNT = "#ACK:payment_due_amount";
    public static final String ACK_PAYMENT_DONE_AMOUNT = "#ACK:payment_done_amount";
    /*********************************END Payments Bill**********************************/
}