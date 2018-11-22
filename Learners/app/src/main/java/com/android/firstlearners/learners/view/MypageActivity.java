package com.android.firstlearners.learners.view;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.firstlearners.learners.R;
import com.android.firstlearners.learners.etc.LearnersApplication;
import com.android.firstlearners.learners.model.DeleteListener;
import com.android.firstlearners.learners.model.MypageApi;
import com.android.firstlearners.learners.model.NetworkService;
import com.android.firstlearners.learners.model.Repository;
import com.android.firstlearners.learners.model.SharedPreferenceManager;
import com.android.firstlearners.learners.model.data.ResponseMessage;
import com.android.firstlearners.learners.model.data.ResponseMypage;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//추가
import android.provider.MediaStore;
import android.widget.Toast;


public class MypageActivity extends AppCompatActivity {
    AccountDeleteDialog deleteDialog;
    private NetworkService networkService;
    @BindView(R.id.changeImage)
    ImageView btn_changeImage;
    @BindView(R.id.userImage)
    ImageView userImage;
    @BindView(R.id.study_manage)
    TextView btn_studymanage;
    @BindView(R.id.userName) TextView userName;
    @BindView(R.id.userLevel) TextView userLevel;
    @BindView(R.id.study_cnt) TextView study_cnt;
    @BindView(R.id.user_delete) TextView user_delete;

    //study_manage
    MypageApi mypageApi;
    private static int PICK_IMAGE_REQUEST = 1;
    SharedPreferenceManager sharedPreferenceManager;

    //이미지 선택
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
//            Uri uri = data.getData();
//            Log.d("hi baby", uri.toString());
//            RequestOptions options = new RequestOptions();
//            options.centerCrop();
//            Glide.with(getApplicationContext())
//                    .load(uri)
//                    .apply(RequestOptions.circleCropTransform())
//                    .into(userImage);
//
//            userImage.setScaleType(ImageView.ScaleType.FIT_XY);

            android.net.Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            android.database.Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            Log.d("test_path", filePath);
            File file = new File(filePath);
            uploadFile(file);

        }

    }

    //  서버로 프로필 이미지 업로드
    private void uploadFile(File file) {
        okhttp3.RequestBody reqFile = okhttp3.RequestBody.create(okhttp3.MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
        Log.d("filename", file.getName());
        Call<ResponseBody> imageUpload = mypageApi.editImage(networkService.getUserToken(), body);
        imageUpload.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Log.d("Upload success", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Log.e("error",t.getMessage());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        //여기 수정

        LearnersApplication context = (LearnersApplication) getApplicationContext();

        sharedPreferenceManager = new SharedPreferenceManager(context);
        networkService = context.getNetworkService();
        Realm realm = context.getRealm();

        Repository repository = new Repository(sharedPreferenceManager, networkService, realm);

        ButterKnife.bind(this);

        deleteDialog = new AccountDeleteDialog(this);
        //retrofit통신
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://54.180.69.136:3000/")
                .build();

        mypageApi = retrofit.create(MypageApi.class);

        //데이터 받아오기
       // "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjo2LCJ1c2VyX25hbWUiOiLsmIHrr7wiLCJ1c2VyX2VtYWlsIjoieXl5IiwidXNlcl9waG9uZSI6IjAxMDEyMzQxMjM1IiwiaWF0IjoxNTQxNTkxOTI0LCJleHAiOjE1NTAyMzE5MjR9.m4OqDj5CkJpEhz81zebOHI62ZV290lpQOHuTW5NRgB0"
        Call<ResponseMypage> mypage = mypageApi.requestMypage(networkService.getUserToken());
        mypage.enqueue(new Callback<ResponseMypage>() {
            @Override
            public void onResponse(Call<ResponseMypage> call, Response<ResponseMypage> response) {
                ResponseMypage responseMypage = response.body();
                if(response.body().isStatus()){
                    Log.d("image success", response.body().getMessage());
                    userName.setText(response.body().getResult().getUser_name());
                    int level = response.body().getResult().getUser_level();
                    switch(level){
                        case 0:
                            userLevel.setText("개설자");
                            break;
                        case 1:
                            userLevel.setText("스터디원");
                            break;
                        case 2:
                            userLevel.setText("퍼스트러너");
                            break;
                        case 3:
                            userLevel.setText("프로참석러 && 개설자");
                            break;
                        case 4:
                            userLevel.setText("프로참석러 && 스터디원");
                            break;
                    }

                    // 사용자의 이미지가 존재할 때
                    if(response.body().getResult().getUser_img()!=null){
                  //  Uri imageUri = Uri.fromFile(new File(response.body().getResult().getUser_img()));
                    //    Uri imageUri = Uri.parse(response.body().getResult().getUser_img());
                        Log.d("image", response.body().getResult().getUser_img());
                        RequestOptions options = new RequestOptions();
                        Glide.with(getApplicationContext())
                                .load(response.body().getResult().getUser_img())
                                .apply(RequestOptions.circleCropTransform())
                                .into(userImage);
                    }
                    else
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.basic_profile)
                                .into(userImage);
                    }
                    study_cnt.setText(response.body().getResult().getUser_att_cnt()+" 회");
                }
            }

            @Override
            public void onFailure(Call<ResponseMypage> call, Throwable t) {

            }
        });
    }


    @OnClick(value = {R.id.study_manage, R.id.changeImage, R.id.user_delete})
    void OnClick(View view){
        switch (view.getId()){
            //스터디 관리 페이지로이동
            case R.id.study_manage:
                if(sharedPreferenceManager.getString("study_id")==null){
                    Toast.makeText(this, "스터디가 없습니다.", Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(this, ManageStudyActivity.class);
                    // intent.putExtra("user_token",networkService.getUserToken());
                    this.startActivity(intent);
                }
                break;
            //사진 선택
            case R.id.changeImage:
//                Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
//                intent2.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                intent2.setType("image/*");
//                startActivityForResult(Intent.createChooser(intent2, "select picture"), PICK_IMAGE_REQUEST);

                Intent intent1 = new Intent();
                intent1.setType("image/*");
                intent1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent1, "Select Image"), PICK_IMAGE_REQUEST);

                break;
            case R.id.user_delete:
                deleteDialog.show();
                deleteDialog.setDeleteListener(new DeleteListener() {
                    @Override
                    public void onDelete() {
                        Call<ResponseMessage> delete = mypageApi.deleteUser(networkService.getUserToken());
                        delete.enqueue(new Callback<ResponseMessage>() {
                            @Override
                            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                                ResponseMessage responseMessage = response.body();
                                Log.d("delete success",responseMessage.getMessage());
                                Log.d("token",networkService.getUserToken());
                                //전체 내부 데이터 삭제
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("Learners", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.clear();
                                editor.commit();
                                goToSplash();
                            }

                            @Override
                            public void onFailure(Call<ResponseMessage> call, Throwable t) {

                            }
                        });
                    }
                });
                break;

        }
    }

    public void  goToSplash(){
        Intent intent = new Intent(this, SplashActivity.class);
        this.startActivity(intent);
    }


}
