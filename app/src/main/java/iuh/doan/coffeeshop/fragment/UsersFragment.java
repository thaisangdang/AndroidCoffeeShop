package iuh.doan.coffeeshop.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import info.hoang8f.widget.FButton;
import iuh.doan.coffeeshop.R;
import iuh.doan.coffeeshop.adapter.UserAdapter;
import iuh.doan.coffeeshop.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UsersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UsersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    // TODO: Initial components
    private ListView listView;
    private UserAdapter userAdapter;
    private ArrayList<User> userArrayList;
    private EditText editTextPhone;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextPassword2;

    // TODO: Initial data
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference tableUser;

    public UsersFragment() {
        // Required empty public constructor
        firebaseDatabase = FirebaseDatabase.getInstance();
        tableUser = firebaseDatabase.getReference("user");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UsersFragment newInstance(String param1, String param2) {
        UsersFragment fragment = new UsersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO: Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_users, container, false);

        // TODO: initial listView
        listView = rootView.findViewById(R.id.listViewUser);
        userArrayList = new ArrayList<>();
        userAdapter = new UserAdapter(getActivity(), R.layout.listview_item_user, userArrayList);
        listView.setAdapter(userAdapter);
        registerForContextMenu(listView);
        loadListView();

        // TODO: initial buttons
        FButton buttonNewUser = rootView.findViewById(R.id.buttonNewUser);
        buttonNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newUser();
            }
        });

        return rootView;
    }

    // TODO: Load data form firebase to listview
    private void loadListView() {
        tableUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userArrayList.clear();
                userAdapter.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    user.setId(snapshot.getKey());
                    userArrayList.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    // TODO: handling for button new user
    private void newUser() {
        // TODO: Initial layout
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View form = layoutInflater.inflate(R.layout.form_user, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Thêm tài khoản");
        alertDialogBuilder.setView(form);

        // TODO: Add controls
        editTextPhone = form.findViewById(R.id.editTextPhoneOnUserForm);
        editTextUsername = form.findViewById(R.id.editTextUsernameOnUserForm);
        editTextPassword = form.findViewById(R.id.editTextPasswordOnUserForm);
        editTextPassword2 = form.findViewById(R.id.editTextPassword2OnUserForm);

        // TODO: Add events
        alertDialogBuilder.setPositiveButton("Lưu", null);
        alertDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button buttonSave = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                buttonSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final User user = getUser();
                        if (user != null) {
                            tableUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child((user.getId())).exists()) {
                                        Toast.makeText(getActivity(), "Số điện thoại đã tồn tại, xin nhập số khác", Toast.LENGTH_LONG).show();
                                    } else {
                                        tableUser.child(user.getId()).setValue(user);
                                        dialog.dismiss();
                                        Toast.makeText(getActivity(), "Thêm thành công", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }

    // TODO: Get user object from dialog
    private User getUser() {
        String phone = editTextPhone.getText().toString();
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String password2 = editTextPassword2.getText().toString();
        if (phone.isEmpty()) {
            Toast.makeText(getActivity(), "Số điện thoại không được để trống", Toast.LENGTH_LONG).show();
            editTextPhone.requestFocus();
        } else if (username.isEmpty()) {
            Toast.makeText(getActivity(), "Tên không được để trống", Toast.LENGTH_LONG).show();
            editTextUsername.requestFocus();
        } else if (password.isEmpty()) {
            Toast.makeText(getActivity(), "Password không được để trống", Toast.LENGTH_LONG).show();
            editTextPassword.requestFocus();
        } else if (!password2.equals(password)) {
            Toast.makeText(getActivity(), "Password nhập lại không không trùng khớp", Toast.LENGTH_LONG).show();
            editTextPassword2.requestFocus();
        } else {
            User user = new User(phone, username, password);
            return user;
        }
        return null;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.listview_item_action, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = adapterContextMenuInfo.position;
        if (item.getItemId() == R.id.menuItemDetails) {
            details(userAdapter.getItem(index));
        } else if (item.getItemId() == R.id.menuItemEdit) {
            edit(userAdapter.getItem(index));
        } else if (item.getItemId() == R.id.menuItemDelete) {
            delete(userAdapter.getItem(index));
        } else if (item.getItemId() == R.id.menuItemClose) {
            getActivity().closeContextMenu();
        }
        return true;
    }

    private void delete(User user) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Xác nhận");
        alertDialogBuilder.setMessage("Muốn xóa thiệt hả?");
        final String key = user.getId();
        alertDialogBuilder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tableUser.child(key).removeValue();
                dialog.dismiss();
                Toast.makeText(getActivity(), "Xóa thành công", Toast.LENGTH_LONG).show();
            }
        });
        alertDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.create().show();
    }

    private void edit(User user) {
        // TODO: Initial layout
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View form = layoutInflater.inflate(R.layout.form_user, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Cập nhật thông tin tài khoản");
        alertDialogBuilder.setView(form);

        // TODO: Add controls
        editTextPhone = form.findViewById(R.id.editTextPhoneOnUserForm);
        editTextUsername = form.findViewById(R.id.editTextUsernameOnUserForm);
        editTextPassword = form.findViewById(R.id.editTextPasswordOnUserForm);
        editTextPassword2 = form.findViewById(R.id.editTextPassword2OnUserForm);

        editTextPhone.setText(user.getId());
        editTextPhone.setEnabled(false);
        editTextUsername.setText(user.getName());
        editTextPassword.setText(user.getPassword());
        editTextPassword2.setText(user.getPassword());

        // TODO: Add events
        alertDialogBuilder.setPositiveButton("Lưu", null);
        alertDialogBuilder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button buttonSave = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                buttonSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final User user = getUser();
                        if (user != null) {
                            tableUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    tableUser.child(user.getId()).setValue(user);
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                    }
                });
            }
        });
        alertDialog.show();
    }

    private void details(User user) {
        // TODO: Initial layout
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View form = layoutInflater.inflate(R.layout.form_user, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Thông tin tài khoản");
        alertDialogBuilder.setView(form);

        // TODO: Add controls
        editTextPhone = form.findViewById(R.id.editTextPhoneOnUserForm);
        editTextUsername = form.findViewById(R.id.editTextUsernameOnUserForm);
        editTextPassword = form.findViewById(R.id.editTextPasswordOnUserForm);
        editTextPassword2 = form.findViewById(R.id.editTextPassword2OnUserForm);

        editTextPhone.setText(user.getId());
        editTextUsername.setText(user.getName());

        editTextPhone.setEnabled(false);
        editTextUsername.setEnabled(false);
        editTextPassword.setVisibility(View.INVISIBLE);
        editTextPassword2.setVisibility(View.INVISIBLE);

        // TODO: add events
        alertDialogBuilder.setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.create().show();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
