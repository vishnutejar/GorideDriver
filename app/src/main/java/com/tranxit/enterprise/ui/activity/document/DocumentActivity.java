package com.tranxit.enterprise.ui.activity.document;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.tranxit.enterprise.driver.R;
import com.tranxit.enterprise.base.BaseActivity;
import com.tranxit.enterprise.common.RecyclerItemClickListener;
import com.tranxit.enterprise.data.network.model.Document;
import com.tranxit.enterprise.ui.activity.photoview.PhotoViewActivity;
import com.tranxit.enterprise.ui.adapter.DocumentAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;

/*
    created by santhosh@appoets.com
*/

public class DocumentActivity extends BaseActivity implements DocumentIView {

    @BindView(R.id.document_rv)
    RecyclerView documentRv;

    DocumentPresenter<DocumentActivity> presenter = new DocumentPresenter<DocumentActivity>();
    DocumentAdapter adapter;
    Integer currentPosition;

    @Override
    public int getLayoutId() {
        return R.layout.activity_document;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        presenter.documents();
    }

    public void pickImage() {
        if (hasPermission(Manifest.permission.CAMERA) && hasPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            EasyImage.openChooserWithGallery(this, "", 0);
        } else {
            requestPermissionsSafely(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onSuccess(DocumentAdapter adapter1) {
        adapter = adapter1;
        documentRv.setLayoutManager(new LinearLayoutManager(activity(), LinearLayoutManager.VERTICAL, false));
        documentRv.setItemAnimator(new DefaultItemAnimator());
        documentRv.setAdapter(adapter);
        documentRv.addOnItemTouchListener(new RecyclerItemClickListener(this, documentRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                /*currentPosition = position;
                pickImage();*/

                Document document = adapter.getList().get(position);
                if(document != null){
                    String url = document.getUrl();
                    if(url != null){
                        Intent intent = new Intent(DocumentActivity.this, PhotoViewActivity.class);
                        intent.putExtra("url", url);
                        intent.putExtra("name", document.getName());
                        startActivity(intent);
                    }
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, DocumentActivity.this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                Document document = adapter.getList().get(currentPosition);
                document.setDocument(imageFiles.get(0));
                adapter.setItem(document, currentPosition);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ASK_MULTIPLE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean permission1 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean permission2 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (permission1 && permission2) {
                        pickImage();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.please_give_permissions, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @OnClick({R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.submit:
                List<MultipartBody.Part> myDocuments = new ArrayList<>();
                for (Document item : adapter.getList()) {
                    if (item.getDocument() != null) {
                        RequestBody requestImageFile = RequestBody.create(MediaType.parse("image/*"), item.getDocument());
                        myDocuments.add(MultipartBody.Part.createFormData("document[" + item.getId() + "]", item.getDocument().getName(), requestImageFile));
                    }
                }

                if (!myDocuments.isEmpty()) {
                    presenter.documents(myDocuments);
                } else {
                    Toast.makeText(this, "Please select documents", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

}



