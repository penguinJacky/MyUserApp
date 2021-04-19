package com.example.myuserapp.customdialog;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.example.myuserapp.R;
import com.example.myuserapp.bean.MyUser;
import com.example.myuserapp.model.DataBaseHelper;
import com.example.myuserapp.model.IGenerateAddUserListener;
import com.example.myuserapp.model.OperationType;

import java.lang.ref.WeakReference;

public class MyActionDialog implements View.OnClickListener {

    private Context mContext;
    private AlertDialog mDialog;
    private IGenerateAddUserListener mListener;
    private MyUser user;

    public MyActionDialog(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_btn:
                mDialog.dismiss();
                new MyUserDialog(mContext, mListener, user, OperationType.UPDATE);
                break;
            case R.id.delete_btn:
                mDialog.dismiss();
                DeleteUserAsyncTask deleteUserAsyncTask = new DeleteUserAsyncTask(mContext);
                deleteUserAsyncTask.execute(user.getId());
                mListener.getUser(null);
                break;
            default:
                break;
        }
    }

    public void createDialogAndShow(MyUser users, IGenerateAddUserListener listener) {
        this.mListener = listener;
        this.user = users;
        View actionDialog = LayoutInflater.from(mContext).inflate(R.layout.action_dialog, null);
        Button editBtn = actionDialog.findViewById(R.id.edit_btn);
        Button deleteBtn = actionDialog.findViewById(R.id.delete_btn);
        editBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(actionDialog);
        mDialog = builder.create();
        mDialog.show();
    }

    private static class DeleteUserAsyncTask extends AsyncTask<Integer, Void, Void> {

        private WeakReference<Context> contextWeakReference;

        public DeleteUserAsyncTask(Context context) {
            contextWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            Context context = contextWeakReference.get();
            if (context != null) {
                new DataBaseHelper(context).deleteUser(integers[0]);
            }
            return null;
        }
    }
}
