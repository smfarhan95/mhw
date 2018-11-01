import java.awt.EventQueue;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextPane;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

public class Mhw {

	private JFrame frame;
	private JTextField textField;
	private JTextArea textArea;
	private String strSetText="";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Mhw window = new Mhw();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Mhw() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 649, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(10, 58, 613, 105);
		frame.getContentPane().add(textArea);
		
		JButton btnGetAll = new JButton("Get All Item");
		btnGetAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread thread1 = new Thread(){
					public void run(){
						String strUrl = "https://mhw-db.com/items";
						JSONArray jArr = makeHttpRequest(strUrl,"GET");
						JSONObject jsnObj=null;
						try {
							for(int i = 0 ;i<jArr.length();i++) {
								jsnObj = jArr.getJSONObject(i);
								String id = jsnObj.get("id").toString();
								String name = jsnObj.getString("name");
								String desc = jsnObj.getString("description");
								String rarity = jsnObj.get("rarity").toString();
								String limit = jsnObj.get("carryLimit").toString();
								String value = jsnObj.get("value").toString();
								strSetText += "ID :"+id+
										" || Item Name :"+name+
										" || Description :"+desc+
										" || Rarity :"+rarity+
										" || Carry Limit :"+limit+
										" || Value :"+value+"\n";
							}
							textArea.setText(strSetText);
					
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					
					public JSONArray makeHttpRequest(String strUrl, String method) {
						InputStream is = null;
						String json = "";
						JSONArray jArr = null;
						
						try {
							DefaultHttpClient httpClient = new DefaultHttpClient();
							HttpGet httpGet = new HttpGet(strUrl);
							HttpResponse httpResponse = httpClient.execute(httpGet);
							HttpEntity httpEntity = httpResponse.getEntity();
							is = httpEntity.getContent();
							
							BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
							StringBuilder sb = new StringBuilder();
							String line = null;
							while((line = reader.readLine())!=null) 
								sb.append(line+"\n");
							is.close();
							json = sb.toString();
							jArr = new JSONArray(json);
							
						}	catch(JSONException e) {
							try {
								jArr = new JSONArray(json);
							}catch(JSONException e1) {
								e1.printStackTrace();
							}
						}	catch (Exception ee) {
							ee.printStackTrace();
						}
						return jArr;
					}
				};
				thread1.start();
			}
		});
		btnGetAll.setBounds(35, 11, 125, 23);
		frame.getContentPane().add(btnGetAll);
		
		textField = new JTextField();
		textField.setBounds(91, 196, 86, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblItemId = new JLabel("Item ID :");
		lblItemId.setBounds(35, 199, 46, 14);
		frame.getContentPane().add(lblItemId);
		
		JButton btnFind = new JButton("Find Item");
		btnFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread thread2 = new Thread(){
					public void run(){
						String params = textField.getText();
						String strUrl = "https://mhw-db.com/items";
						JSONObject jsnObj = makeHttpRequest(strUrl,"GET",params);
						try {
							String id = jsnObj.get("id").toString();
							String name = jsnObj.getString("name");
							String desc = jsnObj.getString("description");
							String rarity = jsnObj.get("rarity").toString();
							String limit = jsnObj.get("carryLimit").toString();
							String value = jsnObj.get("value").toString();
							strSetText += "ID :"+id+
									" || Item Name :"+name+
									" || Description :"+desc+
									" || Rarity :"+rarity+
									" || Carry Limit :"+limit+
									" || Value :"+value+"\n";
							textArea.setText(strSetText);
						}catch(JSONException e) {
							e.printStackTrace();
						}
					}
					
					public JSONObject makeHttpRequest(String strUrl, String method, String params) {
						InputStream is = null;
						String json = "";
						JSONObject jObj = null;
						
						try {
							DefaultHttpClient httpClient = new DefaultHttpClient();
							strUrl+="/"+params;
							HttpGet httpGet = new HttpGet(strUrl);
							HttpResponse httpResponse = httpClient.execute(httpGet);
							HttpEntity httpEntity = httpResponse.getEntity();
							is = httpEntity.getContent();
							
							BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
							StringBuilder sb = new StringBuilder();
							String line = null;
							while((line = reader.readLine())!=null) 
								sb.append(line+"\n");
							is.close();
							json = sb.toString();
							jObj = new JSONObject(json);
							
						}	catch(JSONException e) {
							try {
								JSONArray jArr = new JSONArray(json);
							}catch(JSONException e1) {
								e1.printStackTrace();
							}
						}	catch (Exception ee) {
							ee.printStackTrace();
						}
						return jObj;
					}
				};
				thread2.start();
			}
		});
		btnFind.setBounds(254, 195, 125, 23);
		frame.getContentPane().add(btnFind);
	}
}
