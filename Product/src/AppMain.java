import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;

public class AppMain extends JPanel {

	JFrame frame;
	JLabel lbl;
	JLabel lbl_code;
	JLabel lbl_name;
	JLabel lbl_pric;
	JLabel lbl_manuf;
	JPanel panel1;
	JPanel panel2;
	JPanel panel3;
	JTextArea txtArea;
	JScrollPane scroll;
	JComboBox cb;
	ProductDAO dao;
	ArrayList<Product> datas = null;
	boolean editmode = false; // ���� ���°� ������ ��ȸ �� ��������, ���ο� ������ �Է��ϱ� ���� �������� ����

	JButton btn_enroll;
	JButton btn_look;
	JButton btn_del;

	JTextField txt1;
	JTextField txt2;
	JTextField txt3;

	public AppMain() {
		dao = new ProductDAO();
		startUI();
		try {
			refreshData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} // AppMain()

	public void startUI() {
		frame = new JFrame("PRODUCT MANAGEMENT");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		lbl = new JLabel("���α׷��� ���۵Ǿ����ϴ�!!");
		panel1 = new JPanel();
		panel2 = new JPanel();
		panel3 = new JPanel();
		txtArea = new JTextArea(10, 40);
		scroll = new JScrollPane(txtArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		panel2.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		panel3.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		frame.add(lbl, BorderLayout.PAGE_START);
		frame.add(panel1, BorderLayout.LINE_START);
		frame.add(panel2, BorderLayout.CENTER);
		frame.add(scroll, BorderLayout.LINE_END);
		frame.add(panel3, BorderLayout.PAGE_END);

		lbl_code = new JLabel("������ȣ");
		lbl_name = new JLabel("��ǰ��");
		lbl_pric = new JLabel("�ܰ�");
		lbl_manuf = new JLabel("������");
		

		panel1.setLayout(new GridLayout(4, 1, 20, 30));
		panel1.add(lbl_code);
		panel1.add(lbl_name);
		panel1.add(lbl_pric);
		panel1.add(lbl_manuf);

		cb = new JComboBox();
		txt1 = new JTextField();
		txt2 = new JTextField();
		txt3 = new JTextField();

		panel2.setLayout(new GridLayout(4, 1, 0, 30));
		panel2.add(cb);
		panel2.add(txt1);
		panel2.add(txt2);
		panel2.add(txt3);

		btn_enroll = new JButton("���");
		btn_look = new JButton("��ȸ");
		btn_del = new JButton("����");

		panel3.add(btn_enroll);
		panel3.add(btn_look);
		panel3.add(btn_del);

		btn_enroll.addMouseListener(new addMouseListener());
		btn_look.addMouseListener(new addMouseListener());
		btn_del.addMouseListener(new addMouseListener());

		frame.setSize(700, 500);
		frame.setVisible(true);

	} // startUI()

	private class addMouseListener implements MouseListener {
		public void mouseClicked(MouseEvent event) {
		} // mouseClicked()

		public void mouseEntered(MouseEvent event) {
		} // mouseEntered()

		public void mouseExited(MouseEvent event) {
		} // mouseExited()

		public void mousePressed(MouseEvent event) {
		} // mousePressed()

		public void mouseReleased(MouseEvent event) {
			Object obj = event.getSource();
			Product product = new Product();
			
			try {
				
				if(obj == btn_enroll) { //��Ϲ�ư
					product.setPrname(txt1.getText());
					product.setPrice(Integer.parseInt(txt2.getText()));
					product.setManufacture(txt3.getText());
					
					//������ ��
					if(editmode) {
						product.setPrcode(Integer.parseInt((String)cb.getSelectedItem()));
						if(dao.updateProduct(product)) {
							lbl.setText("��ǰ�� �����߽��ϴ�!!");
							clearField();
							editmode = false;
						} else {
							lbl.setText("��ǰ ������ �����߽��ϴ�!!");
							}
							 
						//����� ��
					} else {
						if(dao.newProduct(product)) {
							lbl.setText("��ǰ�� ����߽��ϴ�!!");
						} else {
							lbl.setText("��ǰ ����� �����߽��ϴ�!!");
							}
					}
					refreshData();
					
				} else if(obj == btn_look) { //��ȸ��ư
					String s = (String)cb.getSelectedItem();
					if(!s.equals("��ü")) {
						product = dao.getProduct(Integer.parseInt(s));
						
						if(product != null) {
							lbl.setText("��ǰ ������ �����Խ��ϴ�!!");
							txt1.setText(product.getPrname());
							txt2.setText(String.valueOf(product.getPrice()));
							txt3.setText(product.getManufacture());
							editmode = true;
						} else {
							lbl.setText("��ǰ�� �˻����� �ʾҽ��ϴ�!!");
							}	
					}
					
				} else if(obj == btn_del) { //������ư
					String s = (String)cb.getSelectedItem();
					if(s.equals("��ü")) {
						lbl.setText("��ü ������ ���� �ʽ��ϴ�!!");
					} else {
						if(dao.delProduct(Integer.parseInt(s))) {
							lbl.setText("��ǰ�� �����Ǿ����ϴ�!!");
						} else {
							lbl.setText("��ǰ�� �������� �ʾҽ��ϴ�!!");
							}
						}
					
					refreshData();
			
				}
			} catch(Exception e) {
					e.printStackTrace();
				}
		} // mouseReleased()
	} // addMouseListener class

	public void refreshData() throws SQLException {
		txtArea.setText("");
		// clearFiled();
		editmode = false;

		txtArea.append("������ȣ\t��ǰ��\t�ܰ�\t������\n");
		datas = dao.getAll();

		// �����͸� �����ϸ� �޺��ڽ� ������ ����
		cb.setModel(new DefaultComboBoxModel(dao.getItems()));

		if (datas != null) {
			for (Product p : datas) {
				StringBuffer sb = new StringBuffer();
				sb.append(p.getPrcode() + "\t");
				sb.append(p.getPrname() + "\t");
				sb.append(p.getPrice() + "\t");
				sb.append(p.getManufacture() + "\t\n");

				txtArea.append(sb.toString());

			}
		} else {
			txtArea.append("��ϵ� ��ǰ�� �����ϴ�. \n ��ǰ�� ������ּ���!!");
		}
	} // refreshData()

	public void clearField() {
		txtArea.append("");
	} // clearFiled()

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub

		AppMain am = new AppMain();

	} // main

} // AppMain class
