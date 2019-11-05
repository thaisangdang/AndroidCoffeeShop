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
import android.widget.TextView;
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

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

import info.hoang8f.widget.FButton;
import iuh.doan.coffeeshop.R;
import iuh.doan.coffeeshop.SignInActivity;
import iuh.doan.coffeeshop.adapter.DrinkAdapter;
import iuh.doan.coffeeshop.model.Drink;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DrinksFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DrinksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DrinksFragment extends Fragment {
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
    private DrinkAdapter drinkAdapter;
    private ArrayList<Drink> drinkArrayList;
    private EditText editTextMa;
    private EditText editTextTen;
    private EditText editTextGia;

    // TODO: Initial data
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference tableDrink;

    public DrinksFragment() {
        // Required empty public constructor
        firebaseDatabase = FirebaseDatabase.getInstance();
        tableDrink = firebaseDatabase.getReference("drink");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DrinksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DrinksFragment newInstance(String param1, String param2) {
        DrinksFragment fragment = new DrinksFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_drinks, container, false);

        // TODO: Initial listView
        listView = rootView.findViewById(R.id.listViewDrink);
        drinkArrayList = new ArrayList<>();
        drinkAdapter = new DrinkAdapter(getActivity(), R.layout.listview_item_drink, drinkArrayList);
        listView.setAdapter(drinkAdapter);
        registerForContextMenu(listView);
        loadListView();

        // TODO: initial buttons
        FButton buttonNewDrink = rootView.findViewById(R.id.buttonNewDrink);
        buttonNewDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newDrink();
            }
        });

        return rootView;
    }

    private void loadListView() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please waiting");
        progressDialog.show();
        tableDrink.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                drinkArrayList.clear();
                drinkAdapter.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Drink drink = snapshot.getValue(Drink.class);
                    drinkArrayList.add(drink);
                }
                drinkAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void newDrink() {
        // TODO: Initial layout
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View form = layoutInflater.inflate(R.layout.form_drink, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Thêm thông tin nước/món");
        alertDialogBuilder.setView(form);

        // TODO: Add controls
        editTextMa = form.findViewById(R.id.editTextMaOnDrinkForm);
        editTextTen = form.findViewById(R.id.editTextTenOnDrinkForm);
        editTextGia = form.findViewById(R.id.editTextGiaOnDrinkForm);

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
                        final Drink drink = getDrink();
                        tableDrink.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(drink.getMa()).exists()) {
                                    Toast.makeText(getActivity(), "Mã nước/món đã tồn tại, xin nhập lại", Toast.LENGTH_LONG).show();
                                } else {
                                    tableDrink.child(drink.getMa()).setValue(drink);
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "Thêm thành công", Toast.LENGTH_LONG).show();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                });
            }
        });
        alertDialog.show();
    }

    private Drink getDrink() {
        String ma = editTextMa.getText().toString();
        String ten = editTextTen.getText().toString();
        String gia = editTextGia.getText().toString();
        if (ma.isEmpty()) {
            Toast.makeText(getActivity(), "Mã nước/món không được để trống", Toast.LENGTH_LONG).show();
            editTextMa.requestFocus();
        } else if (ten.isEmpty()) {
            Toast.makeText(getActivity(), "Tên nước/món không được để trống", Toast.LENGTH_LONG).show();
            editTextTen.requestFocus();
        } else if (gia.isEmpty()) {
            Toast.makeText(getActivity(), "Giá nước/món không được để trống", Toast.LENGTH_LONG).show();
            editTextGia.requestFocus();
        } else {
            Drink drink = new Drink(ma, ten, Long.parseLong(gia));
            return  drink;
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
            details(drinkAdapter.getItem(index));
        } else if (item.getItemId() == R.id.menuItemEdit) {
            edit(drinkAdapter.getItem(index));
        } else if (item.getItemId() == R.id.menuItemDelete) {
            delete(drinkAdapter.getItem(index));
        } else if (item.getItemId() == R.id.menuItemClose) {
            getActivity().closeContextMenu();
        }
        return true;
    }

    private void delete(final Drink drink) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Xác nhận");
        alertDialogBuilder.setMessage("Muốn xóa thiệt hả?");
        final String key = String.valueOf(drink.getMa());
        alertDialogBuilder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tableDrink.child(key).removeValue();
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

    private void edit(Drink drink) {
        // TODO: Initial layout
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View form = layoutInflater.inflate(R.layout.form_drink, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Cập nhật thông tin nước/món");
        alertDialogBuilder.setView(form);

        // TODO: Add controls
        editTextMa = form.findViewById(R.id.editTextMaOnDrinkForm);
        editTextTen = form.findViewById(R.id.editTextTenOnDrinkForm);
        editTextGia = form.findViewById(R.id.editTextGiaOnDrinkForm);

        editTextMa.setText(drink.getMa());
        editTextTen.setText(drink.getTen());
        editTextGia.setText(String.valueOf(drink.getGia()));
        editTextMa.setEnabled(false);

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
                        final Drink drink = getDrink();
                        tableDrink.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                tableDrink.child(drink.getMa()).setValue(drink);
                                dialog.dismiss();
                                Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_LONG).show();
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                });
            }
        });
        alertDialog.show();
    }

    private void details(Drink drink) {
        // TODO: Initial layout
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View form = layoutInflater.inflate(R.layout.form_drink, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("Thông tin nước/món");
        alertDialogBuilder.setView(form);

        // TODO: Add controls
        editTextMa = form.findViewById(R.id.editTextMaOnDrinkForm);
        editTextTen = form.findViewById(R.id.editTextTenOnDrinkForm);
        editTextGia = form.findViewById(R.id.editTextGiaOnDrinkForm);

        editTextMa.setText(drink.getMa());
        editTextTen.setText(drink.getTen());
        editTextGia.setText(String.valueOf(drink.getGia()));

        editTextMa.setEnabled(false);
        editTextTen.setEnabled(false);
        editTextGia.setEnabled(false);

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
