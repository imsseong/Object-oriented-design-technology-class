
// 상품 정보 테이블 데이터 표현을 위한 클래스
public class Product {
	
	private int prcode;
	private String prname;
	private int price;
	private String manufacture;
	
	public int getPrcode() {
		return prcode;
	}
	public void setPrcode(int prcode) {
		this.prcode = prcode;
	}
	public String getPrname() {
		return prname;
	}
	public void setPrname(String prname) {
		this.prname = prname;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getManufacture() {
		return manufacture;
	}
	public void setManufacture(String manufacture) {
		this.manufacture = manufacture;
	}
	
	public String toString() {
		return "prcode -> " + prcode + " prname -> " + prname + " price -> " + price + " manufacture -> " + manufacture;
	}
}
