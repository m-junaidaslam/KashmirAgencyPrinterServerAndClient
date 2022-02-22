package aslam.junaid.kashmiragencyprintclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.channels.FileChannel;

public class ClientActivity extends Activity implements Serializable {

    Context context;
    private TcpClient mTcpClient;
    Button btnSend, btnReset, btnSendFile;
    EditText etItemNumber, etItemName, etItemQuantity, etItemPrice, etPaidAmount;
    private boolean isCustomerBill;
    private String ip;
    BillItem[] items;
    int itemsCounter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        context = this;
        isCustomerBill = false;
        ip = "";
        itemsCounter = 0;

        btnSend = (Button) findViewById(R.id.btn_send);
        btnReset = (Button) findViewById(R.id.btn_reset);
        btnSendFile = (Button) findViewById(R.id.btn_send_file);
        etItemNumber = (EditText) findViewById(R.id.et_item_number);
        etItemName = (EditText) findViewById(R.id.et_item_name);
        etItemQuantity = (EditText) findViewById(R.id.et_item_quantity);
        etItemPrice = (EditText) findViewById(R.id.et_item_price);
        etPaidAmount = (EditText) findViewById(R.id.et_paid_amount);

        ipDialog();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTcpClient != null) {
                    isCustomerBill = true;
                    mTcpClient.sendMessage(Constants.CMD_NEW_BILL);
                }
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etItemNumber.setText("");
                etItemName.setText("");
                etItemQuantity.setText("");
                etItemPrice.setText("");
                etPaidAmount.setText("");
            }
        });
        btnSendFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mTcpClient != null){
                    mTcpClient.sendMessage(Constants.CMD_FILE_SEND);
                }
            }
        });


        items = new BillItem[5];
        for(int i = 0; i < items.length; i++) {
            items[i] = new BillItem((i + 1), "آئٹم کا نام", 2, 50);
        }
        Log.d("ItemsLength", items.length + "");
    }

    public void ipDialog() {
        final AlertDialog.Builder myAlert = new AlertDialog.Builder(ClientActivity.this);

        myAlert.setTitle(getString(R.string.ip_address_alert));

        LinearLayout layout = new LinearLayout(ClientActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText etIpField = new EditText(ClientActivity.this);
        etIpField.setHint(R.string.ip_address_alert);
        etIpField.setText("192.168.43.");
        etIpField.setInputType(InputType.TYPE_CLASS_NUMBER);
        etIpField.setFilters(new InputFilter[]{new InputFilter.LengthFilter(14)});
        layout.addView(etIpField);

        myAlert.setView(layout);

        myAlert.setPositiveButton(R.string.save, null);

        myAlert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        final AlertDialog myAlertDialog = myAlert.create();

        myAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button btnPos = myAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnPos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ipTemp = etIpField.getText().toString();
                        if(ipTemp.startsWith("192.168.43.") && (ipTemp.length() > 11)) {
                            ip = ipTemp;
                            new ConnectTask().execute("");
                            myAlertDialog.dismiss();
                        } else {
                            Toast.makeText(ClientActivity.this, getString(R.string.invalid_ip), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

        myAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
        });
        myAlertDialog.show();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // disconnect
        if(mTcpClient != null)
            mTcpClient.stopClient();
        mTcpClient = null;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (mTcpClient != null) {
            // if the client is connected, enable the connect button and disable the disconnect one
            menu.getItem(1).setEnabled(true);
            menu.getItem(0).setEnabled(false);
        } else {
            // if the client is disconnected, enable the disconnect button and disable the connect one
            menu.getItem(1).setEnabled(false);
            menu.getItem(0).setEnabled(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.connect:
                // connect to the server
                new ConnectTask().execute("");
                return true;
            case R.id.disconnect:
                // disconnect
                mTcpClient.stopClient();
                mTcpClient = null;
                isCustomerBill = false;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    /********************************
     Customer Bill States:
     0. Bill is Nill
     1. Bill began
     2. Bill Id Received
     3. Bill Date Received
     4. Customer Name Received
     5. Customer Company Received
     6. Items Count Received
     7. BillItem Id Received
     8. BillItem Name Received
     9. BillItem Quantity Received
     10. BillItem Price Received
     11. Paid Amount Received
     12. Bill Print Received
     0. Bill Done Received
     */

    public void handleCustomerBill(String msg) {
        Log.d("Messages", msg);
        switch (msg) {
            case Constants.LOGIN_ACK:
                Log.d("ACK", msg);
                break;
            case Constants.ACK_NEW_BILL:
                btnSend.setEnabled(false);
                mTcpClient.sendMessage(Constants.CMD_BILL_ID + ">" + 4);
                Log.d("ACK", msg);
                break;
            case Constants.ACK_BILL_ID:
                mTcpClient.sendMessage(Constants.CMD_BILL_DATE + ">" + "08:24 PM 22-09-2016");
                Log.d("ACK", msg);
                break;
            case Constants.ACK_BILL_DATE:
                mTcpClient.sendMessage(Constants.CMD_CUSTOMER_NAME + ">" + "جنید اسلم");
                Log.d("ACK", msg);
                break;
            case Constants.ACK_CUSTOMER_NAME:
                mTcpClient.sendMessage(Constants.CMD_COMPANY_NAME + ">" + "جنید کریانہ سٹور");
                Log.d("ACK", msg);
                break;
            case Constants.ACK_COMPANY_NAME:
                mTcpClient.sendMessage(Constants.CMD_ITEMS_COUNT + ">" + items.length);
                Log.d("ACK", msg);
                itemsCounter = 0;
                break;
            case Constants.ACK_ITEMS_COUNT:
                if(itemsCounter < items.length) {
                    mTcpClient.sendMessage(Constants.KEY_ITEM_NUMBER + itemsCounter + ">" + items[itemsCounter].getId());
                    Log.d("ACK", msg);
                } else {
                    mTcpClient.sendMessage(Constants.CMD_CUSTOMER_BILL_ERROR);
                    itemsCounter = 0;
                    Log.d("ACK", "Error CMD Customer Bill");
                }
                break;
            case Constants.ACK_ITEM_NUMBER:
                if(itemsCounter < items.length) {
                    mTcpClient.sendMessage(Constants.KEY_ITEM_NAME + itemsCounter + ">" + items[itemsCounter].getName());
                    Log.d("ACK", msg);
                } else {
                    mTcpClient.sendMessage(Constants.CMD_CUSTOMER_BILL_ERROR);
                    itemsCounter = 0;
                    Log.d("ACK", "Error CMD Customer Bill");
                }
                break;
            case Constants.ACK_ITEM_NAME:
                if(itemsCounter < items.length) {
                    mTcpClient.sendMessage(Constants.KEY_ITEM_QUANTITY + itemsCounter + ">" + items[itemsCounter].getQty());
                    Log.d("ACK", msg);
                } else {
                    mTcpClient.sendMessage(Constants.CMD_CUSTOMER_BILL_ERROR);
                    itemsCounter = 0;
                    Log.d("ACK", "Error CMD Customer Bill");
                }
                break;
            case Constants.ACK_ITEM_QUANTITY:
                Log.d("Items", "BillItem Counter: " + itemsCounter + ", Items Length: " + items.length);
                if(itemsCounter < items.length) {
                    mTcpClient.sendMessage(Constants.KEY_ITEM_PRICE + itemsCounter + ">" + items[itemsCounter].getUnitPrice());
                    Log.d("ACK", msg);
                } else {
                    mTcpClient.sendMessage(Constants.CMD_CUSTOMER_BILL_ERROR);
                    itemsCounter = 0;
                    Log.d("ACK", "Error CMD Customer Bill");
                }
                break;
            case Constants.ACK_ITEM_PRICE:
                itemsCounter++;
                if(itemsCounter < items.length) {
                    mTcpClient.sendMessage(Constants.KEY_ITEM_NUMBER + itemsCounter + ">" + items[itemsCounter].getId());
                    Log.d("ACK", msg);
                } else if(itemsCounter == items.length) {
                    itemsCounter = 0;
                    mTcpClient.sendMessage(Constants.CMD_PAID_AMOUNT + ">" + "1000");
                    Log.d("ACK", msg);
                } else {
                    mTcpClient.sendMessage(Constants.CMD_CUSTOMER_BILL_ERROR);
                    itemsCounter = 0;
                    Log.d("ACK", msg);
                }
                break;
            case Constants.ACK_PAID_AMOUNT:
                mTcpClient.sendMessage(Constants.CMD_BILL_PRINT);
                Log.d("ACK", msg);
                break;
            case Constants.ACK_BILL_PRINT:
                mTcpClient.sendMessage(Constants.CMD_BILL_DONE);
                Log.d("ACK", msg);
                break;
            case Constants.ACK_BILL_DONE:
                isCustomerBill = false;
                btnSend.setEnabled(true);
                Toast.makeText(ClientActivity.this, "Bill Transferred!", Toast.LENGTH_SHORT).show();
                Log.d("ACK", msg);
                break;
            case Constants.ACK_FILE_SEND:
                Log.d("FileSending", "Sending File ACK_FILE_SEND received");
                mTcpClient.sendDataBase();
                mTcpClient.sendMessage(Constants.CMD_FILE_COMPLETE);
                break;
            case Constants.ACK_FILE_COMPLETE:
                Toast.makeText(ClientActivity.this, "File Transfer Completed", Toast.LENGTH_SHORT).show();
                break;
            /*case Constants.CMD_FILE_SEND:
                mTcpClient.setReceivingObject(true);
                mTcpClient.sendMessage(Constants.ACK_FILE_SEND);
                break;
            case Constants.CMD_FILE_COMPLETE:
                mTcpClient.setReceivingObject(false);
                mTcpClient.sendMessage(Constants.ACK_FILE_COMPLETE);
                Toast.makeText(ClientActivity.this, "File Received!", Toast.LENGTH_SHORT).show();
                break;*/
        }
    }

    public class ConnectTask extends AsyncTask<String, String, TcpClient> {

        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object and
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }

                /*
                @Override
                public void objectReceived(ObjectInputStream objectInputStream) {
                    try {
                        byte[] buffer = (byte[]) objectInputStream.readObject();
                        FileOutputStream fos = new FileOutputStream("/sdcard/Download/receivedMyPic.jpg");
                        fos.write(buffer);
                        fos.close();
                    } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }*/

            }, ip);
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            String msg = values[0];
            if(!isCustomerBill) {
                handleCustomerBill(msg);
            }
        }
    }
}