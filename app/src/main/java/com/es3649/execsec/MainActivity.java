package com.es3649.execsec;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.es3649.execsec.messaging.UI.MessagerActivity;
import com.es3649.execsec.model.Model;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.mainFragmentHolder, LoginFragment.newInstance("",""))
                .commit();

        Iconify.with(new FontAwesomeModule());
        requestSmsPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this, "Stuff", Toast.LENGTH_SHORT).show();
        invalidateOptionsMenu();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragmentHolder, TransactionCountFragment.newInstance(new TransactionCountFragment.Listener() {
                    @Override
                    public void onFragmentInteraction(Uri uri) {
                        Toast.makeText(getApplicationContext(), "+1", Toast.LENGTH_SHORT).show();
                    }
                }))
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (Model.isLoggedIn()) {
            // inflate the options menu
            getMenuInflater().inflate(R.menu.top_menu, menu);

            // add the settings button
            menu.findItem(R.id.menuTopSettingsButton).setIcon(
                    new IconDrawable(this, FontAwesomeIcons.fa_cogs)
                    .colorRes(R.color.colorPrimaryLighter)
                    .actionBarSize());

            menu.findItem(R.id.menuNewMessageButton).setIcon(
                    new IconDrawable(this, FontAwesomeIcons.fa_pencil_square)
                    .colorRes(R.color.colorPrimaryLighter)
                    .actionBarSize());
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
        case R.id.menuTopSettingsButton:
            startActivity(new Intent(this, SettingsActivity.class));
            return true;

        case R.id.menuNewMessageButton:
            startActivity(new Intent(this, MessagerActivity.class));
            return true;

        default:
            Log.e(TAG, "bad menu choice");
            return super.onOptionsItemSelected(item);
        }

    }

    /**
     * Rends a permission request to the screen to read and send text messages.
     *
     * I copied this from stack overflow.
     * https://stackoverflow.com/questions/33347809/android-marshmallow-sms-received-permission
     */
    private void requestSmsPermission() {
        String permission = Manifest.permission.RECEIVE_SMS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if ( grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }
    }

    /**
     * A simple {@link Fragment} subclass.
     * Activities that contain this fragment must implement the
     * {@link OnFragmentInteractionListener} interface
     * to handle interaction events.
     * Use the {@link MessagerFragment#newInstance} factory method to
     * create an instance of this fragment.
     */
    public static class MessagerFragment extends Fragment {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private static final String ARG_PARAM1 = "param1";
        private static final String ARG_PARAM2 = "param2";

        // TODO: Rename and change types of parameters
        private String mParam1;
        private String mParam2;

        private OnFragmentInteractionListener mListener;

        public MessagerFragment() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MessagerFragment.
         */
        // TODO: Rename and change types and number of parameters
        public static MessagerFragment newInstance(String param1, String param2) {
            MessagerFragment fragment = new MessagerFragment();
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
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_messager, container, false);
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
            if (context instanceof OnFragmentInteractionListener) {
                mListener = (OnFragmentInteractionListener) context;
            } else {
                throw new RuntimeException(context.toString()
                        + " must implement OnFragmentInteractionListener");
            }
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
}
