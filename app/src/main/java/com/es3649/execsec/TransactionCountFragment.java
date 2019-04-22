package com.es3649.execsec;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.es3649.execsec.model.Model;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Listener} interface
 * to handle interaction events.
 * Use the {@link TransactionCountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransactionCountFragment extends Fragment {

    private Listener mListener;
    private TextView counter;

    public TransactionCountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param listener an interaction listener
     * @return A new instance of fragment TransactionCountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TransactionCountFragment newInstance(Listener listener) {
        TransactionCountFragment fragment = new TransactionCountFragment();
        fragment.mListener = listener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_transaction_count, container, false);
        counter = v.findViewById(R.id.tcfTransactionCounterTextView);
        updateCounter();

        v.findViewById(R.id.tcfMainLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.increment();
                updateCounter();
            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mListener == null) {
            if (context instanceof Listener) {
                mListener = (Listener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement Listener or one must be provided");
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
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
    public interface Listener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void updateCounter() {
        counter.setText(String.format(Locale.getDefault(), "%d", Model.getTransactionCount()));
    }
}
