package afyapepe.mobile.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import afyapepe.mobile.R;
import afyapepe.mobile.activity.Patient;
import afyapepe.mobile.app.AppConfig;
import afyapepe.mobile.app.AppController;
import afyapepe.mobile.app.CustomListAdapter;
import afyapepe.mobile.helper.SQLiteHandler;
import afyapepe.mobile.model.Movie;
import afyapepe.mobile.model.Patients;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PatientListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PatientListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodaysPatients extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = PatientListFragment.class.getSimpleName();

    // Movies json url
    private static final String url = "http://10.0.2.2/movies.txt";
    private ProgressDialog pDialog;
    private List<Patients> patientList = new ArrayList<Patients>();
    private ListView listView;
    private CustomListAdapter adapter;
    private SearchView searchView;
    private SQLiteHandler db;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TodaysPatients() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PatientListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodaysPatients newInstance(String param1, String param2) {
        TodaysPatients fragment = new TodaysPatients();
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

        View view=inflater.inflate(R.layout.fragment_patients, container, false);
        listView = (ListView) view.findViewById(R.id.patients);
        searchView = (SearchView) view.findViewById(R.id.searchPatients);
        adapter = new CustomListAdapter(getActivity(), patientList);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                int textlength = newText.length();
                ArrayList<Patients> tempArrayList = new ArrayList<Patients>();
                for(Patients c: patientList){
                    if (textlength <= c.getName().length()) {
                        if (c.getName().toLowerCase().contains(newText.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                adapter = new CustomListAdapter(getActivity(), tempArrayList);
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

        StringRequest patientrequest = new StringRequest(Request.Method.POST, AppConfig.URL_PATIENTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray request= new JSONArray(response);


                            for (int i = 0; i < request.length(); i++) {
                                Patients myPatient = new Patients();
                                JSONObject patient = null;

                                patient = request.getJSONObject(i);
                                myPatient.setId(patient.getInt("id"));
                                myPatient.setName(patient.getString("firstname")+" "+patient.getString("secondName"));
                                if(patient.getString("age").equals("null")){
                                    myPatient.setAge(0);
                                }else {
                                    myPatient.setAge(patient.getInt("age"));
                                }
                                if(patient.getString("gender").equals("1")) {
                                    myPatient.setGender("Male");
                                }else{
                                    myPatient.setGender("Female");
                                }
                                if(patient.getString("p_status").equals("11")) {
                                    myPatient.setStatus("Existing");
                                }else{
                                    myPatient.setStatus("New");
                                }
                                myPatient.setDate(patient.getString("created_at"));
                                myPatient.setCheckPage("todaypatients");
                                patientList.add(myPatient);


                            }

                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            hidePDialog();
                        }catch (JSONException e) {
                            hidePDialog();
                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                HashMap<String, String> user = db.getUserDetails();

                String name = user.get("name");
                String email = user.get("email");

                params.put("email", email);
                return params;
            }
        };

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
