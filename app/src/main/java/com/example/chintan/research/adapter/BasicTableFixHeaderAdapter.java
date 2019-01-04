package com.example.chintan.research.adapter;

import android.content.Context;

import com.example.chintan.research.R;
import com.example.chintan.research.widget.fixheader.wrapper.TableFixHeaderAdapter;

import java.util.ArrayList;
import java.util.List;


public class BasicTableFixHeaderAdapter extends TableFixHeaderAdapter<
        String, BasicCellViewGroup,
        String, BasicCellViewGroup,
        List<String>,
        BasicCellViewGroup,
        BasicCellViewGroup,
        BasicCellViewGroup> {

    public static int mColumn = -2;
    private Context context;

    public BasicTableFixHeaderAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected BasicCellViewGroup inflateFirstHeader() {
        return new BasicCellViewGroup(context);
    }

    @Override
    protected BasicCellViewGroup inflateHeader() {
        return new BasicCellViewGroup(context);
    }

    @Override
    protected BasicCellViewGroup inflateFirstBody() {
        return new BasicCellViewGroup(context);
    }

    @Override
    protected BasicCellViewGroup inflateBody() {
        return new BasicCellViewGroup(context);
    }

    @Override
    protected BasicCellViewGroup inflateSection() {
        return new BasicCellViewGroup(context);
    }

    @Override
    protected List<Integer> getHeaderWidths() {
        List<Integer> headerWidths = new ArrayList<>();

        // First header
        headerWidths.add((int) context.getResources().getDimension(R.dimen._150dp));

        for (int i = 0; i < 20; i++)
            headerWidths.add((int) context.getResources().getDimension(R.dimen._100dp));

        return headerWidths;
    }

    @Override
    protected int getHeaderHeight() {
        return (int) context.getResources().getDimension(R.dimen._40dp);
    }

    @Override
    protected int getSectionHeight() {
        return (int) context.getResources().getDimension(R.dimen._40dp);
    }

    @Override
    protected int getBodyHeight() {
        return (int) context.getResources().getDimension(R.dimen._40dp);
    }

    @Override
    protected boolean isSection(List<List<String>> items, int row) {
        return false;
    }


}