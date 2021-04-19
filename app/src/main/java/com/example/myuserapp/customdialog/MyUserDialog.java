package com.example.myuserapp.customdialog;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.myuserapp.R;
import com.example.myuserapp.bean.MyUser;
import com.example.myuserapp.model.DataBaseHelper;
import com.example.myuserapp.model.IGenerateAddUserListener;
import com.example.myuserapp.model.OperationType;

import java.lang.ref.WeakReference;

public class MyUserDialog implements View.OnClickListener {

    private final Context context;
    private AlertDialog alertDialog;
    private TextView dialogTitle;
    private EditText userName;
    private CheckBox maleCb;
    private CheckBox femaleCb;
    private EditText userPhone;
    private EditText userAddress;
    private Button yesBtn;
    private Button noBtn;
    private IGenerateAddUserListener mListener;
    private MyUser oldUser;
    private final OperationType operationType;

    public MyUserDialog(Context context, IGenerateAddUserListener mListener, MyUser oldUser,
                        OperationType operationType) {
        this.context = context;
        this.mListener = mListener;
        this.oldUser = oldUser;
        this.operationType = operationType;
        createAddDialogAndShow();
    }

    public MyUserDialog(Context context, IGenerateAddUserListener mListener,
                        OperationType operationType) {
        this.context = context;
        this.mListener = mListener;
        this.operationType = operationType;
        createAddDialogAndShow();
    }

    public MyUserDialog(Context context, MyUser oldUser, OperationType operationType) {
        this.context = context;
        this.oldUser = oldUser;
        this.operationType = operationType;
        createAddDialogAndShow();
    }

    public void createAddDialogAndShow() {
        View view = LayoutInflater.from(context).inflate(R.layout.add_user_dialog, null);
        findView(view);
        initOperationType();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        alertDialog = builder.create();
        if (operationType.equals(OperationType.QUERY)) {
            alertDialog.setCanceledOnTouchOutside(true);
        } else {
            alertDialog.setCanceledOnTouchOutside(false);
        }
        alertDialog.show();
    }

    private void findView(View v) {
        dialogTitle = v.findViewById(R.id.dialog_title);
        userName = v.findViewById(R.id.user_name);
        maleCb = v.findViewById(R.id.male_cb);
        femaleCb = v.findViewById(R.id.female_cb);
        userPhone = v.findViewById(R.id.user_phone);
        userAddress = v.findViewById(R.id.user_address);
        yesBtn = v.findViewById(R.id.yes_btn);
        noBtn = v.findViewById(R.id.no_btn);
        maleCb.setOnClickListener(this);
        femaleCb.setOnClickListener(this);
        yesBtn.setOnClickListener(this);
        noBtn.setOnClickListener(this);
    }

    private void initOperationType() {
        switch (operationType) {
            case INSERT:
                dialogTitle.setText("添加用户数据");
                break;
            case QUERY:
                dialogTitle.setText("查询用户数据");
                initViewContent();
                yesBtn.setVisibility(View.GONE);
                yesBtn.setEnabled(false);
                noBtn.setVisibility(View.GONE);
                noBtn.setEnabled(false);
                userName.setEnabled(false);
                maleCb.setEnabled(false);
                femaleCb.setEnabled(false);
                userPhone.setEnabled(false);
                userAddress.setEnabled(false);
                break;
            case UPDATE:
                dialogTitle.setText("更新用户数据");
                initViewContent();
                break;
            default:
                break;
        }
    }

    private void initViewContent() {
        if (oldUser != null) {
            userName.setText(oldUser.getName());
            userPhone.setText(oldUser.getPhone());
            userAddress.setText(oldUser.getAddress());
            if (oldUser.getGender()) {
                maleCb.setChecked(true);
            } else {
                femaleCb.setChecked(true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.yes_btn:
                MyUser myNewUser = generateNewUser();
                if (myNewUser == null) {
                    Toast.makeText(context, "请输入数据!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (operationType.equals(OperationType.UPDATE)) {
                    updateUser(myNewUser);
                } else if (operationType.equals(OperationType.INSERT)) {
                    insertUser(myNewUser);
                }
                alertDialog.dismiss();
                break;
            case R.id.no_btn:
                alertDialog.dismiss();
                break;
            case R.id.male_cb:
                if (maleCb.isChecked()) {
                    femaleCb.setChecked(false);
                }
                break;
            case R.id.female_cb:
                if (femaleCb.isChecked()) {
                    maleCb.setChecked(false);
                }
                break;
            default:
                break;
        }
    }

    private void updateUser(MyUser user) {
        UpdateUserAsyncTask userAsyncTask = new UpdateUserAsyncTask(context, oldUser.getId(),
                mListener);
        userAsyncTask.execute(user);
    }

    private void insertUser(MyUser user) {
        InsertUserAsyncTask insertUserAsyncTask = new InsertUserAsyncTask(context, mListener);
        insertUserAsyncTask.execute(user);
    }

    private MyUser generateNewUser() {
        MyUser user = new MyUser();
        if (!maleCb.isChecked() && !femaleCb.isChecked()) {
            return null;
        } else if (maleCb.isChecked()) {
            user.setGender(true);
        } else {
            user.setGender(false);
        }
        user.setAvatar(user.getGender() ? R.drawable.male_avatar_icon :
                R.drawable.female_avatar_icon);
        user.setName(userName.getText().toString());
        user.setPhone(userPhone.getText().toString());
        user.setAddress(userAddress.getText().toString());
        return user;
    }

    private static class UpdateUserAsyncTask extends AsyncTask<MyUser, Void, MyUser> {

        private WeakReference<Context> contextWeakReference;
        private IGenerateAddUserListener listener;
        private int oldUserId;

        public UpdateUserAsyncTask(Context context, int oldUserId,
                                   IGenerateAddUserListener listener) {
            this.contextWeakReference = new WeakReference<>(context);
            this.listener = listener;
            this.oldUserId = oldUserId;
        }

        @Override
        protected MyUser doInBackground(MyUser... myUsers) {
            Context context = contextWeakReference.get();
            if (context != null) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
                int i = dataBaseHelper.updateUser(oldUserId, myUsers[0]);
                if (i > 0) {
                    return myUsers[0];
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(MyUser myUser) {
            super.onPostExecute(myUser);
            Context context = contextWeakReference.get();
            if (context != null) {
                if (myUser != null) {
                    listener.getUser(myUser);
                } else {
                    Toast.makeText(context, "更新失败!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private static class InsertUserAsyncTask extends AsyncTask<MyUser, Void, MyUser> {

        private WeakReference<Context> contextWeakReference;
        private IGenerateAddUserListener listener;

        public InsertUserAsyncTask(Context context, IGenerateAddUserListener listener) {
            this.contextWeakReference = new WeakReference<>(context);
            this.listener = listener;
        }

        @Override
        protected MyUser doInBackground(MyUser... myUsers) {
            MyUser user = myUsers[0];
            long i = -1;
            if (contextWeakReference.get() != null) {
                i = new DataBaseHelper(contextWeakReference.get()).insertUser(user);
            }
            if (i > -1) {
                user.setId((int) i);
                return user;
            }
            return null;
        }

        @Override
        protected void onPostExecute(MyUser myUser) {
            super.onPostExecute(myUser);
            if (contextWeakReference.get() != null) {
                if (myUser != null) {
                    listener.getUser(myUser);
                } else {
                    Toast.makeText(contextWeakReference.get(), "添加失败!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
