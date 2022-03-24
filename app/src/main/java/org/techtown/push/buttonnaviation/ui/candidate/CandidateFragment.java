package org.techtown.push.buttonnaviation.ui.candidate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.techtown.push.buttonnaviation.CandidateActivity;
import org.techtown.push.buttonnaviation.MainActivity;
import org.techtown.push.buttonnaviation.R;
import org.techtown.push.buttonnaviation.databinding.CandidateItemBinding;
import org.techtown.push.buttonnaviation.databinding.FragmentCandidateBinding;

public class CandidateFragment extends Fragment {
    private CandidateViewModel candidateViewModel;
    private FragmentCandidateBinding binding;
    private View.OnClickListener itemListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        candidateViewModel =
                new ViewModelProvider(this).get(CandidateViewModel.class);

        binding = FragmentCandidateBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //adjust candidate item settings
        CandidateItemBinding c1 = binding.candidate1;
        c1.cLogo.setImageResource(R.drawable.c1_logo);
        c1.cItemUnderbar.setBackground(getResources().getDrawable(R.drawable.c1_gradient));
        c1.cArrow.setColorFilter(getResources().getColor(R.color.c1_color));
        c1.cText.setText(" 이재명");
        c1.getRoot().setOnClickListener(makeListener("1", 0));
        c1.cButton1.setOnClickListener(makeListener("1", 0));
        c1.cButton2.setOnClickListener(makeListener("1", 1));
        c1.cButton3.setOnClickListener(makeListener("1", 2));

        CandidateItemBinding c2 = binding.candidate2;
        c2.cLogo.setImageResource(R.drawable.c2_logo);
        c2.cItemUnderbar.setBackground(getResources().getDrawable(R.drawable.c2_gradient));
        c2.cArrow.setColorFilter(getResources().getColor(R.color.c2_color));
        c2.cText.setText(" 윤석열");
        c2.getRoot().setOnClickListener(makeListener("2", 0));
        c2.cButton1.setOnClickListener(makeListener("2", 0));
        c2.cButton2.setOnClickListener(makeListener("2", 1));
        c2.cButton3.setOnClickListener(makeListener("2", 2));

        CandidateItemBinding c3 = binding.candidate3;
        c3.cLogo.setImageResource(R.drawable.c3_logo);
        c3.cItemUnderbar.setBackground(getResources().getDrawable(R.drawable.c3_gradient));
        c3.cArrow.setColorFilter(getResources().getColor(R.color.c3_color));
        c3.cText.setText(" 심상정");
        c3.getRoot().setOnClickListener(makeListener("3", 0));
        c3.cButton1.setOnClickListener(makeListener("3", 0));
        c3.cButton2.setOnClickListener(makeListener("3", 1));
        c3.cButton3.setOnClickListener(makeListener("3", 2));

        CandidateItemBinding c4 = binding.candidate4;
        c4.cLogo.setImageResource(R.drawable.c4_logo);
        c4.cItemUnderbar.setBackground(getResources().getDrawable(R.drawable.c4_gradient));
        c4.cArrow.setColorFilter(getResources().getColor(R.color.c4_color));
        c4.cText.setText(" 안철수");
        c4.getRoot().setOnClickListener(makeListener("4", 0));
        c4.cButton1.setOnClickListener(makeListener("4", 0));
        c4.cButton2.setOnClickListener(makeListener("4", 1));
        c4.cButton3.setOnClickListener(makeListener("4", 2));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public View.OnClickListener makeListener(String candidateNum, int pageNum) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CandidateActivity.class);
                intent.putExtra("candidate_num", candidateNum);
                intent.putExtra("page_num", pageNum);
                startActivity(intent);
            }
        };
    }
}