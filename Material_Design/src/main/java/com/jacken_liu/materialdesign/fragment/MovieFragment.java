package com.jacken_liu.materialdesign.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jacken_liu.material_design.R;
import com.jacken_liu.materialdesign.adapter.NormalAdapter;
import com.jacken_liu.materialdesign.bean.Movie;
import com.jacken_liu.materialdesign.net.HttpMethods;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;

public class MovieFragment extends Fragment {
    @BindView(R.id.rv_fr_list)
    RecyclerView rvFrList;
    @BindView(R.id.srl_fr_refresh)
    SwipeRefreshLayout srlFrRefresh;
    Unbinder unbinder;
    private boolean firstShow = true;
    private NormalAdapter normalAdapter;


    private ArrayList<Movie.SubjectsBean> mMovieList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rv, null, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initData();
    }

    private void initData() {
        srlFrRefresh.setRefreshing(true);
        rvFrList.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (firstShow) {
            HttpMethods.getInstance().getTopMovie(new Subscriber<Movie>() {
                @Override
                public void onCompleted() {

                    srlFrRefresh.setRefreshing(false);
                    Log.i("ToolTAG", "onCompleted: ");
                }

                @Override
                public void onError(Throwable e) {
                    Log.i("ToolTAG", "onError: " + e.getMessage());
                    srlFrRefresh.setRefreshing(false);
                }

                @Override
                public void onNext(Movie movie) {

                    Log.i("ToolTAG", "onNext: " + movie.getSubjects().size());


                    mMovieList.addAll(movie.getSubjects());
                    normalAdapter = new NormalAdapter(mMovieList, getActivity());
                    rvFrList.setAdapter(normalAdapter);

                }
            }, 0, 10);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
