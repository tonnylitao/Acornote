package tonnysunm.com.acornote.ui.editfolder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.databinding.EditfolderFragmentBinding;
import tonnysunm.com.acornote.model.Folder;

public class EditFolderFragment extends Fragment implements EditFolderMVP.View {
    private static final String TAG = EditFolderFragment.class.getSimpleName();

    private EditFolderPresenter mPresenter;

    private Folder mFolder;

    @Override
    public void setPresenter(EditFolderPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.editfolder_fragment, container, false);

        final Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            mFolder = bundle.getParcelable(EditFolderActivity.EXTRA_FOLDER);

            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.sky));

        }else {
            view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.sky));
        }

        EditfolderFragmentBinding binding = EditfolderFragmentBinding.bind(view);
        final EditFolderViewModel viewModel = new EditFolderViewModel(getActivity(), mFolder);
        viewModel.setMVPView(this);

        binding.setViewModel(viewModel);

        return view;
    }

    @Override
    public void refresh(String string) {

        Log.d("Home", string);
    }

    @Override
    public void onResume() {
        super.onResume();

        mPresenter.loadData();
    }

    @Override
    public void onCancel() {
        getActivity().setResult(Activity.RESULT_CANCELED);
        getActivity().finish();
    }

    @Override
    public void onSure(EditFolderViewModel model) {
        Log.d(TAG, "onSure");

        final Intent intent = new Intent();
        intent.putExtra("SOMETHING", "EXTRAS");

        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finishActivity(EditFolderActivity.CREATE_FOLDER_REQUEST);
    }
}