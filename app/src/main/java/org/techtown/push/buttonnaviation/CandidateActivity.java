package org.techtown.push.buttonnaviation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.techtown.push.buttonnaviation.candidate.SectionsPagerAdapter;
import org.techtown.push.buttonnaviation.candidate.promise.PromiseFragment;
import org.techtown.push.buttonnaviation.candidate.sns.SNSFragment;
import org.techtown.push.buttonnaviation.candidate.video.VideoFragment;
import org.techtown.push.buttonnaviation.databinding.ActivityCandidateBinding;

public class CandidateActivity extends AppCompatActivity {
    private ActivityCandidateBinding binding;
    private ViewPager2 viewPager;
    private String[] candidate_names = {"", "이재명 후보", "윤석열 후보", "심상정 후보", "안철수 후보"};
    String candidateNum;
    int pageNum = 0;
    Fragment f1, f2, f3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCandidateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //actionbar back button setting
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        viewPager = binding.viewPager;

        //get intent, and send intent to fragments
        sendIntent(null);

        //adapter setting
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, f1, f2, f3);
        binding.title.setText(candidate_names[Integer.parseInt(candidateNum)]);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.tabs, viewPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position) {
                    case 0: {
                        tab.setText(getResources().getString(R.string.tab_text_1));
                        break;
                    }
                    case 1: {
                        tab.setText(getResources().getString(R.string.tab_text_2));
                        break;
                    }
                    case 2: {
                        tab.setText(getResources().getString(R.string.tab_text_3));
                        break;
                    }
                }
            }
        });
        tabLayoutMediator.attach();
        viewPager.setCurrentItem(pageNum, false);

    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        sendIntent(intent);
    }

    public void sendIntent(Intent intent) {
        if(intent == null) intent = getIntent();

        candidateNum = intent.getStringExtra("candidate_num");
        pageNum = intent.getIntExtra("page_num", 0);
        Bundle bundle = new Bundle(1);
        bundle.putString("candidate_num", candidateNum);

        f1 = new VideoFragment();
        f2 = new SNSFragment();
        f3 = new PromiseFragment();

        f1.setArguments(bundle);
        f2.setArguments(bundle);
        f3.setArguments(bundle);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}