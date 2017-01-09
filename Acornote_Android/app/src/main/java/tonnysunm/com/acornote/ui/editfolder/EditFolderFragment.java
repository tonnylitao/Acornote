package tonnysunm.com.acornote.ui.editfolder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.realm.Realm;
import tonnysunm.com.acornote.R;
import tonnysunm.com.acornote.databinding.EditfolderFragmentBinding;
import tonnysunm.com.acornote.model.Folder;
import tonnysunm.com.acornote.ui.base.MVP;

public class EditFolderFragment extends Fragment implements EditFolderMVP.View {
    private static final String TAG = EditFolderFragment.class.getSimpleName();

    private Folder mFolder;

    @Override
    public void setPresenter(MVP.Presenter presenter) {}

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
        viewModel.setView(this);

        binding.setViewModel(viewModel);

        return view;
    }

    @Override
    public void onCancel() {
        final Activity activity = getActivity();
        activity.setResult(Activity.RESULT_CANCELED);
        activity.finish();
    }

    @Override
    public void onSure(EditFolderViewModel model) {
        final Context context = getContext();

        if (model.title == null) {
            Toast.makeText(context, R.string.need_folder_title, Toast.LENGTH_SHORT).show();
            return;
        }

        final Realm.Transaction.OnSuccess onSuccess = () -> {
            final Activity activity = EditFolderFragment.this.getActivity();
            activity.setResult(Activity.RESULT_OK);
            activity.finish();
        };

        final Realm.Transaction.OnError onError = (error) -> {
            Toast.makeText(context, R.string.create_error, Toast.LENGTH_SHORT).show();
        };

        if (mFolder == null) {
            Folder.createFolder(model, onSuccess, onError);
        }else {
            Folder.updateFolder(model, mFolder.id, onSuccess, onError);
        }
    }
}