package top.lmoon.util;

/**
 * 平安银行交互金钱解析工具类，平安银行用"元"作单位
 * @author gzy
 * @date 2018年9月20日
 *
 */
public class PinganAmount {
	
	private String amountSrc;
	
	private long amountFen;
	
	private double amountYuan;
	
	private String dbAmountSrc;
	
	/**
	 * 平安银行交互金钱解析工具类构造方法，平安银行用"元"作单位
	 * @param amountSrc
	 */
	public PinganAmount(String amountSrc){
		this.amountSrc = amountSrc;
		amountYuan = Double.valueOf(amountSrc);
		double tranAmountFen = amountYuan * 100;// 本地系统用"分"作单位，银行系统交互时用"元"
		amountFen = (long)tranAmountFen;
		dbAmountSrc = EncrypterUtil.encrypt(String.valueOf(amountFen));
	}

	public String getAmountSrc() {
		return amountSrc;
	}

	public long getAmountFen() {
		return amountFen;
	}

	public double getAmountYuan() {
		return amountYuan;
	}

	/**
	 * 加密后本地系统存储的金额
	 * @return
	 */
	public String getDbAmountSrc() {
		return dbAmountSrc;
	}

}
