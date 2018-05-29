package gcm.play.android.samples.com.gcmquickstart;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PreloadFragment extends Fragment {
    private final static String TAG = "PreloadFragment";
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View child = inflater.inflate(R.layout.fragment_preload, container, false);
        return child;
    }

    private void transactFragment(Fragment fragment) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                final FragmentManager fm = getFragmentManager();
                if (fm != null) {
                    final Bundle arguments = getArguments();
                    //FIXME no env can test
                    if (arguments != null) {
                        Log.d(TAG, "Here  arguments  E: " + arguments.getParcelable(C.PUSH_NOTIFICATION_PAYLOAD));
                        fragment.setArguments(arguments);
                    }
                    fm.beginTransaction().replace(R.id.content, fragment).commitAllowingStateLoss();
                } else {
                    mHandler.post(this);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mHandler.postDelayed(() -> transactFragment(new MainFragment()), 3000);
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
