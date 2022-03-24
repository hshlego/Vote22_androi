package org.techtown.push.buttonnaviation.candidate.promise;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.techtown.push.buttonnaviation.databinding.FragmentPromiseBinding;

public class PromiseFragment extends Fragment {
    private FragmentPromiseBinding binding;
    private String[] urls = {"",
            "https://www.jmleetogether.com/web/center.php",
            "https://blog.naver.com/PostList.naver?blogId=yoonsukyeol&from=postList&categoryNo=9",
            "http://www.xn--hg4br3bj9g.com/home/post_list.php?mc=4",
            "https://ahncheolsoo.kr/promise"
    };

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPromiseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String candidate_num = getArguments().getString("candidate_num");

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(candidate_num == null) {
                    Toast.makeText(getContext(), "죄송합니다, 오류로 인해 페이지를 띄울 수 없습니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                String url = urls[Integer.parseInt(candidate_num)];
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(Uri.parse(url));
                startActivity(webIntent);
            }
        });


        return root;
    }
}
