package com.example.goobee.testftp;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;

public class MainActivity extends AppCompatActivity {

    private FTPClient ftpClient;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ftpClient = new FTPClient();
        tv = findViewById(R.id.test_tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFile();
            }
        });
//        ftpClient.connect(TrackConstants.FTP_URL, TrackConstants.FTP_PORT);
//        ftpClient.login(TrackConstants.FTP_USERNAME, TrackConstants.FTP_PWD);
//        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

    }

    private void sendFile(){
        new Thread() {
            @Override public void run() {
                super.run();
                try {
                    //1.要连接的FTP服务器Url,Port
                    ftpClient.connect("192.168.1.8", 21);
                    //2.登陆FTP服务器
                    ftpClient.login("ftp_user", "AAbb20181115");
                    //3.看返回的值是不是230，如果是，表示登陆成功
                    int reply = ftpClient.getReplyCode();
                    if (!FTPReply.isPositiveCompletion(reply))
                    {
                        //断开
                        ftpClient.disconnect();
                        Log.d("Tets", "run: 登陆失败");
                        return;
                    }
                    Log.d("Tets", "run: 登陆成功");
                    //设置存储路径
                    ftpClient.makeDirectory("/data/");
                    ftpClient.changeWorkingDirectory("/data/");
                    //设置上传文件需要的一些基本信息
                    ftpClient.setBufferSize(1024);
                    ftpClient.setControlEncoding("UTF-8");
                    // ftpClient.enterLocalPassiveMode();
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                    //文件上传吧～
                    FileInputStream fileInputStream = new FileInputStream(Environment.getExternalStorageDirectory().getPath() +"/"+ "ucpaasRtcTrace.txt");
                    ftpClient.storeFile("Test.txt", fileInputStream);
                    //关闭文件流
                    fileInputStream.close();
                    //退出登陆FTP，关闭ftpCLient的连接
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
                catch (SocketException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
