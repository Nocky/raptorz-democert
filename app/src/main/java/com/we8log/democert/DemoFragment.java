package com.we8log.democert;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * Created by hying on 14-1-20.
 */
public class DemoFragment extends Fragment implements AdapterView.OnItemSelectedListener, AsyncDemo.AsyncDemoListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "DemoFragment";

    private Spinner mUrlList;
    private TextView mContent;
    private String mUrl;

    public static DemoFragment newInstance(int sectionNumber) {
        DemoFragment fragment = new DemoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public DemoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mUrlList = (Spinner)rootView.findViewById(R.id.sp_url);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.list_url, android.R.layout.simple_spinner_dropdown_item);
        mUrlList.setAdapter(adapter);
        mUrlList.setOnItemSelectedListener(this);
        mContent = (TextView)rootView.findViewById(R.id.tvContent);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    public void fetch() {
        mContent.setText("");
        int i = getArguments().getInt(ARG_SECTION_NUMBER);
        AsyncDemo task = new AsyncDemo(this);
        task.execute(i, mUrl, this.getActivity());
        //mContent.setText(AsyncDemo.doDemo(i, mUrl, this.getActivity()));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mUrl = adapterView.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mUrl = "";
    }

    @Override
    public void onSuccess(String result) {
        mContent.setText(result);
    }

    @Override
    public void onErrorOrCancel() {
        mContent.setText("Error!");
    }
}
