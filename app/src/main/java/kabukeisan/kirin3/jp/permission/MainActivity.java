package kabukeisan.kirin3.jp.permission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE1 = 1001;
    private static final int REQUEST_CODE2 = 1002;
    private static final int REQUEST_CODE3 = 1003;
    Button button1,button2,button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button1 = (Button)findViewById(R.id.button1);
        button2 = (Button)findViewById(R.id.button2);
        button3 = (Button)findViewById(R.id.button3);

        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkPermission1();
            }
        });

        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                checkPermission2(Manifest.permission.ACCESS_COARSE_LOCATION,REQUEST_CODE2);
            }
        });

        button3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String permissions[] = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_EXTERNAL_STORAGE};
                checkPermission3(permissions,REQUEST_CODE3);
            }
        });

    }

    public void checkPermission1(){
        // ・現在地取得のパーミッションが許可確認
        // 許可されていない
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE1);
        }
        // パーミッションが許可されている
        else {
            Toast.makeText(this, "権限が許可されています", Toast.LENGTH_SHORT).show();
            // 以下通常処理等に飛ばす・・・
        }
    }


    // パーミッションの状態を確認して、各処理に飛ばす
    public void checkPermission2(final String permission,final int request_code){
        // ・現在地取得のパーミッションが許可確認
        // 許可されていない
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // ・許可が必要な説明を表示するかの判定
            // 「今後は確認しない」にチェックがないので、表示が必要
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                // 説明をダイアログで表示
                showInfomationDialog(permission, request_code);
            }
            // 初回は shouldShowRequestPermissionRationale() は必ずfalse を返すため
            // 必要ならプリファランスなどに権限を許可したかの判定を持っていて、強制的にダイアログを表示する
//          else if ( プリファランス == false) {
//              showInfomationDialog(permission, request_code);
//          }
            // 初回、または、「今後は確認しない」にチェックがある
            else {
                // 「今後は確認しない」の場合、パーミッション追加のポップアップは出ないで、コールバックは強制的に失敗[PERMISSION_DENIED]を返してくるので注意
                ActivityCompat.requestPermissions(this, new String[]{permission}, request_code);

            }
        }
        // パーミッションが許可されている
        else {
            Toast.makeText(this, "権限が許可されています", Toast.LENGTH_SHORT).show();
            // 以下通常処理等に飛ばす・・・
        }
    }

    // 複数のパーミッションを設定
    public void checkPermission3(final String permissions[],final int request_code){
        // 許可されていないものだけダイアログが表示される
        ActivityCompat.requestPermissions(this, permissions, request_code);
    }

    // ダイアログを表示してOKを押したら、パーミッション追加リクエスト
    public void showInfomationDialog(final String permission, final int request_code){
        final WeakReference<MainActivity> weakSelf = new WeakReference<>(this);
        new AlertDialog.Builder(this)
                .setTitle("パーミッションについて")
                .setMessage("最寄りの本屋を検索するのに現在地取得を許可してもらう必要があります。")
                .setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 権限を追加
                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, request_code);
                            }
                        })
                .show();

    }

    // requestPermissionsのコールバック
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "パーミッション追加しました", Toast.LENGTH_SHORT).show();
                    // 以下通常処理等に飛ばす・・・
                } else {
                    Toast.makeText(this, "パーミッション追加できませんでした", Toast.LENGTH_SHORT).show();
                }
                break;


            case REQUEST_CODE2:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "パーミッション追加しました", Toast.LENGTH_SHORT).show();
                    // 以下通常処理等に飛ばす・・・
                } else {
                    Toast.makeText(this, "パーミッション追加できませんでした。パーミッションがないと機能が使えませんよ！", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE3:
                for(int i = 0; i < permissions.length; i++ ){
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.w( "DEBUG_DATA", "パーミッション追加しました " + permissions[i] + " " + grantResults[i]);
                        // 以下通常処理等に飛ばす・・・
                    } else {
                        Log.w( "DEBUG_DATA", "パーミッション追加できませんでした。 " + permissions[i] + " " + grantResults[i]);
                    }
                }
                break;
            default:
                break;
        }
    }
}
