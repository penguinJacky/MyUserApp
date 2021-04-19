package com.example.myuserapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myuserapp.adapter.MyRecyclerViewAdapter;
import com.example.myuserapp.bean.MyUser;
import com.example.myuserapp.customdialog.MyActionDialog;
import com.example.myuserapp.customdialog.MyUserDialog;
import com.example.myuserapp.model.DataBaseHelper;
import com.example.myuserapp.model.IGenerateAddUserListener;
import com.example.myuserapp.model.OperationType;
import com.example.myuserapp.utils.MyRecyclerItemClickListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button addBtn;
    private ArrayList<MyUser> userArrayList = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        addBtn.setOnClickListener(mAddListener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        dataBaseHelper = new DataBaseHelper(this);
        mAdapter = new MyRecyclerViewAdapter(userArrayList);
        recyclerView.setAdapter(mAdapter);
        GetAllUsersAsyncTask getAllUsersAsyncTask = new GetAllUsersAsyncTask(this);
        getAllUsersAsyncTask.execute();
        recyclerView.addOnItemTouchListener(new MyRecyclerItemClickListener(this,
                new MyRecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MyActionDialog actionDialog = new MyActionDialog(MainActivity.this);
                MyUser users = userArrayList.get(position);
                actionDialog.createDialogAndShow(users, new IGenerateAddUserListener() {
                    @Override
                    public void getUser(MyUser myUser) {
                        if (myUser != null) {
                            userArrayList.set(position, myUser);
                        } else {
                            userArrayList.remove(position);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataBaseHelper.close();
    }

    public void initUI() {
        addBtn = (Button) findViewById(R.id.add_btn);
        recyclerView = findViewById(R.id.my_recycler_view);
    }

    private View.OnClickListener mAddListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new MyUserDialog(MainActivity.this, new IGenerateAddUserListener() {
                @Override
                public void getUser(MyUser myUser) {
                    userArrayList.add(myUser);
                    mAdapter.notifyDataSetChanged();
                }
            }, OperationType.INSERT);
        }
    };

    private static class GetAllUsersAsyncTask extends AsyncTask<Void, Void, ArrayList<MyUser>> {

        private WeakReference<MainActivity> activityWeakReference;

        public GetAllUsersAsyncTask(MainActivity context) {
            this.activityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected ArrayList<MyUser> doInBackground(Void... voids) {
            ArrayList<MyUser> users = new ArrayList<>();
            MainActivity mainActivity = activityWeakReference.get();
            if (mainActivity != null) {
                users = new DataBaseHelper(mainActivity).getAllUsers();
            }
            return users;
        }

        @Override
        protected void onPostExecute(ArrayList<MyUser> myUsers) {
            super.onPostExecute(myUsers);
            MainActivity mainActivity = activityWeakReference.get();
            if (mainActivity != null && !mainActivity.isFinishing()) {
                mainActivity.userArrayList = myUsers;
                mainActivity.mAdapter = new MyRecyclerViewAdapter(myUsers);
                RecyclerView recyclerView = mainActivity.findViewById(R.id.my_recycler_view);
                recyclerView.setAdapter(mainActivity.mAdapter);
            }
        }
    }
}