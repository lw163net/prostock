package com.liwei.youmi;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import net.youmi.android.appoffers.EarnedPointsNotifier;
import net.youmi.android.appoffers.EarnedPointsOrder;

/**
 * 
 * (绀轰緥)锟?锟斤拷镄勮嚜瀹氢箟绉垎绠＄悊绫伙紝鍦ㄨ繖閲屼娇鐢ㄦ湰鍦版枃浠惰繘琛岀亩鍗旷殑绉垎鎿崭綔锟?镇ㄥ彲浠ヤ娇鐢ㄦ湰鍦版枃浠舵垨浣跨敤浜戠链嶅姟鍣ㄥ瓨鍌ㄧН鍒嗭紝骞朵笖浣跨敤镟村姞瀹夊叏镄勬柟寮忔潵杩涜绠＄悊锟? * 
 */
public class MyPointsManager implements EarnedPointsNotifier {

	private static final String KEY_FILE_POINTS="ProstockPoints";
	private static final String KEY_POINTS="prosotckpoints";
	private static final String KEY_FILE_ORDERS="ProstockOrders";


	private static MyPointsManager instance;

	public static MyPointsManager getInstance() {
		if (instance == null) {
			instance = new MyPointsManager();
		}

		return instance;
	}

	@Override
	public void onEarnedPoints(Context context,
			List pointsList) {
		// TODO Auto-generated method stub
		try {

			if (pointsList != null) {

				for (int i = 0; i < pointsList.size(); i++) {

					// 灏嗙Н鍒嗗瓨鍌ㄥ埌镊畾涔夌Н鍒呜处鎴蜂腑
					storePoints(context, (EarnedPointsOrder) pointsList.get(i));

					// (鍙拷?)澶勭悊鎴栧瓨鍌ㄧН鍒呜幏鍙栬锟?					recordOrder(context, (EarnedPointsOrder) pointsList.get(i));

				}

			} else {
				infoMsg("onPullPoints:pointsList is null");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 镆ヨ绉垎
	 * 
	 * @param context
	 * @return
	 */
	public int queryPoints(Context context) {
		SharedPreferences sp = context.getSharedPreferences(KEY_FILE_POINTS,
				Context.MODE_PRIVATE);

		return sp.getInt(KEY_POINTS, 0);
	}

	/**
	 * 娑堣垂绉垎
	 * 
	 * @param context
	 * @param amount
	 * @return
	 */
	public boolean spendPoints(Context context, int amount) {
		if (amount <= 0) {
			return false;
		}

		SharedPreferences sp = context.getSharedPreferences(KEY_FILE_POINTS,
				Context.MODE_PRIVATE);

		int p = sp.getInt(KEY_POINTS, 0);
		if (p < amount) {
			return false;
		}

		p -= amount;

		return sp.edit().putInt(KEY_POINTS, p).commit();
	}

	/**
	 * 濂栧姳绉垎
	 * 
	 * @param context
	 * @param amount
	 * @return
	 */
	public boolean awardPoints(Context context, int amount) {
		if (amount <= 0) {
			return false;
		}
		SharedPreferences sp = context.getSharedPreferences(KEY_FILE_POINTS,
				Context.MODE_PRIVATE);

		int p = sp.getInt(KEY_POINTS, 0);

		p += amount;

		return sp.edit().putInt(KEY_POINTS, p).commit();
	}


	/**
	 * 瀛桦偍绉垎
	 * 
	 * @param context
	 * @param order
	 */
	private void storePoints(Context context, EarnedPointsOrder order) {
		try {
			if (order != null) {

				if (order.getPoints() > 0) {
					// 灏嗙Н鍒嗗姞鍏ョН鍒呜处鎴蜂腑锛岃繖閲屽亣璁剧Н鍒呜处鎴锋槸瀛桦偍鍦ㄦ湰锟?
					SharedPreferences sp = context.getSharedPreferences(
							KEY_FILE_POINTS, Context.MODE_PRIVATE);

					int p = sp.getInt(KEY_POINTS, 0);
					p += order.getPoints();

					sp.edit().putInt(KEY_POINTS, p).commit();

				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void recordOrder(Context context, EarnedPointsOrder order) {
		try {
			if (order != null) {
				// 鍙互澶勭悊杩欎簺璁㈠崟璇︾粏淇℃伅锛岃繖閲屽彧鏄綔涓虹亩鍗旷殑璁板綍.
				StringBuilder stringBuilder = new StringBuilder(256);
				stringBuilder.append("[").append("璁㈠崟锟?=> ")
						.append(order.getOrderId()).append("]\t[")
						.append("娓犻亾锟?=> ").append(order.getChannelId())
						.append("]\t[").append("璁剧疆镄勭敤鎴稩d(md5) => ")
						.append(order.getUserId()).append("]\t[")
						.append("銮峰缑镄勭Н锟?=> ").append(order.getPoints())
						.append("]\t[")
						.append("銮峰缑绉垎镄勭被锟?1涓烘湁鏀跺叆镄勭Н鍒嗭紝2涓烘棤鏀跺叆镄勭Н锟? => ")
						.append(order.getStatus()).append("]\t[")
						.append("绉垎镄勭粨绠楁椂锟?镙兼滠濞佹不镞堕棿锛屽崟浣岖) => ")
						.append(order.getTime()).append("]\t[")
						.append("链銮峰缑绉垎镄勬弿杩颁俊锟?=> ").append(order.getMessage())
						.append("]");

				String msg = stringBuilder.toString();

				SharedPreferences sp = context.getSharedPreferences(KEY_FILE_ORDERS,
						Context.MODE_PRIVATE);

				Editor editor = sp.edit();
				editor.putString(
						order.getOrderId() != null ? order.getOrderId() : Long
								.toString(System.currentTimeMillis()), msg);

				editor.commit();

				infoMsg(msg);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void errMsg(String msg) {
		Log.e("MyPointsManager", msg);
	}

	private void infoMsg(String msg) {
		Log.e("MyPointsManager", msg);
	}

}
