package zoro.test.com.functionset;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zoro.test.com.functionset.upnpdemo.UDPClient;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.getinfo)
    Button getinfo;
    @Bind(R.id.info)
    TextView info;

    private UDPClient client;
    private String sendInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // 开启服务器
//        ExecutorService exec = Executors.newCachedThreadPool();
//        UDPServer server = new UDPServer();
//        exec.execute(server);
    }

    final Handler mHander = new Handler() {

        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            //super.handleMessage(msg);
            info.setText(sendInfo);

        }
    };

    @OnClick(R.id.getinfo)
    public void onViewClicked() {
        myThread1 thread = new myThread1("22");
        new Thread(thread).start();
    }

    class myThread1 implements Runnable {

        private String threadName;

        public myThread1(String name) {
            this.threadName = name;
        }

        public void run() {
            client = new UDPClient("M-SEARCH * HTTP/1.1\r\nMAN: \"ssdp:discover\"\r\nMX: 5\nHOST: 239.255.255.250:1900\r\nST: urn:schemas-upnp-org:service:AVTransport:1\r\n\r\n");
            sendInfo = client.send();

            Message msg = mHander.obtainMessage();
            msg.arg1 = 1;
            mHander.sendMessage(msg);
        }
    }
}
