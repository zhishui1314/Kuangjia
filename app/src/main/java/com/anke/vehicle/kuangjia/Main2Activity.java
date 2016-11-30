package com.anke.vehicle.kuangjia;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import permissiongen.PermissionFail;
import permissiongen.PermissionGen;
import permissiongen.PermissionSuccess;

/**
 * android6.0所有权限获取
 */
public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionGen.with(Main2Activity.this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }
    @PermissionSuccess(requestCode = 100)
    public void doSuccess(){
//        Toast.makeText(this, "Contact permission is granted", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,MainActivity.class));
    }
    @PermissionFail(requestCode = 100)
    public void doFail(){
        Toast.makeText(this, "Contact permission is not granted", Toast.LENGTH_SHORT).show();
    }
}


