package com.android.firstlearners.learners.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.contract.MainContract;
import com.android.firstlearners.learners.etc.LearnersApplication;
import com.android.firstlearners.learners.etc.StudyProgressBar;
import com.android.firstlearners.learners.model.DialogListener;
import com.android.firstlearners.learners.model.MypageApi;
import com.android.firstlearners.learners.model.NetworkService;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.model.SharedPreferenceManager;
import com.android.firstlearners.learners.model.StudyOutListener;
import com.android.firstlearners.learners.model.data.ResponseMessage;
import com.android.firstlearners.learners.model.data.Study;
import com.android.firstlearners.learners.model.data.StudyUsers;
import com.android.firstlearners.learners.presenter.MainPresenter;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ManageStudyActivity extends AppCompatActivity implements View.OnClickListener {
        //, MainContract.View{
    //추가
//    private MainContract.Action presenter;
//    private RankingRecyclerViewAdapter adapter;
   //여기까지
    ChangeCountDialog countDialog;
    StudyOutDialog studyOutDialog;
    private NetworkService networkService;
    MypageApi mypageApi;
    @BindView(R.id.study_duration)
    TextView study_duration;
    @BindView(R.id.study_out) TextView study_out;
    @BindView(R.id.study_invite) TextView study_invite;
    @BindView(R.id.study_count) TextView study_count;
//
//    //새로추가
//    @BindView(R.id.dashboard_container) View dashboardContainer;
//    @BindView(R.id.default_container) View defaultContainer;
//    @BindView(R.id.network_container) View networkContainer;
//    @BindView(R.id.btn_make) TextView btn_make;
//    @BindView(R.id.btn_profile)
//    ImageView profile;
//    @BindView(R.id.study_progress)
//    StudyProgressBar progressBarStudy;
//    @BindView(R.id.during) TextView during;
//    @BindView(R.id.duringTitle) TextView duringTitle;
//    @BindView(R.id.rankingBox)
//    RecyclerView rankingBox;
//    @BindView(R.id.study_day) TextView day;
//    @BindView(R.id.study_goal) TextView goal;
//    @BindView(R.id.refresh) ImageView refresh;
//    @BindView(R.id.btn_attendance)
//    FloatingActionButton btn_attendance;
//    @BindView(R.id.first) ImageView first;
//    @BindView(R.id.second) ImageView second;
//    @BindView(R.id.third) ImageView third;
//    @BindView(R.id.firstName) TextView firstName;
//    @BindView(R.id.secondName) TextView secondName;
//    @BindView(R.id.thirdName) TextView thirdName;
//    @BindView(R.id.thirdBackground)
//    RelativeLayout thirdBackground;
//    @BindView(R.id.secondBackground) RelativeLayout secondBackground;
SharedPreferenceManager sharedPreferenceManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_study);
        countDialog = new ChangeCountDialog(this);
        studyOutDialog = new StudyOutDialog(this);
        ButterKnife.bind(this);

        //추가
        LearnersApplication context = (LearnersApplication) getApplicationContext();

        sharedPreferenceManager = new SharedPreferenceManager(context);
        networkService = context.getNetworkService();
        Realm realm = context.getRealm();

        Repository repository = new Repository(sharedPreferenceManager, networkService, realm);
    //    presenter = new MainPresenter(repository, this);


        //retrofit통신
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://54.180.69.136:3000/")
                .build();

        mypageApi = retrofit.create(MypageApi.class);
    }


    @OnClick(value = {R.id.study_duration, R.id.study_out, R.id.study_invite, R.id.study_count})
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //스터디 관리 페이지로이동
            case R.id.study_invite:
                Intent intent = new Intent(this, InviteActivity.class);
                this.startActivity(intent);
                break;
            case R.id.study_duration:
                countDialog.show();
                countDialog.setDialogListener(new DialogListener() {
                    @Override
                    public void onPositive(int count) {
                        Call<ResponseMessage> change = mypageApi.changeCount(networkService.getUserToken(), count);
                        change.enqueue(new Callback<ResponseMessage>() {
                            @Override
                            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                                ResponseMessage responseMessage = response.body();
                                Log.d("okay", responseMessage.getMessage());

                            }

                            @Override
                            public void onFailure(Call<ResponseMessage> call, Throwable t) {

                            }
                        });
                    }

                });
                break;
                //스터디 나가기
            case R.id.study_out:
                studyOutDialog.show();
                studyOutDialog.setStudyOutListener(new StudyOutListener() {
                    @Override
                    public void studyOut() {
                        Call<ResponseMessage> delete = mypageApi.studyOut(networkService.getUserToken());
                        delete.enqueue(new Callback<ResponseMessage>() {
                            @Override
                            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                                ResponseMessage responseMessage = response.body();
                                Log.d("delete success",responseMessage.getMessage());
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("Learners", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();

                                String id =  sharedPreferenceManager.getString("study_id");
                                Log.d("study_out", id);
                                sharedPreferenceManager.setString("study_id",null);
                                editor.remove("study_id");

                                editor.commit();
                                Log.d("study_out_after", id);
                                goToMain();

                            }

                            @Override
                            public void onFailure(Call<ResponseMessage> call, Throwable t) {

                            }
                        });

                    }
                });

            break;
            //스터디 기간 변경
            case R.id.study_count:
                Intent intent1 = new Intent(this, StudyDateDialog.class);
                intent1.putExtra("user_token",networkService.getUserToken());
                this.startActivity(intent1);
                break;
        }
    }

    public void goToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("flag",false);
        this.startActivity(intent);
    }
//    @Override
//    public void setStudyData(Study study) {
//      //
//        //  dashboardContainer.setVisibility(View.VISIBLE);
//        defaultContainer.setVisibility(View.INVISIBLE);
//        networkContainer.setVisibility(View.INVISIBLE);
//        RealmList<StudyUsers> studyUsers = new RealmList<>();
//
//        for( StudyUsers s : study.study_users){
//            studyUsers.add(s);
//        }
//
//        if(studyUsers.size() > 2){
//            thirdName.setText(studyUsers.get(2).user_name);
//            //스터디 유저의 이미지 업로드
//            Glide.with(getApplicationContext()).load(R.drawable.basic_profile).into(third);
//            thirdBackground.setVisibility(View.VISIBLE);
//            third.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(),IndividualDialog.class);
//                    intent.putExtra("user_idx",2);
//                    startActivity(intent);
//                }
//            });
//            studyUsers.remove(2);
//        }
//        if(studyUsers.size() > 1){
//            secondName.setText(studyUsers.get(1).user_name);
//            Glide.with(getApplicationContext()).load(R.drawable.basic_profile).into(second);
//            secondBackground.setVisibility(View.VISIBLE);
//            second.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getApplicationContext(),IndividualDialog.class);
//                    intent.putExtra("user_idx",1);
//                    startActivity(intent);
//                }
//            });
//            studyUsers.remove(1);
//        }
//        firstName.setText(studyUsers.get(0).user_name);
//        Glide.with(getApplicationContext()).load(R.drawable.basic_profile).into(first);
//        first.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(),IndividualDialog.class);
//                intent.putExtra("user_idx",0);
//                startActivity(intent);
//            }
//        });
//
//        studyUsers.remove(0);
//
//        Study dump = new Study();
//        dump.study_users = studyUsers;
//        dump.study_count = study.study_count;
//
//        //study.study_users = studyUsers;
//        adapter = new RankingRecyclerViewAdapter(dump);
//
//        rankingBox.setAdapter(adapter);
//        rankingBox.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//
//        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREAN);
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_MONTH,1);
//
//        String start = format.format(calendar.getTime());
//
//        int endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
//        calendar.set(Calendar.DAY_OF_MONTH, endDay);
//
//        String end = format.format(calendar.getTime());
//
//        int month = calendar.get(Calendar.MONTH) + 1;
//
//        String string = "스터디 "+study.study_day+"일째 | 목표 달성까지 "+study.study_day_goal+"일";
//        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(string);
//        int count = 1;
//        int temp = study.study_day;
//
//        while((temp = temp / 10) > 0){
//            count++;
//        }
//        stringBuilder.setSpan(new ForegroundColorSpan(Color.RED),4,4 + count, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        temp = study.study_day_goal;
//
//        int count2 = 1;
//        while((temp = temp / 10) > 0 ){
//            count2++;
//        }
//        stringBuilder.setSpan(new ForegroundColorSpan(Color.RED),17 + count, 17 + count + count2,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        day.setText(stringBuilder);
//        goal.setText(study.study_goal);
//
//        if(study.study_day_goal == 0){
//            during.setVisibility(View.GONE);
//            duringTitle.setText("목표를 달성하였습니다.");
//        }
//        else{
//            during.setText(start+" ~ "+end);
//            duringTitle.setText(month+"월 목표 달성률");
//        }
//        progressBarStudy.setProgress(study.study_percent);
//    }
//    @Override
//    public void setShownView(boolean flag) {
//        if(flag){
//            networkContainer.setVisibility(View.INVISIBLE);
//            defaultContainer.setVisibility(View.VISIBLE);
//        }
//        else{
//            networkContainer.setVisibility(View.VISIBLE);
//            defaultContainer.setVisibility(View.INVISIBLE);
//        }
//    }
//
//    @Override
//    public void showAttendanceDialog(boolean flag, Map<String, Object> result) {
//
//    }
//
//    @Override
//    public void startInvitationActivity(String userName) {
//
//    }
}

