package iuh.doan.coffeeshop.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import iuh.doan.coffeeshop.adapter.OrderAdapter;
import iuh.doan.coffeeshop.model.Order;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {
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
    private OrderAdapter orderAdapter;
    private ArrayList<Order> orderArrayList;

    // TODO: Initial data
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference tableOrder;

    public OrderFragment() {
        // Required empty public constructor
        firebaseDatabase = FirebaseDatabase.getInstance();
        tableOrder = firebaseDatabase.getReference("order");
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
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_order, container, false);

        // TODO: Initial listView
        listView = rootView.findViewById(R.id.listViewOrder);
        orderArrayList = new ArrayList<>();
        orderAdapter = new OrderAdapter(getActivity(), R.layout.listview_item_order, orderArrayList);
        listView.setAdapter(orderAdapter);
        registerForContextMenu(listView);
        loadListView();

        // TODO: initial buttons
        FButton buttonNewOrder = rootView.findViewById(R.id.buttonNewOrder);
        buttonNewOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newOrder();
            }
        });

        getActivity().findViewById(R.id.fab).setVisibility(View.INVISIBLE);

        return rootView;
    }

    private void loadListView() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please waiting");
        progressDialog.show();
        tableOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderArrayList.clear();
                orderAdapter.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    if (order.getStatus().equals("unpaid")){
                        orderArrayList.add(order);
                    }
                }
                orderAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void newOrder() {
        ChooseTableFragment chooseTableFragment = new ChooseTableFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame, chooseTableFragment, chooseTableFragment.getTag()).commit();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Tạo Order");
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);
        HomeActivity.navItemIndex = 1;
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
            details(orderAdapter.getItem(index));
        } else if (item.getItemId() == R.id.menuItemEdit) {
            edit(orderAdapter.getItem(index));
        } else if (item.getItemId() == R.id.menuItemDelete) {
            delete(orderAdapter.getItem(index));
        } else if (item.getItemId() == R.id.menuItemClose) {
            getActivity().closeContextMenu();
        }
        return true;
    }

    private void delete(Order order) {
    }

    private void edit(Order order) {
    }

    private void details(Order order) {
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
