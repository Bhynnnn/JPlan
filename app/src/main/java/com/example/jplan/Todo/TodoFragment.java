package com.example.jplan.Todo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jplan.ItemTouchHelperCallback;
import com.example.jplan.Model.Constants;
import com.example.jplan.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.ArrayList;

public class TodoFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private TodoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Todo> mTodoData = new ArrayList<>();
    private ArrayList<String> mTodoKeyData = new ArrayList();

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private EditText todo_edt;
    private Spinner plan_spinner;
    private Button add_todo_btn;
    private String title_str, title_db, str_temp;
    String isCheck_bool;
    ArrayAdapter<String> arrayAdapter;
    static ArrayList<String> arr;
//    long num = 0;
    ItemTouchHelper helper;
    //Todo RealTimeDB - Todo - User uid - 할 일 uid1 - 할 일 제목, 실행 여부
    //                                  - 할 일 uid2 - 할 일 제목, 실행 여부

    public TodoFragment() {
        // Required empty public constructor
    }

    public static TodoFragment newInstance() {
        TodoFragment fragment = new TodoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        init(view);


        System.out.println("+++Todo mTodoData " + mTodoData.size());

        database.getReference().child("TodoList").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    mTodoData.clear();
                    mTodoKeyData.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        System.out.println("+++Todo snapshot children key 0 " + dataSnapshot);
                        System.out.println("+++Todo snapshot children key 1 " + dataSnapshot.getKey());
                        System.out.println("+++Todo snapshot children title " + dataSnapshot.child("title").getValue().toString());

                        title_db = dataSnapshot.child("title").getValue().toString();
                        System.out.println("+++Todo snapshot title " + title_db);
                        isCheck_bool = dataSnapshot.child("selected").getValue().toString();
                        System.out.println("+++Todo snapshot isCheck_bool " + isCheck_bool);

                        mTodoData.add(new Todo(title_db, isCheck_bool));
                        mTodoKeyData.add(dataSnapshot.getKey());
                    }
                    mAdapter.notifyDataSetChanged();

                    for(int i=0; i<mTodoKeyData.size(); i++){
                        System.out.println("+++Todo snapshot keyData " + mTodoKeyData.get(i));
                    }

                }
                else{
                    System.out.println("list 존재 x");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        firestore.collection("User").document(auth.getCurrentUser().getUid()).collection("Plan")
                .whereEqualTo("check_Plan", true).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                arr = new ArrayList<>();
                arr.add("PLAN");
                // arr = ["0", "title1", "title2", "title3"]
                for (QueryDocumentSnapshot item : queryDocumentSnapshots) {
                    System.out.println("todoFragment item id " + item.getId());
                    System.out.println("todoFragment item title " + item.get(Constants.TITLE_PLAN));

                    arr.add(String.valueOf(item.get(Constants.TITLE_PLAN)));
                    // plan에서의 ischecked 가 true 인 item의 title 값을 arr에 담음

                    // 이 array들은 spinner 에 보여야함
                    System.out.println("todoFragment arr " + arr);

                }
                System.out.println("todoFragment arr 1 " + arr);
                arrayAdapter = new ArrayAdapter<>(getContext().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arr);
                plan_spinner.setAdapter(arrayAdapter);

                plan_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        Toast.makeText(getContext().getApplicationContext(), arr.get(position) + "가 선택되었습니다.", Toast.LENGTH_SHORT).show()
                        // 선택된 값 보임 = arr.get(position)
                        if(position == 0 ){
                            // edttext 값이 str값
                            str_temp = "";
                        }
                        else{
                            str_temp = arr.get(position);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });



        add_todo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("+++Todo 추가버튼 clicked");

                title_str = todo_edt.getText().toString();

                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    public void run() {

                        if (title_str.equals("")) {
                            if (!str_temp.equals("")) {
                                mTodoData.clear();
                                mTodoKeyData.clear();
                                title_str = str_temp;

                                //edittext 값이 빈 값이 아님
                                Todo todo = new Todo();
                                todo.setTitle(title_str);
                                todo.setSelected("false");
                                database.getReference().child("TodoList").child(auth.getCurrentUser().getUid()).push().setValue(todo);
                                System.out.println("+++Todo push 성공 - Plan에 저장한 것 추가");
                            }
                            else{
                                Toast.makeText(getContext(), "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            //edittext 값이 빈 값이 아님
                            mTodoData.clear();
                            mTodoKeyData.clear();
                            Todo todo = new Todo();
                            todo.setTitle(title_str);
                            todo.setSelected("false");
//                            todo.setNum((int) (num));
                            database.getReference().child("TodoList").child(auth.getCurrentUser().getUid()).push().setValue(todo);
                            System.out.println("+++Todo push 성공 - 새로 추가");
                        }
                        System.out.println("+++Todo mTodoData 0 " + mTodoData.size());
                        System.out.println("+++Todo mTodoKeyData " + mTodoKeyData.size());

                        todo_edt.setText("");
                    }
                }, 500); // 0.5초후
                mAdapter.notifyDataSetChanged();

            }



        });

        return view;
    }

    private void init(View view){

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        mAdapter = new TodoAdapter(mTodoData, mTodoKeyData);

        helper = new ItemTouchHelper(new ItemTouchHelperCallback(mAdapter));
        helper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        todo_edt = view.findViewById(R.id.todo_edt);
        plan_spinner = view.findViewById(R.id.plan_spinner);
        add_todo_btn = view.findViewById(R.id.add_todo_btn);

    }
    @Override
    public void onResume() {
        super.onResume();

    }
}