package tonnysunm.com.acornote.ui.editfolder;

import android.app.Activity;
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

        final EditfolderFragmentBinding binding = EditfolderFragmentBinding.bind(view);
        final EditFolderViewModel viewModel = new EditFolderViewModel(mFolder);
        viewModel.setContext(getActivity())
                .setView(this);

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
        final Activity activity = getActivity();
        activity.setResult(Activity.RESULT_CANCELED);
        activity.finish();
    }

    @Override
    public void onSure(EditFolderViewModel model) {
        if (model.title == null) {
            return;
        }

        Folder.createFolder(model, mFolder, ()-> {
            final Activity activity = EditFolderFragment.this.getActivity();
            activity.setResult(Activity.RESULT_OK);
            activity.finish();
        }, (error) -> {

        });
    }
}