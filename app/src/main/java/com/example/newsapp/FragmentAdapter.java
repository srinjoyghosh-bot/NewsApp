package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 1:
                return new IndiaFragment();
            case 2:
                return new BusinessFragment();
            case 3:
                return new EntertainmentFragment();
            case 4:
                return new SportsFragment();
            case 5:
                return new TechFragment();
        }
        return new HomeFragment();
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
