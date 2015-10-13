package balakumaran.hawabazz_design;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class LocationListLoading extends Fragment implements AppData {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationListLoading.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationListLoading newInstance(String param1, String param2) {
        LocationListLoading fragment = new LocationListLoading();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LocationListLoading() {
        // Required empty public constructor
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

        new AsyncHttp(loactionListUrl, new HttpParam(), new AsyncHttpListener() {
            @Override
            public void onResponse(String response) {
                if(response==null){
                    retry();
                    return;
                }
                try {
                    JSONObject responseObject=new JSONObject(response);
                    if(responseObject.getInt(ERROR)==0) {
                        ArrayList<String> listString=new ArrayList<>();
                        JSONArray listJson=responseObject.getJSONArray("list");
                        for(int i=0;i<listJson.length();i++){
                            JSONArray listJsonElem=listJson.getJSONArray(i);
                            listString.add(listJsonElem.getString(1));
                        }

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.container, LocationFragment.newInstance(listString))
                                .commit();


                    }else{
                        myToast(SORRY);
                        throw new ServerException(responseObject.getInt(ERROR));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("JSON parse", e.toString());
                    retry();
                } catch (ServerException e){
                    Log.d("Server error",e.toString());
                    retry();
                } catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
        return inflater.inflate(R.layout.fragment_location_list_loading, container, false);
    }

    private void retry(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,new LocationListRetry())
                .commit();
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    void myToast(String msg){
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

}
