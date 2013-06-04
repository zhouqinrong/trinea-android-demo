package cn.trinea.android.demo;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;

import com.trinea.android.common.view.DropDownListView;
import com.trinea.android.common.view.DropDownListView.OnDropDownListener;

/**
 * DropDownListViewDemo
 * 
 * @author Trinea 2013-6-1
 */
public class DropDownListViewDemo extends BaseActivity {

    private LinkedList<String>   listItems = null;
    private DropDownListView     listView  = null;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.drop_down_listview_demo);

        listView = (DropDownListView)findViewById(R.id.list_view);
        // set refresh listener
        listView.setOnDropDownListener(new OnDropDownListener() {

            @Override
            public void onDropDown() {
                new GetDataTask(true).execute();
            }
        });
        listView.setOnLoadMoreListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                new GetDataTask(false).execute();
            }
        });

        listItems = new LinkedList<String>();
        listItems.addAll(Arrays.asList(mStrings));
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        listView.setAdapter(adapter);
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        private boolean isDropDown;

        public GetDataTask(boolean isDropDown){
            this.isDropDown = isDropDown;
        }

        @Override
        protected String[] doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ;
            }
            return mStrings;
        }

        @SuppressLint("SimpleDateFormat")
        @Override
        protected void onPostExecute(String[] result) {

            if (isDropDown) {
                listItems.addFirst("Added after refresh");
                adapter.notifyDataSetChanged();

                // should call onDropDownComplete function of DropDownListView at end of drop down complete.
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
                listView.onDropDownComplete(getString(R.string.update_at) + dateFormat.format(new Date()));
            } else {
                listItems.add("Added after load more");

                listView.onLoadMoreComplete();
                adapter.notifyDataSetChanged();
            }

            super.onPostExecute(result);
        }
    }

    private String[] mStrings = { "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
            "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
            "Allgauer Emmentaler" };

}
