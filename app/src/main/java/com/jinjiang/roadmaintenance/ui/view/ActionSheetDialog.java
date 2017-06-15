package com.jinjiang.roadmaintenance.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jinjiang.roadmaintenance.R;

public class ActionSheetDialog {

	public interface OnActionSheetSelected {
		void onClick(int whichButton);
	}

	private ActionSheetDialog() {
	}

	public static Dialog showSheet(Context context, final OnActionSheetSelected actionSheetSelected, final OnCancelListener cancelListener) {
		final Dialog dlg = new Dialog(context, R.style.ActionSheet);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.item_actionsheetdialog, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		TextView mCamera = (TextView) layout.findViewById(R.id.text_camera);
		TextView mGallery = (TextView) layout.findViewById(R.id.text_gallery);
		TextView mCancel = (TextView) layout.findViewById(R.id.cancel);

		mCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionSheetSelected.onClick(0);
				dlg.dismiss();
			}
		});
		
		mGallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				actionSheetSelected.onClick(1);
				dlg.dismiss();
			}
		});

		mCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelListener.onCancel(dlg);
				dlg.dismiss();
			}
		});

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(false);
		if (cancelListener != null)
			dlg.setOnCancelListener(cancelListener);

		dlg.setContentView(layout);
		dlg.show();

		return dlg;
	}

}
