package org.techtown.push.buttonnaviation.candidate;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class SectionsPagerAdapter extends FragmentStateAdapter {
    private ArrayList<Fragment> fragmentList;

    public SectionsPagerAdapter(@NonNull FragmentActivity fragmentActivity, Fragment f1, Fragment f2, Fragment f3) {
        super(fragmentActivity);
        fragmentList = new ArrayList<>();
        fragmentList.add(f1);
        fragmentList.add(f2);
        fragmentList.add(f3);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

}