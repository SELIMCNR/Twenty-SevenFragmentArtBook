package com.cinar.artbook.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.cinar.artbook.adapter.ListAdapter;
import com.cinar.artbook.databinding.FragmentListBinding;
import com.cinar.artbook.model.Art;
import com.cinar.artbook.roomdb.ArtDao;
import com.cinar.artbook.roomdb.ArtDatabase;

import java.util.List;


import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class listfragment extends Fragment {

    ListAdapter listAdapter;
    private FragmentListBinding binding;
    ArtDatabase artDatabase;
    ArtDao artDao;
    private final CompositeDisposable mDisposable = new CompositeDisposable();



    public listfragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artDatabase = Room.databaseBuilder(requireContext(),
                        ArtDatabase.class, "Arts")
                .build();

        artDao = artDatabase.artDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        getData();

    }

    public void getData() {

        mDisposable.add(artDao.getArtWithNameAndId()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listfragment.this ::handleResponse));

    }

    private void handleResponse(List<Art> artList) {

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        listAdapter = new ListAdapter(artList);
        binding.recyclerView.setAdapter(listAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        mDisposable.clear();
    }
}