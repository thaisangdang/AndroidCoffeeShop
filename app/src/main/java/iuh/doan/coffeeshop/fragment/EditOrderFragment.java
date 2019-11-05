package iuh.doan.coffeeshop.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import info.hoang8f.widget.FButton;
import iuh.doan.coffeeshop.HomeActivity;
import iuh.doan.coffeeshop.R;
import iuh.doan.coffeeshop.adapter.ChooseDrinkAdapter;
import iuh.doan.coffeeshop.model.Drink;
import iuh.doan.coffeeshop.model.Order;
import iuh.doan.coffeeshop.model.OrderDetails;
import iuh.doan.coffeeshop.model.Table;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditOrderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditOrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditOrderFragment extends Fragment {
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
    private ChooseDrinkAdapter chooseDrinkAdapter;
    private ArrayList<OrderDetails> orderDetailsArrayList;
    private ArrayList<Drink> drinkArrayList;
    private EditText editTextOrderNote;

    // TODO: Initial data
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference tableDrink, tableOrder, tableBan;

    public EditOrderFragment() {
        // Required empty public constructor
        firebaseDatabase = FirebaseDatabase.getInstance();
        tableDrink = firebaseDatabase.getReference("drink");
        tableOrder = firebaseDatabase.getReference("order");
        tableBan = firebaseDatabase.getReference("table");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditOrderFragment newInstance(String param1, String param2) {
        EditOrderFragment fragment = new EditOrderFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_editorder, container, false);

        // TODO: Initial listView
        listView = rootView.findViewById(R.id.listViewChooseDrink);
        orderDetailsArrayList = OrderFragment.orderSelected.getDrinks();
        drinkArrayList = new ArrayList<>();
        chooseDrinkAdapter = new ChooseDrinkAdapter(getActivity(), R.layout.listview_item_chooosedrink, orderDetailsArrayList);
        listView.setAdapter(chooseDrinkAdapter);
        loadListView();

        // TODO: initial other components
        editTextOrderNote = rootView.findViewById(R.id.editTextOrderNote);
        editTextOrderNote.setText(OrderFragment.orderSelected.getNote());
        FButton buttonSave = rootView.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order();
            }
        });
        getActivity().findViewById(R.id.fab).setVisibility(View.INVISIBLE);
        TextView textViewTitle = rootView.findViewById(R.id.textViewTitle);
        textViewTitle.setText("Bàn số " + mParam1);

        FButton buttonTinhTien = rootView.findViewById(R.id.buttonTinhTien);
        buttonTinhTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinhTien();
            }
        });

        return rootView;
    }

    private void tinhTien() {
        tableOrder.child(OrderFragment.orderSelected.getMa()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Order order = dataSnapshot.getValue(Order.class);
                order.setStatus("paid");
                tableOrder.child(OrderFragment.orderSelected.getMa()).setValue(order);
                updateTableStatus("available");
                Toast.makeText(getActivity(), "Đã tính tiền", Toast.LENGTH_LONG).show();
                openOrderFragment();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void order() {
        final Order order = getOrder();
        if (order == null) {
            Toast.makeText(getActivity(), "Chưa chọn nước/món", Toast.LENGTH_LONG).show();
        } else {
            tableOrder.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    tableOrder.child(order.getMa()).setValue(order);
                    Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_LONG).show();
                    openOrderFragment();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    private void openOrderFragment() {
        OrderFragment orderFragment = new OrderFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame, orderFragment, orderFragment.getTag()).commit();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Order");
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);
        HomeActivity.navItemIndex = 1;
    }

    private void updateTableStatus(final String status) {
        tableBan.child(mParam1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Table table = dataSnapshot.getValue(Table.class);
                table.setStatus(status);
                tableBan.child(mParam1).setValue(table);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private Order getOrder() {
        OrderFragment.orderSelected.setNote(editTextOrderNote.getText().toString());
        OrderFragment.orderSelected.setDrinks(orderDetailsArrayList);
        // tổng tiền
        long totalCost = 0;
        for (OrderDetails orderDetails: orderDetailsArrayList) {
            int index = orderDetailsArrayList.indexOf(orderDetails);
            long price = drinkArrayList.get(index).getGia();
            totalCost += price*orderDetails.getNum();
        }
        if (totalCost == 0) {
            return null;
        }
        OrderFragment.orderSelected.setTotalCost(totalCost);
        return OrderFragment.orderSelected;
    }

    private String getCurrentTimeStamp() {
        long timeStamp = System.currentTimeMillis();
        return String.valueOf(timeStamp);
    }

    private void loadListView() {
        tableDrink.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Drink drink = snapshot.getValue(Drink.class);
                    drink.setMa(snapshot.getKey());
                    drinkArrayList.add(drink);
                }
                chooseDrinkAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
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
