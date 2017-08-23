package afyapepe.mobile.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import afyapepe.mobile.R;
import afyapepe.mobile.app.AppConfig;
import afyapepe.mobile.app.AppController;
import afyapepe.mobile.app.CustomListAdapter;
import afyapepe.mobile.app.CustomWaitingListAdapter;
import afyapepe.mobile.helper.SQLiteHandler;
import afyapepe.mobile.model.Patients;
import afyapepe.mobile.model.WaitingList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WaitingListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WaitingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WaitingListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = PatientListFragment.class.getSimpleName();

    // Movies json url
    private static final String url = "http://10.0.2.2/movies.txt";
    private ProgressDialog pDialog;
    private ListView listView;
    private SearchView searchView;
    private List<WaitingList> waitingList = new ArrayList<WaitingList>();
    private CustomWaitingListAdapter adapter;
    private SQLiteHandler db;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public WaitingListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WaitingListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WaitingListFragment newInstance(String param1, String param2) {
        WaitingListFragment fragment = new WaitingListFragment();
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
        View view=inflater.inflate(R.layout.fragment_waiting_list, container, false);
        listView = (ListView) view.findViewById(R.id.waitingList);
        searchView = (SearchView) view.findViewById(R.id.searchWaitingList);
        adapter = new CustomWaitingListAdapter(getActivity(), waitingList);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                int textlength = newText.length();
                ArrayList<WaitingList> tempArrayList = new ArrayList<WaitingList>();
                for(WaitingList c: waitingList){
                    if (textlength <= c.getName().length()) {
                        if (c.getName().toLowerCase().contains(newText.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                adapter = new CustomWaitingListAdapter(getActivity(), tempArrayList);
                listView.setAdapter(adapter);
                //adapter.getFilter().filter(newText);
                return false;
            }
        });

        db = new SQLiteHandler(getActivity());

        HashMap<String, String> user = db.getUserDetails();

        //Toast.makeText(getActivity(),user.get("email"),Toast.LENGTH_SHORT).show();

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonArrayRequest patientrequest = new JsonArrayRequest(AppConfig.URL_WAITING_LIST,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                WaitingList patient = new WaitingList();
                                patient.setName(obj.getString("firstname")+" "+obj.getString("secondName"));
                                //patient.setThumbnailUrl(obj.getString("image"));
                                if (obj.getString("gender").equals("1")) {
                                    patient.setGender("Male");
                                }else{
                                    patient.setGender("Female");
                                }
                                if (obj.getString("age").equals("null")) {
                                    patient.setAge(0);
                                }else {
                                    patient.setAge(obj.getInt("age"));
                                }
                                patient.setTime(obj.getString("created_at"));
                                // Genre is json array


                                // adding movie to movies array
                                waitingList.add(patient);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(patientrequest);

        return view;

        //return inflater.inflate(R.layout.fragment_patients, container, false);
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
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
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
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
