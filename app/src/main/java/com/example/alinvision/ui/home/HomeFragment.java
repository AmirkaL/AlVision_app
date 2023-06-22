package com.example.alinvision.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.alinvision.LoadingActivity;
import com.example.alinvision.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class HomeFragment extends Fragment {

    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        textView = root.findViewById(R.id.text_home);
        Button scanButton = root.findViewById(R.id.addCamera);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // подгрузка сканера штрихкодов
                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(HomeFragment.this);
                integrator.setPrompt("Отсканируйте штрих-код или QR-код");
                integrator.setOrientationLocked(false);
                integrator.initiateScan();
            }
        });

        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null && result.getContents() != null) {
                String cameraID = result.getContents();

                // идентификатор текущего пользователя
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                // Сохранение отсканированного штрихкода в Firebase для текущего пользователя
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                DatabaseReference cameraIDsRef = userRef.child("cameraIDs");
                String newCameraIDKey = cameraIDsRef.push().getKey();
                cameraIDsRef.child(newCameraIDKey).setValue(cameraID);

                // Запуск LoadingActivity на 3 секунды
                Intent loadingIntent = new Intent(getActivity(), LoadingActivity.class);
                startActivity(loadingIntent);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Возврат к предыдущему фрагменту
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                }, 3000); // Задержка 3 секунды
            }
        }
    }
}
