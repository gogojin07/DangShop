package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bean.Cart;
import bean.Order;
import bean.Product;

public class ProductDao {

	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	public ProductDao() {
		con = JdbcUtil.getConnection();
	}
	
	public void close() {
		JdbcUtil.close(rs, pstmt, con);		
	}
	
	public boolean insertProduct(Product product) {
		String sql = "INSERT INTO P (P_CODE, P_ID, P_NAME, P_PRICE, P_QTY,"
					+ " P_CONTENTS, P_DATE, P_ORGFILENAME, P_SYSFILENAME)"
					+ " VALUES (?||LPAD(P_SEQ.NEXTVAL,4,0),?,?,?,?,?,DEFAULT,?,?)";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setNString(1, product.getP_kind());
			pstmt.setNString(2, product.getP_id());
			pstmt.setNString(3, product.getP_name());
			pstmt.setInt(4, product.getP_price());
			pstmt.setInt(5, product.getP_qty());
			pstmt.setNString(6, product.getP_contents());
			pstmt.setNString(7, product.getP_orgFileName());
			pstmt.setNString(8, product.getP_sysFileName());			
			int result = pstmt.executeUpdate();
			con.commit();
			
			if(result != 0) {	//성공
				System.out.println("상품 DB등록 성공");
				return true;
			}			
		} catch (SQLException e) {
			System.out.println("상품등록 예외 발생");
			e.printStackTrace();
		}		
		return false;
	}

	public List<Product> getItemList(String kind) {
		String sql = "SELECT * FROM P WHERE P_CODE LIKE ?||'%' AND P_QTY>0 ORDER BY P_CODE DESC";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setNString(1, kind);
			rs = pstmt.executeQuery();
			
			List<Product> pList = new ArrayList<>();
			Product p;
			
			while(rs.next()) {
				p = new Product();
				p.setP_code(rs.getNString("P_CODE"));
				p.setP_name(rs.getNString("P_NAME"));
				p.setP_sysFileName(rs.getNString("P_SYSFILENAME"));
				p.setP_price(rs.getInt("P_PRICE"));
				pList.add(p);
			}
			return pList;
			
		} catch (SQLException e) {
			System.out.println("getItemList dao 예외 발생");
			e.printStackTrace();
		}
		return null;
	}

	public HashMap<String, String> getItemDetail(String pCode) {
		String sql = "SELECT P.*, TO_CHAR(P_DATE,'YY/MM/DD HH24:MI') P_DATE2 FROM P WHERE P_CODE = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setNString(1, pCode);
			rs = pstmt.executeQuery();
			
			HashMap<String, String> hMap = new HashMap<>();
			DecimalFormat df = new DecimalFormat("###,###,###");
			
			if(rs.next()) {
				hMap.put("date", rs.getNString("P_DATE2"));
				hMap.put("name", rs.getNString("P_NAME"));
				hMap.put("price", df.format(rs.getInt("P_PRICE")));
				hMap.put("qty", rs.getNString("P_QTY"));
				hMap.put("contents", rs.getNString("P_CONTENTS"));
				hMap.put("id", rs.getNString("P_ID"));
				hMap.put("sysFileName", rs.getNString("p_sysFileName"));
				return hMap;
			}			
		} catch (SQLException e) {
			System.out.println("getItemDetail dao 예외 발생");
			e.printStackTrace();
		}
		return null;
	}
	
	public Cart checkQty(String pCode, String c_id) {
		//장바구니에 물건이 이미 들어 있는지 없는지 확인하는 용
		Cart c = new Cart();
		try {
			String sql = "select * from cart where  c_id=? and c_p_code=? and c_qty>0";
			pstmt =con.prepareStatement(sql);
			pstmt.setString(1, c_id);
			pstmt.setString(2, pCode);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				c.setC_id(rs.getString("C_ID"));
				c.setC_p_code(rs.getString("c_p_code"));
				return c;
			}
		} catch (Exception e) {
			System.out.println("checkQty");
			e.printStackTrace();
		}
		return null;
	}	
	
	public boolean addCartPlus(String pCode, String c_id) {
		//장바구니에 물건이 이미 있어서 갯수만 더할 경우
		String sql = "update cart set c_qty=c_qty+1 where c_p_code=? and c_id=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, pCode);
			pstmt.setString(2, c_id);
			int result = pstmt.executeUpdate();
			con.commit();
			
			if (result != 0) {// 성공
				System.out.println("plus등록 성공");
				return true;
			}
		} catch (SQLException e) {
			System.out.println("실패");
			e.printStackTrace();
		}
		return false;
	}// add cart end

	public boolean addCart(String pCode, String c_id) {
		//장바구니에 물건이 없어서 새로 추가할경우
		String sql = "insert into c (c_p_code, c_id,c_qty) values (?,?,?)";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, pCode);
			pstmt.setString(2, c_id);
			pstmt.setString(3, "1");
			int result = pstmt.executeUpdate();
			con.commit();
			
			if (result != 0) {// 성공
				System.out.println("등록 성공");
				return true;
			}
		} catch (SQLException e) {
			System.out.println("실패");
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean addCart_gym(String pCode, String c_id) {
		String sql = "";
		//1.이미 등록한 상품인지 확인 -- 생략
		
		//2.등록한 상품이면 C_QTY+1 UPDATE
		sql = "UPDATE CART SET C_QTY=C_QTY+1 WHERE C_P_CODE=? AND C_ID=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setNString(1, pCode);
			pstmt.setNString(2, c_id);
			
			int result = 0;
			result = pstmt.executeUpdate();
			con.commit();
			System.out.println("update cnt : "+result);
			
			if(result == 0) {	//실행한 row가 없으면
				//3.등록한 상품이 아니면 INSERT
				sql = "INSERT INTO C (C_P_CODE, C_ID) VALUES (?,?)";
				try {
					pstmt = con.prepareStatement(sql);
					pstmt.setNString(1, pCode);
					pstmt.setNString(2, c_id);					
					result = pstmt.executeUpdate();
					con.commit();
					
					if(result != 0) {	//성공
						System.out.println("장바구니 DB등록 성공");
						return true;
					}			
				} catch (SQLException e) {
					System.out.println("장바구니등록 예외 발생");
					e.printStackTrace();
				}
			}	else {
				System.out.println("장바구니 DB-UPDATE 성공");
				return true;
			}			
		} catch (SQLException e) {
			System.out.println("장바구니등록 예외 발생");
			e.printStackTrace();
		}				
		return false;			
	}

	public List<Product> getCartList(String id) {
		String sql = "SELECT P_CODE, P_SYSFILENAME, P_NAME, C_QTY, C_QTY*P_PRICE AS PRICE, C_DATE FROM CART C JOIN PRODUCT P "
					+ " ON C.C_P_CODE=P.P_CODE "
					+ " WHERE C.C_ID=? "
					+ " ORDER BY C_DATE DESC";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setNString(1, id);
			rs = pstmt.executeQuery();
			List<Product> cList = new ArrayList<>();
			Product p;
			
			while(rs.next()) {
				p = new Product();
				p.setP_code(rs.getNString("P_CODE"));
				p.setP_sysFileName(rs.getNString("P_SYSFILENAME"));
				p.setP_name(rs.getNString("P_NAME"));
				p.setP_qty(rs.getInt("C_QTY"));
				p.setP_price(rs.getInt("PRICE"));
				cList.add(p);
			}
			System.out.println("cart list.size : "+cList.size());
			return cList;
			
		} catch (SQLException e) {
			System.out.println("장바구니 리스트 예외 발생");
			e.printStackTrace();
		}	
		return null;
	}

	public boolean delCart(String[] pCodes, String c_id) {
		String sql = "DELETE FROM CART WHERE C_ID=? AND C_P_CODE=?";
		int result = 0;

		for(String pcode : pCodes) {
			try {
				pstmt = con.prepareStatement(sql);
				pstmt.setNString(1, c_id);
				pstmt.setNString(2, pcode);				
				result = pstmt.executeUpdate();
				
				if(result != 0) {	//성공
					result += 1;
				}
				con.commit();
			} catch (SQLException e) {
				System.out.println("장바구니등록 예외 발생");
				e.printStackTrace();
			}
		}
		if(result > 0) {
			System.out.println("장바구니 삭제 => "+ (result-1) +" 건 완료");
			return true;
		} else 	return false;
	}

	public boolean addOrder(List<Product> oList, String o_id) {
		//1.주문(ORDER2) 데이터 등록
		String sql = ""; 
		sql = "INSERT INTO O (O_NO, O_ID) VALUES (SEQ_O_NO.NEXTVAL, ?)";
		
		int result = 0;
		int total_price = 0;		
		String[] pCodes = new String[oList.size()];
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setNString(1, o_id);
			
			result = pstmt.executeUpdate();
			if(result != 0) {	//성공
				System.out.println("ORDER2 등록 성공");
				
				//2.주문 상세(ORDER_DETAIL) 등록
				sql = "INSERT INTO OD (O_NO, P_CODE, O_QTY, OD_PRICE) VALUES (SEQ_O_NO.CURRVAL, ?, ?, ?)";
				int cnt = 0;
				
				for(Product p : oList) {
					try {
						pstmt = con.prepareStatement(sql);
						pstmt.setNString(1, p.getP_code());
						pstmt.setInt(2, p.getP_qty());
						pstmt.setInt(3, p.getP_qty()*p.getP_price());	
						
						total_price += (p.getP_qty()*p.getP_price());	
						pCodes[cnt] = p.getP_code();
						cnt++;
						result = pstmt.executeUpdate();
						con.commit();
						
						if(result != 0) {	//성공
							System.out.println("order_detail 등록 성공");
							
							//3.상품 재고 UPDATE
							if(!updateP_Qty(p.getP_code(), p.getP_qty()))	System.out.println(p.getP_code()+" 상품 재고 업데이트 실패");
						}
					} catch (SQLException e) {
						System.out.println("ORDER_DETAIL 등록 예외 발생");
						e.printStackTrace();
					}
				}
				//4.SEQ_O_NO.CURRVAL 추출
				sql = "SELECT SEQ_O_NO.CURRVAL AS SEQ_O_NO FROM DUAL";
				int seq_o_no = 0;
				try {
					pstmt = con.prepareStatement(sql);
					rs = pstmt.executeQuery();
					
					if(rs.next()) {
						seq_o_no = rs.getInt("SEQ_O_NO");
					}
					
				} catch (SQLException e) {
					System.out.println("SEQ_O_NO.CURRVAL 추출 예외 발생");
					e.printStackTrace();
				}
				
				//5.ORDER2 테이블 : total_price 등록
				try {
					System.out.println("total_price => "+ total_price);
					sql = "UPDATE ORDER2 SET O_PRICE=? WHERE O_NO=?";
					pstmt = con.prepareStatement(sql);
					pstmt.setInt(1, total_price);
					pstmt.setInt(2, seq_o_no);
					result = pstmt.executeUpdate();
					
					if(result != 0) {	//성공
						System.out.println("ORDER2 : O_PRICE 등록 성공");
					}					
				} catch (SQLException e) {
					System.out.println("ORDER2 : O_PRICE 등록 예외 발생");
					e.printStackTrace();
				}
				
				//6.장바구니 데이터 삭제
				if(!delCart(pCodes, o_id))	System.out.println("장바구니 데이터 삭제 실패");
							
				con.commit();
				return true;
			}
		} catch (SQLException e) {
			System.out.println("ORDER2 등록 예외 발생");
			e.printStackTrace();
		}
		return false;
	}

	private boolean updateP_Qty(String p_code, int o_qty) {
		int result = 0;
		String sql = "UPDATE P SET P_QTY=P_QTY-? WHERE P_CODE=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, o_qty);
			pstmt.setNString(2, p_code);
			result = pstmt.executeUpdate();
			con.commit();
			if(result != 0)	return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return false;
	}

	public List<Order> getOrderList(String o_id) {
		String sql = "SELECT * FROM O WHERE O_ID=? ORDER BY O_NO DESC";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setNString(1, o_id);
			rs = pstmt.executeQuery();
			List<Order> oList = new ArrayList<>();
			Order o;
			
			while(rs.next()) {
				o = new Order();
				o.setO_id(rs.getNString("O_ID"));
				o.setO_date(rs.getNString("O_DATE"));
				o.setO_no(rs.getInt("O_NO"));
				o.setO_price(rs.getInt("O_PRICE"));
				oList.add(o);
			}
			System.out.println("order2 list.size : "+ oList.size());
			return oList;
			
		} catch (SQLException e) {
			System.out.println("주문 리스트 예외 발생");
			e.printStackTrace();
		}	
		return null;
	}

	public List<Product> getOrderDetail(int o_no) {
		String sql = "SELECT OD.P_CODE AS P_CODE, O_QTY, OD_PRICE, S_NAME, P_NAME, P_ID, P_SYSFILENAME "
					+ "FROM OD JOIN OS ON OD.S_CODE=OS.S_CODE "
					+ "JOIN P ON OD.P_CODE=P.P_CODE "
					+ "WHERE O_NO=?";		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, o_no);
			rs = pstmt.executeQuery();
			List<Product> pList = new ArrayList<>();
			Product p;
			
			while(rs.next()) {
				p = new Product();
				p.setP_code(rs.getNString("P_CODE"));
				p.setP_id(rs.getNString("P_ID"));
				p.setP_sysFileName(rs.getNString("P_SYSFILENAME"));
				p.setP_name(rs.getNString("P_NAME"));
				p.setP_qty(rs.getInt("O_QTY"));
				p.setP_price(rs.getInt("OD_PRICE"));
				p.setS_name(rs.getNString("S_NAME"));
				pList.add(p);
			}
			System.out.println("OrderDetail list.size : "+ pList.size());
			return pList;
			
		} catch (SQLException e) {
			System.out.println("주문 상세리스트 예외 발생");
			e.printStackTrace();
		}		
		return null;
	}

	public List<Product> getSalesList(String id) {
		String sql = "SELECT OD.P_CODE AS P_CODE, OD.O_NO AS O_NO, O_QTY, OD_PRICE, OD.S_CODE AS S_CODE, O_ID, O_DATE, P_NAME, P_SYSFILENAME, S_NAME "
					+ "FROM OD JOIN OS ON OD.S_CODE=OS.S_CODE "
					+ "JOIN P ON OD.P_CODE=P.P_CODE "
					+ "JOIN O ON OD.O_NO=O.O_NO "
					+ "WHERE P_ID=? "
					+ "ORDER BY O_NO DESC";		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setNString(1, id);
			rs = pstmt.executeQuery();
			List<Product> pList = new ArrayList<>();
			Product p;
			
			while(rs.next()) {
				p = new Product();
				p.setP_code(rs.getNString("P_CODE"));			//제품번호
				p.setO_no(rs.getInt("O_NO"));					//주문번호
				p.setP_qty(rs.getInt("O_QTY"));						//수량
				p.setP_price(rs.getInt("OD_PRICE"));				//가격
				p.setS_code(rs.getInt("S_CODE"));				//상태코드
				p.setP_id(rs.getNString("O_ID"));					//주문자
				p.setP_date(rs.getNString("O_DATE"));				//주문날짜
				p.setP_name(rs.getNString("P_NAME"));				//제품명
				p.setP_sysFileName(rs.getNString("P_SYSFILENAME"));	//파일명
				p.setS_name(rs.getNString("S_NAME"));				//주문상태
				pList.add(p);
			}
			System.out.println("Sales list.size : "+ pList.size());
			return pList;
			
		} catch (SQLException e) {
			System.out.println("판매리스트 예외 발생");
			e.printStackTrace();
		}		
		return null;
	}

	public boolean chgScode(String[] scode_arr) {
		String sql = "UPDATE ORDER_DETAIL SET S_CODE=? WHERE O_NO=? AND P_CODE=? ";
		int result = 0;
		//scode_arr : [0]O_NO, [1]P_CODE, [2]S_CODE
		//System.out.println("scode_arr[0]="+scode_arr[0] +" [1]="+scode_arr[1]+" [2]"+scode_arr[2]);
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(scode_arr[2]));
			pstmt.setInt(2, Integer.parseInt(scode_arr[0]));
			pstmt.setNString(3, scode_arr[1]);
			result = pstmt.executeUpdate();
			con.commit();
			
			if(result != 0) {
				System.out.println("주문상태 업데이트 성공");
				con.commit();
				return true;
			}			
		} catch (SQLException e) {
			System.out.println("주문상태 업데이트 예외 발생");
			e.printStackTrace();
		}
		return false;
	}

	public HashMap<String, String> userInfo(String userId) {
		String sql = "SELECT NAME, PHONE, ADDR FROM M WHERE ID = ?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setNString(1, userId);
			rs = pstmt.executeQuery();
			
			HashMap<String, String> hMap = new HashMap<>();
			
			if(rs.next()) {
				hMap.put("name", rs.getNString("NAME"));
				hMap.put("phone", rs.getNString("PHONE"));
				hMap.put("addr", rs.getNString("ADDR"));
				return hMap;
			}			
		} catch (SQLException e) {
			System.out.println("userInfo dao 예외 발생");
			e.printStackTrace();
		}
		return null;
	}
}
