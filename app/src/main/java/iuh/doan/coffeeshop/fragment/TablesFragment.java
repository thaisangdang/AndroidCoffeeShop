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
import iuh.doan.coffeeshop.adapter.TableAdapter;
import iuh.doan.coffeeshop.model.Table;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TablesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TablesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TablesFragment extends Fragment {
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
    private TableAdapter tableAdapter;
    private ArrayList<Table> tableArrayList;
    private EditText editTextSoBan;
    private EditText editTextMoTa;

    // TODO: Initial data
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference tableBan;

    public TablesFragment() {
        // Required empty public constructor
        firebaseDatabase = FirebaseDatabase.getInstance();
        tableBan = firebaseDatabase.getReference("table");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TablesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TablesFragment newInstance(String param1, String param2) {
        TablesFragment fragment = new TablesFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_tables, container, false);

        // TODO: Initial listView
        listView = rootView.findViewById(R.id.listViewTable);
        tableArrayList = new ArrayList<>();
        tableAdapter = new TableAdapter(getActivity(), R.layout.listview_item_table, tableArrayList);
        listView.setAdapter(tableAdapter);
        registerForContextMenu(listView);
        loadListView();

        // TODO: initial buttons
        FButton buttonNewTable = rootView.findViewById(R.id.buttonNewTable);
        buttonNewTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newTable();
            }
        });

        return rootView;
    }

    // TODO: Load data form firebase to listview
    private void loadListView() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please waiting");
        progressDialog.show();
        tableBan.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tableArrayList.clear();
                tableAdapter.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Table table = snapshot.getValue(Table.class);
                    tableArrayList.add(table);
                }
                tableAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    // TODO: handling for button new table
    private void newTable() {
        // TODO: Initial layout
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View form = layoutInflater.inflate(R.layout.form_table, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Thêm thông tin bàn");
        alertDialogBuilder.setView(form);

        // TODO: Add controls
        editTextSoBan = form.findViewById(R.id.editTextSoBanOnTableForm);
        editTextMoTa = form.findViewById(R.id.editTextMoTaOnTableForm);

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
                        final Table table = getTable();
                        if (table != null) {
                            tableBan.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.child(String.valueOf(table.getSoBan())).exists()) {
                                        Toast.makeText(getActivity(), "Số bàn đã tồn tại, xin nhập số khác", Toast.LENGTH_LONG).show();
                                    } else {
                                        tableBan.child(String.valueOf(table.getSoBan())).setValue(table);
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

    private Table getTable() {
        String soBan = editTextSoBan.getText().toString();
        String moTa = editTextMoTa.getText().toString();
        if (soBan.isEmpty()) {
            Toast.makeText(getActivity(), "Số bàn không được để trống", Toast.LENGTH_LONG).show();
            editTextSoBan.requestFocus();
        } else if (moTa.isEmpty()) {
            Toast.makeText(getActivity(), "Mô tả không được để trống", Toast.LENGTH_LONG).show();
            editTextMoTa.requestFocus();
        } else {
            Table table = new Table(Long.parseLong(soBan), moTa, "available");
            return table;
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
            details(tableAdapter.getItem(index));
        } else if (item.getItemId() == R.id.menuItemEdit) {
            edit(tableAdapter.getItem(index));
        } else if (item.getItemId() == R.id.menuItemDelete) {
            delete(tableAdapter.getItem(index));
        } else if (item.getItemId() == R.id.menuItemClose) {
            getActivity().closeContextMenu();
        }
        return true;
    }

    private void delete(Table table) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Xác nhận");
        alertDialogBuilder.setMessage("Muốn xóa thiệt hả?");
        final String key = String.valueOf(table.getSoBan());
        alertDialogBuilder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tableBan.child(key).removeValue();
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

    private void edit(Table table) {
        // TODO: Initial layout
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View form = layoutInflater.inflate(R.layout.form_table, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Cập nhật thông tin bàn");
        alertDialogBuilder.setView(form);

        // TODO: Add controls
        editTextSoBan = form.findViewById(R.id.editTextSoBanOnTableForm);
        editTextMoTa = form.findViewById(R.id.editTextMoTaOnTableForm);

        editTextSoBan.setText(String.valueOf(table.getSoBan()));
        editTextMoTa.setText(table.getMoTa());

        editTextSoBan.setEnabled(false);

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
                        final Table table = getTable();
                        if (table != null) {
                            tableBan.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    tableBan.child(String.valueOf(table.getSoBan())).setValue(table);
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

    private void details(Table table) {
        // TODO: Initial layout
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View form = layoutInflater.inflate(R.layout.form_table, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Thêm thông tin bàn");
        alertDialogBuilder.setView(form);

        // TODO: Add controls
        editTextSoBan = form.findViewById(R.id.editTextSoBanOnTableForm);
        editTextMoTa = form.findViewById(R.id.editTextMoTaOnTableForm);

        editTextSoBan.setText(String.valueOf(table.getSoBan()));
        editTextMoTa.setText(table.getMoTa());

        editTextSoBan.setEnabled(false);
        editTextMoTa.setEnabled(false);

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
