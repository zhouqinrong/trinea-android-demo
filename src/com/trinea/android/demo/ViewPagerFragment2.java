package com.trinea.android.demo;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.trinea.android.common.R;
import com.trinea.android.common.view.DropDownToRefreshListView;
import com.trinea.android.common.view.DropDownToRefreshListView.OnRefreshListener;

/**
 * 定义自己的Fragment，内容为listView
 * 
 * @author gxwu@lewatek.com 2012-11-15
 */
public class ViewPagerFragment2 extends Fragment {

    private LinkedList<String>        listItems = null;
    private DropDownToRefreshListView listView  = null;

    /**
     * 覆盖此函数，先通过inflater inflate函数得到view最后返回
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_pager_fragment_demo2, container, false);
        listView = (DropDownToRefreshListView)v.findViewById(R.id.statusListView);
        listView.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new GetDataTask().execute();
            }
        });
        listItems = new LinkedList<String>();
        listItems.addAll(Arrays.asList(mStrings));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                                                                listItems);
        listView.setAdapter(adapter);

        return v;
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                ;
            }
            return mStrings;
        }

        @Override
        protected void onPostExecute(String[] result) {

            listItems.addFirst("Added after refresh...");

            // 刷新结束时需手动调用listView的onRefreshComplete函数
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
            listView.onRefreshComplete("updated at: " + dateFormat.format(new Date()));

            super.onPostExecute(result);
        }
    }

    private String[] mStrings = { "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
            "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler" };

}
