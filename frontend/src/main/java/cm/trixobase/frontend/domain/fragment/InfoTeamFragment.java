package cm.trixobase.frontend.domain.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import cm.trixobase.frontend.R;

/*
 * Powered by Trixobase Enterprise on 30/11/20.
 */

public class InfoTeamFragment extends Fragment {

    private InfoTeamFragment() {
    }

    public static class Builder {

        private InfoTeamFragment instance;

        private Builder() {
            instance = new InfoTeamFragment();
        }

        public InfoTeamFragment build() {
            return instance;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_team, container, false);
    }
}