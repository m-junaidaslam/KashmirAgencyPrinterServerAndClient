import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.AbstractDocument;

/**
 * Description
 *
 * @author Muhammad Junaid Aslam
 *         Date: 25/09/16
 */
public class MainScreen extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea messagesArea;
    private JButton startServer;
    private JButton stopServer;
    private TcpMessageServer mServer;
    private JLabel lblNewLabel;
    private JScrollPane spMessageArea;
    private int orderBillState;
    private int paymentBillState;
    OrderBill orderBill;
    PaymentBill paymentBill;
    BillItem[] items;
    BillPayment[] payments;
    int billItemsCounter;
    int billPaymentsCounter;
    private String ip;
    private int paymentPrintCounts;
    BillPayment[] partPayments[];
    PrintService requiredService = null;
    private JButton btnRefresh;
    JLabel lblIpAddress;
    
    /********************************
     Order Bill States:
     	0. Bill is Nill
     	1. Bill began
     	2. Bill Id Received
     	3. Bill Date Received
     	4. Customer Name Received
     	5. Customer Company Received
     	6. Items Count Received
     	7. Item Id Received
     	8. Item Name Received
     	9. Item Quantity Received
     	10. Item Price Received
     	11. Paid Amount Received
     	12. Bill Print Received
     	0. Bill Done Received
     */
    
    /********************************
    Payment Bill States:
    	0. Bill is Nill
    	1. Bill began
    	2. Bill Id Received
    	3. Bill Date Received
    	4. Customer Name Received
    	5. Customer Company Received
    	6. Payments Count Received
    	7. Payment Id Received
    	8. Payment Date Received
    	9. Payment dueAmount Received
    	10. Payment doneAmount Received
    	11. Bill Print Received
    	0. Bill Done Received
    */
    
    public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainScreen window = new MainScreen();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

    @SuppressWarnings("rawtypes")
	public MainScreen() {
    	
        super("MainScreen");
        setTitle("Kashmir Agency Printing Server");
        Image iconTitle = new ImageIcon(this.getClass().getResource("/print_icon.png")).getImage();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(iconTitle);
		orderBillState = 0;
		billItemsCounter = 0;
		orderBill = new OrderBill();
		paymentBillState = 0;
		billPaymentsCounter = 0;
		paymentBill = new PaymentBill();
		ip = "";
        JPanel panelFields = new JPanel();
        
        lblIpAddress = new JLabel("IP Address: ");
        lblIpAddress.setBounds(322, 0, 170, 35);
        panelFields.add(lblIpAddress);
        
        setIp(true, "192.168.43.");
        
        startServer = new JButton("\u0634\u0631\u0648\u0639");
        startServer.setFont(new Font("Times New Roman", Font.BOLD, 16));
        startServer.setBounds(136, 236, 116, 52);
        startServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startServerService();
            }
        });

        stopServer = new JButton("\u062E\u062A\u0645");
        stopServer.setFont(new Font("Times New Roman", Font.BOLD, 16));
        stopServer.setBounds(10, 236, 116, 52);
        stopServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (mServer != null) {
                    mServer.close();
                }

                // disable the stop button and enable the start one
                startServer.setEnabled(true);
                stopServer.setEnabled(false);

            }
        });
        panelFields.setLayout(null);
                
	    spMessageArea = new JScrollPane();
	    spMessageArea.setBounds(0, 34, 492, 191);
	    panelFields.add(spMessageArea);
	
	    //here we will have the text messages screen
	    messagesArea = new JTextArea();
	    spMessageArea.setViewportView(messagesArea);
	    messagesArea.setColumns(30);
	    messagesArea.setRows(10);
	    messagesArea.setEditable(false);
        panelFields.add(startServer);
        panelFields.add(stopServer);

        getContentPane().add(panelFields);
        
        lblNewLabel = new JLabel("\u0646\u06AF\u0631\u0627\u0646\u06CC");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBackground(new Color(70, 130, 180));
        lblNewLabel.setBounds(0, 0, 492, 35);
        panelFields.add(lblNewLabel);
        
        JComboBox cbPrinterSelector = new JComboBox();
        cbPrinterSelector.setBounds(262, 236, 162, 52);
        cbPrinterSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String printer = (String) cbPrinterSelector.getSelectedItem();
				PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
				for (int i = 0; i < services.length; i++) {
					   //System.out.println(services[i].getName());
					   if(services[i].getName().contains(printer)) {
						   requiredService = services[i];
					   }
					}
			}
		});
        cbPrinterSelector.setSelectedItem(0);
        panelFields.add(cbPrinterSelector);
        
        btnRefresh = new JButton("");
        btnRefresh.addActionListener(new ActionListener() {
        	@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent arg0) {
        		requiredService = null;
        		int selectedIndex = 0;
        			cbPrinterSelector.removeAllItems();
        		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        		for (int i = 0; i < services.length; i++) {
        		   //System.out.println(services[i].getName());
        		   if(services[i].getName().contains("1300")) {
        			   requiredService = services[i];
        			   selectedIndex = i;
        		   }
        		   cbPrinterSelector.addItem(services[i].getName().toString());
        		   
        		}
        		cbPrinterSelector.setSelectedIndex(selectedIndex);
        	}
        });
        Image iconStatus = new ImageIcon(this.getClass().getResource("/icon_update.png")).getImage();
        btnRefresh.setIcon(new ImageIcon(iconStatus));
        btnRefresh.setBounds(434, 236, 48, 52);
        panelFields.add(btnRefresh);
        
        JButton btnChangeIp = new JButton("Change IP");
        btnChangeIp.setBounds(10, 8, 116, 23);
        btnChangeIp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showIpDialog();
            }
        });
        panelFields.add(btnChangeIp);
        btnRefresh.doClick();
        
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setSize(508, 338);
       
        setVisible(true);
    }
    
    public void showIpDialog() {
		JPanel myPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(4, 4, 4, 4);
		
		JTextField fieldIpPrefix = new JTextField(14);
		AbstractDocument doc = (AbstractDocument) fieldIpPrefix.getDocument();
		fieldIpPrefix.setFont(new Font("Times New Roman", Font.BOLD, 16));
		doc.setDocumentFilter(new SizeFilter(14));
		fieldIpPrefix.addAncestorListener(new RequestFocusListener());
		fieldIpPrefix.setText("192.168.43.");
		fieldIpPrefix.setToolTipText("XXX.XXX.XXX.");

    	gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 1;
		myPanel.add(new JLabel("New IP"), gridBagConstraints);

    	gridBagConstraints.gridx = 1;
		myPanel.add(fieldIpPrefix, gridBagConstraints);
		
		javax.swing.UIManager.put("OptionPane.messageFont", new FontUIResource(
				new Font("Times New Roman", Font.BOLD, 16)));
		int resultIpDialog = JOptionPane.showConfirmDialog(null, myPanel,
				"New IP Prefix",
				JOptionPane.OK_CANCEL_OPTION);
		if(resultIpDialog == JOptionPane.OK_OPTION) {
			String ipPrefix = fieldIpPrefix.getText().toString();
            boolean isIpValid = true;
            try {
                if ( ipPrefix == null || ipPrefix.isEmpty() ) {
                    isIpValid = false;
                }
                
                String[] parts = ipPrefix.split( "\\." );
                if ( parts.length != 3 ) {
                    isIpValid = false;
                }

                for ( String s : parts ) {
                    int i = Integer.parseInt( s );
                    if ( (i < 0) || (i > 255) ) {
                        isIpValid = false;
                    }
                }
                if (!ipPrefix.endsWith(".")) {
                    isIpValid = false;
                }

                if(ipPrefix.contains("..")) {
                    isIpValid = false;
                }
                parts = null;

            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
	        	javax.swing.UIManager.put("OptionPane.messageFont",
						new FontUIResource(new Font("Times New Roman",
								Font.BOLD, 16)));
				JOptionPane
						.showMessageDialog(null, "Number Format is NOT Applicable!");
                isIpValid = false;
            }

            if(isIpValid) {
            	if(ip.startsWith(ipPrefix)) {
            		//System.out.println("Invalid IP");
    	        	javax.swing.UIManager.put("OptionPane.messageFont",
    						new FontUIResource(new Font("Times New Roman",
    								Font.BOLD, 16)));
    				JOptionPane
    						.showMessageDialog(null, "New IP is same as old IP");
            	} else {
            		setIp(false, ipPrefix);
            	}
            } else {
            	//System.out.println("Invalid IP");
	        	javax.swing.UIManager.put("OptionPane.messageFont",
						new FontUIResource(new Font("Times New Roman",
								Font.BOLD, 16)));
				JOptionPane
						.showMessageDialog(null, "New IP is NOT Valid!");
            }
		}
    }
    
    public void startServerService() {
    	//System.out.println("Server Started.....");
    	//creates the object OnMessageReceived asked by the TCPServer constructor
        mServer = new TcpMessageServer(new TcpMessageServer.OnMessageReceived() {
            @Override
            //this method declared in the interface from TCPServer class is implemented here
            //this method is actually a callback method, because it will run every time when it will be called from
            //TCPServer class (at while)
            public void messageReceived(String msg) {
            	//System.out.println("Customer Bill State: " + orderBillState);
                //System.out.println(msg);
                customerBillCheck(msg);
                paymentBillCheck(msg);
                if(msg.matches(Constants.LOGIN_NAME )) {
                	messagesArea.setText(msg);
                	mServer.sendMessage(Constants.LOGIN_ACK);
                } else if(msg.matches(Constants.CLOSED_CONNECTION)) {
                	String tempMsg = messagesArea.getText().toString();
                	messagesArea.setText(msg + "\n" + tempMsg);
                } else if(msg.matches(Constants.CMD_BACK_UP)) {
                	//System.out.println(msg);
                	messagesArea.setText(msg);
                	mServer.sendMessage(Constants.ACK_BACK_UP);
                	stopServer.doClick();
                	try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                	new TcpBackUpServer();
                	try {
						Thread.sleep(4000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	startServer.doClick();
                	try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	mServer.sendMessage(Constants.CMD_BACK_UP_COMPLETE);
                	//System.out.println("Back Up Complete Message Sent");
                } else if(msg.matches(Constants.CMD_RESTORE)) {
                	messagesArea.setText(msg);
                	File varTmpDir = new File("AgencyDB.sqlite");
                	boolean exists = varTmpDir.exists();
                	if(exists) {
                		mServer.sendMessage(Constants.ACK_RESTORE);
                		stopServer.doClick();
                    	try {
    						Thread.sleep(1000);
    					} catch (InterruptedException e1) {
    						// TODO Auto-generated catch block
    						e1.printStackTrace();
    					}
                    	new TcpRestoreServer();
                    	try {
    						Thread.sleep(4000);
    					} catch (InterruptedException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
                    	startServer.doClick();
                    	try {
    						Thread.sleep(2000);
    					} catch (InterruptedException e) {
    						// TODO Auto-generated catch block
    						e.printStackTrace();
    					}
                		mServer.sendMessage(Constants.CMD_RESTORE_COMPLETE);
                    	//System.out.println("Restore Complete Message Sent");
                	} else {
                		//System.out.println("File Not Found");
                		mServer.sendMessage(Constants.NACK_RESTORE);
                	}
                }
            }
        });
        mServer.start();

        // disable the start button and enable the stop one
        startServer.setEnabled(false);
        stopServer.setEnabled(true);

    }
    
    private void customerBillCheck(String msg) {
    	if(msg.matches(Constants.CMD_NEW_BILL_ORDER) && (orderBillState == 0)) {
        	if (mServer != null) {
            	messagesArea.setText(msg);
                // send the message to the client
                mServer.sendMessage(Constants.ACK_NEW_BILL_ORDER);
                orderBillState++;
                //System.out.println("Command: "+ msg);
            }
    	} else if(msg.matches(Constants.CMD_ORDER_BILL_PRINT) && (orderBillState == 12)) {
        	if(mServer != null) {
        		String tempMsg = messagesArea.getText().toString();
            	messagesArea.setText(msg + "\n" + tempMsg);
        		mServer.sendMessage(Constants.ACK_ORDER_BILL_PRINT);
        		orderBillState++;
        		//System.out.print("Command: " + msg);
        		startBillPrinting(requiredService, true);
        	}
        } else if(msg.matches(Constants.CMD_ORDER_BILL_DONE) && (orderBillState == 13)) {
        	if (mServer != null) {
        		String tempMsg = messagesArea.getText().toString();
            	messagesArea.setText(msg + "\n" + tempMsg);
                // send the message to the client
                mServer.sendMessage(Constants.ACK_ORDER_BILL_DONE);
                orderBillState = 0;
                //System.out.println("Command: " + msg);
                
            }
        } else if((orderBillState > 0) && (orderBillState < 12)) {
        	handleOrderBill(msg);
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
    	7. Item Id Received
    	8. Item Name Received
    	9. Item Quantity Received
    	10. Item Price Received
    	11. Paid Amount Received
    	12. Bill Print Received
    	0. Bill Done Received
    */
    
    private void handleOrderBill(String msg) {
    	//System.out.println("Customer Bill State: " + orderBillState);
    	//System.out.println("Message: " + msg);
    	if((orderBillState < 6) || (orderBillState == 10) || (orderBillState == 11)) {
    		String cmd = msg.substring(0, msg.indexOf('>'));
        	String data = msg.substring((1 + msg.indexOf('>')), msg.length());
        	//System.out.println("Command: " + cmd);
            //System.out.println("Data: " + data);
    		switch (cmd) {
	    	case Constants.CMD_ORDER_BILL_ID:
	    		if(mServer != null) {
		    		if(orderBillState == 1) {
	                	messagesArea.setText(msg);
	    				mServer.sendMessage(Constants.ACK_ORDER_BILL_ID);
	    				orderBill.setId(Integer.valueOf(data));
	    				orderBillState++;
		    		} else {
		    			mServer.sendMessage(Constants.CMD_ORDER_BILL_ERROR);
		    			orderBillState = 0;
		    		}
	    		}
	    		break;
	    	case Constants.CMD_ORDER_BILL_DATE:
	    		if(mServer != null) {
		    		if(orderBillState == 2) {
	    				mServer.sendMessage(Constants.ACK_ORDER_BILL_DATE);
	    				orderBill.setDate(data);
	    				orderBillState++;
		    		} else {
		    			mServer.sendMessage(Constants.CMD_ORDER_BILL_ERROR);
		    			orderBillState = 0;
		    		}
	    		}
	    		break;
	    	case Constants.CMD_ORDER_CUSTOMER_NAME:
	    		if(mServer != null) {
		    		if(orderBillState == 3) {
	    				mServer.sendMessage(Constants.ACK_ORDER_CUSTOMER_NAME);
	    				orderBill.setCustomerName(data);
	    				orderBillState++;
		    		} else {
		    			mServer.sendMessage(Constants.CMD_ORDER_BILL_ERROR);
		    			orderBillState = 0;
		    		}
	    		}
	    		break;
	    	case Constants.CMD_ORDER_COMPANY_NAME:
	    		if(mServer != null) {
		    		if(orderBillState == 4) {
	    				mServer.sendMessage(Constants.ACK_ORDER_COMPANY_NAME);
	    				orderBill.setCompanyName(data);
	    				orderBillState++;
		    		} else {
		    			mServer.sendMessage(Constants.CMD_ORDER_BILL_ERROR);
		    			orderBillState = 0;
		    		}
	    		}
	    		break;
	    	case Constants.CMD_ORDER_ITEMS_COUNT:
	    		if(mServer != null) {
		    		if(orderBillState == 5) {
	    				orderBill.setItemsCount(Integer.valueOf(data));
	    				items = new BillItem[orderBill.getItemsCount()];
	    				for(int i = 0; i < items.length; i++) {
	    					items[i] = new BillItem();
	    				}
	    				billItemsCounter = 0;
	    				
	    				mServer.sendMessage(Constants.ACK_ORDER_ITEMS_COUNT);
	    				orderBillState++;
		    		} else {
		    			mServer.sendMessage(Constants.CMD_ORDER_BILL_ERROR);
		    			orderBillState = 0;
		    		}
	    		}
	    		break;
	    	case Constants.CMD_ORDER_DISCOUNT:
	    		if(mServer != null) {
		    		if(orderBillState == 10) {
		    			orderBill.setDiscount(Double.valueOf(data));
	    				billItemsCounter = 0;
	    				orderBill.setItems(items);
	    				mServer.sendMessage(Constants.ACK_ORDER_DISCOUNT);
	    				orderBillState++;
		    		} else {
		    			mServer.sendMessage(Constants.CMD_ORDER_BILL_ERROR);
		    			orderBillState = 0;
		    		}
	    		}
	    		break;
	    	case Constants.CMD_ORDER_PAID_AMOUNT:
	    		if(mServer != null) {
		    		if(orderBillState == 11) {
		    			orderBill.setPaidAmount(Double.valueOf(data));
	    				mServer.sendMessage(Constants.ACK_ORDER_PAID_AMOUNT);
	    				orderBillState++;
		    		} else {
		    			mServer.sendMessage(Constants.CMD_ORDER_BILL_ERROR);
		    			orderBillState = 0;
		    		}
	    		}
	    		break;
	    	}
    	} else if(msg.startsWith(Constants.KEY_ITEM_NUMBER.substring(0, 8)) && (orderBillState > 5) && (orderBillState < 10)) {
    		String cmd = msg.substring(0, msg.indexOf('>'));
        	String data = msg.substring((1 + msg.indexOf('>')), msg.length());
        	//System.out.println("Command: " + cmd);
            //System.out.println("Data: " + data);
    		handleOrderBillItem(cmd, data);
		}
    }
    
    public void handleOrderBillItem(String cmd, String data) {
    	int recCounter;
    	if(billItemsCounter < 10) {
    		recCounter = Integer.valueOf(cmd.substring(cmd.length()-1, cmd.length())); 
    	} else {
    		recCounter = Integer.valueOf(cmd.substring(cmd.length()-2, cmd.length())); 
    	}
    	//System.out.println("Items Length: " + items.length + " ,Rec Counter: " + recCounter + " , billItemsCounter: " + billItemsCounter);
    	if((billItemsCounter == recCounter) && (recCounter <= items.length)) {
	    	if (cmd.contains(Constants.KEY_ITEM_NUMBER)){
				if(mServer != null) {
					//System.out.println("Visited KEY_Item_Number with state = " + orderBillState);
		    		if(orderBillState == 6) {
						items[billItemsCounter].setId(Integer.valueOf(data));
						orderBillState++;
						mServer.sendMessage(Constants.ACK_ITEM_NUMBER);
						
		    		} else {
		    			orderBillState = 0;
		    			mServer.sendMessage(Constants.CMD_ORDER_BILL_ERROR);
		    		}
				}
	    	} else if (cmd.contains(Constants.KEY_ITEM_NAME)){
				if(mServer != null) {
		    		if(orderBillState == 7) {
						items[billItemsCounter].setName(data);
						orderBillState++;
						mServer.sendMessage(Constants.ACK_ITEM_NAME);
						
		    		} else {
		    			orderBillState = 0;
		    			mServer.sendMessage(Constants.CMD_ORDER_BILL_ERROR);
		    		}
				}
	    	} else if (cmd.contains(Constants.KEY_ITEM_QUANTITY)){
				if(mServer != null) {
					//System.out.println("Quantity visited with state: " + orderBillState);
		    		if(orderBillState == 8) {
						mServer.sendMessage(Constants.ACK_ITEM_QUANTITY);
						items[billItemsCounter].setQty((Integer.valueOf(data)));
						orderBillState++;		
						
		    		} else {
		    			
		    			mServer.sendMessage(Constants.CMD_ORDER_BILL_ERROR);
		    			orderBillState = 0;
		    		}
				}
	    	} else if (cmd.contains(Constants.KEY_ITEM_PRICE)){
				if(mServer != null) {
		    		if((orderBillState == 9) && (billItemsCounter < (items.length - 1))) {
		    			items[billItemsCounter].setUnitPrice(Double.valueOf(data));
		    			orderBillState = 6;
						billItemsCounter++;
						mServer.sendMessage(Constants.ACK_ITEM_PRICE);
		    		} else if((orderBillState == 9) && (billItemsCounter == (items.length - 1))) {
		    			items[billItemsCounter].setUnitPrice(Double.valueOf(data));
		    			orderBillState++;
		    			billItemsCounter = 0;
		    			mServer.sendMessage(Constants.ACK_ITEM_PRICE);
		    		} else {
		    			mServer.sendMessage(Constants.CMD_ORDER_BILL_ERROR);
		    			orderBillState = 0;
		    		}
				}
	    	}
    	} else {
    		mServer.sendMessage(Constants.CMD_ORDER_BILL_ERROR);
			orderBillState = 0;
    	}
    }
    
    private void paymentBillCheck(String msg) {
    	if(msg.matches(Constants.CMD_NEW_BILL_PAYMENT) && (paymentBillState == 0)) {
        	if (mServer != null) {
        		messagesArea.setText(msg);
                // send the message to the client
                mServer.sendMessage(Constants.ACK_NEW_BILL_PAYMENT);
                paymentBillState++;
                //System.out.println("Command: "+ msg);
            }
    	} else if(msg.matches(Constants.CMD_PAYMENT_BILL_PRINT) && (paymentBillState == 10)) {
        	if(mServer != null) {
        		String tempMsg = messagesArea.getText().toString();
        		messagesArea.setText(msg + "\n" + tempMsg);
        		startBillPrinting(requiredService, false);
        		//System.out.println("Part Payments Length: " + paymentPrintCounts);
        		if(paymentPrintCounts > 0) {
        			for(int i = 1; i < paymentPrintCounts; i++) {
        				//System.out.println("Index Tracking: " +  i);
        				paymentBill.setPayments(partPayments[i]);
        				//System.out.println("Bill Print" + i);
        				startBillPrinting(requiredService, false);
        			}
        		}
        		paymentPrintCounts = 0;
        		paymentBillState++;
        		//System.out.print("Command: " + msg);
        		mServer.sendMessage(Constants.ACK_PAYMENT_BILL_PRINT);
        	}
        } else if(msg.matches(Constants.CMD_PAYMENT_BILL_DONE) && (paymentBillState == 11)) {
        	if (mServer != null) {
        		String tempMsg = messagesArea.getText().toString();
            	messagesArea.setText(msg + "\n" + tempMsg);
                // send the message to the client
                mServer.sendMessage(Constants.ACK_PAYMENT_BILL_DONE);
                paymentBillState = 0;
                //System.out.println("Command: " + msg);
            }
        } else if((paymentBillState > 0) && (paymentBillState < 10)) {
        	handlePaymentBill(msg);
        }
    }
    
    private void handlePaymentBill(String msg) {
    	//System.out.println("Payment Bill State: " + paymentBillState);
    	//System.out.println("Message: " + msg);
    	if((paymentBillState < 6) || (paymentBillState == 10)) {
    		String cmd = msg.substring(0, msg.indexOf('>'));
        	String data = msg.substring((1 + msg.indexOf('>')), msg.length());
        	//System.out.println("Command: " + cmd);
            //System.out.println("Data: " + data);
    		switch (cmd) {
	    	case Constants.CMD_PAYMENT_BILL_ID:
	    		if(mServer != null) {
		    		if(paymentBillState == 1) {
	    				mServer.sendMessage(Constants.ACK_PAYMENT_BILL_ID);
	    				paymentBill.setId(Integer.valueOf(data));
	    				paymentBillState++;
		    		} else {
		    			mServer.sendMessage(Constants.CMD_PAYMENT_BILL_ERROR);
		    			paymentBillState = 0;
		    		}
	    		}
	    		break;
	    	case Constants.CMD_PAYMENT_BILL_DATE:
	    		if(mServer != null) {
		    		if(paymentBillState == 2) {
	    				mServer.sendMessage(Constants.ACK_PAYMENT_BILL_DATE);
	    				paymentBill.setDate(data);
	    				paymentBillState++;
		    		} else {
		    			mServer.sendMessage(Constants.CMD_PAYMENT_BILL_ERROR);
		    			paymentBillState = 0;
		    		}
	    		}
	    		break;
	    	case Constants.CMD_PAYMENT_CUSTOMER_NAME:
	    		if(mServer != null) {
		    		if(paymentBillState == 3) {
	    				mServer.sendMessage(Constants.ACK_PAYMENT_CUSTOMER_NAME);
	    				paymentBill.setCustomerName(data);
	    				paymentBillState++;
		    		} else {
		    			mServer.sendMessage(Constants.CMD_PAYMENT_BILL_ERROR);
		    			paymentBillState = 0;
		    		}
	    		}
	    		break;
	    	case Constants.CMD_PAYMENT_COMPANY_NAME:
	    		if(mServer != null) {
		    		if(paymentBillState == 4) {
	    				mServer.sendMessage(Constants.ACK_PAYMENT_COMPANY_NAME);
	    				paymentBill.setCompanyName(data);
	    				paymentBillState++;
		    		} else {
		    			mServer.sendMessage(Constants.CMD_PAYMENT_BILL_ERROR);
		    			paymentBillState = 0;
		    		}
	    		}
	    		break;
	    	case Constants.CMD_PAYMENTS_COUNT:
	    		if(mServer != null) {
		    		if(paymentBillState == 5) {
	    				paymentBill.setPaymentsCount(Integer.valueOf(data));
	    				payments = new BillPayment[paymentBill.getPaymentsCount()];
	    				for(int i = 0; i < payments.length; i++) {
	    					payments[i] = new BillPayment();
	    				}
	    				billPaymentsCounter = 0;
	    				
	    				mServer.sendMessage(Constants.ACK_PAYMENTS_COUNT);
	    				paymentBillState++;
		    		} else {
		    			mServer.sendMessage(Constants.CMD_PAYMENT_BILL_ERROR);
		    			paymentBillState = 0;
		    		}
	    		}
	    		break;
	    	}
    	} else if(msg.startsWith(Constants.KEY_PAYMENT_NUMBER.substring(0, 11)) && (paymentBillState > 5) && (paymentBillState < 10)) {
    		String cmd = msg.substring(0, msg.indexOf('>'));
        	String data = msg.substring((1 + msg.indexOf('>')), msg.length());
        	//System.out.println("Command: " + cmd);
            //System.out.println("Data: " + data);
    		handleBillPayment(cmd, data);
		}
    }
    
    public void handleBillPayment(String cmd, String data) {
    	int recCounter;
    	if(billPaymentsCounter < 10) {
    		recCounter = Integer.valueOf(cmd.substring(cmd.length()-1, cmd.length())); 
    	} else {
    		recCounter = Integer.valueOf(cmd.substring(cmd.length()-2, cmd.length())); 
    	}
    	//System.out.println("Items Length: " + payments.length + " ,Rec Counter: " + recCounter + " , billPaymentsCounter: " + billPaymentsCounter);
    	if((billPaymentsCounter == recCounter) && (recCounter <= payments.length)) {
	    	if (cmd.contains(Constants.KEY_PAYMENT_NUMBER)){
				if(mServer != null) {
					//System.out.println("Visited KEY_PAYMENT_Number with state = " + paymentBillState);
		    		if(paymentBillState == 6) {
						payments[billPaymentsCounter].setId(Integer.valueOf(data));
						paymentBillState++;
						mServer.sendMessage(Constants.ACK_PAYMENT_NUMBER);
						
		    		} else {
		    			paymentBillState = 0;
		    			mServer.sendMessage(Constants.CMD_PAYMENT_BILL_ERROR);
		    		}
				}
	    	} else if (cmd.contains(Constants.KEY_PAYMENT_DATE)){
				if(mServer != null) {
		    		if(paymentBillState == 7) {
						payments[billPaymentsCounter].setDate(data);
						paymentBillState++;
						mServer.sendMessage(Constants.ACK_PAYMENT_DATE);
						
		    		} else {
		    			paymentBillState = 0;
		    			mServer.sendMessage(Constants.CMD_PAYMENT_BILL_ERROR);
		    		}
				}
	    	} else if (cmd.contains(Constants.KEY_PAYMENT_DUE_AMOUNT)){
				if(mServer != null) {
					//System.out.println("Quantity visited with state: " + paymentBillState);
		    		if(paymentBillState == 8) {
						mServer.sendMessage(Constants.ACK_PAYMENT_DUE_AMOUNT);
						payments[billPaymentsCounter].setDueAmount(Double.valueOf(data));
						paymentBillState++;		
						
		    		} else {
		    			
		    			mServer.sendMessage(Constants.CMD_PAYMENT_BILL_ERROR);
		    			paymentBillState = 0;
		    		}
				}
	    	} else if (cmd.contains(Constants.KEY_PAYMENT_DONE_AMOUNT)){
				if(mServer != null) {
		    		if((paymentBillState == 9) && (billPaymentsCounter < (payments.length - 1))) {
		    			payments[billPaymentsCounter].setDoneAmount(Double.valueOf(data));
		    			paymentBillState = 6;
						billPaymentsCounter++;
						mServer.sendMessage(Constants.ACK_PAYMENT_DONE_AMOUNT);
		    		} else if((paymentBillState == 9) && (billPaymentsCounter == (payments.length - 1))) {
		    			payments[billPaymentsCounter].setDoneAmount(Double.valueOf(data));
		    			paymentBillState++;
		    			billPaymentsCounter = 0;
		    			//System.out.println("PaymentsLength" + payments.length);
		    			if(payments.length < 46) {
		    				paymentBill.setPayments(payments);
		    			} else {
		    				//System.out.println("Payments Length2: " + payments.length);
		    				double doubleValue = ((double) payments.length)/45;
		    				//System.out.println("Double Value: " + doubleValue);
		    				int partPaymentsLength = (int) doubleValue;
		    				//System.out.println("Part Payment Length2: " + partPaymentsLength);
		    				double tempPartPaymentsLength = (double) partPaymentsLength;
		    				//System.out.println("Temp Part Payment Length: " + tempPartPaymentsLength);
		    				if(doubleValue > tempPartPaymentsLength) {
		    					partPaymentsLength++;
		    				}
		    				//System.out.println("PartPaymentsLength: " + partPaymentsLength);
		    				partPayments = new BillPayment[partPaymentsLength][45];
		    				for(int i = 0; i < partPaymentsLength; i++) {
		    					for(int j = 0; j < 45 ; j++) {
		    						if(((i * 45) + j) < payments.length) {
		    							partPayments[i][j] = payments[(i * 45) + j];
		    						} else {
		    							partPayments[i][j] = new BillPayment();
		    						}
		    					}
		    				}
		    				paymentBill.setPayments(partPayments[0]);
		    				paymentPrintCounts = partPaymentsLength;
		    			}
	    					    				
		    			mServer.sendMessage(Constants.ACK_PAYMENT_DONE_AMOUNT);
		    		} else {
		    			mServer.sendMessage(Constants.CMD_PAYMENT_BILL_ERROR);
		    			paymentBillState = 0;
		    		}
				}
	    	}
    	} else {
    		mServer.sendMessage(Constants.CMD_PAYMENT_BILL_ERROR);
			paymentBillState = 0;
    	}
    }
    
    public void setIp(boolean firstTime, String prefix) {
    	try {
			@SuppressWarnings("rawtypes")
			Enumeration e = NetworkInterface.getNetworkInterfaces();
			while(e.hasMoreElements()) {
				NetworkInterface n =(NetworkInterface) e.nextElement();
				@SuppressWarnings("rawtypes")
				Enumeration ee = n.getInetAddresses();
				while(ee.hasMoreElements()) {
					InetAddress i = (InetAddress) ee.nextElement();
					if(i.getHostAddress().startsWith(prefix)) {
						//System.out.println(i.getHostAddress());
						ip = i.getHostAddress();
					}
				}
			}
			if(ip.length() > 9) {
	        	lblIpAddress.setText("IP Address: " + ip);
	        	if(!firstTime) {
		        	javax.swing.UIManager.put("OptionPane.messageFont",
							new FontUIResource(new Font("Times New Roman",
									Font.BOLD, 16)));
					JOptionPane
							.showMessageDialog(null, "IP Changed!");
	        	}
	        } else {
	        	if(!firstTime) {
		        	javax.swing.UIManager.put("OptionPane.messageFont",
							new FontUIResource(new Font("Times New Roman",
									Font.BOLD, 16)));
					JOptionPane
							.showMessageDialog(null, "IP NOT Changed!");
	        	} else {
	        		javax.swing.UIManager.put("OptionPane.messageFont",
							new FontUIResource(new Font("Times New Roman",
									Font.BOLD, 16)));
					JOptionPane
							.showMessageDialog(null, "Connection Problem, IP NOT Found!");
	        	}
	        }
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			if(!firstTime) {
	        	javax.swing.UIManager.put("OptionPane.messageFont",
						new FontUIResource(new Font("Times New Roman",
								Font.BOLD, 16)));
				JOptionPane
						.showMessageDialog(null, "Socket Problem, IP NOT Changed!");
        	} else {
        		javax.swing.UIManager.put("OptionPane.messageFont",
						new FontUIResource(new Font("Times New Roman",
								Font.BOLD, 16)));
				JOptionPane
						.showMessageDialog(null, "Socket Problem, IP NOT Found!");	
        	}
		}
    }
    
    
    public void startBillPrinting(PrintService service, boolean isOrderBill) {
		final PrintService tempRequiredService = service;
		PrintSupport ps = new PrintSupport();
		/*Item[] items = new Item[5];
		for(int i = 0; i < items.length; i++) {
			items[i] = new Item((i+1), "آئٹم کا نام", 2, 50);
		}
		CustomerOrderBill orderBill = new CustomerOrderBill(1, "22-09-2016", "جنید اسلم", "جنید کریانہ سٹور",
				items.length, items, 10, 400);
		*/
		
		PrinterJob pj = PrinterJob.getPrinterJob();
		
		if(tempRequiredService != null) {
			try {
				pj.setPrintService(tempRequiredService);
			} catch (PrinterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(isOrderBill) {
			pj.setPrintable(new OrderBillPrintable(orderBill), ps.getPageFormat(pj));
		} else {
			pj.setPrintable(new PaymentBillPrintable(paymentBill), ps.getPageFormat(pj));
		}
		try {
			pj.print();
		} catch (PrinterException ex) {
			ex.printStackTrace();
		}
    }
}