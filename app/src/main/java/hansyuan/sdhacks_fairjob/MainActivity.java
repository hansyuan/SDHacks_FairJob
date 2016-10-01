package hansyuan.sdhacks_fairjob;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dropbox.chooser.android.DbxChooser;

public class MainActivity extends AppCompatActivity {
    NfcAdapter mNfcAdapter;
    private FileUriCallback mFileUriCallback;

    static final int DBX_CHOOSER_REQUEST = 0;  // You can change this if needed

    private Button mChooserButton;
    private DbxChooser mChooser;

    // List of URIs to provide to Android Beam
    private Uri[] mFileUris = new Uri[10];

    /**
     * Callback that Android Beam file transfer calls to get
     * files to share
     */
    private class FileUriCallback implements
            NfcAdapter.CreateBeamUrisCallback {
        public FileUriCallback() {
        }
        /**
         * Create content URIs as needed to share with another device
         */
        @Override
        public Uri[] createBeamUris(NfcEvent event) {
            return mFileUris;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Android Beam file transfer is available, continue

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        /*
         * Instantiate a new FileUriCallback to handle requests for
         * URIs
         */
        mFileUriCallback = new FileUriCallback();
        // Set the dynamic callback for URI requests.
        mNfcAdapter.setBeamPushUrisCallback(mFileUriCallback,this);

        mChooser = new DbxChooser("gfzgo38zc2yxpsv");


        mChooserButton = (Button) findViewById(R.id.chooser_button);
        mChooserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChooser.forResultType(DbxChooser.ResultType.FILE_CONTENT)
                        .launch(MainActivity.this, DBX_CHOOSER_REQUEST);
            }


        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DBX_CHOOSER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                DbxChooser.Result result = new DbxChooser.Result(data);
                mFileUris[0] = result.getLink();
                Log.d("main", "Link to selected file: " + result.getLink());

                // Handle the result
            } else {
                // Failed or was cancelled by the user.
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
