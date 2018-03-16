package com.carl_yang.meifuoa;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.carl_yang.tools.Common;

import java.util.TimerTask;

@SuppressLint("HandlerLeak")
public abstract class MeifuActivity extends AppCompatActivity {
	protected ProgressDialog proDialog = null;
	protected int timeValue = 0;
	protected int timeOutVal = 1;
	protected static final int TIME_OUT = 1001;

	protected boolean isNetAvilable() {
		if (!Common.isNetAvilable(this)) {
			Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}
	}

	protected void showProDialog() {
		proDialog = ProgressDialog.show(this, "", "请稍后...");
		proDialog.setCancelable(false);
		proDialog.setCanceledOnTouchOutside(false);
	}

	protected void showProDialog(String msg, String title) {
		proDialog = ProgressDialog.show(this, title, msg);
	}

	@SuppressWarnings("unused")
	private class Task extends TimerTask {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (timeValue > timeOutVal) {
				handler.sendEmptyMessage(0);
			}
			timeValue++;
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				cancelProDialog();
				Toast.makeText(MeifuActivity.this, "网络不给力!",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	protected void cancelProDialog() {
		if (proDialog != null && proDialog.isShowing()) {
			proDialog.dismiss();
			proDialog = null;
		}
	}

	protected boolean isGpsAvilable(int flag) {
		LocationManager alm = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		if (!alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			if (flag == 1) {
				builder.setTitle("温馨提示")
						.setMessage("为了更精准的定位,请打开GPS!")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
			} else {
				builder.setTitle("提示")
						.setMessage("请打开GPS!")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
			}
			builder.create().show();
			return false;
		}
		return true;
	}
}
