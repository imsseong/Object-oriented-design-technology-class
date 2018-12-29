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
	boolean editmode = false; // 현재 상태가 데이터 조회 후 상태인지, 새로운 데이터 입력하기 위한 상태인지 설정

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

		lbl = new JLabel("프로그램이 시작되었습니다!!");
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

		lbl_code = new JLabel("관리번호");
		lbl_name = new JLabel("상품명");
		lbl_pric = new JLabel("단가");
		lbl_manuf = new JLabel("제조사");
		

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

		btn_enroll = new JButton("등록");
		btn_look = new JButton("조회");
		btn_del = new JButton("삭제");

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
				
				if(obj == btn_enroll) { //등록버튼
					product.setPrname(txt1.getText());
					product.setPrice(Integer.parseInt(txt2.getText()));
					product.setManufacture(txt3.getText());
					
					//수정일 때
					if(editmode) {
						product.setPrcode(Integer.parseInt((String)cb.getSelectedItem()));
						if(dao.updateProduct(product)) {
							lbl.setText("상품을 수정했습니다!!");
							clearField();
							editmode = false;
						} else {
							lbl.setText("상품 수정이 실패했습니다!!");
							}
							 
						//등록일 때
					} else {
						if(dao.newProduct(product)) {
							lbl.setText("상품을 등록했습니다!!");
						} else {
							lbl.setText("상품 등록이 실패했습니다!!");
							}
					}
					refreshData();
					
				} else if(obj == btn_look) { //조회버튼
					String s = (String)cb.getSelectedItem();
					if(!s.equals("전체")) {
						product = dao.getProduct(Integer.parseInt(s));
						
						if(product != null) {
							lbl.setText("상품 정보를 가져왔습니다!!");
							txt1.setText(product.getPrname());
							txt2.setText(String.valueOf(product.getPrice()));
							txt3.setText(product.getManufacture());
							editmode = true;
						} else {
							lbl.setText("상품이 검색되지 않았습니다!!");
							}	
					}
					
				} else if(obj == btn_del) { //삭제버튼
					String s = (String)cb.getSelectedItem();
					if(s.equals("전체")) {
						lbl.setText("전체 삭제는 되지 않습니다!!");
					} else {
						if(dao.delProduct(Integer.parseInt(s))) {
							lbl.setText("상품이 삭제되었습니다!!");
						} else {
							lbl.setText("상품이 삭제되지 않았습니다!!");
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

		txtArea.append("관리번호\t상품명\t단가\t제조사\n");
		datas = dao.getAll();

		// 데이터를 변경하면 콤보박스 데이터 갱신
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
			txtArea.append("등록된 상품이 없습니다. \n 상품을 등록해주세요!!");
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
