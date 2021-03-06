package com.example.chintan.research.main;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.widget.Toast;

import com.example.chintan.research.R;
import com.example.chintan.research.adapter.BasicCellViewGroup;
import com.example.chintan.research.adapter.BasicTableFixHeaderAdapter;
import com.example.chintan.research.model.TravelAndStopSummaryItem;
import com.example.chintan.research.remote.ApiResponse;
import com.example.chintan.research.remote.MyRetrofitdebug;
import com.example.chintan.research.remote.VtsService;
import com.example.chintan.research.widget.fixheader.main.TableFixHeaders;
import com.example.chintan.research.widget.fixheader.wrapper.TableFixHeaderAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class WrapperActivity extends AppCompatActivity implements TableFixHeaderAdapter.ClickListener<String, BasicCellViewGroup> {
    private ProgressDialog dialog;
    private BasicTableFixHeaderAdapter adapter;
    private ArrayList<TravelAndStopSummaryItem> alData;
    private SearchView searchView;
    private ArrayList<TravelAndStopSummaryItem> alTemp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrapper);

        TableFixHeaders tablefixheaders = findViewById(R.id.tablefixheaders);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait...");

        alData = new ArrayList<>();
        alTemp = new ArrayList<>();

        adapter = new BasicTableFixHeaderAdapter(this);
        adapter.setFirstHeader("Vehicle Number");
        adapter.setHeader(Arrays.asList(getResources().getStringArray(R.array.header_array)));
        tablefixheaders.setAdapter(adapter);
        adapter.setClickListenerHeader(this);
        adapter.setClickListenerFirstHeader((s, basicCellViewGroup, i, i1) -> {
            BasicTableFixHeaderAdapter.mColumn = -1;
            Collections.sort(alData, (left, right) ->
                    left.getVEHICLE_NUMBER().
                            compareToIgnoreCase(right.getVEHICLE_NUMBER()));
            addData(alData);
        });
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_and_three_dot, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(new MySearchChangeListener());
        return true;
    }

    private void getData() {
        dialog.show();
        alData.clear();
        MyRetrofitdebug.getInstance(this).create(VtsService.class)
                .getData("1dzpjo")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ApiResponse<ArrayList<TravelAndStopSummaryItem>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ApiResponse<ArrayList<TravelAndStopSummaryItem>> response) {
                        dialog.dismiss();
                        try {
                            if (response.isSuccess()) {
                                alData = response.getData();
                                alTemp = response.getData();
                                addData(alData);
                                Toast.makeText(WrapperActivity.this, "Successfully added..!!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        dialog.dismiss();
                        Toast.makeText(WrapperActivity.this, "Something went wrong..!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void onClickItem(String s, BasicCellViewGroup basicCellViewGroup, int row, int column) {
        BasicTableFixHeaderAdapter.mColumn = column;
        switch (column) {
            case 0:
                Collections.sort(alData, (left, right) -> left.getCOMPANY().compareToIgnoreCase(right.getCOMPANY()));
                break;
            case 1:
                Collections.sort(alData, (left, right) -> left.getDATE().compareToIgnoreCase(right.getDATE()));
                break;
            case 2:
                Collections.sort(alData, (left, right) -> right.getMAXSPEED().compareToIgnoreCase(left.getMAXSPEED()));
                break;
            case 3:
                Collections.sort(alData, (left, right) -> right.getRUNNINGTIME().compareToIgnoreCase(left.getRUNNINGTIME()));
                break;
            case 4:
                Collections.sort(alData, (left, right) -> right.getAVGSPEED().compareToIgnoreCase(left.getAVGSPEED()));
                break;
        }
        addData(alData);
    }

    public void addData(ArrayList<TravelAndStopSummaryItem> alData) {
        List<List<String>> rows = new ArrayList<>();
        for (TravelAndStopSummaryItem item : alData)
            rows.add(new ArrayList<>(Arrays.asList(item.getUseAbleData()).subList(0, adapter.getHeader().size() + 1)));
        adapter.setFirstBody(rows);
        adapter.setBody(rows);
        adapter.setSection(rows);
        adapter.notifyDataSetChanged();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        ArrayList<TravelAndStopSummaryItem> alSearch = new ArrayList<>();
        if (charText.length() == 0) {
            alSearch.addAll(alTemp);
        } else {
            for (TravelAndStopSummaryItem item : alTemp) {
                if (item.getVEHICLE_NUMBER().toLowerCase(Locale.getDefault()).contains(charText) ||
                        item.getCOMPANY().toLowerCase(Locale.getDefault()).contains(charText)) {
                    alSearch.add(item);
                }
            }
        }
        alData = alSearch;
        addData(alData);
    }

    private class MySearchChangeListener implements SearchView.OnQueryTextListener {
        @Override
        public boolean onQueryTextSubmit(final String query) {
            filter(query);
            searchView.clearFocus();
            return true;
        }

        @Override
        public boolean onQueryTextChange(final String newText) {
            filter(newText);
            return true;
        }
    }
}
